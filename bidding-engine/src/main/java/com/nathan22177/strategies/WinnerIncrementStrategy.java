package com.nathan22177.strategies;

import com.nathan22177.bidder.BidderBot;
import com.nathan22177.util.StrategyUtil;

/***
 * Waits for an advantage then bids last winning bid plus one;
 * */
public class WinnerIncrementStrategy implements BiddingStrategy {

    private int calculateBiddingAmount(BidderBot bidder) {
        if (StrategyUtil.bidderHasAdvantageOverItsOpponent(bidder)) {
            return StrategyUtil.getPreviousWinnerBid(bidder) + 1;
        } else {
            return 0;
        }
    }

    @Override
    public int getBiddingAmount(BidderBot bidder) {
        int bid = calculateBiddingAmount(bidder);
        return bid >= 0 && bid <= bidder.getBalance()
                ? bid
                : 0;
    }
}
