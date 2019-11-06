package com.nathan22177.game.resolvers;

import com.nathan22177.enums.Status;
import com.nathan22177.game.AbstractGame;

/**
 * Resolves natural conclusion of a game.
 *
 * @author Valery Kokorev
 * @author https://github.com/Nathan22177
 */
public class EndGameResolver {

    /**
     * Determines who's the victor by won QU.
     */
    public static void resolveGamesEnd(AbstractGame game) {
        if (neitherHasAdvantageByQU(game)) {
            resolveDrawByWonQuantity(game);
        } else if (blueHasMoreQuantity(game)) {
            game.setStatus(Status.BLUE_WON);
        } else {
            game.setStatus(Status.RED_WON);
        }
    }

    /**
     * Determines who's the victor by money left on the balance.
     */
    private static void resolveDrawByWonQuantity(AbstractGame game) {
        if (neitherHasAdvantageByMoney(game)) {
            game.setStatus(Status.DRAW);
        } else if (blueHasMoreMoney(game)) {
            game.setStatus(Status.BLUE_WON);
        } else {
            game.setStatus(Status.RED_WON);
        }
    }

    /**
     * Determines if both players have won the same amount of QU.
     */
    private static boolean neitherHasAdvantageByQU(AbstractGame game) {
        return game.getRedPlayer().getAcquiredAmount() == game.getBluePlayer().getAcquiredAmount();
    }

    /**
     * Determines if blue player have won more QU.
     */
    private static boolean blueHasMoreQuantity(AbstractGame game) {
        return game.getRedPlayer().getAcquiredAmount() < game.getBluePlayer().getAcquiredAmount();
    }

    /**
     * Determines if both players have same amount of money left on the balance.
     */
    private static boolean neitherHasAdvantageByMoney(AbstractGame game) {
        return game.getRedPlayer().getBalance() == game.getBluePlayer().getBalance();
    }

    /**
     * Determines if blue player have more money left.
     */
    private static boolean blueHasMoreMoney(AbstractGame game) {
        return game.getRedPlayer().getBalance() < game.getBluePlayer().getBalance();
    }



}
