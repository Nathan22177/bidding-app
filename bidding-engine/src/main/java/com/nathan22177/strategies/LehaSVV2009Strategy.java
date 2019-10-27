package com.nathan22177.strategies;

import com.nathan22177.bidder.BidderBot;
import com.nathan22177.util.StrategyUtil;


/***
 * lehaSVV2009s AwesomeBidder strategy refactored and appropriated.
 * Comments remain true to the source.
 * */
public class LehaSVV2009Strategy implements BiddingStrategy {

    @Override
    public int getBiddingAmount(BidderBot bidder) {
        int bid = calculateBiddingAmount(bidder);
        return bid >= 0 && bid <= bidder.getBalance()
                ? bid
                : 0;
    }

    private static int calculateBiddingAmount(BidderBot bidder) {
        // Skip calculations if there is no cash or turns
        if (bidder.getBalance() == 0 || StrategyUtil.getRoundsLeft(bidder) == 0) {
            return 0;
        }

        // Don't waste money if opponent's cash is 0
        if (StrategyUtil.getOpponentBalance(bidder) == 0) {
            return 1;
        }

        // Place maximum bid if there is only one round
        if (bidder.getConditions().getQuantity() == 2) {
            return bidder.getBalance();
        }

        // Check if it is possible to win by placing opponent's cash + 1 (when opponent's cash is too small)
        long minimumTurnsToWin = (bidder.getConditions().getQuantity() / 2) - (bidder.getAcquiredAmount() / 2);
        if (minimumTurnsToWin > 0 && bidder.getBalance() >= (StrategyUtil.getOpponentBalance(bidder) + 1) * minimumTurnsToWin) {
            return StrategyUtil.getOpponentBalance(bidder) + 1;
        }

        // First bid is always small to not waste all money from the start (the algorithm is aggressive)
        if (bidder.getBiddingHistory() != null
                && bidder.getBiddingHistory().size() == 0) {
            int firstBid = bidder.getRandom().nextBoolean() ? 1 : 2;
            return firstBid > bidder.getBalance()
                    ? bidder.getRandom().nextInt(bidder.getBalance())
                    : firstBid;
        }

        // According to statistics median plus two bidder wins even random algorithm on small quantity
        if (bidder.getConditions().getQuantity() <= 10) {
            int median = StrategyUtil.allBidsMedian(bidder);
            return median + 2 > bidder.getBalance()
                    ? bidder.getRandom().nextInt(bidder.getBalance())
                    : median + 2;
        }

        // Not allow bot to make too many large bids
        // If previous 2-4 rounds all the bids were greater than initial mean, place smaller one
        if (StrategyUtil.bidsOverMeanPriceForNRounds(bidder, bidder.getRandom().nextInt(3) + 2)) {
            int smallBidLimit = StrategyUtil.getMeanPriceForOneUnit(bidder);
            int smallBid = bidder.getRandom().nextInt(smallBidLimit != 0 ? smallBidLimit : 1);
            return smallBid > bidder.getBalance()
                    ? bidder.getRandom().nextInt(bidder.getBalance())
                    : smallBid;
        }

        // Use winner plus one or two algorithm
        int previousWinnerBid = StrategyUtil.getPreviousWinnerBid(bidder);
        int nextValue = previousWinnerBid + (bidder.getRandom().nextBoolean() ? 1 : 2);
        return nextValue > bidder.getBalance()
                ? bidder.getRandom().nextInt(bidder.getBalance())
                : nextValue;
    }
}
