package dao;

import model.Account;
import utils.DBConnection;

import java.sql.*;
import java.util.Vector;

public class AccountDAO {
    private static final int MINIMUM_BALANCE = 1000;

    public void createAccount(Account a) {
        String sql = "INSERT INTO accounts (acno, name, balance) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, a.getAcno());
            pst.setString(2, a.getName());
            pst.setInt(3, a.getBalance());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Vector<Vector<Object>> getAllAccounts() {
        Vector<Vector<Object>> data = new Vector<>();
        String sql = "SELECT * FROM accounts";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("acno"));
                row.add(rs.getString("name"));
                row.add(rs.getInt("balance"));
                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void deposit(int acno, int amount) {
        String sql = "UPDATE accounts SET balance = balance + ? WHERE acno = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, amount);
            pst.setInt(2, acno);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void withdraw(int acno, int amount) {
        String sqlCheck = "SELECT balance FROM accounts WHERE acno = ?";
        String sqlUpdate = "UPDATE accounts SET balance = balance - ? WHERE acno = ?";
        Connection conn = null;
        PreparedStatement pstCheck = null;
        PreparedStatement pstUpdate = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            pstCheck = conn.prepareStatement(sqlCheck);
            pstCheck.setInt(1, acno);
            rs = pstCheck.executeQuery();
            if (rs.next()) {
                int balance = rs.getInt("balance");
                if (balance - amount >= MINIMUM_BALANCE) {
                    pstUpdate = conn.prepareStatement(sqlUpdate);
                    pstUpdate.setInt(1, amount);
                    pstUpdate.setInt(2, acno);
                    pstUpdate.executeUpdate();
                    conn.commit();
                } else {
                    System.out.println("Not enough balance for withdrawal");
                }
            } else {
                System.out.println("Account number not found");
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstCheck != null) pstCheck.close();
                if (pstUpdate != null) pstUpdate.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
