package com.example.itjaproject;

import java.sql.Connection;
import java.sql.DriverManager;

public class dbConnection {

    private static String servername="10.0.0.100";
    private static String dbname="flightapp";
    private static String username="nico";
    private static Integer portnumber=3306;
    private static String password="nico";


    public static Connection getConnectionToDb() {
        try {

            Class.forName("com.mysql.jdbc.Driver");

            Connection connection = DriverManager.getConnection("jdbc:mysql://"+servername+":"+portnumber+"/"+dbname, username, password);
    return connection;
        }catch (ClassNotFoundException classNotFoundException) {

        } catch (Exception e) {

e.printStackTrace();
        }

        return null;
    }


}