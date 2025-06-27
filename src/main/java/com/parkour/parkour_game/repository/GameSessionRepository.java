package com.parkour.parkour_game.repository;

import com.parkour.parkour_game.entity.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
}