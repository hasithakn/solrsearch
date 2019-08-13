package com.hSenid.solrsearch.Dao;

import com.hSenid.solrsearch.DbConnection.MYSQLConnecter;
import com.hSenid.solrsearch.Entity.AnalysedData;

import java.sql.*;
import java.util.ArrayList;

public class AnalysisDao {
    public static final Connection con = MYSQLConnecter.getConnection();

    public void set(AnalysedData analysedData, String tableName) {

        String query = " insert into " + tableName + " ( app_id, doc_id, D101, D102, D103, D104 ) "
                + " values (?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStmt = null;

        try {

            preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1, analysedData.getApp_id());
            preparedStmt.setString(2, analysedData.getDoc_id());
            preparedStmt.setInt(3, analysedData.getD101());
            preparedStmt.setInt(4, analysedData.getD102());
            preparedStmt.setInt(5, analysedData.getD103());
            preparedStmt.setInt(6, analysedData.getD104());

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


    public int getSize() {
        Connection con = MYSQLConnecter.getConnection();
        PreparedStatement statement = null;
        String noOfRows = "SELECT count(*) as a FROM message_history";

        int rows = 0;
        try {
            statement = con.prepareStatement(noOfRows);
            ResultSet rs = statement.executeQuery();
            rs.next();
            rows = Integer.parseInt(rs.getObject("a").toString());
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return rows;
    }


}
