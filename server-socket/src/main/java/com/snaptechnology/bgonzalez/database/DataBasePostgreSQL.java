package com.snaptechnology.bgonzalez.database;

import java.sql.*;

/**
 * Created by bgonzalez on 06/09/2016.
 */
public class DataBasePostgreSQL {

    private Connection connection;
    private Statement statement;
    private ResultSet result;
    private ResultSetMetaData data;
    private String driver;
    private String url;
    private String username;
    private String password;

    public DataBasePostgreSQL(){
        this.driver = SettingsDB.DRIVER.getValue();
        this.url = SettingsDB.URL.getValue();
        this.username = SettingsDB.USERNAME.getValue();
        this.password = SettingsDB.PASSWORD.getValue();
    }

    public void connect(){
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url,username, password);
            setStatement(connection.createStatement());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");

    }


    public void executeUpdate(String query) {
        try { statement.executeUpdate(query); }
        catch (SQLException e) { e.printStackTrace(); } }



    public ResultSet executeQuery(String query) {
        try {
            setResult(statement.executeQuery(query));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void disconnect(){
        try {
            connection.close(); }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setResult(ResultSet result) {
        this.result = result;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public ResultSet getResult() {
        return result;
    }

    public ResultSetMetaData getData() {
        return data;
    }

    public void setData(ResultSetMetaData data) {
        this.data = data;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static void main(String args[]) {
        Connection c = null;

    }
}
