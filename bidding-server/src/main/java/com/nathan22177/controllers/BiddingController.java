package com.nathan22177.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nathan22177.enums.Player;
import com.nathan22177.game.PlayerVersusBotGame;
import com.nathan22177.game.PlayerVersusPlayerGame;
import com.nathan22177.game.dto.StateDTO;
import com.nathan22177.service.BiddingService;
import com.nathan22177.util.CollectorUtils;

@Controller
public class BiddingController {

    @Autowired
    private final BiddingService service;

    public BiddingController(BiddingService service) {
        this.service = service;
    }

    @GetMapping("/start_new_game_vs_bot/{opponent}")
    public String startVersusBotGame(Model model, @PathVariable String opponent) {
        if (opponent.contains("RANDOM")) {
            opponent = getRandomOpponent();
        }
        model.addAttribute("opponent", service.getAvailableOpponents().get(opponent));
        Long gameId = service.createNewGameAgainstTheBot(opponent);
        model.addAttribute("gameId", gameId);
        return "redirect:/vs_bot/" + gameId;
    }

    @GetMapping("/start_new_game_vs_another_player/{username}")
    public String startVersusPlayerGame(Model model, @PathVariable String username) {
        Long gameId = service.createNewGameAgainstAnotherPlayer(username);
        model.addAttribute("gameId", gameId);
        return "redirect:/vs_player/" + gameId + "/" + username;
    }

    @GetMapping("/vs_bot/{gameId}")
    public String loadVersusBotGame(Model model, @PathVariable Long gameId) {
        PlayerVersusBotGame game = service.loadVersusBotGame(gameId);
        model.addAttribute("red", game.getRedPlayer());
        model.addAttribute("blue", game.getBluePlayer());
        model.addAttribute("gameId", game.getId());
        model.addAttribute("history", game.getBluePlayer().getBiddingHistory());
        model.addAttribute("state", new StateDTO(game));
        return "vs_bot_interface";
    }

    @GetMapping("/vs_player/{gameId}/{username}")
    public String loadVersusPlayerGame(Model model, @PathVariable Long gameId, @PathVariable String username) {
        PlayerVersusPlayerGame game = service.loadVersusPlayerGame(gameId);
        model.addAttribute("player", game.getBluePlayer().getUsername().equals(username) ? Player.BLUE : Player.RED);
        model.addAttribute("gameId", game.getId());
        model.addAttribute("history", game.getBluePlayer().getBiddingHistory());
        model.addAttribute("state", new StateDTO(game));
        return "vs_player_interface";
    }

    @GetMapping("/vs_bot/{gameId}/{bid}")
    @ResponseBody
    public StateDTO placeBidVersusBot(@PathVariable Long gameId, @PathVariable Integer bid) {
        return service.placeBidVersusBot(gameId, bid);
    }

    private String getRandomOpponent() {
        return service.getAvailableOpponents().keySet()
                .stream()
                .filter(key -> !key.equalsIgnoreCase("RANDOM"))
                .collect(CollectorUtils.toShuffledStream())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("There are no bots available. Something is wrong."));
    }

    @GetMapping("/")
    public String menu(Model model) {
        model.addAttribute("bots", service.getAvailableOpponents());
        model.addAttribute("listOfGamesVersusBots", service.getStartedGamesVersusBots());
        return "index";
    }
}
