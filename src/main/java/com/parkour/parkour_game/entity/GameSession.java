package com.parkour.parkour_game.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "game_sessions")
@Data
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 主键，自动增长

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 关联用户，外键

    private int score; // 玩家得分
    private int duration; // 游戏时长（秒）

    @Column(name = "game_time")
    private LocalDateTime gameTime = LocalDateTime.now(); // 记录时间，默认当前时间
}