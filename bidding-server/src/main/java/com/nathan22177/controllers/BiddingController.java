package com.nathan22177.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nathan22177.enums.Bot;
import com.nathan22177.enums.Side;
import com.nathan22177.game.PlayerVersusBotGame;
import com.nathan22177.game.PlayerVersusPlayerGame;
import com.nathan22177.game.dto.StateDTO;
import com.nathan22177.service.BiddingService;
import com.nathan22177.util.NewGameUtil;

@Controller
public class BiddingController {

    private final BiddingService service;

    public BiddingController(BiddingService service) {
        this.service = service;
    }

    @GetMapping("/start_new_game_vs_bot/{bot}")
    public String startVersusBotGame(Model model, @PathVariable String bot) {
        if (bot.contains("RANDOM")) {
            bot = NewGameUtil.getRandomBot();
        }
        Long gameId = service.createNewGameAgainstTheBot(Bot.valueOf(bot));
        model.addAttribute("gameId", gameId);
        return "redirect:/vs_bot/" + gameId;
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

    @GetMapping("/vs_bot/{gameId}/{bid}")
    @ResponseBody
    public StateDTO placeBidVersusBot(@PathVariable Long gameId, @PathVariable Integer bid) {
        return service.placeBidVersusBot(gameId, bid);
    }

    @GetMapping("/start_new_game_vs_another_player/{username}")
    public String startVersusPlayerGame(Model model, @PathVariable String username) {
        Long gameId = service.getPendingGameOrCreateNewOne(username);
        model.addAttribute("gameId", gameId);
        return "redirect:/vs_player/" + gameId + "/" + username;
    }

    @GetMapping("/vs_player/{gameId}/{username}")
    public String loadVersusPlayerGame(Model model, @PathVariable Long gameId, @PathVariable String username) {
        PlayerVersusPlayerGame game = service.loadVersusPlayerGame(gameId);
        Side side = game.getBluePlayer().getUsername().equals(username) ? Side.BLUE : Side.RED;
        model.addAttribute("player", side);
        model.addAttribute("gameId", game.getId());
        model.addAttribute("history", side == Side.BLUE
                ? game.getBluePlayer().getBiddingHistory()
                : game.getRedPlayer().getBiddingHistory());
        model.addAttribute("state", new StateDTO(game, side));
        return "vs_player_interface";
    }



    @GetMapping("/")
    public String menu(Model model) {
        model.addAttribute("bots", NewGameUtil.getAvailableBots());
        model.addAttribute("listOfGamesVersusBots", service.getStartedGamesVersusBots());
        return "index";
    }
}
