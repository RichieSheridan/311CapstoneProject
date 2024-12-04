package com.explore.inventorymanagementsystem.services;

import com.explore.inventorymanagementsystem.models.User;
import com.explore.inventorymanagementsystem.utils.DatabaseUtil;
import com.explore.inventorymanagementsystem.utils.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.UUID;
import java.util.logging.Level;

public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public boolean createUser(User user) {
        String sql = "INSERT INTO users (id, username, password, email, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, user.getId());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, PasswordUtil.hashPassword(user.getPassword()));
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getRole());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Error creating user", e, Level.SEVERE);
            return false;
        }
    }

    public User getUserById(UUID id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email"),
                            rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching user", e, Level.SEVERE);
        }
        return null;
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, email = ?, role = ? WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getRole());
            pstmt.setObject(5, user.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Error updating user", e, Level.SEVERE);
            return false;
        }
    }

    public boolean deleteUser(UUID id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, id);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Error deleting user", e, Level.SEVERE);
            return false;
        }
    }

    public User getUserByUsername(String username) {
        String query = "SELECT username, password, role FROM Users WHERE username = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getString("username"), rs.getString("password"), rs.getString("role"));
            }
        } catch (SQLException e) {
            LOGGER.error("Error retrieving user {}", e.getMessage());
        }
        return null;
    }
}
