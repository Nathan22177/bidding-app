package com.nathan22177.collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class BiddingRound {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    private int blueBid;
    private int redBid;

    public BiddingRound(int red, int blue){
        this.blueBid = red;
        this.redBid = blue;
    }

    /**
     * Used by JPA.
     */
    public BiddingRound(){}
}
