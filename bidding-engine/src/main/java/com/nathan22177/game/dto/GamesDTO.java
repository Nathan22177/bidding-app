package com.nathan22177.game.dto;

import com.nathan22177.game.PlayerVersusBotGame;

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
     * Name if opponent is a player and title if opponent is a bot.
     */
    private String opponent;

    /**
     * How many times player cam place a bid before game runs out of QU.
     */
    private int roundsLeft;

    /**
     *  Amount of already won QUs.
     */
    private int acquired;

    /**
     * Amount of money left on the balance.
     * */
    private int balance;
    private String status;
    private boolean active;

    public GamesDTO(PlayerVersusBotGame game) {
        this.gameId = game.getId();
        this.opponent = game.getRedPlayer().getTitle();
        this.roundsLeft = (game.getConditions().getQuantity() / 2) - game.getBluePlayer().getBiddingHistory().size();
        this.acquired = game.getBluePlayer().getAcquiredAmount();
        this.balance = game.getBluePlayer().getBalance();
        this.status = game.getStatus().toString();
        this.active = game.getStatus().isActive();
    }
}