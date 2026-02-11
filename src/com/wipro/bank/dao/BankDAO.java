package com.wipro.bank.dao;

import java.sql.*;
import com.wipro.bank.bean.TransferBean;
import com.wipro.bank.util.DBUtil;

public class BankDAO {

    public boolean validateAccount(String accountNumber) {
        try (Connection con = DBUtil.getDBConnection()) {
            String sql = "SELECT COUNT(*) FROM ACCOUNT_TBL WHERE ACCOUNT_NUMBER = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public float findBalance(String accountNumber) {
        try (Connection con = DBUtil.getDBConnection()) {
            String sql = "SELECT BALANCE FROM ACCOUNT_TBL WHERE ACCOUNT_NUMBER = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getFloat("BALANCE");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean updateBalance(String accountNumber, float balance) {
        try (Connection con = DBUtil.getDBConnection()) {
            String sql = "UPDATE ACCOUNT_TBL SET BALANCE=? WHERE ACCOUNT_NUMBER=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setFloat(1, balance);
            ps.setString(2, accountNumber);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int generateTransactionId() throws SQLException {
        int transactionId = 0;
        try (Connection con = DBUtil.getDBConnection()) {
            String sql = "SELECT TRANSACTION_SEQ.NEXTVAL FROM DUAL";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) transactionId = rs.getInt(1);

            if(transactionId == 0) {
                throw new SQLException("Failed to generate transaction ID.");
            }

        } catch (SQLException e) {
            throw e; 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactionId;
    }

    public boolean transferMoney(TransferBean transferBean) {
        try (Connection con = DBUtil.getDBConnection()) {
            con.setAutoCommit(false); 
            int transactionId = generateTransactionId(); 

            String sql = "INSERT INTO TRANSFER_TBL " +
                         "(TRANSACTION_ID, FROM_ACCOUNT_NUMBER, TO_ACCOUNT_NUMBER, TRANSACTION_DATE, TRANSACTION_AMOUNT) " +
                         "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, transactionId);
            ps.setString(2, transferBean.getFromAccountNumber());
            ps.setString(3, transferBean.getToAccountNumber());
            ps.setDate(4, new java.sql.Date(System.currentTimeMillis()));
            ps.setFloat(5, transferBean.getAmount());

            ps.executeUpdate();
            con.commit(); 
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
