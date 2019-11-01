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
     * Bids of the blue player in the context of a whole game
     * but when passed to client trough {@link com.nathan22177.game.dto.StateDTO}
     * it is the client's bid.
     * */
    private int ownBid;

    /**
     * Bids of the red player in the context of a whole game
     * but when passed to client trough {@link com.nathan22177.game.dto.StateDTO}
     * it is the client's opponent's bid.
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
