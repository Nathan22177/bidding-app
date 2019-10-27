package com.nathan22177.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nathan22177.game.PlayerVersusPlayerGame;

public interface VersusPlayerRepository extends JpaRepository<PlayerVersusPlayerGame, Long> {
}
