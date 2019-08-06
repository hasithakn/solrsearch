package com.hSenid.solrsearch.Dao;

import com.hSenid.solrsearch.DbConnection.MYSQLConnecter;
import com.hSenid.solrsearch.Entity.DetailedDuplicate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DetailedDuplicateDao {
    public static final Connection con = MYSQLConnecter.getConnection();

    public void set(DetailedDuplicate detailedDuplicate, String tableName) {

        String query = " insert into " + tableName + " ( app_id, timestamp, D1, D7, D30, Details ) "
                + " values (?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStmt = null;

        try {

            preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1, detailedDuplicate.getApp_id());
            preparedStmt.setString(2, detailedDuplicate.getTimestamp());
            preparedStmt.setInt(3, detailedDuplicate.getD1());
            preparedStmt.setInt(4, detailedDuplicate.getD7());
            preparedStmt.setInt(5, detailedDuplicate.getD30());
            preparedStmt.setString(6, detailedDuplicate.getDetails());

            preparedStmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (preparedStmt != null) {
                try {
                    preparedStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
