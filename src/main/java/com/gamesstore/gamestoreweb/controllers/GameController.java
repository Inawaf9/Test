package com.gamesstore.gamestoreweb.controllers;

import com.gamesstore.gamestoreweb.models.Game;
import com.gamesstore.gamestoreweb.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/games")
public class GameController {

    private final GameRepository gameRepository;

    @Autowired
    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    // Display all games (View)
    @GetMapping
    public String getAllGames(Model model) {
        List<Game> games = gameRepository.findAll();
        model.addAttribute("games", games);
        return "games"; // Thymeleaf template name
    }

    // Show form to add a new game
    @GetMapping("/add")
    public String showAddGameForm(Model model) {
        model.addAttribute("game", new Game());
        return "addGame";
    }

    // Handle the form submission for adding a new game
    @PostMapping("/add")
    public String addGame(@Validated @ModelAttribute Game game, BindingResult result) {
        if (result.hasErrors()) {
            return "addGame"; // Return to form with validation errors
        }
        gameRepository.save(game);
        return "redirect:/games";
    }

    // Show form to update an existing game by ID
    @GetMapping("/update/{id}")
    public String showUpdateGameForm(@PathVariable Long id, Model model) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found with ID: " + id));
        model.addAttribute("game", game);
        return "updateGame";
    }

    // Handle the form submission for updating an existing game
    @PostMapping("/update/{id}")
    public String updateGame(@PathVariable Long id, @Validated @ModelAttribute Game updatedGame, BindingResult result) {
        if (result.hasErrors()) {
            updatedGame.setId(id);
            return "updateGame"; // Return to form with validation errors
        }
        updatedGame.setId(id);
        gameRepository.save(updatedGame);
        return "redirect:/games";
    }

    // Delete a game by ID
    @PostMapping("/delete/{id}")
    public String deleteGame(@PathVariable Long id) {
        if (!gameRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found with ID: " + id);
        }
        gameRepository.deleteById(id);
        return "redirect:/games";
    }
}
