package com.gamesstore.gamestoreweb.modelsTest;



import static org.junit.jupiter.api.Assertions.*;

import com.gamesstore.gamestoreweb.models.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game("Test Game", "Action", 19.99, "Test Developer", "2023-01-01", "imageUrl");
    }

    @Test
    void testGameProperties() {
        assertEquals("Test Game", game.getTitle(), "Game title should be 'Test Game'");
        assertEquals("Action", game.getGenre(), "Game genre should be 'Action'");
        assertEquals(19.99, game.getPrice(), "Game price should be 19.99");
        assertEquals("Test Developer", game.getDeveloper(), "Game developer should be 'Test Developer'");
        assertEquals("2023-01-01", game.getReleaseDate(), "Game release date should be '2023-01-01'");
        assertEquals("imageUrl", game.getImageUrl(), "Game image Url should be 'imageUrl'");
    }
}

