package com.gamesstore.gamestoreweb.service;

import com.gamesstore.gamestoreweb.models.Game;
import com.gamesstore.gamestoreweb.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    // الحصول على جميع الألعاب
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    // الحصول على لعبة حسب المعرف
    public Optional<Game> getGameById(Long id) {
        return gameRepository.findById(id);
    }

    // حفظ لعبة جديدة أو تحديث لعبة موجودة
    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    // حذف لعبة حسب المعرف
    public void deleteGameById(Long id) {
        if (gameRepository.existsById(id)) {
            gameRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Invalid game Id:" + id);
        }
    }

    // تصفية الألعاب حسب حالة معينة (مثلاً، ألعاب تم إصدارها)
    public List<Game> getReleasedGames() {
        return getAllGames().stream()
                .filter(game -> game.getReleaseDate() != null)
                .collect(Collectors.toList());
    }

    // تصفية الألعاب حسب المطور
    public List<Game> getGamesByDeveloper(String developer) {
        return getAllGames().stream()
                .filter(game -> game.getDeveloper().equalsIgnoreCase(developer))
                .collect(Collectors.toList());
    }

    // تحديث حالة اللعبة (مثلاً، تحديث السعر)
    public void updateGamePrice(Long id, double newPrice) {
        Game game = gameRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid game Id:" + id));
        game.setPrice(newPrice);
        gameRepository.save(game);
    }
}
