package com.gamesstore.gamestoreweb.controllers;

import com.gamesstore.gamestoreweb.models.Game;
import com.gamesstore.gamestoreweb.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/")
public class StoreController {

    private final GameRepository gameRepository;

    @Autowired
    public StoreController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    // Display all games for the store view
    @GetMapping
    public String viewStore(Model model) {
        model.addAttribute("games", gameRepository.findAll());
        return "store/store";
    }

    // Display game details
    @GetMapping("/details/{id}")
    public String viewGameDetails(@PathVariable Long id, Model model) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found with ID: " + id));
        model.addAttribute("game", game);
        return "store/gameDetails";
    }

    // Purchase page
    @GetMapping("/purchase/{id}")
    public String purchaseGame(@PathVariable Long id, Model model) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found with ID: " + id));
        model.addAttribute("game", game);
        return "store/purchaseGame";
    }
    @GetMapping("/confirm")
    public String confirm() {
        return "store/completePurchase";
    }
}
