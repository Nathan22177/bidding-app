package com.nathan22177.websocket;

import javax.websocket.Session;

import com.nathan22177.enums.Player;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GameSession {
    Long gameId;
    Session session;
    String username;
    Player player;
}
