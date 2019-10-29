package com.nathan22177.game;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.nathan22177.bidder.BidderPlayer;
import com.nathan22177.collection.BiddingRound;
import com.nathan22177.enums.Status;
import com.nathan22177.util.EndGameUtil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PlayerVersusPlayerGame extends AbstractGame implements Game{

    @OneToOne(cascade = CascadeType.ALL)
    BidderPlayer redPlayer;

    public PlayerVersusPlayerGame(Conditions conditions, String username) {
        this.conditions = conditions;
        this.bluePlayer = new BidderPlayer(conditions, username);
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

    public void playersPlaceTheirBids(Integer bluesBid, Integer redsBid) {
        resolveBids(bluesBid, redsBid);
        if (EndGameUtil.theGameHasEnded(this)) {
            EndGameUtil.resolveGamesEnd(this);
        }
    }

    private void resolveBids(int bluesBid, int redsBid) {
        BidderPlayer bluePlayer = getBluePlayer();
        BidderPlayer redPlayer = getRedPlayer();
        bluePlayer.placeBidAndWithdraw(bluesBid);
        redPlayer.placeBidAndWithdraw(redsBid);
        bluePlayer.resolveBidsAndAppendHistory(bluesBid, redsBid);
        redPlayer.resolveBidsAndAppendHistory(redsBid, bluesBid);
        setStatus(Status.WAITING_FOR_BIDS);
    }

    /**
     * Used by JPA.
     */
    public PlayerVersusPlayerGame(){}
}
