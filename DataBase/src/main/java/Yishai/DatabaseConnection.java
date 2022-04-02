package Yishai;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

public class DatabaseConnection {
    private Connection conn;

    public DatabaseConnection() {
        try {
            conn = getConnection();
            System.out.println("Connection to DB start");
        } catch (Exception e) {
            System.out.println("Error while creating connection to db:");
            System.out.println(e.toString());
        }
    }

    private static Connection getConnection() throws URISyntaxException, SQLException { //establish connection to DB
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
    }

    public boolean sendStatement(String str) { //Actions on the DB
        try {
//            System.out.println("executing: " + str);
            conn.prepareStatement(str).execute();
            return true;
        } catch (SQLException throwables) {
            return false;
        }
    }

    public ResultSet sendQuery(String sqlQuery) {
        try {
//            StringBuilder result = new StringBuilder();
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sqlQuery);

//            ResultSet rs = stmt.executeQuery(sqlQuery);
//            ResultSetMetaData rsMeta = rs.getMetaData();
//            ResultSetMetaData rst = rs.getMetaData();
//            int columnsNum = rst.getColumnCount();
//            while (rs.next()) {
//                for (int i = 1; i < columnsNum + 1; i++)
//                    result.append(rs.getObject(i).toString()).append(" |");
//                result.append("\n-------------------------\n");
//            }
//            //       System.out.println(result.toString());
//            return result.toString();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}


