package com.tibetan.platform.controller;

import com.tibetan.platform.dto.ApiResponse;
import com.tibetan.platform.entity.Note;
import com.tibetan.platform.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteRepository noteRepository;

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Long) auth.getCredentials();
    }

    @GetMapping
    public ApiResponse<List<Note>> list() {
        return ApiResponse.ok(noteRepository.findByUserIdOrderByUpdatedAtDesc(getCurrentUserId()));
    }

    @PostMapping
    public ApiResponse<Note> create(@RequestBody Note note) {
        note.setId(null);
        note.setUserId(getCurrentUserId());
        return ApiResponse.ok(noteRepository.save(note));
    }

    @PutMapping("/{id}")
    public ApiResponse<Note> update(@PathVariable Long id, @RequestBody Note note) {
        Long userId = getCurrentUserId();
        return noteRepository.findById(id)
                .filter(n -> n.getUserId().equals(userId))
                .map(existing -> {
                    existing.setTitle(note.getTitle());
                    existing.setContent(note.getContent());
                    return ApiResponse.ok(noteRepository.save(existing));
                })
                .orElse(ApiResponse.error(404, "笔记不存在"));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        return noteRepository.findById(id)
                .filter(n -> n.getUserId().equals(userId))
                .map(n -> { noteRepository.delete(n); return ApiResponse.<Void>ok("删除成功", null); })
                .orElse(ApiResponse.error(404, "笔记不存在"));
    }
}
