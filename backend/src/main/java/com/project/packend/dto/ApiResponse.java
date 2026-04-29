package com.project.packend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ApiResponse {
    private Long id;
    private LocalDateTime createdAt;
}

