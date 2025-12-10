package com.devlab.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private Long telegramId;
    
    private String name;
    private String email;
    
    @Column(name = "joined_at")
    private LocalDateTime joinedAt;
    
    @Column(name = "waiting_for_email")
    private Boolean waitingForEmail = false;

    @PrePersist
    protected void onCreate() {
        joinedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTelegramId() { return telegramId; }
    public void setTelegramId(Long telegramId) { this.telegramId = telegramId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDateTime getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }
    public Boolean getWaitingForEmail() { return waitingForEmail; }
    public void setWaitingForEmail(Boolean waitingForEmail) { this.waitingForEmail = waitingForEmail; }
}

