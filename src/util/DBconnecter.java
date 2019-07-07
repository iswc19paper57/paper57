package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnecter {
    public static String url = "jdbc:mysql://localhost:3306/open?"
            + "useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
    public static String name = "com.mysql.cj.jdbc.Driver";
    public static String user = "root";
    public static String password = "123456";
    public Connection conn = null;

    public DBconnecter() {
        try {
            Class.forName(name);
        }catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(url,user, password);
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
