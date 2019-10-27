package com.nathan22177.game;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.nathan22177.bidder.BidderBot;
import com.nathan22177.bidder.BidderPlayer;
import com.nathan22177.collection.BiddingRound;
import com.nathan22177.enums.Opponent;
import com.nathan22177.enums.Status;
import com.nathan22177.util.EndGameUtil;

import lombok.Setter;

@Setter
@Entity
public class PlayerVersusBotGame extends AbstractGame implements Game{

    @OneToOne(cascade = CascadeType.ALL)
    BidderBot redPlayer;

    public PlayerVersusBotGame(Conditions conditions, Opponent opponent) {
        this.conditions = conditions;
        this.redPlayer = new BidderBot(conditions, opponent);
        this.bluePlayer = new BidderPlayer(conditions);
        this.status = Status.WAITING_FOR_BIDS;
    }

    public BiddingRound playerPlacesBidVersusBot(Integer bluesBid) {
        int redsBid = getRedPlayer().getNextBid();
        resolveBidsVersusBot(bluesBid, redsBid);
        if (EndGameUtil.theGameHasEnded(this)) {
            EndGameUtil.resolveGamesEnd(this);
        }
        return getRedPlayer().getBiddingHistory().get(getRedPlayer().getBiddingHistory().size() - 1);
    }

    private void resolveBidsVersusBot(int bluesBid, int redsBid) {
        BidderPlayer player = getBluePlayer();
        BidderBot bot = getRedPlayer();
        player.placeBidAndWithdraw(bluesBid);
        bot.placeBidAndWithdraw(redsBid);
        player.resolveBidsAndAppendHistory(bluesBid, redsBid);
        bot.resolveBidsAndAppendHistory(redsBid, bluesBid);
        setStatus(Status.WAITING_FOR_BIDS);
    }

    @Override
    public BidderBot getRedPlayer() {
        return this.redPlayer;
    }

    /**
     * Used by JPA.
     */
    public PlayerVersusBotGame(){}
}
