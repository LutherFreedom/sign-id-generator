package com.luther.base.service.impl.bean;

import java.util.Objects;

public enum IdType {
    MAX_PEAK("max_peak"),
    MIN_GRANULARITY("min_granularity");
    private String name;

    IdType(String name) {
        this.name = name;
    }

    public long value() {
        switch (this) {
            case MIN_GRANULARITY:
                return 1;
            case MAX_PEAK:
            default:
                return 0;
        }
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static IdType parse(String name) {
        if (Objects.equals("min_granularity", name)) {
            return MIN_GRANULARITY;
        } else if (Objects.equals("max_peak", name)) {
            return MAX_PEAK;
        }
        return null;
    }

    public static IdType parse(long type) {
        if (type == 1) {
            return MIN_GRANULARITY;
        } else if (type == 0) {
            return MAX_PEAK;
        }
        return null;
    }
}
