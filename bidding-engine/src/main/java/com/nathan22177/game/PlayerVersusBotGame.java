package com.nathan22177.game;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.nathan22177.bidder.BidderBot;
import com.nathan22177.bidder.BidderPlayer;
import com.nathan22177.enums.Bot;
import com.nathan22177.enums.Side;
import com.nathan22177.enums.Status;
import com.nathan22177.util.EndGameUtil;

import lombok.Setter;

@Setter
@Entity
@Table(name = "player_versus_bot_game")
public class PlayerVersusBotGame extends AbstractGame {

    @OneToOne(cascade = CascadeType.ALL)
    private BidderBot redPlayer;

    public PlayerVersusBotGame(Conditions conditions, Bot bot) {
        this.conditions = conditions;
        this.redPlayer = new BidderBot(conditions, bot);
        this.bluePlayer = new BidderPlayer(conditions, "local", Side.BLUE);
        this.status = Status.WAITING_FOR_BIDS;
    }

    public void playerPlacesBidVersusBot(Integer bluesBid) {
        int redsBid = getRedPlayer().getNextBid();
        resolveBidsVersusBot(bluesBid, redsBid);
        if (EndGameUtil.theGameHasEnded(this)) {
            EndGameUtil.resolveGamesEnd(this);
        }
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

    public BidderBot getRedPlayer() {
        return this.redPlayer;
    }

    /**
     * Used by JPA.
     */
    public PlayerVersusBotGame(){}
}
