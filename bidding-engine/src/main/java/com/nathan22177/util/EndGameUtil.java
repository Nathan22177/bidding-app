package com.nathan22177.util;

import com.nathan22177.enums.Status;
import com.nathan22177.game.AbstractGame;

public class EndGameUtil {

    public static boolean theGameHasEnded(AbstractGame game) {
        return game.getRedPlayer().getAcquiredAmount() + game.getBluePlayer().getAcquiredAmount()
                == game.getConditions().getQuantity();
    }

    public static void resolveGamesEnd(AbstractGame game) {
        if (redHasMoreQuantity(game)) {
            game.setStatus(Status.RED_WON);
        } else if (blueHasMoreQuantity(game)) {
            game.setStatus(Status.BLUE_WON);
        } else {
            resolveDraw(game);
        }
    }

    private static void resolveDraw(AbstractGame game) {
        if (redHasMoreCash(game)) {
            game.setStatus(Status.RED_WON);
        } else if (blueHasMoreCash(game)) {
            game.setStatus(Status.BLUE_WON);
        } else {
            game.setStatus(Status.DRAW);
        }
    }

    private static boolean redHasMoreQuantity(AbstractGame game) {
        return game.getRedPlayer().getAcquiredAmount() > game.getBluePlayer().getAcquiredAmount();
    }

    private static boolean blueHasMoreQuantity(AbstractGame game) {
        return game.getRedPlayer().getAcquiredAmount() < game.getBluePlayer().getAcquiredAmount();
    }

    private static boolean redHasMoreCash(AbstractGame game) {
        return game.getRedPlayer().getBalance() > game.getBluePlayer().getBalance();
    }

    private static boolean blueHasMoreCash(AbstractGame game) {
        return game.getRedPlayer().getBalance() < game.getBluePlayer().getBalance();
    }



}
