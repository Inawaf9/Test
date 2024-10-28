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
@RequestMapping("/admin")
public class AdminController {

    private final GameRepository gameRepository;

    @Autowired
    public AdminController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    // Display all games (Read)
    @GetMapping
    public String getAllGames(Model model) {
        List<Game> games = gameRepository.findAll();
        model.addAttribute("games", games);
        return "admin/games"; // Thymeleaf template name
    }

    // Show form to add a new game (Create)
    @GetMapping("/add")
    public String showAddGameForm(Model model) {
        model.addAttribute("game", new Game());
        return "admin/addGame"; // Thymeleaf form for adding a game
    }

    // Handle the form submission for adding a new game (Create)
    @PostMapping("/add")
    public String addGame(@Validated @ModelAttribute Game game, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/addGame"; // Return to form with validation errors
        }
        gameRepository.save(game); // Save game to the database
        return "redirect:/admin"; // Redirect to admin page after saving
    }

    // Show form to update an existing game by ID (Update)
    @GetMapping("/update/{id}")
    public String showUpdateGameForm(@PathVariable Long id, Model model) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found with ID: " + id));
        model.addAttribute("game", game);
        return "admin/updateGame"; // Thymeleaf form for updating a game
    }

    // Handle the form submission for updating an existing game (Update)
    @PostMapping("/update/{id}")
    public String updateGame(@PathVariable Long id, @Validated @ModelAttribute Game updatedGame, BindingResult result) {
        if (result.hasErrors()) {
            updatedGame.setId(id); // Set the ID back to the updated game
            return "admin/updateGame"; // Return to form with validation errors
        }
        updatedGame.setId(id); // Ensure the ID is set for the update
        gameRepository.save(updatedGame);
        return "redirect:/admin"; // Redirect to the admin page after updating
    }

    // Delete a game by ID (Delete)
    @PostMapping("/delete/{id}")
    public String deleteGame(@PathVariable Long id) {
        if (!gameRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found with ID: " + id);
        }
        gameRepository.deleteById(id);
        return "redirect:/admin"; // Redirect to the admin page after deletion
    }
}
