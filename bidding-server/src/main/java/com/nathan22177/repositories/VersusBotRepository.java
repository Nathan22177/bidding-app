package com.nathan22177.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nathan22177.game.PlayerVersusBotGame;

public interface VersusBotRepository extends JpaRepository<PlayerVersusBotGame, Long> {

    @Query("select game from player_versus_bot_game game where game.status is not null and game.status.active = TRUE")
    List<PlayerVersusBotGame> findActiveGames();
}
