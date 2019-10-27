package com.nathan22177.biddingcommon;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.nathan22177.bidder.BidderBot;
import com.nathan22177.collection.BiddingRound;
import com.nathan22177.enums.Opponent;
import com.nathan22177.game.Conditions;
import com.nathan22177.util.StrategyUtil;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class StrategyUtilUnitTests {

    private BidderBot bidder;
    private BidderBot opponent;
    private int startQuantity;
    private int startBalance;

    @Before
    public void init() {
        startBalance = 10_000;
        startQuantity = 100;
        bidder = new BidderBot(new Conditions(startQuantity, startBalance), Opponent.RISING_BOT);
        opponent = new BidderBot(new Conditions(startQuantity, startBalance), Opponent.FAIR_BOT);
        for (int i = 0; i < startQuantity / 2; i++) {
            int bidderBid = bidder.getNextBid();
            int opponentBid = opponent.getNextBid();
            bidder.placeBidAndWithdraw(bidderBid);
            opponent.placeBidAndWithdraw(opponentBid);
            bidder.resolveBidsAndAppendHistory(bidderBid, opponentBid);
            opponent.resolveBidsAndAppendHistory(opponentBid, bidderBid);
        }
        /*
         * At the end of the game:
         *
         * bidder's balance: 803
         * bidder's QU: 24
         *
         * opponent's balance: 0
         * opponent's QU: 76
         * */
        Assert.assertEquals(803, bidder.getBalance());
        Assert.assertEquals(24, bidder.getAcquiredAmount());

        Assert.assertEquals(0, opponent.getBalance());
        Assert.assertEquals(76, opponent.getAcquiredAmount());

        /*
         *  Bids:                           Winner:
         *  _________________________
         *  |           |           |
         *  | bidder:   | opponent: |
         *  |___________|___________|
         *  |   102     |    200    |       opponent
         *  |   103     |    200    |       opponent
         *  |   100     |    200    |       opponent
         *  |   101     |    200    |       opponent
         *  |   106     |    200    |       opponent
         *  |   107     |    200    |       opponent
         *  |   104     |    200    |       opponent
         *  |   105     |    200    |       opponent
         *  |   111     |    200    |       opponent
         *  |   108     |    200    |       opponent
         *  |   109     |    200    |       opponent
         *  |   115     |    200    |       opponent
         *  |   112     |    200    |       opponent
         *  |   118     |    200    |       opponent
         *  |   116     |    200    |       opponent
         *  |   117     |    200    |       opponent
         *  |   123     |    200    |       opponent
         *  |   121     |    200    |       opponent
         *  |   127     |    200    |       opponent
         *  |   125     |    200    |       opponent
         *  |   131     |    200    |       opponent
         *  |   129     |    200    |       opponent
         *  |   132     |    200    |       opponent
         *  |   138     |    200    |       opponent
         *  |   137     |    200    |       opponent
         *  |   140     |    200    |       opponent
         *  |   147     |    200    |       opponent
         *  |   150     |    200    |       opponent
         *  |   149     |    200    |       opponent
         *  |   153     |    200    |       opponent
         *  |   157     |    200    |       opponent
         *  |   161     |    200    |       opponent
         *  |   165     |    200    |       opponent
         *  |   174     |    200    |       opponent
         *  |   176     |    200    |       opponent
         *  |   186     |    200    |       opponent
         *  |   188     |    200    |       opponent
         *  |   196     |    200    |       opponent
         *  |   204     |    200    |        bidder
         *  |   218     |    200    |        bidder
         *  |   224     |    200    |        bidder
         *  |   237     |    200    |        bidder
         *  |   252     |    200    |        bidder
         *  |   275     |    200    |        bidder
         *  |   293     |    200    |        bidder
         *  |   327     |    200    |        bidder
         *  |   364     |    200    |        bidder
         *  |   425     |    200    |        bidder
         *  |   533     |    200    |        bidder
         *  |   806     |    200    |        bidder
         *  |___________|___________|
         *
         * */
    }


    @Test
    public void allBidsMedianTest() {
        // Should be {2, 3, 5, 9, 14, 17} after .flatMap() and .sorted()
        // Median should be 7
        LinkedList<BiddingRound> mockHistory = new LinkedList<>();
        mockHistory.add(new BiddingRound(5, 17));
        mockHistory.add(new BiddingRound(3, 9));
        mockHistory.add(new BiddingRound(14, 2));
        mockHistory.add(new BiddingRound(2, 5));
        mockHistory.add(new BiddingRound(9, 3));

        int median = StrategyUtil.allBidsMedian(mockHistory);

        Assert.assertEquals(7, median);
    }

    @Test
    public void getOpponentBalanceTest() {
        /*
        * Comparing balance of particular bidder with the
        * result of its opponent's call of getOpponentBalance().
        * */
        Assert.assertEquals(opponent.getBalance(), StrategyUtil.getOpponentBalance(bidder));
        Assert.assertEquals(bidder.getBalance(), StrategyUtil.getOpponentBalance(opponent));
    }

    @Test
    public void getMeanPriceForOneUnit() {
        /*
         * Checking that mean price is in fact 100 for
         * when initial QU is 1000 and initial cash is 10.000,
         * additionally checking that both bidders got the same result.
         * */
        Assert.assertEquals(100, StrategyUtil.getMeanPriceForOneUnit(bidder));
        Assert.assertEquals(100, StrategyUtil.getMeanPriceForOneUnit(opponent));
    }

    @Test
    public void getLastNBidsTest() {
        // Bidders last 5 bids FIFO
        List bidders = Arrays.asList(327, 364, 425, 533, 806);

        // Opponents last 5 bids FIFO
        List opponents = Arrays.asList(200, 200, 200, 200, 200);

        Assert.assertEquals(bidders, StrategyUtil.getLastNBids(5, bidder));
        Assert.assertEquals(opponents, StrategyUtil.getLastNBids(5, opponent));
    }

    @Test
    public void getLastNOpponentBidsTest() {
        // Bidders last 5 bids FIFO
        List bidders = Arrays.asList(327, 364, 425, 533, 806);

        // Opponents last 5 bids FIFO
        List opponents = Arrays.asList(200, 200, 200, 200, 200);

        Assert.assertEquals(opponents, StrategyUtil.getLastNOpponentBids(5, bidder));
        Assert.assertEquals(bidders, StrategyUtil.getLastNOpponentBids(5, opponent));
    }

    @Test
    public void bidderRaisesForNRoundsTest() {
        // Bidder raises over mean price for last 12 rounds
        Assert.assertTrue(StrategyUtil.bidsOverMeanPriceForNRounds(bidder, 5));
        Assert.assertTrue(StrategyUtil.bidsOverMeanPriceForNRounds(bidder, 10));
        Assert.assertTrue(StrategyUtil.bidsOverMeanPriceForNRounds(bidder, 12));

        // Opponent never raises over mean price
        Assert.assertFalse(StrategyUtil.bidsOverMeanPriceForNRounds(opponent, 0));
        Assert.assertFalse(StrategyUtil.bidsOverMeanPriceForNRounds(opponent, 1));
        Assert.assertFalse(StrategyUtil.bidsOverMeanPriceForNRounds(opponent, 2));
        Assert.assertFalse(StrategyUtil.bidsOverMeanPriceForNRounds(opponent, 10));
    }

    @Test
    public void bidderHasUpperHandOverItsOpponentTest() {
        Assert.assertTrue(StrategyUtil.bidderHasAdvantageOverItsOpponent(bidder));
        Assert.assertFalse(StrategyUtil.bidderHasAdvantageOverItsOpponent(opponent));
    }


    @Test
    public void getPreviousWinnerBidTest() {
        Assert.assertEquals(806, StrategyUtil.getPreviousWinnerBid(bidder));
        Assert.assertEquals(806, StrategyUtil.getPreviousWinnerBid(opponent));
    }

    @Test
    public void bidderHasReachedTargetAmountTest() {
        Assert.assertTrue(StrategyUtil.bidderHasReachedTargetAmount(opponent));
        Assert.assertFalse(StrategyUtil.bidderHasReachedTargetAmount(bidder));
    }

    @Test
    public void getRoundsLeftTest() {
        Assert.assertEquals(0, StrategyUtil.getRoundsLeft(bidder));
        Assert.assertEquals(0, StrategyUtil.getRoundsLeft(opponent));
    }

    @Test
    public void opponentAlwaysRaisesTest() {
        Assert.assertTrue(StrategyUtil.opponentAlwaysRaises(opponent));
        Assert.assertFalse(StrategyUtil.opponentAlwaysRaises(bidder));
    }

    @Test
    public void getLastOpponentBidTest() {
        Assert.assertEquals(806, StrategyUtil.getLastOpponentBid(opponent));
        Assert.assertEquals(200, StrategyUtil.getLastOpponentBid(bidder));
    }

    @Test
    public void opponentBidsTheSameLastNRoundsTest() {
        Assert.assertTrue(StrategyUtil.opponentBidsTheSameLastNRounds(2, bidder));
        Assert.assertTrue(StrategyUtil.opponentBidsTheSameLastNRounds(4, bidder));
        Assert.assertTrue(StrategyUtil.opponentBidsTheSameLastNRounds(8, bidder));
        Assert.assertTrue(StrategyUtil.opponentBidsTheSameLastNRounds(16, bidder));

        Assert.assertFalse(StrategyUtil.opponentBidsTheSameLastNRounds(2, opponent));
        Assert.assertFalse(StrategyUtil.opponentBidsTheSameLastNRounds(4, opponent));
        Assert.assertFalse(StrategyUtil.opponentBidsTheSameLastNRounds(8, opponent));
        Assert.assertFalse(StrategyUtil.opponentBidsTheSameLastNRounds(16, opponent));
    }
}
