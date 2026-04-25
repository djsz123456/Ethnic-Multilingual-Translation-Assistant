package com.tibetan.platform.controller;

import com.tibetan.platform.dto.ApiResponse;
import com.tibetan.platform.entity.Favorite;
import com.tibetan.platform.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteRepository favoriteRepository;

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Long) auth.getCredentials();
    }

    @GetMapping
    public ApiResponse<List<Favorite>> list() {
        return ApiResponse.ok(favoriteRepository.findByUserIdOrderByCreatedAtDesc(getCurrentUserId()));
    }

    @PostMapping
    public ApiResponse<Favorite> add(@RequestBody Favorite fav) {
        Long userId = getCurrentUserId();
        fav.setUserId(userId);
        if (favoriteRepository.findByUserIdAndItemTypeAndItemId(userId, fav.getItemType(), fav.getItemId()).isPresent()) {
            return ApiResponse.error(400, "已收藏");
        }
        return ApiResponse.ok(favoriteRepository.save(fav));
    }

    @DeleteMapping
    @Transactional
    public ApiResponse<Void> remove(@RequestParam String itemType, @RequestParam Long itemId) {
        favoriteRepository.deleteByUserIdAndItemTypeAndItemId(getCurrentUserId(), itemType, itemId);
        return ApiResponse.ok("取消收藏成功", null);
    }
}
