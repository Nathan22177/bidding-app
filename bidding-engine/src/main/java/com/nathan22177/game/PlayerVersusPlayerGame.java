package com.nathan22177.game;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.transaction.Transactional;

import com.nathan22177.bidder.BidderPlayer;
import com.nathan22177.collections.Conditions;
import com.nathan22177.enums.Side;
import com.nathan22177.enums.Status;
import com.nathan22177.util.EndGameUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a game between two
 * player-controlled bidders.
 *
 * @author Valery Kokorev
 * @author https://github.com/Nathan22177
 */
@Getter
@Setter
@Entity
@Table(name = "player_versus_player_game")
public class PlayerVersusPlayerGame extends AbstractGame {
    /**
     * Second player-controlled participant.
     */
    @OneToOne(cascade = CascadeType.ALL)
    BidderPlayer redPlayer;

    /**
     * Constructor that we use in order to instantiate a game.
     *
     * @param conditions initial {@link com.nathan22177.bidder.AbstractBidder#balance}
     *                   and amount of winnable QUs.
     * @param name       name of the {@link #bluePlayer} that
     *                   started the game.
     * @return new instance of {@link PlayerVersusPlayerGame}.
     */
    public PlayerVersusPlayerGame(Conditions conditions, String name) {
        this.conditions = conditions;
        this.bluePlayer = new BidderPlayer(conditions, name, Side.BLUE);
        setStatus(Status.MATCHMAKING);
    }

    /**
     * Called when second player joins the game.
     *
     * @param name name of the {@link #redPlayer} that
     *             joined the game.
     */
    @Transactional
    public void redPlayerJoined(String name) {
        this.redPlayer = new BidderPlayer(getConditions(), name, Side.RED);
        setStatus(Status.WAITING_FOR_BIDS);
    }

    /**
     * @param name name of the player.
     * @return player-controlled participant of the game.
     */
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

    /**
     * Accepts bids from both players.
     *
     * @param bluesBid bid of the {@link #bluePlayer}.
     * @param redsBid  bid of the {@link #redPlayer}.
     */
    public void playersPlaceTheirBids(Integer bluesBid, Integer redsBid) {
        resolveBiddingRound(bluesBid, redsBid);
        if (EndGameUtil.theGameHasEnded(this)) {
            EndGameUtil.resolveGamesEnd(this);
        }
    }

    /**
     * Resolves pair of bids from both players.
     *
     * @param bluesBid bid of a client.
     * @param redsBid  bid of a bot.
     */
    private void resolveBiddingRound(int bluesBid, int redsBid) {
        BidderPlayer bluePlayer = getBluePlayer();
        BidderPlayer redPlayer = getRedPlayer();
        bluePlayer.resolveBidsAndAppendHistory(bluesBid, redsBid);
        redPlayer.resolveBidsAndAppendHistory(redsBid, bluesBid);
        setStatus(Status.WAITING_FOR_BIDS);
    }

    @Override
    public BidderPlayer getRedPlayer() {
        return this.redPlayer;
    }

    /**
     * Used by persistence to create new instance via reflection upon fetching.
     */
    public PlayerVersusPlayerGame() {
    }
}
