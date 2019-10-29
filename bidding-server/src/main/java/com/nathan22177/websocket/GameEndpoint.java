package com.nathan22177.websocket;

import java.io.IOException;
import java.util.HashSet;
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
import com.nathan22177.enums.Player;
import com.nathan22177.game.PlayerVersusPlayerGame;
import com.nathan22177.repositories.VersusPlayerRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ServerEndpoint(value = "pvp/{gameId}/{username}",
        encoders = EventEncoder.class,
        decoders = EventDecoder.class)
public class GameEndpoint {

    private static Set<GameSession> gameSessions = new HashSet<>();
    private static Set<Event> bids = new HashSet<>();


    @Autowired
    VersusPlayerRepository versusPlayerRepository;

    @OnOpen
    public void onOpen(Session session, @PathParam("gameId") Long gameId, @PathParam("username") String username) throws IOException {
        PlayerVersusPlayerGame game = versusPlayerRepository.getOne(gameId);
        BidderPlayer player = game.getPlayerByUsername(username);
        GameSession newGameSession = new GameSession(gameId, session, username, player.getPlayer());
        broadcast(new Event(newGameSession.getGameId(), EventType.PLAYER_JOINED));
        gameSessions.add(newGameSession);
    }

    @OnMessage
    public void onMessage(Event event) {
        Assert.isTrue(event.getType() == EventType.BID, "The only messages we're accepting are bids.");
        Optional<Event> otherPlayersBid = getOtherPlayersBid(event);
        if (otherPlayersBid.isPresent()){
            Event blueBid = event.getPlayer().equals(Player.BLUE) ? event : otherPlayersBid.get();
            Event redBid = event.getPlayer().equals(Player.RED) ? event : otherPlayersBid.get();
            broadcast(getEventForBroadcasting(blueBid, redBid));
            bids.remove(otherPlayersBid.get());
        } else {
            bids.add(event);
        }
    }

    private static Event getEventForBroadcasting(Event blueBid, Event redBid) {
        return new Event(blueBid.getGameId(), EventType.BID, blueBid.getBlueBid(), redBid.getRedBid());
    }


    private static Optional<Event> getOtherPlayersBid(Event event) {
        return bids
                .stream()
                .filter(bid -> bid.getGameId().equals(event.getGameId())
                        && !bid.getPlayer().equals(event.getPlayer()))
                .findAny();
    }

    private static void broadcast(Event event) {
        gameSessions.stream()
                .filter(gameSessions -> gameSessions.getGameId().equals(event.getGameId()))
                .forEach(gameSession -> {
                    try {
                        gameSession.getSession().getBasicRemote().sendObject(event);
                    } catch (IOException | EncodeException e) {
                        log.error("failed to broadcast event for gameId: " + gameSession.getGameId());
                        e.printStackTrace();
                    }
                });

    }

    @OnClose
    public void onClose(Session session) {
        GameSession closingSession = getBySession(session);
        gameSessions.remove(closingSession);
        broadcast(new Event(closingSession.getGameId(), EventType.PLAYER_LEFT));

    }

    private GameSession getBySession(Session session) {
        return gameSessions.stream().filter(gameSession -> gameSession.getSession().equals(session)).findFirst().orElse(null);
    }

    private Set<GameSession> getAffectedGameSessions(GameSession session) {
        return gameSessions.stream().filter(gameSession -> gameSession.getGameId().equals(session.getGameId())).collect(Collectors.toSet());
    }

    @Getter
    @AllArgsConstructor
    private static class GameSession {
        Long gameId;
        Session session;
        String username;
        Player player;
    }


    @Getter
    @AllArgsConstructor
    static class Event {
        Long gameId;
        EventType type;
        int blueBid;
        int redBid;
        Player player;

        Event(Long gameId, EventType type) {
            this.gameId = gameId;
            this.type = type;
        }

        Event(Long gameId, EventType type, int blueBid, int redBid) {
            this.gameId = gameId;
            this.type = type;
            this.blueBid = blueBid;
            this.redBid = redBid;
        }
    }

    enum EventType {
        PLAYER_JOINED,
        BID,
        PLAYER_LEFT
    }

}

