package com.gamesstore.gamestoreweb.controllers;

import com.gamesstore.gamestoreweb.models.Game;
import com.gamesstore.gamestoreweb.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameRepository gameRepository;

    // نقطة نهاية لعرض جميع الألعاب في واجهة المستخدم
    @GetMapping
    public String getAllGames(Model model) {
        List<Game> games = gameRepository.findAll();
        model.addAttribute("games", games);
        return "games"; // اسم ملف HTML بدون .html
    }

    // استرجاع جميع الألعاب (API)
    @GetMapping("/api")
    public List<Game> getAllGamesApi() {
        return gameRepository.findAll();
    }

    // عرض نموذج إضافة لعبة جديدة
    @GetMapping("/add")
    public String showAddGameForm(Model model) {
        model.addAttribute("game", new Game()); // إنشاء كائن Game جديد
        return "addGame"; // اسم الملف HTML لإضافة اللعبة
    }

    // إضافة لعبة جديدة
    @PostMapping
    public Game addGame(@ModelAttribute Game game) {
        return gameRepository.save(game);
    }

    // عرض نموذج تحديث لعبة موجودة
    @GetMapping("/update/{id}")
    public String showUpdateGameForm(@PathVariable Long id, Model model) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid game Id:" + id));
        model.addAttribute("game", game);
        return "updateGame"; // اسم الملف HTML لتحديث اللعبة
    }

    // تحديث لعبة موجودة
    @PutMapping("/{id}")
    public Game updateGame(@PathVariable Long id, @ModelAttribute Game updatedGame) {
        return gameRepository.findById(id).map(game -> {
            game.setTitle(updatedGame.getTitle());
            game.setGenre(updatedGame.getGenre());
            game.setPrice(updatedGame.getPrice());
            game.setDeveloper(updatedGame.getDeveloper());
            game.setReleaseDate(updatedGame.getReleaseDate());
            return gameRepository.save(game);
        }).orElseGet(() -> {
            updatedGame.setId(id);
            return gameRepository.save(updatedGame);
        });
    }

    // حذف لعبة
    @DeleteMapping("/{id}")
    public void deleteGame(@PathVariable Long id) {
        gameRepository.deleteById(id);
    }
}

