package com.nathan22177.biddingclient;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.nathan22177.BiddingServerApplication;
import com.nathan22177.bidder.BidderBot;
import com.nathan22177.enums.Bot;
import com.nathan22177.game.Conditions;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BiddingServerApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BidderBotsIntegrationTests {
    private List<Integer> moneyPoll = Arrays.asList(1000, 5000, 10_000, 50_000, 100_000, 500_000, 1000_000, 10_000_000);
    private List<Integer> quantityPoll = Arrays.asList(2, 4, 8, 16, 20, 30, 40, 50, 80, 100, 200, 400, 800, 1000);

    @Test
    public void NathanVsWinnerIncrementBidder() {
        // Wins or gets a draw 96 games out of 112 every test (≈ 85.7% win or draw rate)
        twoStrategiesCompetition(Bot.NATHAN22177_BOT, Bot.WIN_INC_BOT, 95);
    }

    @Test
    public void NathanVsRaiseBidder() {
        // Wins or gets a draw 109 games out of 112 every test (≈ 97.3% win or draw rate)
        twoStrategiesCompetition(Bot.NATHAN22177_BOT, Bot.RISING_BOT, 109);
    }

    @Test
    public void NathanVsCopycat() {
        // Wins or gets a draw 95 games out of 112 every test (≈ 84.8% - 98.2%  win or draw rate)
        twoStrategiesCompetition(Bot.NATHAN22177_BOT, Bot.COPYCAT_BOT, 96);
    }

    @Test
    public void NathanVsSafeBidder() {
        // Wins or gets a draw 109 games out of 112 every test (≈ 97.3% win or draw rate)
        twoStrategiesCompetition(Bot.NATHAN22177_BOT, Bot.SAFE_BOT, 109);
    }

    @Test
    public void NathanVsFairBidder() {
        // Wins or gets a draw 102 games out of 112 every test (≈ 91.1%  win or draw rate)
        twoStrategiesCompetition(Bot.NATHAN22177_BOT, Bot.FAIR_BOT, 102);
    }

    @Test
    public void NathanVsLehaSVV2009() {
        // Wins or gets a draw in at least 95 games every test, sometimes up to 110 times due to it's opponent randomised behaviour
        // (≈ 84.8% - 98.2%  win or draw rate)
        twoStrategiesCompetition(Bot.NATHAN22177_BOT, Bot.LEHASVV2009_BOT, 95);
    }

    @Test
    public void NathanVsPyramidPlayer() {
        // Wins or gets a draw in at least 52 games every test, sometimes up to 70 times due to it's opponent randomised behaviour
        // (≈ 46.4% - 74.3%  win or draw rate)
        twoStrategiesCompetition(Bot.NATHAN22177_BOT, Bot.PYRAMID_PLAYER_BOT, 52);
    }

    @Test
    public void LehaSVV2009PyramidPlayer() {
        // Wins or gets a draw in at least 92 games every test, sometimes up to 110 times due to it's opponent randomised behaviour
        // (≈ 82.1% - 91.1%  win or draw rate)
        twoStrategiesCompetition(Bot.LEHASVV2009_BOT, Bot.PYRAMID_PLAYER_BOT, 92);
    }

    public void twoStrategiesCompetition(Bot bidder, Bot bot, int winningThreshold) {
        int winOrDrawCount = 0;
        for (int money : moneyPoll) {
            for (int quantity : quantityPoll) {
                BidderBot bidderBot = new BidderBot(new Conditions(quantity, money), bidder);
                BidderBot opponentBot = new BidderBot(new Conditions(quantity, money), bot);
                for (int i = 0; i < quantity / 2; i++) {
                    int bidderBid = bidderBot.getNextBid();
                    int opponentBid = opponentBot.getNextBid();
                    bidderBot.withdrawBiddingAmount(bidderBid);
                    opponentBot.withdrawBiddingAmount(opponentBid);
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
