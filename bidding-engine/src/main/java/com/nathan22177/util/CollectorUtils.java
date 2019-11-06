package com.nathan22177.util;

import java.util.Collections;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utilities for Stream API.
 */
public class CollectorUtils {

    /**
     * Used to chose random bot.
     * */
    public static <T> Collector<T, ?, Stream<T>> toShuffledStream() {
        return Collectors.collectingAndThen(Collectors.toList(), collected -> {
            Collections.shuffle(collected);
            return collected.stream();
        });
    }
}
