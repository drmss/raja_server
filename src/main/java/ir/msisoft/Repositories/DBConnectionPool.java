package ir.msisoft.Repositories;

import org.apache.commons.dbcp.BasicDataSource;


import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectionPool {
    private static BasicDataSource ds = new BasicDataSource();
    private DBConnectionPool(){ }

    private final static String dbURL = "jdbc:sqlite:raja.db";

    static {
        ds.setUrl(dbURL);
        ds.setMinIdle(1);
        ds.setMaxIdle(2);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
