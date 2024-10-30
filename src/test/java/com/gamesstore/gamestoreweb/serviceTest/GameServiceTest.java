package com.gamesstore.gamestoreweb.serviceTest;


import com.gamesstore.gamestoreweb.models.Game;
import com.gamesstore.gamestoreweb.repositories.GameRepository;
import com.gamesstore.gamestoreweb.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllGames() {
        Game game1 = new Game("Game 1", "Action", 19.99, "Developer 1", "2023-01-01", "imageURL1");
        Game game2 = new Game("Game 2", "Adventure", 29.99, "Developer 2", "2023-02-01", "imageURL2");

        when(gameRepository.findAll()).thenReturn(Arrays.asList(game1, game2));

        List<Game> games = gameService.getAllGames();
        assertEquals(2, games.size(), "The repository should return 2 games");
        assertTrue(games.contains(game1), "The list should contain game1");
        assertTrue(games.contains(game2), "The list should contain game2");

        verify(gameRepository, times(1)).findAll();
    }

    @Test
    void testGetGameByIdFound() {
        Game game = new Game("Test Game", "Action", 19.99, "Developer", "2023-01-01", "imageURL");
        game.setId(1L);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        Optional<Game> foundGame = gameService.getGameById(1L);
        assertTrue(foundGame.isPresent(), "The game should be found by its ID");
        assertEquals("Test Game", foundGame.get().getTitle(), "The game title should match");

        verify(gameRepository, times(1)).findById(1L);
    }

    @Test
    void testGetGameByIdNotFound() {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Game> foundGame = gameService.getGameById(1L);
        assertFalse(foundGame.isPresent(), "The game should not be found if ID does not exist");

        verify(gameRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveGame() {
        Game game = new Game("Test Game", "Puzzle", 9.99, "Developer", "2023-03-01", "imageURL");

        when(gameRepository.save(game)).thenReturn(game);

        Game savedGame = gameService.saveGame(game);
        assertNotNull(savedGame, "The saved game should not be null");
        assertEquals("Test Game", savedGame.getTitle(), "The game title should match");

        verify(gameRepository, times(1)).save(game);
    }

    @Test
    void testDeleteGameByIdSuccess() {
        // Mock a successful deletion by doing nothing on deleteById
        doNothing().when(gameRepository).deleteById(1L);

        assertDoesNotThrow(() -> gameService.deleteGameById(1L), "Deleting an existing game should not throw");

        verify(gameRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteGameByIdNotFound() {
        // Mock the behavior for a non-existent ID by throwing an EmptyResultDataAccessException
        doThrow(new EmptyResultDataAccessException(1)).when(gameRepository).deleteById(1L);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> gameService.deleteGameById(1L));
        assertEquals("Game not found for deletion: 1", exception.getMessage(), "Exception message should match expected text");

        verify(gameRepository, times(1)).deleteById(1L);
    }
}
