package com.nathan22177.util;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.nathan22177.enums.Bot;
import com.nathan22177.game.Conditions;
import com.nathan22177.game.PlayerVersusBotGame;
import com.nathan22177.game.PlayerVersusPlayerGame;

public class NewGameUtil {
    public static PlayerVersusBotGame createNewGameAgainstTheBot(Bot bot) {
        return new PlayerVersusBotGame(Conditions.getRandomConditions(), bot);
    }

    public static PlayerVersusPlayerGame createNewGameAgainstThePlayer(String name) {
        return new PlayerVersusPlayerGame(Conditions.getRandomConditions(), name);
    }

    public static String getRandomBot() {
        return getAvailableBots().keySet()
                .stream()
                .filter(key -> !key.equalsIgnoreCase("RANDOM"))
                .collect(CollectorUtils.toShuffledStream())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("There are no bots available. Something is wrong."));
    }

    public static Map<String, Bot> getAvailableBots() {
        return Arrays.stream(Bot.values()).collect(Collectors.toMap(Bot::getName, Function.identity()));
    }
}
