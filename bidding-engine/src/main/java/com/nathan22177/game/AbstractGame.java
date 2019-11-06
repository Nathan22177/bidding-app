package com.nathan22177.game;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

import com.nathan22177.bidder.AbstractBidder;
import com.nathan22177.bidder.BidderPlayer;
import com.nathan22177.collections.Conditions;
import com.nathan22177.enums.Status;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Inherited to define a game.
 *
 * @author Valery Kokorev
 * @author https://github.com/Nathan22177
 */
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
@Setter
@Getter
public abstract class AbstractGame {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    /**
     * Player-controlled participant.
     */
    @OneToOne(cascade = CascadeType.ALL)
    BidderPlayer bluePlayer;

    /**
     * Current status of the game.
     */
    @Enumerated(EnumType.STRING)
    Status status;

    /**
     * Initial amount of money and QU.
     */
    @Setter(AccessLevel.PRIVATE)
    @Embedded
    Conditions conditions;



    /**
     * Player or bot-controlled participant should be accessible for every game.
     */
    public AbstractBidder getRedPlayer() {
        return null;
    }
}
