package com.nathan22177.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nathan22177.game.PlayerVersusBotGame;

public interface VersusBotRepository extends JpaRepository<PlayerVersusBotGame, Long> {

    @Query("select game from PlayerVersusBotGame game where game.status is not NULL and game.status='WAITING_FOR_BIDS'")
    List<PlayerVersusBotGame> findActiveGames();
}