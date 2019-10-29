package com.nathan22177.util;

import com.nathan22177.enums.Opponent;
import com.nathan22177.game.Conditions;
import com.nathan22177.game.PlayerVersusBotGame;
import com.nathan22177.game.PlayerVersusPlayerGame;

public class NewGameUtil {
    public static PlayerVersusBotGame createNewGameAgainstTheBot(Opponent opponent) {
        return new PlayerVersusBotGame(Conditions.getRandomConditions(), opponent);
    }

    public static PlayerVersusPlayerGame createNewGameAgainstThePlayer(String username) {
        return new PlayerVersusPlayerGame(Conditions.getRandomConditions(), username);
    }
}
