package com.nathan22177.websocket;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;

import sun.plugin2.message.Message;

import com.nathan22177.bidder.BidderPlayer;
import com.nathan22177.enums.Player;
import com.nathan22177.game.PlayerVersusPlayerGame;
import com.nathan22177.repositories.VersusPlayerRepository;
import com.nathan22177.util.NewGameUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@ServerEndpoint(value = "pvp/{gameId}/{username}",
encoders = BidEncoder.class,
decoders = BidDecoder.class)
public class GameEndpoint {

    private Set<GameSession> userSessionMap = new HashSet<>();

    @Autowired
    VersusPlayerRepository versusPlayerRepository;

    @OnOpen
    public void onOpen(Session session, @PathParam("gameId") Long gameId, @PathParam("username") String username) throws IOException {
        PlayerVersusPlayerGame game = versusPlayerRepository.getOne(gameId);
        BidderPlayer player = game.getPlayerByUsername(username);
        GameSession gameSession = new GameSession(gameId, session, username, player.getPlayer());
        userSessionMap.add(gameSession);
    }

    @OnMessage
    public void onMessage(Session session, Bid bid)throws IOException {
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class GameSession {
        Long gameId;
        Session session;
        String username;
        Player player;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    static class Bid {
        Long gameId;
        int bid;
        String username;
    }

}
