package com.nathan22177.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.nathan22177.enums.Bot;
import com.nathan22177.collections.Conditions;
import com.nathan22177.game.PlayerVersusBotGame;
import com.nathan22177.game.PlayerVersusPlayerGame;

public class NewGameUtil {
    /**
     * Instance of {@link Random} used to determine conditions.
     **/
    private final static Random rand = new Random();

    /**
     * Produces random starting conditions.
     * */
    private static Conditions getRandomConditions() {
        return new Conditions(getRandomCondition(quantityPoll), getRandomCondition(moneyPoll));
    }


    /**
     * Possible winnable QUs.
     * */
    private final static List<Integer> quantityPoll = Arrays.asList(8, 16, 20);

    /**
     * Possible initial balance.
     * */
    private final static List<Integer> moneyPoll = Arrays.asList(1000, 5000, 20_000, 80_000);

    private static Integer getRandomCondition(List<Integer> poll) {
        return poll.get(rand.nextInt(poll.size()));
    }

    public static PlayerVersusBotGame createNewGameAgainstTheBot(Bot bot) {
        return new PlayerVersusBotGame(getRandomConditions(), bot);
    }

    public static PlayerVersusPlayerGame createNewGameAgainstThePlayer(String name) {
        return new PlayerVersusPlayerGame(getRandomConditions(), name);
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
        return Arrays.stream(Bot.values()).collect(Collectors.toMap(Bot::name, Function.identity()));
    }
}
