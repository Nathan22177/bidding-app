package com.nathan22177.bidder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import org.springframework.util.Assert;

import com.nathan22177.collection.BiddingRound;
import com.nathan22177.game.Conditions;

import lombok.Getter;
import lombok.Setter;


/**
 * Used as a guideline to define participant of an auction.
 * @author Valery Kokorev
 * @author https://github.com/Nathan22177
 */
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
@Getter
@Setter
public abstract class AbstractBidder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    /***
     * Amount of cash left.
     * */
    private int balance;

    /***
     * Amount of won QUs.
     * */
    private int acquiredAmount;

    /***
     * Initial amount cash and QU.
     * */
    @Embedded
    private Conditions conditions;


    /***
     * Track of {@link BiddingRound}s.
     * */
    @OneToMany(cascade = CascadeType.ALL)
    @OrderColumn
    private List<BiddingRound> biddingHistory;

    /**
     * Used to withdraw cash from {@link AbstractBidder#balance}.
     * @param
     * */
    private void withdraw(int cash) {
        this.balance -= cash;
    }

    /**
     * Used to add quantity units to {@link AbstractBidder#acquiredAmount}.
     * */
    private void acquire(int quantity) {
        this.acquiredAmount += quantity;
    }

    /**
     * Initiates cash withdrawal from {@link AbstractBidder#balance}
     * and logging into {@link AbstractBidder#biddingHistory}.
     * @param own amount of cash bid by the player.
     * @param other amount of cash bid by the player.
     * @return Nothing.
     * */
    public void resolveBidsAndAppendHistory(int own, int other) {
        appendBiddingHistory(own, other);
        resolveBids(own, other);
    }

    private void appendBiddingHistory(int own, int other) {
        if (getBiddingHistory() == null) {
            setBiddingHistory(new LinkedList<>(Collections.singletonList(new BiddingRound(own, other))));
        } else {
            getBiddingHistory().add(new BiddingRound(own, other));
        }
    }

    private void resolveBids(int own, int other) {
        if (other < own) {
            acquire(2);
        } else if (own == other) {
            acquire(1);
        }
    }

    public void placeBidAndWithdraw(int bid) {
        Assert.isTrue(bid >= 0, "Bid should be positive number");
        Assert.isTrue(bid <= getBalance(), "Bid should not be larger than amount of cash on the balance");
        withdraw(bid);
    }
}
