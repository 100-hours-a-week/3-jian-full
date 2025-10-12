package com.jian.community.presentation.dto;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class CursorResponse<T> {

    private List<T> items;
    private LocalDateTime nextCursor;
    private boolean hasNext;
}
