package com.nathan22177.websocket;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.util.Assert;

import com.nathan22177.bidder.BidderPlayer;
import com.nathan22177.collection.BiddingRound;
import com.nathan22177.enums.MessageType;
import com.nathan22177.enums.Player;
import com.nathan22177.game.PlayerVersusPlayerGame;
import com.nathan22177.repositories.VersusPlayerRepository;
import com.nathan22177.websocket.messages.incoming.IncomingMessage;
import com.nathan22177.websocket.messages.outgoing.OutgoingMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ServerEndpoint(value = "pvp/{gameId}/{username}",
        encoders = OutgoingMessageEncoder.class,
        decoders = IncomingMessageDecoder.class)
public class GameEndpoint {

    private static Set<GameSession> gameSessions = new HashSet<>();
    private static Set<IncomingMessage> bids = new HashSet<>();


    @Autowired
    VersusPlayerRepository versusPlayerRepository;

    @OnOpen
    public void onOpen(Session session, @PathParam("gameId") Long gameId, @PathParam("username") String username) throws IOException {
        PlayerVersusPlayerGame game = versusPlayerRepository.getOne(gameId);
        BidderPlayer player = game.getPlayerByUsername(username);
        GameSession newGameSession = new GameSession(gameId, session, username, player.getPlayer());
        broadcast(new OutgoingMessage(newGameSession.getGameId(), MessageType.PLAYER_JOINED));
        gameSessions.add(newGameSession);
    }

    @OnMessage
    public void onMessage(IncomingMessage incomingMessage) {
        Optional<IncomingMessage> otherPlayersBid = getOtherPlayersBid(incomingMessage);
        if (otherPlayersBid.isPresent()) {
            IncomingMessage blueBid = incomingMessage.getPlayer().equals(Player.BLUE) ? incomingMessage : otherPlayersBid.get();
            IncomingMessage redBid = incomingMessage.getPlayer().equals(Player.RED) ? incomingMessage : otherPlayersBid.get();
            broadcastBids(blueBid, redBid);
            bids.remove(otherPlayersBid.get());
        } else {
            bids.add(incomingMessage);
        }
    }


    private static Optional<IncomingMessage> getOtherPlayersBid(IncomingMessage incomingMessage) {
        return bids
                .stream()
                .filter(bid -> bid.getGameId().equals(incomingMessage.getGameId())
                        && !bid.getPlayer().equals(incomingMessage.getPlayer()))
                .findAny();
    }

    private static void broadcast(OutgoingMessage outgoingEvent) {
        gameSessions.stream()
                .filter(gameSessions -> gameSessions.getGameId().equals(outgoingEvent.getGameId()))
                .forEach(gameSession -> {
                    try {
                        gameSession.getSession().getBasicRemote().sendObject(outgoingEvent);
                    } catch (IOException | EncodeException e) {
                        log.error("failed to broadcast event for gameId: " + gameSession.getGameId());
                        e.printStackTrace();
                    }
                });

    }

    private static void broadcastBids(IncomingMessage blueBid, IncomingMessage redBid) {
        Set<GameSession> affectedSessions = gameSessions.stream()
                .filter(gameSessions -> gameSessions.getGameId().equals(blueBid.getGameId()))
                .collect(Collectors.toSet());
        Assert.isTrue(affectedSessions.size() == 2, "There should be only two sessions per game.");
        Session redSession = Objects.requireNonNull(affectedSessions.stream()
                .filter(gameSession -> gameSession.getPlayer().equals(Player.RED))
                .findFirst().orElse(null))
                .getSession();
        Session blueSession = Objects.requireNonNull(affectedSessions.stream()
                .filter(gameSession -> gameSession.getPlayer().equals(Player.BLUE))
                .findFirst().orElse(null))
                .getSession();
        try {
            blueSession.getBasicRemote().sendObject(getOutGoingMessageForBids(blueBid, redBid));
        } catch (IOException | EncodeException e) {
            log.error("failed to broadcast bids to blue player of gameId: " + blueBid.getGameId());
            e.printStackTrace();
        }
        try {
            redSession.getBasicRemote().sendObject(getOutGoingMessageForBids(redBid, blueBid));
        } catch (IOException | EncodeException e) {
            log.error("failed to broadcast bids to red player of gameId: " + redBid.getGameId());
            e.printStackTrace();
        }
    }

    private static OutgoingMessage getOutGoingMessageForBids(IncomingMessage own, IncomingMessage opponents) {
        return new OutgoingMessage(own.getGameId(), MessageType.BID, new BiddingRound(own.getBid(), opponents.getBid()));
    }

        @OnClose
    public void onClose(Session session) {
        GameSession closingSession = getBySession(session);
        gameSessions.remove(closingSession);
        broadcast(new OutgoingMessage(closingSession.getGameId(), MessageType.PLAYER_LEFT));

    }

    private GameSession getBySession(Session session) {
        return gameSessions.stream().filter(gameSession -> gameSession.getSession().equals(session)).findFirst().orElse(null);
    }

}

