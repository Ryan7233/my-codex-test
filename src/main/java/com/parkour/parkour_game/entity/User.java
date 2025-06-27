package com.parkour.parkour_game.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true) // 只包含明确指定的字段，避免 Lombok 生成错误
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 主键，自动增长

    @Column(unique = true, nullable = false)
    private String username; // 用户名，唯一，不能为空

    @Column(nullable = false)
    private String password; // 密码（需加密存储）

    @Column(unique = true)
    private String email; // 邮箱，唯一

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now(); // 自动填充创建时间
    }
}