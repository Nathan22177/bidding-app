package com.nathan22177.game;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.nathan22177.bidder.BidderBot;
import com.nathan22177.bidder.BidderPlayer;
import com.nathan22177.collections.Conditions;
import com.nathan22177.enums.Bot;
import com.nathan22177.enums.Side;
import com.nathan22177.enums.Status;
import com.nathan22177.game.resolvers.EndGameResolver;

import lombok.Setter;

/**
 * Represents a game between player-controlled
 * bidder and bot-controlled bidder.
 *
 * @author Valery Kokorev
 * @author https://github.com/Nathan22177
 */
@Setter
@Entity
@Table(name = "player_versus_bot_game")
public class PlayerVersusBotGame extends AbstractGame {

    /**
     * Bot-controlled participant.
     */
    @OneToOne(cascade = CascadeType.ALL)
    private BidderBot redPlayer;

    /**
     * Constructor that we use in order to instantiate a game.
     *
     * @param conditions initial {@link com.nathan22177.bidder.AbstractBidder#balance} and amount
     *                   of winnable QUs.
     * @param bot        defines how bot evaluates how much to bid
     *                   and bot's {@link BidderBot#name}.
     * @return new instance of {@link PlayerVersusBotGame}.
     */
    public PlayerVersusBotGame(Conditions conditions, Bot bot) {
        this.conditions = conditions;
        this.redPlayer = new BidderBot(conditions, bot);
        this.bluePlayer = new BidderPlayer(conditions, "local", Side.BLUE);
        this.status = Status.WAITING_FOR_BIDS;
    }

    /**
     * Accepts client's bid.
     *
     * @param bluesBid bid of a client.
     */
    public void playerPlacesBidVersusBot(Integer bluesBid) {
        int redsBid = getRedPlayer().getNextBid();
        resolveBiddingRound(bluesBid, redsBid);
        if (theGameHasEnded()) {
            EndGameResolver.resolveGamesEnd(this);
        }
    }

    /**
     * Checks if all pf the QUs were won.
     */
    private boolean theGameHasEnded() {
        return getRedPlayer().getAcquiredAmount() + getBluePlayer().getAcquiredAmount()
                == getConditions().getWinnableQuantity();
    }

    /**
     * Accepts both client's and their opponent's bids.
     *
     * @param bluesBid bid of a client.
     * @param redsBid  bid of a bot.
     */
    private void resolveBiddingRound(int bluesBid, int redsBid) {
        BidderPlayer player = getBluePlayer();
        BidderBot bot = getRedPlayer();
        player.resolveBidsAndAppendHistory(bluesBid, redsBid);
        bot.resolveBidsAndAppendHistory(redsBid, bluesBid);
        setStatus(Status.WAITING_FOR_BIDS);
    }

    @Override
    public BidderBot getRedPlayer() {
        return this.redPlayer;
    }

    /**
     * Used by persistence to create new instance via reflection upon fetching.
     */
    public PlayerVersusBotGame() {
    }
}
