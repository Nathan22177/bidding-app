package com.nathan22177.services;

import java.util.Optional;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nathan22177.bidder.BidderPlayer;
import com.nathan22177.enums.Side;
import com.nathan22177.game.PlayerVersusPlayerGame;
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

    public Long getPendingGameOrCreateNewOne(String name) {
        Optional<PlayerVersusPlayerGame> pendingGame = versusPlayerRepository
                .findPendingGames()
                .stream()
                .findFirst();
        PlayerVersusPlayerGame game;
        if (pendingGame.isPresent()) {
            game = pendingGame.get();
            pendingGame.get().setRedPlayer(new BidderPlayer(game.getConditions(), name, Side.RED));
        } else {
            game = NewGameUtil.createNewGameAgainstThePlayer(name);
            versusPlayerRepository.saveAndFlush(game);
        }
        return game.getId();
    }
    @Transactional
    public PlayerVersusPlayerGame loadVersusPlayerGame(Long gameId) {
        return versusPlayerRepository.findById(gameId).orElse(null);
    }
}
