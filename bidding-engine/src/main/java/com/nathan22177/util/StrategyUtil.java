package com.nathan22177.util;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.util.Assert;


import com.nathan22177.bidder.BidderBot;
import com.nathan22177.collection.BiddingRound;

/**
 * Class with methods to use while implementing the strategy.
 */
public class StrategyUtil {

    /***
     * Retrieves the median all the bids.
     * @param bidder instance of bidder
     * @return median
     * */
    public static int allBidsMedian(BidderBot bidder) {
        return allBidsMedian(bidder.getBiddingHistory());
    }

    /***
     * Retrieves the median all the bids.
     * @param history of all bids
     * @return median
     * */
    public static int allBidsMedian(List<BiddingRound> history) {



        double[] bids = history.stream()
                .flatMap(biddingRound -> Stream.of(biddingRound.getOwnBid(), biddingRound.getOpponentBid()))
                .mapToDouble(val -> val)
                .sorted()
                .toArray();
        if (bids.length == 0) {
            return 0;
        }

        if (bids.length == 1) {
            return (int) bids[0];
        }

        if (bids.length == 2) {
            return (int) (bids[0] + bids[1]) / 2;
        }
        return (int) (bids[bids.length / 2] + bids[(bids.length / 2) + 1]) / 2;

    }

    /***
     * Retrieves {@param bidder's} opponent's balance
     * after all the bids they have made.
     * @return current balance
     * */
    public static int getOpponentBalance(BidderBot bidder) {
        return bidder.getBiddingHistory() != null && bidder.getBiddingHistory().size() > 0
                ? bidder.getConditions().getCash() - bidder.getBiddingHistory().stream().mapToInt(BiddingRound::getOpponentBid).sum()
                : bidder.getConditions().getCash();
    }

    /***
     * Retrieves mean price of one QU.
     * @param bidder instance of bidder
     * @return price
     * */
    public static int getMeanPriceForOneUnit(BidderBot bidder) {
        return bidder.getConditions().getCash() / bidder.getConditions().getQuantity();
    }

    /***
     * Retrieves last {@param n} bids of a {@param bidder}.
     * @return FIFO list of bids
     * */
    public static List<Integer> getLastNBids(int n, BidderBot bidder) {
        return bidder.getBiddingHistory()
                .stream()
                .map(BiddingRound::getOwnBid)
                .skip(bidder.getBiddingHistory().size() - n)
                .collect(Collectors.toList());
    }

    /***
     * Retrieves last {@param n} bids of a {@param bidder's} opponent.
     * @return FIFO list of bids
     * */
    public static List<Integer> getLastNOpponentBids(int n, BidderBot bidder) {
        return bidder.getBiddingHistory()
                .stream()
                .map(BiddingRound::getOpponentBid)
                .skip(bidder.getBiddingHistory().size() - n)
                .collect(Collectors.toList());
    }

    /***
     * Determines if {@param bidder} bidded over
     * mean price of two QUs for last {@param n} bids.
     * */
    public static boolean bidsOverMeanPriceForNRounds(BidderBot bidder, int n) {
        if (bidder.getBiddingHistory() == null || bidder.getBiddingHistory().size() == 0
                || bidder.getBiddingHistory().size() < n
                || n <= 1) {
            return false;
        }
        return getMeanPriceForOneUnit(bidder) * 2 < Collections.min(getLastNBids(n, bidder));
    }

    /***
     * Determines if {@param bidder} has more money
     * than their opponent.
     * */
    public static boolean bidderHasAdvantageOverItsOpponent(BidderBot bidder) {
        if (bidder.getBiddingHistory() == null  || bidder.getBiddingHistory().size() == 0) {
            return false;
        }
        return bidder.getBalance() > getOpponentBalance(bidder);
    }

    /***
     * Retrieves bid that won last round.
     * @param bidder - the instance of a bidder
     * @return bid
     * */
    public static int getPreviousWinnerBid(BidderBot bidder) {
        return bidder.getBiddingHistory() != null && bidder.getBiddingHistory().size() > 0
                ? Stream.of(bidder.getBiddingHistory().get(bidder.getBiddingHistory().size() - 1).getOwnBid(), bidder.getBiddingHistory().get(bidder.getBiddingHistory().size()-1).getOpponentBid())
                .mapToInt(value -> value)
                .max()
                .orElse(0)
                : 0;
    }

    /***
     * Determines if {@param bidder} has enough QU
     * to win.
     * */
    public static boolean bidderHasReachedTargetAmount(BidderBot bidder) {
        return bidder.getAcquiredAmount() >= (bidder.getConditions().getQuantity() / 2) + 1;
    }

    public static int getRoundsLeft(BidderBot bidder) {
        return bidder.getBiddingHistory() == null  || bidder.getBiddingHistory().size() == 0
                ? bidder.getConditions().getQuantity() / 2
                : (bidder.getConditions().getQuantity() / 2) - bidder.getBiddingHistory().size();
    }

    public static int getRoundsToWin(BidderBot bidder) {
        return (bidder.getConditions().getQuantity() / 4) + 1;
    }

    /***
     * Determines if {@param bidder's} opponent always
     * raises it's bid.
     * */
    public static boolean opponentAlwaysRaises(BidderBot bidder) {
        if (bidder.getBiddingHistory() == null || bidder.getBiddingHistory().size() <= 1) {
            return false;
        }
        List<Integer> bids = bidder.getBiddingHistory()
                .stream()
                .map(BiddingRound::getOpponentBid)
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());

        return IntStream.range(1, bids.size())
                .allMatch(i -> bids.get(i) < bids.get(i - 1));
    }

    /***
     * Retrieves last bid of {@param bidder's} opponent.
     * @return bid
     * */
    public static int getLastOpponentBid(BidderBot bidder) {
        return bidder.getBiddingHistory().get(bidder.getBiddingHistory().size()-1).getOpponentBid();
    }

    /***
     * Determines if {@param bidder's} opponent bids
     * the same amount for the last {@param n} rounds.
     * */
    public static boolean opponentBidsTheSameLastNRounds(int n, BidderBot bidder) {
        if (bidder.getBiddingHistory() == null  || bidder.getBiddingHistory().size() == 0) {
            return false;
        }

        Assert.isTrue(n > 1, "Can't check if he bids the same if we only check 1 or less round");
        Assert.isTrue(bidder.getBiddingHistory().size() >= n, "Can't check for not existing rounds");

        if (bidder.getBiddingHistory() == null  || bidder.getBiddingHistory().size() == 0) {
            return false;
        }

        int lastBid = bidder.getBiddingHistory().get(bidder.getBiddingHistory().size()-1).getOpponentBid();
        return getLastNOpponentBids(n, bidder).stream().sorted(Collections.reverseOrder()).skip(1).allMatch(bid -> bid == lastBid);
    }
}
