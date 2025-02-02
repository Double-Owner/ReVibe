package com.doubleowner.revibe.domain.point;

import lombok.Getter;

@Getter
public enum PointPolicy {
    TEXT_ONLY_POINT(100), TEXT_IMAGE_POINT(500);

    private final int point;

    PointPolicy(int point) {
        this.point = point;
    }
}
