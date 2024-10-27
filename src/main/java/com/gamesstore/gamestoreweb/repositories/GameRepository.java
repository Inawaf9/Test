package com.gamesstore.gamestoreweb.repositories;

import com.gamesstore.gamestoreweb.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  // Added annotation
public interface GameRepository extends JpaRepository<Game, Long> {
    // You can add custom query methods here if needed in the future
}
