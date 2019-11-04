package com.nathan22177.game.dto;

import com.nathan22177.bidder.AbstractBidder;
import com.nathan22177.bidder.BidderPlayer;
import com.nathan22177.collections.BiddingRound;
import com.nathan22177.enums.Side;
import com.nathan22177.enums.Status;
import com.nathan22177.collections.Conditions;
import com.nathan22177.game.PlayerVersusBotGame;
import com.nathan22177.game.PlayerVersusPlayerGame;

import lombok.Getter;

/**
 * Used when passing info about the game to the client.
 *
 * @author Valery Kokorev
 * @author https://github.com/Nathan22177
 */
@Getter
public class StateDTO {
    private Long gameId;

    /**
     * Amount of money left on the balance.
     */
    private int balance;

    /**
     * Amount of currently won QU.
     */
    private int acquiredAmount;

    /**
     * Initial amount of money and QU.
     */
    private Conditions conditions;

    /**
     * Bids from both players.
     */
    private BiddingRound biddingRound;

    /**
     * Amount of money left on the opponent's balance.
     */
    private int opponentBalance;

    /**
     * Amount of QU currently won by opponent.
     */
    private int opponentAcquiredAmount;

    /**
     * How many times player can place a bid before game runs out of QU.
     */
    private int roundsLeft;

    /**
     * Current status of the game.
     */
    private Status status;

    /**
     * Name of a player.
     * Used on the client-side to interact with websocket.
     */
    private String ownName;

    /**
     * Name of a player's opponent.
     * Used on the client-side.
     */
    private String opponentsName;

    /**
     * Constructor that we use to pass a state to the client.
     *
     * @param game current game.
     */
    public StateDTO(PlayerVersusBotGame game) {
        BidderPlayer player = game.getBluePlayer();
        this.gameId = game.getId();
        this.balance = player.getBalance();
        this.acquiredAmount = player.getAcquiredAmount();
        this.biddingRound = getBiddingRound(player);
        this.conditions = game.getConditions();
        this.status = game.getStatus();
        this.opponentBalance = game.getRedPlayer().getBalance();
        this.opponentAcquiredAmount = game.getRedPlayer().getAcquiredAmount();
        this.roundsLeft = (game.getConditions().getWinnableQuantity() / 2) - player.getBiddingHistory().size();
        this.ownName = game.getBluePlayer().getName();
        this.opponentsName = game.getRedPlayer().getName();
    }

    /**
     * Constructor that we use to pass a state to the client.
     *
     * @param game current game.
     */
    public StateDTO(PlayerVersusPlayerGame game, Side side) {
        BidderPlayer player = side == Side.BLUE
                ? game.getBluePlayer()
                : game.getRedPlayer();
        BidderPlayer opponent = side == Side.BLUE
                ? game.getRedPlayer()
                : game.getBluePlayer();
        this.gameId = game.getId();
        this.balance = player.getBalance();
        this.acquiredAmount = player.getAcquiredAmount();
        this.biddingRound = getBiddingRound(player);
        this.conditions = game.getConditions();
        this.status = game.getStatus();
        if (opponent != null) {
            this.opponentBalance = opponent.getBalance();
            this.opponentAcquiredAmount = opponent.getAcquiredAmount();
            this.opponentsName = opponent.getName();
        }
        this.roundsLeft = (game.getConditions().getWinnableQuantity() / 2) - player.getBiddingHistory().size();
        this.ownName = player.getName();

    }

    /**
     * Getting last bidding round.
     *
     * @param player addressee of the message with a state.
     * @return last bidding round.
     */
    private BiddingRound getBiddingRound(AbstractBidder player) {
        if (player.getBiddingHistory().size() == 0) {
            return null;
        }
        return player.getBiddingHistory().get(player.getBiddingHistory().size() - 1);
    }
}
