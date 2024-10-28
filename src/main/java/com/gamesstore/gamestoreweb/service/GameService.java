package com.gamesstore.gamestoreweb.service;

import com.gamesstore.gamestoreweb.models.Game;
import com.gamesstore.gamestoreweb.repositories.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);
    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    // Retrieve all games from the repository
    public List<Game> getAllGames() {
        logger.info("Retrieving all games from the repository");
        return gameRepository.findAll();
    }

    // Retrieve a game by its ID
    public Optional<Game> getGameById(Long id) {
        logger.info("Retrieving game with ID: {}", id);
        return gameRepository.findById(id);
    }

    // Save a new or updated game
    public Game saveGame(Game game) {
        logger.info("Saving game: {}", game);
        // You can add validation logic here if needed
        return gameRepository.save(game);
    }

    // Delete a game by its ID
    public void deleteGameById(Long id) {
        try {
            logger.info("Deleting game with ID: {}", id);
            gameRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            logger.warn("Attempted to delete non-existent game with ID: {}", id);
            // Handle the case where the game ID does not exist
            throw new IllegalArgumentException("Game not found for deletion: " + id);
        } catch (Exception e) {
            logger.error("Error occurred while deleting game with ID: {}", id, e);
            throw e; // Optionally rethrow the exception or handle accordingly
        }
    }
}
