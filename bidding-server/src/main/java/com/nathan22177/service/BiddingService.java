package com.nathan22177.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nathan22177.bidder.BidderPlayer;
import com.nathan22177.enums.Bot;
import com.nathan22177.enums.Side;
import com.nathan22177.enums.Status;
import com.nathan22177.game.PlayerVersusPlayerGame;
import com.nathan22177.repositories.VersusBotRepository;
import com.nathan22177.game.PlayerVersusBotGame;
import com.nathan22177.game.dto.GamesDTO;
import com.nathan22177.game.dto.StateDTO;
import com.nathan22177.repositories.VersusPlayerRepository;
import com.nathan22177.util.NewGameUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BiddingService {

    private final
    VersusBotRepository versusBotRepository;

    private final
    VersusPlayerRepository versusPlayerRepository;

    public BiddingService(VersusBotRepository versusBotRepository, VersusPlayerRepository versusPlayerRepository) {
        this.versusBotRepository = versusBotRepository;
        this.versusPlayerRepository = versusPlayerRepository;
    }



    public Long createNewGameAgainstTheBot(Bot bot) {
        PlayerVersusBotGame game = NewGameUtil.createNewGameAgainstTheBot(bot);
        versusBotRepository.saveAndFlush(game);
        return game.getId();
    }

    public Long getPendingGameOrCreateNewOne(String username) {
        Optional<PlayerVersusPlayerGame> pendingGame = versusPlayerRepository
                .findPendingGames()
                .stream()
                .findFirst();
        PlayerVersusPlayerGame game;
        if (pendingGame.isPresent()) {
            game = pendingGame.get();
            pendingGame.get().setRedPlayer(new BidderPlayer(game.getConditions(), username, Side.RED));
            pendingGame.get().getRedPlayer().setSide(Side.RED);
        } else {
            game = NewGameUtil.createNewGameAgainstThePlayer(username);
            versusPlayerRepository.saveAndFlush(game);
        }
        return game.getId();
    }

    public PlayerVersusBotGame loadVersusBotGame(Long gameId) {
        return versusBotRepository.getOne(gameId);
    }

    public PlayerVersusPlayerGame loadVersusPlayerGame(Long gameId) {
        return versusPlayerRepository.getOne(gameId);
    }

    @Transactional
    public StateDTO placeBidVersusBot(Long gameId, Integer bid) {
        PlayerVersusBotGame game = versusBotRepository.getOne(gameId);
        game.playerPlacesBidVersusBot(bid);
        return new StateDTO(game);
    }

    public List<GamesDTO> getStartedGamesVersusBots() {
        return versusBotRepository.findActiveGames().stream().map(GamesDTO::new).collect(Collectors.toList());
    }
}
