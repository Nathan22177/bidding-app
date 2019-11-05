package com.nathan22177.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nathan22177.game.PlayerVersusPlayerGame;
import com.nathan22177.game.dto.GamesDTO;
import com.nathan22177.repositories.VersusPlayerRepository;
import com.nathan22177.util.NewGameUtil;

@Service
@Transactional
public class VersusPlayerService {


    private final
    VersusPlayerRepository versusPlayerRepository;

    public VersusPlayerService(VersusPlayerRepository versusPlayerRepository) {
        this.versusPlayerRepository = versusPlayerRepository;
    }

    public Long getNewGameVsPlayer(String name) {
        PlayerVersusPlayerGame game = NewGameUtil.createNewGameAgainstThePlayer(name);
        versusPlayerRepository.saveAndFlush(game);
        return game.getId();
    }

    @Transactional
    public PlayerVersusPlayerGame loadVersusPlayerGame(Long gameId) {
        return versusPlayerRepository.findById(gameId).orElse(null);
    }

    public List<GamesDTO> getPendingGamesVersusPlayers() {
        return versusPlayerRepository.findPendingGames().stream().map(GamesDTO::new).collect(Collectors.toList());
    }

    public void redPlayerJoined(Long gameId, String name) {
        PlayerVersusPlayerGame game = loadVersusPlayerGame(gameId);
        game.redPlayerJoined(name);
        versusPlayerRepository.saveAndFlush(game);
    }
}
