package com.gamesstore.gamestoreweb.controllersTest;

import com.gamesstore.gamestoreweb.controllers.StoreController;
import com.gamesstore.gamestoreweb.models.Game;
import com.gamesstore.gamestoreweb.repositories.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class StoreControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private StoreController storeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(storeController).build();
    }

    @Test
    void testViewStore() throws Exception {
        Game game1 = new Game("Game 1", "Action", 19.99, "Developer 1", "2023-01-01", "imageURL1");
        Game game2 = new Game("Game 2", "Adventure", 29.99, "Developer 2", "2023-02-01", "imageURL2");

        when(gameRepository.findAll()).thenReturn(Arrays.asList(game1, game2));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("store/store"))
                .andExpect(model().attributeExists("games"))
                .andExpect(model().attribute("games", Arrays.asList(game1, game2)));

        verify(gameRepository, times(1)).findAll();
    }

    @Test
    void testViewGameDetailsFound() throws Exception {
        Game game = new Game("Game Details", "Strategy", 29.99, "Developer", "2023-05-01", "imageURL");
        game.setId(1L);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        mockMvc.perform(get("/details/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("store/gameDetails"))
                .andExpect(model().attributeExists("game"))
                .andExpect(model().attribute("game", game));

        verify(gameRepository, times(1)).findById(1L);
    }

    @Test
    void testViewGameDetailsNotFound() throws Exception {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/details/1"))
                .andExpect(status().isNotFound());

        verify(gameRepository, times(1)).findById(1L);
    }

    @Test
    void testPurchaseGameFound() throws Exception {
        Game game = new Game("Purchase Game", "RPG", 39.99, "Developer", "2023-06-01", "imageURL");
        game.setId(1L);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        mockMvc.perform(get("/purchase/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("store/purchaseGame"))
                .andExpect(model().attributeExists("game"))
                .andExpect(model().attribute("game", game));

        verify(gameRepository, times(1)).findById(1L);
    }

    @Test
    void testPurchaseGameNotFound() throws Exception {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/purchase/1"))
                .andExpect(status().isNotFound());

        verify(gameRepository, times(1)).findById(1L);
    }

    @Test
    void testConfirmPurchase() throws Exception {
        mockMvc.perform(get("/confirm"))
                .andExpect(status().isOk())
                .andExpect(view().name("store/completePurchase"));
    }
}
