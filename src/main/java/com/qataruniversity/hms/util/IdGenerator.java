package com.qataruniversity.hms.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class IdGenerator {
    private static final ConcurrentHashMap<String, AtomicInteger> COUNTERS = new ConcurrentHashMap<>();

    private IdGenerator() { }

    public static String next(String prefix) {
        int value = COUNTERS.computeIfAbsent(prefix, ignored -> new AtomicInteger()).incrementAndGet();
        return "%s-%04d".formatted(prefix.toUpperCase(), value);
    }
}
