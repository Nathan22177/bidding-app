# bidding-engine

## Classes to look at:
* [BidderImpl](src/main/java/com/nathan22177/bidder/BidderImpl.java) - class to instantiate and use for competition.
* [NathanStrategy](src/main/java/com/nathan22177/strategies/NathanStrategy.java) - a strategy I came up with.

## Util classes:
* [StrategyUtil](src/main/java/com/nathan22177/util/StrategyUtil.java) - class that provides static methods to formulate and implement strategies.

## The [NathanStrategy](src/main/java/com/nathan22177/strategies/NathanStrategy.java) was tested against the following strategies:
* [CopycatStrategy](src/main/java/com/nathan22177/strategies/CopycatStrategy.java) - bids it's bot's last bid plus one if has an advantage, else skips round.
* [FairStrategy](src/main/java/com/nathan22177/strategies/FairStrategy.java) - always bids mean price of 2 QU's.
* [RisingStrategy](src/main/java/com/nathan22177/strategies/RisingStrategy.java) - gradually raises bid so that would go with empty balance at the end.
* [SafeStrategy](src/main/java/com/nathan22177/strategies/SafeStrategy.java) - waits for advantage then bids median plus 2.
* [WinnerIncrementStrategy](src/main/java/com/nathan22177/strategies/WinnerIncrementStrategy.java) - waits for advantage then bids last winning bid plus one.
* [LehaSVV2009Strategy](src/main/java/com/nathan22177/strategies/LehaSVV2009Strategy.java) - strategy formulated and implemented by @lehaSVV2009.
* [PyramidPlayerStrategy](src/main/java/com/nathan22177/strategies/PyramidPlayerStrategy.java) - strategy formulated and implemented by @PyramidPlayer.

## RULES:
A product x QU (quantity units) will be auctioned under 2 parties. The parties have each y MU (monetary units) for auction. They offer then simultaneously an arbitrary number of its MU on the first 2 QU of the product. After that, the bids will be visible to both. The 2 QU of the product is awarded to who has offered the most MU; if both bid the same, then both get 1 QU. Both bidders must pay their amount - including the defeated. A bid of 0 MU is allowed. Bidding on each 2 QU is repeated until the supply of x QU is fully auctioned. Each bidder aims to get a larger amount than its competitor.
In an auction wins the program that is able to get more QU than the other. With a tie, the program that retains more MU wins.

* [Bidder](src/main/java/com/nathan22177/bidder/Bidder.java) - the bidder interface.
