package itstep.learning.dal.dao;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import itstep.learning.dal.dto.Token;
import itstep.learning.dal.dto.User;
import itstep.learning.services.hash.HashService;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TokenDao {
    private final Connection connection;
    private final Logger logger;

    @Inject
    public TokenDao(Connection connection, Logger logger) {
        this.connection = connection;
        this.logger = logger;
    }

    public Token create(User user) {
        String sql = String.format(Locale.ROOT, "SELECT * FROM tokens WHERE user_id = '%s'", user.getId().toString());

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            Token latestToken = null;

            List<Token> tokens = new ArrayList<>();

            while (resultSet.next()) {
                Token token = new Token(resultSet);
                tokens.add(token);
            }

            for (Token token : tokens) {
                if (latestToken == null || token.getIat().after(latestToken.getIat())) {
                    latestToken = token;
                }
            }
            if (latestToken != null) {
                if (new Date().getTime() < latestToken.getExp().getTime()) {
                    latestToken.setExp(new Date(System.currentTimeMillis() + 1000 * 60 * 5));
                    sql = "UPDATE tokens SET exp=? WHERE token_id=?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                        preparedStatement.setTimestamp(1, new Timestamp(latestToken.getExp().getTime()));
                        preparedStatement.setString(2, latestToken.getTokenId().toString());
                        preparedStatement.executeUpdate();
                        return latestToken;
                    }
                }
            }

            Token newToken = new Token();
            newToken.setTokenId(UUID.randomUUID());
            newToken.setUserId(user.getId());
            newToken.setIat(new Date(System.currentTimeMillis()));
            newToken.setExp(new Date(System.currentTimeMillis() + 1000 * 60 * 10));

            sql = "INSERT INTO tokens (token_id, user_id, iat, exp) VALUES (?, ?, ?, ?)";
            try (PreparedStatement prep = connection.prepareStatement(sql)) {
                prep.setString(1, newToken.getTokenId().toString());
                prep.setString(2, newToken.getUserId().toString());
                prep.setTimestamp(3, new Timestamp(newToken.getIat().getTime()));
                prep.setTimestamp(4, new Timestamp(newToken.getExp().getTime()));
                prep.executeUpdate();
            }
            return newToken;
        } catch (SQLException ex) {
            logger.log(Level.WARNING, ex.getMessage() + " -- " + sql, ex);
            return null;
        }
    }


    public boolean installTables() {
        String sql =
                "CREATE TABLE IF NOT EXISTS tokens (" +
                        "token_id CHAR(36) PRIMARY KEY  DEFAULT( UUID() )," +
                        "user_id CHAR(36) NOT NULL," +
                        "exp DATETIME NULL," +
                        "iat DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                        ") ENGINE = InnoDB, DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci";

        try(Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
            return true;
        }
        catch (SQLException ex){
            logger.log( Level.WARNING, ex.getMessage() + " -- " + sql, ex );
            return false;
        }
    }
}
