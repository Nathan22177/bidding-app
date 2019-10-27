package com.nathan22177.strategies;

import com.nathan22177.bidder.BidderBot;

public interface BiddingStrategy {

    int getBiddingAmount(BidderBot bidder);

}
