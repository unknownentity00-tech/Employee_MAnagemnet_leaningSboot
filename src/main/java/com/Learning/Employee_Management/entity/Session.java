package com.Learning.Employee_Management.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private Long id;



    private String refreshToken;

    @CreationTimestamp
    private LocalDateTime lastUsedAt;

    @ManyToOne
    private User user;

}
