package com.nathan22177.game;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.nathan22177.bidder.BidderPlayer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PlayerVersusPlayerGame extends AbstractGame implements Game{

    @OneToOne(cascade = CascadeType.ALL)
    BidderPlayer redPlayer;

    public PlayerVersusPlayerGame(Conditions conditions, BidderPlayer bluePlayer) {
        this.conditions = conditions;
        this.bluePlayer = bluePlayer;
    }

    @Override
    public BidderPlayer getRedPlayer() {
        return this.redPlayer;
    }

    public BidderPlayer getPlayerByUsername(String username) {
        if (getBluePlayer().getUsername().equals(username)) {
            return getBluePlayer();
        }
        if (getRedPlayer().getUsername().equals(username)) {
            return getRedPlayer();
        }
        throw new IllegalArgumentException("No such player in this game. Username: " + username + "; gameId: " + getId());
    }
}
