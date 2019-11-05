package com.nathan22177.bidder.strategies;

import com.nathan22177.bidder.BidderBot;
import com.nathan22177.util.StrategyUtil;

/**
 * Waits for an advantage then bids last winning bid plus one;
 * */
public class WinnerIncrementStrategy {
    public static int getBiddingAmount(BidderBot bidder) {
        int bid = calculateBiddingAmount(bidder);
        return bid >= 0 && bid <= bidder.getBalance()
                ? bid
                : 0;
    }

    private static int calculateBiddingAmount(BidderBot bidder) {
        if (StrategyUtil.bidderHasAdvantageOverItsOpponent(bidder)) {
            return StrategyUtil.getPreviousWinnerBid(bidder) + 1;
        } else {
            return 0;
        }
    }
}
