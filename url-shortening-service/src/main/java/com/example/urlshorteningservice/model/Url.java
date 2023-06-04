package com.example.urlshorteningservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "urls")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Url implements Serializable {
    @Id
    private String id;

    @Column(name = "original_url")
    private String originalUrl;

    @Column(name = "usage_counter", nullable = false)
    private long usageCounter;

    @Column(name = "insert_attempts", nullable = false)
    private int insertAttempts;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "is_active")
    @Builder.Default
    private boolean isActive = true;
}
