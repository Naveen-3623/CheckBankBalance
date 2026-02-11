package com.wipro.bank.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DBUtil {
    public static Connection getDBConnection() {
    	 try {
             Class.forName("oracle.jdbc.driver.OracleDriver");
             String url = "jdbc:oracle:thin:@localhost:1521:XE";
             String username = "SAMPLEDB";
             String password = "sampledb";
             return DriverManager.getConnection(url, username, password);
         } catch (ClassNotFoundException e) {
             System.out.println("Oracle Driver not found");
             e.printStackTrace();
         } catch (SQLException e) {
             System.out.println("Database connection failed");
             e.printStackTrace();
         }
    	 return null;
    }
}