package com.nathan22177.game.dto;

import com.nathan22177.bidder.AbstractBidder;
import com.nathan22177.bidder.BidderPlayer;
import com.nathan22177.collection.BiddingRound;
import com.nathan22177.enums.Status;
import com.nathan22177.game.Conditions;
import com.nathan22177.game.PlayerVersusBotGame;

import lombok.Getter;

@Getter
public class StateDTO {
    private int balance;
    private int acquiredAmount;
    private Conditions conditions;
    private BiddingRound biddingRound;
    private int opponentBalance;
    private int opponentAcquiredAmount;
    private int roundsLeft;
    private Status status;


    public StateDTO(PlayerVersusBotGame game) {
        BidderPlayer player = game.getBluePlayer();
        this.balance = player.getBalance();
        this.acquiredAmount = player.getAcquiredAmount();
        this.biddingRound = getBiddingRound(player);
        this.conditions = game.getConditions();
        this.status = game.getStatus();
        this.opponentBalance = game.getRedPlayer().getBalance();
        this.opponentAcquiredAmount = game.getRedPlayer().getAcquiredAmount();
        this.roundsLeft = (game.getConditions().getQuantity() / 2) - player.getBiddingHistory().size();
    }

    BiddingRound getBiddingRound(AbstractBidder player) {
        if (player.getBiddingHistory().size() == 0) {
            return null;
        }
        return player.getBiddingHistory().get(player.getBiddingHistory().size() - 1);
    }
}
