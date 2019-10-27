package com.nathan22177.strategies;

import com.nathan22177.bidder.BidderBot;
import com.nathan22177.util.StrategyUtil;

/***
 * PyramidPlayers AdvancedBidder strategy refactored and appropriated.
 * Comments remain true to the source.
 * */
public class PyramidPlayerStrategy implements BiddingStrategy {

    @Override
    public int getBiddingAmount(BidderBot bidder) {
        int spread = bidder.getConditions().getCash() / bidder.getConditions().getQuantity() * 2;
        int restWinRounds = bidder.getConditions().getQuantity() / 2 + 1 - bidder.getAcquiredAmount();
        if (restWinRounds == 0) {
            return 0;
        }

        int restRounds = StrategyUtil.getRoundsLeft(bidder);
        int winRounds = restRounds / 2 + 1;
        int winBid = bidder.getBalance() / winRounds;

        int bid = winBid - spread + bidder.getRandom().nextInt(spread * 2 + 1);

        if (bid > bidder.getBalance() || bid < 0){
            return bidder.getBalance();
        }

        return bid;
    }
}
