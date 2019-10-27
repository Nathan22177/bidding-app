package com.nathan22177.websocket;

import java.io.IOException;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;

import com.nathan22177.bidder.BidderPlayer;
import com.nathan22177.game.PlayerVersusPlayerGame;
import com.nathan22177.repositories.VersusPlayerRepository;
import com.nathan22177.util.NewGameUtil;

@ServerEndpoint(value = "pvp/{gameId}/{username}")
public class GameEndpoint {

    @Autowired
    VersusPlayerRepository versusPlayerRepository;

    @OnOpen
    public void onOpen(Session session, @PathParam("gameId") Long gameId, @PathParam("username") String username) throws IOException {
        PlayerVersusPlayerGame game = versusPlayerRepository.getOne(gameId);
        if (NewGameUtil.redPlayerIsEmpty(game) &&
                !game.getBluePlayer().getUsername().equals(username)) {
            game.setRedPlayer(new BidderPlayer(game.getConditions(), username));
        }
    }
    
}
