package com.nathan22177.collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class BiddingRound {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    /**
     * Client's bid.
     * */
    private int ownBid;

    /**
     * Client's opponent's bid.
     * */
    private int opponentBid;

    /**
     * Constructor used when processing both players bids.
     * */
    public BiddingRound(int own, int other){
        this.ownBid = own;
        this.opponentBid = other;
    }

    /**
     * Used by persistence to create new instance via reflection upon fetching.
     * */
    public BiddingRound(){}
}
