package com.example.running.service;

import com.example.running.sse.SseEmitterManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class SseService {
    private final SseEmitterManager emitterManager;

    public SseEmitter subscribe(Long userId) {
        return emitterManager.createEmitter(userId);
    }
}
