/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokerstar;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Bertie
 */
public class DBManager {
    private String dbDriver = "org.apache.derby.jdbc.ClientDriver"; //Lagrer drivern i en variabel
    private Connection connection = null; //forbindelse
    private String dbName = "jdbc:derby://localhost:1527/pokerstar;user=poker;password=poker";

    DBManager() {
        try {
            Class.forName(dbDriver);
            connection = DriverManager.getConnection(dbName);
            System.out.println("Connected to database");
        } catch (Throwable e) {
            System.out.println("DB connection fail: " + e);
        }
    }

    public void addRates(String cardType, String card1Value, String card2Value, int nPlayers, double rating, double tieRate, double lossRate) {

        try {
            PreparedStatement pStatement;
            String sql = "INSERT INTO PREFLOPTABLE VALUES(?,?,?,?,?,?,?)";
            pStatement = connection.prepareStatement(sql);
            pStatement.setString(1, cardType);
            pStatement.setString(2, "" + card1Value);
            pStatement.setString(3, "" + card2Value);
            pStatement.setInt(4, nPlayers);
            pStatement.setDouble(5, rating);
            pStatement.setDouble(6, tieRate);
            pStatement.setDouble(7, lossRate);
            pStatement.executeUpdate();
        } catch (Throwable e) {
            System.out.println("addRates() "+e);
        }
    }

    public ArrayList<String> getRating(String cardType1, String cardType2, String numPlayers) {
        try {
            ResultSet result;
            ArrayList ratingList = new ArrayList<String>();
            PreparedStatement pStatement;
            String sql = "select * from prefloptable where nPlayers =  '" + numPlayers + "'"
                        + "AND ((card1value = '" + cardType1 + "'"
                        + "AND card2value = '" + cardType2 + "')"
                        + "OR (card1value = '" + cardType2 + "'"
                        + "AND card2value = '" + cardType1 + "'))";
            pStatement = connection.prepareStatement(sql);
            result = pStatement.executeQuery();
            while (result.next()) {
                ratingList.add(result.getString("winRate"));
            }
            Collections.sort(ratingList);
            return ratingList;

        } catch (Throwable e) {
            System.out.println("getRating() " + e);
        }
        return null;
    }

    public ArrayList<String> getRating(String cardType, String cardType1, String cardType2, String numPlayers) {
        try {
            ResultSet result;
            ArrayList ratingList = new ArrayList<String>();
            PreparedStatement pStatement;
            String sql = "select * from prefloptable where nPlayers =  '" + numPlayers + "'"
                    + "AND cardtype = '" + cardType + "'"
                    + "AND ((card1value = '" + cardType1 + "'"
                    + "AND card2value = '" + cardType2 + "')"
                    + "OR (card1value = '" + cardType2 + "'"
                    + "AND card2value = '" + cardType1 + "'))";
            pStatement = connection.prepareStatement(sql);
            result = pStatement.executeQuery();
            while (result.next()) {
                ratingList.add(result.getString("winRate"));

            }
            Collections.sort(ratingList);
            return ratingList;
        }catch (Throwable e) {
            System.out.println("getRating() " + e);
        }
        return null;
    }
}
