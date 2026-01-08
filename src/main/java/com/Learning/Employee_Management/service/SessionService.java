package com.Learning.Employee_Management.service;

import com.Learning.Employee_Management.entity.Session;
import com.Learning.Employee_Management.entity.User;
import com.Learning.Employee_Management.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.server.authentication.SessionLimit;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
@RequiredArgsConstructor
@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    // The maximum number of active devices allowed for a single user
    private final int SESSION_LIMIT = 2;

    /**
     * Workflow: Creation & Concurrency Control
     * This runs every time a user logs in.
     */
    public void generateNewSession(User user, String refreshToken) {
        // 1. Fetch all existing active sessions for this specific user from the DB
        List<Session> userSessions = sessionRepository.findByUser(user);

        // 2. Concurrency Check: If the user is already logged in on 2 devices
        if (userSessions.size() == SESSION_LIMIT) {

            // 3. LRU (Least Recently Used) Logic:
            // Sort sessions by the 'lastUsedAt' timestamp (Oldest first)
            userSessions.sort(Comparator.comparing(Session::getLastUsedAt));

            // 4. Identify the session that hasn't been used for the longest time
            Session leastRecentlyUsedSession = userSessions.getFirst();

            // 5. Eviction: Delete the oldest session to make room for the new one
            // This effectively "logs out" the user from their oldest device
            sessionRepository.delete(leastRecentlyUsedSession);
        }

        // 6. Registration: Build the new session object linked to the Refresh Token
        Session newSession = Session
                .builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();

        // 7. Persistence: Save the new session record to the Database
        sessionRepository.save(newSession);
    }

    /**
     * Workflow: Verification & Heartbeat
     * This runs when the user tries to use their Refresh Token to get a new Access Token.
     */
    public void validateSession(String refreshToken) {
        // 1. Database Check: See if this Refresh Token is actually registered in our 'Session' table
        Session session = sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new SessionAuthenticationException(
                        "Session not found for refreshToken: " + refreshToken));

        // 2. Heartbeat: Update the timestamp to 'Now'
        // This ensures this device is now considered the "Most Recently Used"
        session.setLastUsedAt(LocalDateTime.now());

        // 3. Synchronization: Update the record in the DB
        sessionRepository.save(session);
    }
}