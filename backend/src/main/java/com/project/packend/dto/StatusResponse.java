package com.project.packend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StatusResponse {
    private List<StatusItem> status;

    @Getter
    @AllArgsConstructor
    public static class StatusItem {
        private String date;
        private String id;
    }
}
