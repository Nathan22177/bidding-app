package com.nathan22177.game.dto;

import com.nathan22177.game.AbstractGame;

import lombok.Getter;

/**
 * Used when making a list of games available to the player.
 *
 * @author Valery Kokorev
 * @author https://github.com/Nathan22177
 */
@Getter
public class GamesDTO {
    private Long gameId;

    /**
     * Name of an opponent.
     */
    private String opponent;

    /**
     * How many times player cam place a bid before game runs out of QU.
     */
    private int roundsLeft;

    /**
     * Amount of already won QUs.
     */
    private int acquired;

    /**
     * Amount of money left on the balance.
     */
    private int balance;

    /**
     * Current status of the game.
     */
    private String status;

    /**
     * Constructor that we use to make a list of available games.
     *
     * @param game current game.
     */
    public GamesDTO(AbstractGame game) {
        this.gameId = game.getId();
        this.opponent = game.getRedPlayer().getName();
        this.roundsLeft = (game.getConditions().getQuantity() / 2) - game.getBluePlayer().getBiddingHistory().size();
        this.acquired = game.getBluePlayer().getAcquiredAmount();
        this.balance = game.getBluePlayer().getBalance();
        this.status = game.getStatus().toString();
    }
}