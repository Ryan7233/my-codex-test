package com.parkour.parkour_game.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "leaderboard")
@Data
public class Leaderboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 主键，自动增长

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 关联用户，外键

    private int highScore; // 玩家最高得分
}