package com.nathan22177.game;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.transaction.Transactional;

import com.nathan22177.bidder.BidderPlayer;
import com.nathan22177.enums.Side;
import com.nathan22177.enums.Status;
import com.nathan22177.util.EndGameUtil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "player_versus_player_game")
public class PlayerVersusPlayerGame extends AbstractGame{

    @OneToOne(cascade = CascadeType.ALL)
    BidderPlayer redPlayer;

    public PlayerVersusPlayerGame(Conditions conditions, String name) {
        this.conditions = conditions;
        this.bluePlayer = new BidderPlayer(conditions, name, Side.BLUE);
        setStatus(Status.MATCHMAKING);
    }

    @Override
    public BidderPlayer getRedPlayer() {
        return this.redPlayer;
    }

    @Transactional
    public BidderPlayer getPlayerByName(String name) {
        if (getBluePlayer().getName().equals(name)) {
            return getBluePlayer();
        }
        if (getRedPlayer().getName().equals(name)) {
            return getRedPlayer();
        }
        throw new IllegalArgumentException("No such player in this game. Name: " + name + "; gameId: " + getId());
    }

    public void playersPlaceTheirBids(Integer bluesBid, Integer redsBid) {
        resolveBids(bluesBid, redsBid);
        if (EndGameUtil.theGameHasEnded(this)) {
            EndGameUtil.resolveGamesEnd(this);
        }
    }

    private void resolveBids(int bluesBid, int redsBid) {
        BidderPlayer bluePlayer = getBluePlayer();
        BidderPlayer redPlayer = getRedPlayer();
        bluePlayer.withdrawBiddingAmount(bluesBid);
        redPlayer.withdrawBiddingAmount(redsBid);
        bluePlayer.resolveBidsAndAppendHistory(bluesBid, redsBid);
        redPlayer.resolveBidsAndAppendHistory(redsBid, bluesBid);
        setStatus(Status.WAITING_FOR_BIDS);
    }

    /**
     * Used by persistence to create new instance via reflection upon fetching.
     */
    public PlayerVersusPlayerGame(){}
}
