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
import com.nathan22177.game.resolvers.NewGameResolver;
import com.nathan22177.services.VersusBotService;
import com.nathan22177.services.VersusPlayerService;

@Controller
public class BiddingController {

    private final VersusBotService vsBotService;
    private final VersusPlayerService vsPlayerService;

    public BiddingController(VersusBotService vsBotService, VersusPlayerService vsPlayerService) {
        this.vsBotService = vsBotService;
        this.vsPlayerService = vsPlayerService;
    }

    @GetMapping(value = "/start_new_game_vs_bot/{bot}")
    public String startVersusBotGame(Model model, @PathVariable String bot) {
        if (bot.contains("RANDOM")) {
            bot = NewGameResolver.getRandomBot();
        }
        Long gameId = vsBotService.createNewGameAgainstTheBot(Bot.valueOf(bot));
        model.addAttribute("gameId", gameId);
        return "redirect:/vs_bot/" + gameId;
    }

    @GetMapping(value = "/vs_bot/{gameId}")
    public String loadVersusBotGame(Model model, @PathVariable Long gameId) {
        PlayerVersusBotGame game = vsBotService.loadVersusBotGame(gameId);
        model.addAttribute("red", game.getRedPlayer());
        model.addAttribute("blue", game.getBluePlayer());
        model.addAttribute("gameId", game.getId());
        model.addAttribute("history", game.getBluePlayer().getBiddingHistory());
        model.addAttribute("state", new StateDTO(game));
        return "vs_bot_interface";
    }

    @GetMapping(value = "/vs_bot/{gameId}/{bid}")
    @ResponseBody
    public StateDTO placeBidVersusBot(@PathVariable Long gameId, @PathVariable Integer bid) {
        return vsBotService.placeBidVersusBot(gameId, bid);
    }

    @GetMapping(value = "/updateGameState/{gameId}/{side}")
    @ResponseBody
    public StateDTO updateGameState(@PathVariable Long gameId, @PathVariable String side) {
        return new StateDTO(vsPlayerService.loadVersusPlayerGame(gameId), Side.valueOf(side));
    }

    @GetMapping(value = "/start_new_game_vs_another_player/{name}")
    public String startVersusPlayerGame(Model model, @PathVariable String name) {
        Long gameId = vsPlayerService.getNewGameVsPlayer(name);
        model.addAttribute("gameId", gameId);
        return "redirect:/vs_player/" + gameId + "/" + name;
    }

    @GetMapping(value = "/join_pvp_game/{gameId}/{name}")
    public String startVersusPlayerGame(Model model, @PathVariable Long gameId, @PathVariable String name) {
        vsPlayerService.redPlayerJoined(gameId, name);
        return "redirect:/vs_player/" + gameId + "/" + name;
    }

    @GetMapping(value = "/vs_player/{gameId}/{name}")
    public String loadVersusPlayerGame(Model model, @PathVariable Long gameId, @PathVariable String name) {
        PlayerVersusPlayerGame game = vsPlayerService.loadVersusPlayerGame(gameId);
        Side side = game.getBluePlayer().getName().equals(name) ? Side.BLUE : Side.RED;
        model.addAttribute("side", side);
        model.addAttribute("player", game.getBluePlayer());
        model.addAttribute("gameId", game.getId());
        model.addAttribute("history", side == Side.BLUE
                ? game.getBluePlayer().getBiddingHistory()
                : game.getRedPlayer().getBiddingHistory());
        model.addAttribute("state", new StateDTO(game, side));
        return "vs_player_interface";
    }

    @GetMapping(value = "/")
    public String menu(Model model) {
        model.addAttribute("bots", NewGameResolver.getAvailableBots());
        model.addAttribute("listOfGamesVersusBots", vsBotService.getStartedGamesVersusBots());
        model.addAttribute("listOfGamesVersusPlayers", vsPlayerService.getPendingGamesVersusPlayers());
        return "index";
    }
}
