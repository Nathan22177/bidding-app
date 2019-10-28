package com.nathan22177.websocket;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;

import com.nathan22177.bidder.BidderPlayer;
import com.nathan22177.enums.Player;
import com.nathan22177.game.PlayerVersusPlayerGame;
import com.nathan22177.repositories.VersusPlayerRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ServerEndpoint(value = "pvp/{gameId}/{username}",
        encoders = BidEncoder.class,
        decoders = BidDecoder.class)
public class GameEndpoint {

    private static Set<GameSession> gameSessions = new HashSet<>();

    @Autowired
    VersusPlayerRepository versusPlayerRepository;

    @OnOpen
    public void onOpen(Session session, @PathParam("gameId") Long gameId, @PathParam("username") String username) throws IOException {
        PlayerVersusPlayerGame game = versusPlayerRepository.getOne(gameId);
        BidderPlayer player = game.getPlayerByUsername(username);
        GameSession gameSession = new GameSession(gameId, session, username, player.getPlayer());
        gameSessions.add(gameSession);
    }

    @OnMessage
    public void onMessage(Bid bid) {
        broadcast(bid);
    }

    private static void broadcast(Bid bid) {
        gameSessions.stream()
                .filter(gameSessions -> gameSessions.getGameId().equals(bid.getGameId()))
                .forEach(gameSession -> {
                    try {
                        gameSession.getSession().getBasicRemote().sendObject(bid);
                    } catch (IOException | EncodeException e) {
                        log.error("failed to broadcast bid for gameId: " + gameSession.getGameId());
                    }
                });

    }

    @OnClose
    public void onClose(Session session) throws IOException {
        gameSessions.removeIf(gameSession -> gameSession.getSession().equals(session));
        //TODO notify other player that player left the game
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
    static class Bid {
        Long gameId;
        int bid;
        String username;
    }

}

