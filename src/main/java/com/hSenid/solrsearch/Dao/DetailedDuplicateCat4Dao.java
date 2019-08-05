package com.hSenid.solrsearch.Dao;

import com.hSenid.solrsearch.DbConnection.MYSQLConnecter;
import com.hSenid.solrsearch.Entity.AnalysedData;
import com.hSenid.solrsearch.Entity.DetailedDuplicateCat4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DetailedDuplicateCat4Dao {
    public static final Connection con = MYSQLConnecter.getConnection();

    public void set(DetailedDuplicateCat4 detailedDuplicateCat4, String tableName) {

        String query = " insert into " + tableName + " ( app_id, timestamp, D1, D7, D30, Details ) "
                + " values (?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStmt = null;

        try {

            preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1, detailedDuplicateCat4.getApp_id());
            preparedStmt.setString(2, detailedDuplicateCat4.getTimestamp());
            preparedStmt.setInt(3, detailedDuplicateCat4.getD1());
            preparedStmt.setInt(4, detailedDuplicateCat4.getD7());
            preparedStmt.setInt(5, detailedDuplicateCat4.getD30());
            preparedStmt.setString(6, detailedDuplicateCat4.getDetails());

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
