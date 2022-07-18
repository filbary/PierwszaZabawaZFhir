package com.example.fhirServerFinal.databaseHandler;


import org.springframework.stereotype.Repository;

import java.sql.*;
@Repository
public class DatabaseHandler {

    private final static String url = "jdbc:postgresql://localhost:5432/FhirDatabase";
    private final static String username = "postgres";
    private final static String password = "docker";
    private static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet createQuery(String query){
        try{
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void update(String update){
        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(update);
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

}
