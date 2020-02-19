package com.jiras.sql;

import javax.xml.transform.Result;
import java.sql.*;

/**
 *
 * @author sqlitetutorial.net
 */
public class Database {
    Connection conn;
    public ResultSet selectAll(String sql) {
        try(Statement stmt = conn.createStatement()) {
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public ResultSet executeStmt(PreparedStatement stmt) throws SQLException {
        return stmt.executeQuery();
    }
    public int executeUpdate(PreparedStatement stmt) throws SQLException {
        return stmt.executeUpdate(stmt.toString(), Statement.RETURN_GENERATED_KEYS);
    }
    public PreparedStatement initQuery(String sql) {
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            return stmt;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public void execute(String sql) {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public Database() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:./main.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            this.conn = conn;
            //create tables if they don't exist
            execute("CREATE TABLE IF NOT EXISTS playlists (\n"
                    + "    id integer PRIMARY KEY,\n"
                    + "    name text NOT NULL\n"
                    + ");");
            execute("CREATE TABLE IF NOT EXISTS playlistSongs (\n"
                    + "    id integer PRIMARY KEY,\n"
                    + "    songID integer NOT NULL,\n"
                    + "    playlistID integer NOT NULL\n"
                    + ");");
            execute("CREATE TABLE IF NOT EXISTS albums (\n"
                    + "    id integer PRIMARY KEY,\n"
                    + "    name text NOT NULL\n"
                    + ");");
            execute("CREATE TABLE IF NOT EXISTS songs (\n"
                    + "    id integer PRIMARY KEY,\n"
                    + "    path text NOT NULL,\n"
                    + "    name text,\n"
                    + "    duration integer,\n"
                    + "    description text,\n"
                    + "    year integer,\n"
                    + "    artist text NOT NULL,\n"
                    + "    albumID integer DEFAULT NULL\n"
                    + ");");
            execute("CREATE TABLE IF NOT EXISTS musicFolder (\n"
                    + "    id integer PRIMARY KEY,\n"
                    + "    path text NOT NULL,\n"
                    + "    lastSync DATETIME DEFAULT CURRENT_TIMESTAMP\n"
                    + ");");
            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }
}