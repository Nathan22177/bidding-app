package com.nathan22177.collections;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Defines initial amount of money and winnable QU.
 *
 * @author Valery Kokorev
 * @author https://github.com/Nathan22177
 */
@AllArgsConstructor
@Getter
@Embeddable
public class Conditions {
    /**
     * Winnable QUs.
     * */
    private int winnableQuantity;

    /**
     * Initial balance.
     * */
    private int initialBalance;

    /**
     * Used by persistence to create new instance via reflection upon fetching.
     */
    public Conditions() {}
}
