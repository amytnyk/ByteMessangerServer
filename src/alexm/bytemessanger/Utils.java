package alexm.bytemessanger;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;

import java.sql.PreparedStatement;
import com.mysql.jdbc.Statement;

public class Utils {
	public Utils() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
			return;
		}
		System.out.println("MySQL JDBC Driver Registered!");
		connection = null;

		try {
			
			connection = DriverManager
			.getConnection("jdbc:mysql://localhost:3306/", "root", "azellion" );

		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}

		if (connection != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}

		
		stmt = null;
		try {
			stmt  = (Statement) connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public Statement stmt;
	public Connection connection;
	
	public String getKey(String login, String password) {
		String sql = "SELECT login, password " +
	             "FROM server.users";
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String key = "UN";
		try {
			while (rs.next()) {
				if (rs.getString("login").equals(login) && rs.getString("password").equals(password)) {
					key = rs.getString("key");
					break;
				}
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return key;
	}
	
	public String generateKey() {
		long time = System.currentTimeMillis();
		char[] keys = { 'a', 'b', 'f', 'g', 'h', 't', 'm', 'k', 'c', 's' };
		String key = new String();
		while (time > 0) {
			key += keys[(int) (time % 10)];
			time /= 10;
		}
		return key;
	}
	
	public String createAccount(String login, String password) {
		ResultSet rs = null;
        int candidateId = 0;
        
        String sql = "INSERT INTO candidates(login, password, key) "
                   + "VALUES(?,?,?)";
        
        try (
             PreparedStatement pstmt = connection.prepareStatement(sql ,Statement.RETURN_GENERATED_KEYS);) {
            
            // set parameters for statement
            pstmt.setString(1, login);
            pstmt.setString(2, password);
            pstmt.setString(3, generateKey());
 
            int rowAffected = pstmt.executeUpdate();
            if(rowAffected == 1)
            {
                // get candidate id
                rs = pstmt.getGeneratedKeys();
                if(rs.next())
                    candidateId = rs.getInt(1);
 
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if(rs != null)  rs.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        
		return getKey(login, password);
	}
}
