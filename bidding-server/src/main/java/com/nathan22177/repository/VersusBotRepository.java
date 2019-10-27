package com.nathan22177.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nathan22177.game.PlayerVersusBotGame;

public interface VersusBotRepository extends JpaRepository<PlayerVersusBotGame, Long> {
}
