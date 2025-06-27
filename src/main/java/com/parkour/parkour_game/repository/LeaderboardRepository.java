package com.parkour.parkour_game.repository;

import com.parkour.parkour_game.entity.Leaderboard;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long> {
    Optional<Leaderboard> findByUserId(Long userId);
}