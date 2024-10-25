package com.gamesstore.repositories;
import com.gamesstore.gamestoreweb.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



public interface GameRepository extends JpaRepository<Game, Long>  {

}
