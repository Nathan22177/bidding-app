package com.nathan22177.strategies;

import com.nathan22177.bidder.BidderBot;
import com.nathan22177.util.StrategyUtil;

/***
 * My own strategy.
 * */
public class NathanStrategy implements BiddingStrategy{

    @Override
    public int getBiddingAmount(BidderBot bidder) {

        /*
          We will always safely bet 2 until opponent runs out of cash.
        */
        boolean thereAreMoreRoundsThanMonetaryUnits = bidder.getConditions().getQuantity() / 2 > bidder.getBalance();

        int roundsLeft = StrategyUtil.getRoundsLeft(bidder);
        int initialQuantity = bidder.getConditions().getQuantity();

        int medianBid = StrategyUtil.allBidsMedian(bidder) + initialQuantity;
        int defaultBid = !thereAreMoreRoundsThanMonetaryUnits
                ? (bidder.getBalance() / (roundsLeft * 2)) + (initialQuantity - roundsLeft) + 2
                : 1;
        int price = bidder.getConditions().getCash() / bidder.getConditions().getQuantity();

        /*
          If opponent consistently bids the same amount we can easily outbid them.
          */
        if (bidder.getBiddingHistory() != null
                && bidder.getBiddingHistory().size() > 2
                && StrategyUtil.opponentBidsTheSameLastNRounds(2, bidder)) {
            defaultBid = StrategyUtil.getLastOpponentBid(bidder) + 1;
        }

        /*
          If we already acquired enough QU to win we don't need to bid.
          We also shouldn't bid if we don't have the money.
          */
        if (StrategyUtil.bidderHasReachedTargetAmount(bidder) || bidder.getBalance() == 0) {
            return 0;
        }

        /*
          If opponent went bankrupt we can just place the minimal amount;
          */
        if (StrategyUtil.getOpponentBalance(bidder) <= 0) {
            return 1;
        }

        /*
          If we can definitely win auction this round - we would like to do so
          by outbidding the opponent
          */
        if (bidder.getAcquiredAmount() + 2 >= bidder.getConditions().getQuantity() / 2) {
            if (StrategyUtil.bidderHasAdvantageOverItsOpponent(bidder)) {
                return StrategyUtil.getOpponentBalance(bidder) + 1;
            } else if (bidder.getConditions().getQuantity() == 2) {
                return bidder.getBalance();
            }
        }

        /*
          Most implementations tend to skip first bid to gain advantage
          or place very little so we will try to win it
          by placing half of price of 1 QU.

          We also want to use this when we're risking to outspend our
          opponent by always raising the bid.

          Although we want to skip it if we are playing against always raising opponent.
         */
        if (!StrategyUtil.opponentAlwaysRaises(bidder)
                && (bidder.getBiddingHistory() == null  || bidder.getBiddingHistory().size() == 0
                || StrategyUtil.bidsOverMeanPriceForNRounds(bidder, 4))) {

            /*
              Unless it is the only round or last round, then we go all-in.
             * */
            if (bidder.getConditions().getQuantity() == 2 || StrategyUtil.getRoundsLeft(bidder) <= 1) {
                return bidder.getBalance();
            }

            if (bidder.getBalance() > price / 2) {
                return price / 2;
            }
        }

        /*
          If our default bid is to large for our balance we either
          bid median plus initialQuantity
          or just bid random int within our budget.
          */
        if (defaultBid > bidder.getBalance() || defaultBid < 0) {
            return medianBid > bidder.getBalance()
                    ? bidder.getRandom().nextInt(bidder.getBalance())
                    : medianBid;
        }

        return defaultBid;
    }
}
