package com.nathan22177.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.nathan22177.enums.Bot;
import com.nathan22177.game.PlayerVersusBotGame;
import com.nathan22177.game.dto.GamesDTO;
import com.nathan22177.game.dto.StateDTO;
import com.nathan22177.repositories.VersusBotRepository;
import com.nathan22177.repositories.VersusPlayerRepository;
import com.nathan22177.game.resolvers.NewGameResolver;

@Service
@Transactional
public class VersusBotService {

    private final
    VersusBotRepository versusBotRepository;


    public VersusBotService(VersusBotRepository versusBotRepository, VersusPlayerRepository versusPlayerRepository) {
        this.versusBotRepository = versusBotRepository;
    }

    public Long createNewGameAgainstTheBot(Bot bot) {
        PlayerVersusBotGame game = NewGameResolver.createNewGameAgainstTheBot(bot);
        versusBotRepository.saveAndFlush(game);
        return game.getId();
    }


    public PlayerVersusBotGame loadVersusBotGame(Long gameId) {
        return versusBotRepository.getOne(gameId);
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
