package com.nathan22177.game.dto;

import com.nathan22177.game.PlayerVersusBotGame;

import lombok.Getter;

@Getter
public class GamesDTO {
    private Long id;
    private String opponent;
    private int roundsLeft;
    private int acquired;
    private int balance;
    private String status;
    private boolean active;

    public GamesDTO(PlayerVersusBotGame game) {
        this.id = game.getId();
        this.opponent = game.getRedPlayer().getTitle();
        this.roundsLeft = (game.getConditions().getQuantity() / 2) - game.getBluePlayer().getBiddingHistory().size();
        this.acquired = game.getBluePlayer().getAcquiredAmount();
        this.balance = game.getBluePlayer().getBalance();
        this.status = game.getStatus().toString();
        this.active = game.getStatus().isActive();
    }
}