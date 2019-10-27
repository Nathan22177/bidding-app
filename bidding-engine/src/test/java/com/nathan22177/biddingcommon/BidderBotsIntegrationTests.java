package com.nathan22177.biddingcommon;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.nathan22177.bidder.BidderBot;
import com.nathan22177.enums.Opponent;
import com.nathan22177.game.Conditions;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BidderBotsIntegrationTests {
    private List<Integer> cashPoll = Arrays.asList(1000, 5000, 10_000, 50_000, 100_000, 500_000, 1000_000, 10_000_000);
    private List<Integer> quantityPoll = Arrays.asList(2, 4, 8, 16, 20, 30, 40, 50, 80, 100, 200, 400, 800, 1000);

    @Test
    public void NathanVsWinnerIncrementBidder() {
        // Wins or gets a draw 96 games out of 112 every test (≈ 85.7% win or draw rate)
        twoStrategiesCompetition(Opponent.NATHAN22177_BOT, Opponent.WIN_INC_BOT, 95);
    }

    @Test
    public void NathanVsRaiseBidder() {
        // Wins or gets a draw 109 games out of 112 every test (≈ 97.3% win or draw rate)
        twoStrategiesCompetition(Opponent.NATHAN22177_BOT, Opponent.RISING_BOT, 109);
    }

    @Test
    public void NathanVsCopycat() {
        // Wins or gets a draw 95 games out of 112 every test (≈ 84.8% - 98.2%  win or draw rate)
        twoStrategiesCompetition(Opponent.NATHAN22177_BOT, Opponent.COPYCAT_BOT, 96);
    }

    @Test
    public void NathanVsSafeBidder() {
        // Wins or gets a draw 109 games out of 112 every test (≈ 97.3% win or draw rate)
        twoStrategiesCompetition(Opponent.NATHAN22177_BOT, Opponent.SAFE_BOT, 109);
    }

    @Test
    public void NathanVsFairBidder() {
        // Wins or gets a draw 102 games out of 112 every test (≈ 91.1%  win or draw rate)
        twoStrategiesCompetition(Opponent.NATHAN22177_BOT, Opponent.FAIR_BOT, 102);
    }

    @Test
    public void NathanVsLehaSVV2009() {
        // Wins or gets a draw in at least 95 games every test, sometimes up to 110 times due to it's opponent randomised behaviour
        // (≈ 84.8% - 98.2%  win or draw rate)
        twoStrategiesCompetition(Opponent.NATHAN22177_BOT, Opponent.LEHASVV2009_BOT, 95);
    }

    @Test
    public void NathanVsPyramidPlayer() {
        // Wins or gets a draw in at least 52 games every test, sometimes up to 70 times due to it's opponent randomised behaviour
        // (≈ 46.4% - 74.3%  win or draw rate)
        twoStrategiesCompetition(Opponent.NATHAN22177_BOT, Opponent.PYRAMID_PLAYER_BOT, 52);
    }

    @Test
    public void LehaSVV2009PyramidPlayer() {
        // Wins or gets a draw in at least 92 games every test, sometimes up to 110 times due to it's opponent randomised behaviour
        // (≈ 82.1% - 91.1%  win or draw rate)
        twoStrategiesCompetition(Opponent.LEHASVV2009_BOT, Opponent.PYRAMID_PLAYER_BOT, 92);
    }

    public void twoStrategiesCompetition(Opponent bidder, Opponent opponent, int winningThreshold) {
        int winOrDrawCount = 0;
        for (int cash : cashPoll) {
            for (int quantity : quantityPoll) {
                BidderBot bidderBot = new BidderBot(new Conditions(quantity, cash), bidder);
                BidderBot opponentBot = new BidderBot(new Conditions(quantity, cash), opponent);
                for (int i = 0; i < quantity / 2; i++) {
                    int bidderBid = bidderBot.getNextBid();
                    int opponentBid = opponentBot.getNextBid();
                    bidderBot.placeBidAndWithdraw(bidderBid);
                    opponentBot.placeBidAndWithdraw(opponentBid);
                    bidderBot.resolveBidsAndAppendHistory(bidderBid, opponentBid);
                    opponentBot.resolveBidsAndAppendHistory(opponentBid, bidderBid);
                }
                if (bidderBot.getAcquiredAmount() >= opponentBot.getAcquiredAmount()) {
                    winOrDrawCount++;
                }
            }
        }
        Assert.assertTrue(winOrDrawCount >= winningThreshold);
    }
}
