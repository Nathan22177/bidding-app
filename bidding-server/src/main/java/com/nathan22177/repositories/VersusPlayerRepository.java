package com.nathan22177.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nathan22177.game.PlayerVersusPlayerGame;

public interface VersusPlayerRepository extends JpaRepository<PlayerVersusPlayerGame, Long> {

    @Query("select game from PlayerVersusPlayerGame game where game.status = 'MATCHMAKING' and game.redPlayer is null")
    List<PlayerVersusPlayerGame> findPendingGames();
}
