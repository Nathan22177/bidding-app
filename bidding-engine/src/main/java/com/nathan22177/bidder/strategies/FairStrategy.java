package com.nathan22177.bidder.strategies;

import com.nathan22177.bidder.BidderBot;
import com.nathan22177.util.StrategyUtil;

/**
 * Always bids mean price of 2 QU's.
 * */
public class FairStrategy {

    public static int getBiddingAmount(BidderBot bidder) {
        return StrategyUtil.getMeanPriceForOneUnit(bidder) * 2;
    }
}
