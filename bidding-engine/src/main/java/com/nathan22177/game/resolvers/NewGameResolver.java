package com.nathan22177.game.resolvers;

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
import com.nathan22177.util.CollectorUtils;

/**
 *  Helps start a game.
 *
 * @author Valery Kokorev
 * @author https://github.com/Nathan22177
 */
public class NewGameResolver {
    /**
     * Instance of {@link Random} used to determine conditions.
     **/
    private final static Random rand = new Random();

    /**
     * Produces random conditions.
     * */
    private static Conditions getRandomConditions() {
        return new Conditions(getRandomCondition(QUANTITY_POLL), getRandomCondition(MONEY_POLL));
    }

    /**
     * Possible winnable QUs.
     * */
    private final static List<Integer> QUANTITY_POLL = Arrays.asList(8, 10, 12, 14, 16, 18, 20, 22, 24);

    /**
     * Possible initial balances.
     * */
    private final static List<Integer> MONEY_POLL = Arrays.asList(1000, 2000, 3000, 4000, 5000, 10_000, 20_000, 80_000);

    /**
     * Used to create new {@link PlayerVersusBotGame}.
     * */
    public static PlayerVersusBotGame createNewGameAgainstTheBot(Bot bot) {
        return new PlayerVersusBotGame(getRandomConditions(), bot);
    }

    /**
     * Used to create new {@link PlayerVersusPlayerGame}.
     * */
    public static PlayerVersusPlayerGame createNewGameAgainstThePlayer(String name) {
        return new PlayerVersusPlayerGame(getRandomConditions(), name);
    }

    public static Map<String, Bot> getAvailableBots() {
        return Arrays.stream(Bot.values()).collect(Collectors.toMap(Bot::name, Function.identity()));
    }

    public static String getRandomBot() {
        List<String> bots = getAvailableBots()
                .keySet()
                .stream()
                .filter(key -> !key.equalsIgnoreCase("RANDOM"))
                .collect(Collectors.toList());
        return bots.get(rand.nextInt(bots.size() - 1));

    }
    private static Integer getRandomCondition(List<Integer> poll) {
        return poll.get(rand.nextInt(poll.size()));
    }
}
