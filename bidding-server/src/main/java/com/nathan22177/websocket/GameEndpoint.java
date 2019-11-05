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
import org.springframework.util.Assert;

import com.nathan22177.bidder.BidderPlayer;
import com.nathan22177.config.CustomSpringConfigurator;
import com.nathan22177.enums.MessageType;
import com.nathan22177.enums.Side;
import com.nathan22177.enums.Status;
import com.nathan22177.game.PlayerVersusPlayerGame;
import com.nathan22177.game.dto.StateDTO;
import com.nathan22177.services.VersusPlayerService;
import com.nathan22177.websocket.decoder.IncomingMessageDecoder;
import com.nathan22177.websocket.encoder.OutgoingMessageEncoder;
import com.nathan22177.websocket.messages.incoming.IncomingMessage;
import com.nathan22177.websocket.messages.outgoing.OutgoingMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ServerEndpoint(value = "/pvp/{gameId}/{name}",
        encoders = OutgoingMessageEncoder.class,
        decoders = IncomingMessageDecoder.class,
        configurator = CustomSpringConfigurator.class)
public class GameEndpoint {

    private static Set<GameSession> gameSessions = new HashSet<>();
    private static Set<IncomingMessage> bids = new HashSet<>();

    @Autowired
    private VersusPlayerService vsPlayerService;


    @OnOpen
    public void onOpen(Session session, @PathParam("gameId") Long gameId, @PathParam("name") String name) {
        PlayerVersusPlayerGame game = vsPlayerService.loadVersusPlayerGame(gameId);
        BidderPlayer player = game.getPlayerByName(name);
        GameSession newGameSession = new GameSession(gameId, session, name, player.getSide());
        if (player.getSide() == Side.BLUE) {
            broadcastStatusChange(new OutgoingMessage(newGameSession.getGameId(), MessageType.WAITING_FOR_OPPONENT));
        } else if  (player.getSide() == Side.RED){
            broadcastStatusChange(new OutgoingMessage(newGameSession.getGameId(), MessageType.PLAYER_JOINED, new StateDTO(game, player.getSide())));
        } else {
            throw new IllegalArgumentException("A player has joined and does not have a side.");
        }
        gameSessions.add(newGameSession);
    }

    @OnMessage
    public void onMessage(IncomingMessage incomingMessage) {
        PlayerVersusPlayerGame game = vsPlayerService.loadVersusPlayerGame(incomingMessage.getGameId());
        Optional<IncomingMessage> otherPlayersBid = getOtherPlayersBid(incomingMessage);
        if (otherPlayersBid.isPresent()) {
            IncomingMessage blueBid = incomingMessage.getSide().equals(Side.BLUE) ? incomingMessage : otherPlayersBid.get();
            IncomingMessage redBid = incomingMessage.getSide().equals(Side.RED) ? incomingMessage : otherPlayersBid.get();
            game.playersPlaceTheirBids(blueBid.getBid(), redBid.getBid());
            broadcastBids(blueBid, redBid);
            bids.remove(otherPlayersBid.get());
        } else {
            bids.add(incomingMessage);
            game.setStatus(incomingMessage.getSide() == Side.BLUE ? Status.WAITING_FOR_RED : Status.WAITING_FOR_BLUE);
        }
    }


    private static Optional<IncomingMessage> getOtherPlayersBid(IncomingMessage incomingMessage) {
        return bids
                .stream()
                .filter(bid -> bid.getGameId().equals(incomingMessage.getGameId())
                        && !bid.getSide().equals(incomingMessage.getSide()))
                .findAny();
    }

    private static void broadcastStatusChange(OutgoingMessage outgoingEvent) {
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

    private void broadcastBids(IncomingMessage blueBid, IncomingMessage redBid) {
        Set<GameSession> affectedSessions = gameSessions.stream()
                .filter(gameSessions -> gameSessions.getGameId().equals(blueBid.getGameId()))
                .collect(Collectors.toSet());
        Assert.isTrue(affectedSessions.size() == 2, "There should be only two sessions per game.");

        Session redSession = Objects.requireNonNull(affectedSessions.stream()
                .filter(gameSession -> gameSession.getSide().equals(Side.RED))
                .findFirst().orElse(null))
                .getSession();

        Session blueSession = Objects.requireNonNull(affectedSessions.stream()
                .filter(gameSession -> gameSession.getSide().equals(Side.BLUE))
                .findFirst().orElse(null))
                .getSession();
        PlayerVersusPlayerGame game = vsPlayerService.loadVersusPlayerGame(blueBid.getGameId());
        try {
            blueSession.getBasicRemote().sendObject(getOutGoingMessageForBids(game, Side.BLUE));
        } catch (IOException | EncodeException e) {
            log.error("failed to broadcast bids to blue player of gameId: " + blueBid.getGameId());
            e.printStackTrace();
        }
        try {
            redSession.getBasicRemote().sendObject(getOutGoingMessageForBids(game, Side.RED));
        } catch (IOException | EncodeException e) {
            log.error("failed to broadcast bids to red player of gameId: " + redBid.getGameId());
            e.printStackTrace();
        }
        game.setStatus(Status.WAITING_FOR_BIDS);
    }

    private static OutgoingMessage getOutGoingMessageForBids(PlayerVersusPlayerGame game, Side side) {
        return new OutgoingMessage(game.getId(), MessageType.BIDS, new StateDTO(game, side));
    }

    @OnClose
    public void onClose(Session session) {
        GameSession closingSession = getBySession(session);
        if (closingSession != null) {
            vsPlayerService.loadVersusPlayerGame(closingSession.getGameId()).setStatus(Status.ENDED_PREMATURELY);
            gameSessions.remove(closingSession);
            broadcastStatusChange(new OutgoingMessage(closingSession.getGameId(), MessageType.PLAYER_LEFT));
        }

    }

    private GameSession getBySession(Session session) {
        return gameSessions.stream().filter(gameSession -> gameSession.getSession().equals(session)).findFirst().orElse(null);
    }

}

