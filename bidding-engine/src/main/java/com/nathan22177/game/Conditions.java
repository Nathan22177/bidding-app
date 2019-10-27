package com.nathan22177.game;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Embeddable
public class Conditions {
    private final static List<Integer> quantityPoll = Arrays.asList(8, 16, 20);
    private final static List<Integer> cashPoll = Arrays.asList(1000, 5000, 20_000, 80_000);
    private final static Random rand = new Random();

    private int quantity;
    private int cash;


    public static Conditions getRandomConditions() {
        return new Conditions(getRandomCondition(quantityPoll), getRandomCondition(cashPoll));
    }

    private static Integer getRandomCondition(List<Integer> poll) {
        return poll.get(rand.nextInt(poll.size()));
    }

    /**
     * Used by JPA.
     */
    public Conditions() {}
}
