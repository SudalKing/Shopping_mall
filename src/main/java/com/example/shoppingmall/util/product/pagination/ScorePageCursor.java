package com.example.shoppingmall.util.product.pagination;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ScorePageCursor<T> {
    private ScoreCursorRequest scoreCursorRequest;
    private List<T> body;

    public ScorePageCursor(ScoreCursorRequest scoreCursorRequest, List<T> body) {
        this.scoreCursorRequest = scoreCursorRequest;
        this.body = body;
    }
}
