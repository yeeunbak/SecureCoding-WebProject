package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {

    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    
    // Oracle XE 기준 예시
    // XE를 쓰면 보통 jdbc:oracle:thin:@localhost:1521:XE
    // 환경에 따라 XE 대신 XEPDB1일 수도 있음
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    
    private static final String USER = "webuser";
    private static final String PASSWORD = "1234";

    static {
        try {
            Class.forName(DRIVER);
            System.out.println("Oracle JDBC Driver 로딩 성공");
        } catch (ClassNotFoundException e) {
            System.out.println("Oracle JDBC Driver 로딩 실패");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void close(ResultSet rs, PreparedStatement pstmt, Connection conn) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (pstmt != null) pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(PreparedStatement pstmt, Connection conn) {
        try {
            if (pstmt != null) pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}