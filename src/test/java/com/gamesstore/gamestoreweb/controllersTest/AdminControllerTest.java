package com.gamesstore.gamestoreweb.controllersTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.gamesstore.gamestoreweb.controllers.AdminController;
import com.gamesstore.gamestoreweb.models.Game;
import com.gamesstore.gamestoreweb.repositories.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllGames() {
        List<Game> games = Arrays.asList(
                new Game("Game 1", "Action", 19.99, "Developer 1", "2023-01-01", "imageURL1"),
                new Game("Game 2", "Adventure", 29.99, "Developer 2", "2023-02-01", "imageURL2")
        );

        when(gameRepository.findAll()).thenReturn(games);

        String viewName = adminController.getAllGames(model);
        assertEquals("admin/games", viewName, "The view name should be 'admin/games'");
        verify(model).addAttribute("games", games);
    }

    @Test
    void testShowAddGameForm() {
        String viewName = adminController.showAddGameForm(model);
        assertEquals("admin/addGame", viewName, "The view name should be 'admin/addGame'");
        verify(model).addAttribute(eq("game"), any(Game.class));
    }

    @Test
    void testAddGameSuccess() {
        Game newGame = new Game("Test Game", "Action", 19.99, "Developer", "2023-01-01", "imageURL");
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = adminController.addGame(newGame, bindingResult);
        assertEquals("redirect:/admin", viewName, "Should redirect to /admin after successful addition");
        verify(gameRepository).save(newGame);
    }

    @Test
    void testAddGameValidationErrors() {
        Game invalidGame = new Game("", "Action", -1, "Developer", "invalid-date", "imageURL");
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = adminController.addGame(invalidGame, bindingResult);
        assertEquals("admin/addGame", viewName, "Should return to 'admin/addGame' form if there are validation errors");
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    void testShowUpdateGameFormSuccess() {
        Game game = new Game("Game 1", "Action", 19.99, "Developer 1", "2023-01-01", "imageURL1");
        game.setId(1L);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        String viewName = adminController.showUpdateGameForm(1L, model);
        assertEquals("admin/updateGame", viewName, "The view name should be 'admin/updateGame'");
        verify(model).addAttribute("game", game);
    }

    @Test
    void testShowUpdateGameFormNotFound() {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            adminController.showUpdateGameForm(1L, model);
        });

        assertEquals(HttpStatus.NOT_FOUND, ((ResponseStatusException) exception).getStatusCode());
    }

    @Test
    void testUpdateGameSuccess() {
        Game updatedGame = new Game("Updated Game", "Adventure", 29.99, "Developer", "2023-01-01", "imageURL");
        updatedGame.setId(1L);
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = adminController.updateGame(1L, updatedGame, bindingResult);
        assertEquals("redirect:/admin", viewName, "Should redirect to /admin after successful update");
        verify(gameRepository).save(updatedGame);
    }

    @Test
    void testUpdateGameValidationErrors() {
        Game invalidGame = new Game("", "Action", -1, "Developer", "invalid-date", "imageURL");
        invalidGame.setId(1L);
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = adminController.updateGame(1L, invalidGame, bindingResult);
        assertEquals("admin/updateGame", viewName, "Should return to 'admin/updateGame' form if there are validation errors");
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    void testDeleteGameSuccess() {
        when(gameRepository.existsById(1L)).thenReturn(true);

        String viewName = adminController.deleteGame(1L);
        assertEquals("redirect:/admin", viewName, "Should redirect to /admin after successful deletion");
        verify(gameRepository).deleteById(1L);
    }

    @Test
    void testDeleteGameNotFound() {
        when(gameRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            adminController.deleteGame(1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, ((ResponseStatusException) exception).getStatusCode());
    }
}
