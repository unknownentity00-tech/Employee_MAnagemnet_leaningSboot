package com.Learning.Employee_Management.repository;

import com.Learning.Employee_Management.entity.Session;
import com.Learning.Employee_Management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session , Long> {

    Optional<Session> findByRefreshToken(String refreshToken);

    List<Session> findByUser(User user);
}
