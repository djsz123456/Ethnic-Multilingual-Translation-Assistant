package com.tibetan.platform.controller;

import com.tibetan.platform.config.JwtUtil;
import com.tibetan.platform.dto.ApiResponse;
import com.tibetan.platform.dto.AuthRequest;
import com.tibetan.platform.entity.User;
import com.tibetan.platform.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ApiResponse<Map<String, Object>> register(@Valid @RequestBody AuthRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            return ApiResponse.error(400, "用户名已存在");
        }
        User user = User.builder()
                .username(req.getUsername())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .nickname(req.getUsername())
                .build();
        User saved = userRepository.save(user);
        String token = jwtUtil.generateToken(saved.getId(), saved.getUsername());
        return ApiResponse.ok("注册成功", Map.of("token", token, "username", saved.getUsername(), "userId", saved.getId()));
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@Valid @RequestBody AuthRequest req) {
        return userRepository.findByUsername(req.getUsername())
                .filter(u -> passwordEncoder.matches(req.getPassword(), u.getPasswordHash()))
                .map(u -> {
                    String token = jwtUtil.generateToken(u.getId(), u.getUsername());
                    return ApiResponse.ok("登录成功", Map.of("token", token, "username", u.getUsername(), "userId", u.getId()));
                })
                .orElse(ApiResponse.error(401, "用户名或密码错误"));
    }
}
