package java_jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Driver {

	private static final String db_driver = "com.mysql.jdbc.Driver";
	private static final String db_connection = "jdbc:mysql://localhost:3306/project_snickr";
	private static final String db_user;  //not initialized purposely
	private static final String db_password;   //not initialized purposely

	public Driver() {}
	
    private static Connection getDBConnection() {
        
		Connection dbConnection = null;
		try {

			Class.forName(db_driver);

		} catch (ClassNotFoundException e) {

			System.out.println(e.getMessage());

		}

		try {

			dbConnection = DriverManager.getConnection(db_connection, db_user, db_password);
			return dbConnection;

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		}

		return dbConnection;

	}

	
	public String getUserId(String user_email) {
		
        Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		String getIdProc = "Select u_id from user where u_email = ?";
		int userID = 0;
		JSONObject jsonObject = new JSONObject();
		
		try {
			dbConnection = getDBConnection();
		
            preparedStatement = dbConnection.prepareStatement(getIdProc);
			preparedStatement.setString(1, user_email);
			
			ResultSet rs = preparedStatement.executeQuery();
			
			while(rs.next()) {
				
				userID = rs.getInt("u_id");	
				jsonObject.put("user_id", userID);
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	
		return jsonObject.toString();	
	}
	
	public String getWorkspaceId(String workspace_name) {
		Connection dbConnection = null;
		
		PreparedStatement preparedStatement = null;
		
		String getIdProc = "Select w_id from workspace where w_name = ?";
		int workspace_id = 0;
		JSONObject jsonObject = new JSONObject();
		
		try {
			
			dbConnection = getDBConnection();	
			preparedStatement = dbConnection.prepareStatement(getIdProc);
			preparedStatement.setString(1, workspace_name);
			ResultSet rs = preparedStatement.executeQuery();
			
			while(rs.next()) {
				
				workspace_id = rs.getInt("w_id");
				String workspaceID = Integer.toString(workspace_id);
				jsonObject.put("workspace_id", workspaceID);
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return jsonObject.toString();
		
	}
	
	public String getChannelId(String channel_name, int workspace_id) {
		Connection dbConnection = null;
		
		PreparedStatement preparedStatement = null;
		String getIdProc = "Select c_id from channel natural join channel_workspace where c_name = ? and w_id = ?";
		int channel_id = 0;
		JSONObject jsonObject = new JSONObject();
		
		try {
            
			dbConnection = getDBConnection();			
			preparedStatement = dbConnection.prepareStatement(getIdProc);
			preparedStatement.setString(1, channel_name);
			preparedStatement.setInt(2, workspace_id);
			ResultSet rs = preparedStatement.executeQuery();
			
			while(rs.next()) {
				
				channel_id = rs.getInt("c_id");
				String channelID = Integer.toString(channel_id);
				jsonObject.put("channel_id", channelID);
				
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	
		return jsonObject.toString();
		
	}
		
	public void insertUserDatabase(String name, String nickname, String email, String password) throws SQLException {

		Connection dbConnection = null;
		CallableStatement callableStatement = null;
		Random rand = new Random();
		int random_number;

		String insertStoreProc = "{call add_user(?,?,?,?,?)}";

		try {

			dbConnection = getDBConnection();
			callableStatement = dbConnection.prepareCall(insertStoreProc);
			random_number = rand.nextInt(100000);
	
			callableStatement.setString(1, name);
			callableStatement.setString(2, nickname);
			callableStatement.setString(3, email);
			callableStatement.setString(4, password);
			callableStatement.setInt(5, random_number);

			callableStatement.executeUpdate();			

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (callableStatement != null) {
				callableStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}

	}

	public String verifyUserFromDatabase(String email) throws SQLException {

		Connection dbConnection = null;
		CallableStatement callableStatement = null;
		PreparedStatement preparedStatement = null;
		String pass = null;
		String selectTableSQL = "SELECT u_password from user where u_email = ?";

		try {

			dbConnection = getDBConnection();

			preparedStatement = dbConnection.prepareStatement(selectTableSQL);

			preparedStatement.setString(1, email);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				pass = rs.getString("u_password");

			}

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (callableStatement != null) {
				callableStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}
		return pass;

	}

	public void insertWorkspaceDatabase(int user_id, String wname, String wdescription) throws SQLException {

		Connection dbConnection = null;
		CallableStatement callableStatement = null;
		String insertWorkspaceProc = "{call create_workspace(?,?,?)}";

		try {
			dbConnection = getDBConnection();
			callableStatement = dbConnection.prepareCall(insertWorkspaceProc);

			callableStatement.setInt(1, user_id);
			callableStatement.setString(2, wname);
			callableStatement.setString(3, wdescription);

			callableStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {

			if (callableStatement != null) {
				callableStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}

		return;
	}

	public void insertChannelDatabase(int user_id, int workspace_id, String channel_name, String channel_type) throws SQLException {

		Connection dbConnection = null;
		CallableStatement callableStatement = null;
		String insertChannelProc = "{call create_channel(?,?,?,?)}";

		try {
			dbConnection = getDBConnection();
			callableStatement = dbConnection.prepareCall(insertChannelProc);

			callableStatement.setInt(1, user_id);
			callableStatement.setInt(2, workspace_id);
			callableStatement.setString(3, channel_name);
			callableStatement.setString(4, channel_type);

			callableStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {

			if (callableStatement != null) {
				callableStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}

		return;
	}

	public void insertIntoWorkspaceUser(int user_id, int workspace_id) throws SQLException {

		Connection dbConnection = null;
		CallableStatement callableStatement = null;

		String insertIntoWorkspaceUserProc = "{call workspace_invite(?,?)}";

		try {
			dbConnection = getDBConnection();
			callableStatement = dbConnection.prepareCall(insertIntoWorkspaceUserProc);

			callableStatement.setInt(1, workspace_id);
			callableStatement.setInt(2, user_id);

			callableStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {

			if (callableStatement != null) {
				callableStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}

		return;

	}
	
	public void insertIntoChannelUser(int user_id, int channel_id, int workspace_id) throws SQLException {

		Connection dbConnection = null;
		CallableStatement callableStatement = null;
		String insertIntoChannelUserProc = "{call channel_invite(?,?,?)}";

		try {
			dbConnection = getDBConnection();
			callableStatement = dbConnection.prepareCall(insertIntoChannelUserProc);

			callableStatement.setInt(1, channel_id);
			callableStatement.setInt(2, user_id);
			callableStatement.setInt(3, workspace_id);
			
			callableStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {

			if (callableStatement != null) {
				callableStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}

		return;

	}

	public String checkWorkspaceInvites(int user_id) throws SQLException {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		String checkInviteSQL = "select w_name from workspace Natural Join Workspace_user as W_S where W_S.w_accept_time is null and u_id = ?";
		JSONArray ja = new JSONArray();
		JSONObject finalObject = new JSONObject();
		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(checkInviteSQL);
			preparedStatement.setInt(1, user_id);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				String workspace_name = rs.getString("w_name");
				
				JSONObject jo = new JSONObject();
				jo.put("workspace_name", workspace_name);
				ja.put(jo);
			}
			finalObject.put("workspaces", ja);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}
		return finalObject.toString();
		
	}

	public String checkChannelInvites(int user_id, int workspace_id) throws SQLException {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		String checkChannelInviteSQL = "select c_name from channel natural join channel_workspace natural join channel_user where w_id = ? and u_id = ? and c_accept_time is null";
		JSONArray ja = new JSONArray();
		JSONObject finalObject = new JSONObject();
        
		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(checkChannelInviteSQL);
			preparedStatement.setInt(1, workspace_id);
			preparedStatement.setInt(2, user_id);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				String workId = rs.getString("c_name");
				
				JSONObject jo = new JSONObject();
				jo.put("channel_name", workId);

				ja.put(jo);		
			}
			finalObject.put("channels", ja);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}

		
		return finalObject.toString();
		

	}

	public void acceptWorkspaceInvite(int workspace_id, int user_id) throws SQLException {
		Connection dbConnection = null;
		CallableStatement callableStatement = null;

		String updateWorkspaceUserProc = "{call workspace_accept_invite(?,?)}";
		
		try {
			dbConnection = getDBConnection();
			callableStatement = dbConnection.prepareCall(updateWorkspaceUserProc);

			callableStatement.setInt(1, workspace_id);
			callableStatement.setInt(2, user_id);

			callableStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {

			if (callableStatement != null) {
				callableStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}

		return;
	}
	
	public void declineWorkspaceInvite(int workspace_id, int user_id) throws SQLException {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		String updateWorkspaceUserProc = "delete from workspace_user where w_id = ? and u_id = ?";
		
		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(updateWorkspaceUserProc);

			preparedStatement.setInt(1, workspace_id);
			preparedStatement.setInt(2, user_id);

			preparedStatement.executeQuery();
						
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}

		return;
		
	}
	
	public String getCurrentChannels(int user_id, int workspace_id) throws SQLException {
		
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		JSONArray ja = new JSONArray();
		JSONObject finalJson = new JSONObject();
		String getCurrentChannelsProc = "select c_name from channel natural join channel_workspace natural join channel_user where u_id = ? and w_id = ?";
		
		try {
            
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(getCurrentChannelsProc);
			preparedStatement.setInt(1, user_id);
			preparedStatement.setInt(2, workspace_id);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				String channel_name = rs.getString("c_name");
				
				JSONObject jo = new JSONObject();
				jo.put("channel_name", channel_name);
				ja.put(jo);
                
			}
			finalJson.put("channels", ja);
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (JSONException e) {
			
			e.printStackTrace();
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}
		
		return finalJson.toString();
		
	}
	
	public String getCurrentWorkspaces(int user_id) throws SQLException {
		
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		JSONArray ja = new JSONArray();
		JSONObject finalJson = new JSONObject();
		String getCurrentWorkspacesProc = "select w_name from workspace natural join workspace_user where u_id = ?";
		
		try {
            
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(getCurrentWorkspacesProc);
			preparedStatement.setInt(1, user_id);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				String workId = rs.getString("w_name");
				
				JSONObject jo = new JSONObject();
				jo.put("workspace_name", workId);
				ja.put(jo);
			}
			finalJson.put("workspaces", ja);
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (JSONException e) {
			
			e.printStackTrace();
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}
		
		return finalJson.toString();
		
	}
	
	
	public String sendChannelMessage(int user_id, int channel_id, String message) throws SQLException {
		
		
		Connection dbConnection = null;
		CallableStatement callableStatement = null;

		String sendChannelMessageProc = "{call send_message(?,?,?)}";
		
		try {
			dbConnection = getDBConnection();
			callableStatement = dbConnection.prepareCall(sendChannelMessageProc);

			callableStatement.setInt(1, user_id);
			callableStatement.setInt(2, channel_id);
			callableStatement.setString(3, message);

			callableStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {

			if (callableStatement != null) {
				callableStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}

		return null;
	}
	
	public String receiveChannelMessage(int user_id, int channel_id, int workspace_id) throws SQLException {
		
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		JSONObject finalJson = new JSONObject();
		String receiveChannelMessageProc = "select m_message, u_name from message natural join user_message natural join message_channel natural join channel_workspace natural join user where c_id = ? and w_id = ? Order by m_id";
		
		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(receiveChannelMessageProc);

			preparedStatement.setInt(1, channel_id);
			preparedStatement.setInt(2, workspace_id);
			JSONArray ja = new JSONArray();
			
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				String message = rs.getString("m_message");
				String username = rs.getString("u_name");
				
				JSONObject jo = new JSONObject();
				jo.put("user_name", username);
				jo.put("channel_message", message);
				ja.put(jo);
	
			}
			finalJson.put("messages", ja);
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (JSONException e) {
			
			e.printStackTrace();
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}

				
		
		return finalJson.toString();
	}

	public void acceptChannelInvite(int channel_id, int user_id) throws SQLException {
		
		Connection dbConnection = null;
		CallableStatement callableStatement = null;
		String updateChannelUserProc = "{call channel_accept_invite(?,?)}";
		
		try {
			dbConnection = getDBConnection();
			callableStatement = dbConnection.prepareCall(updateChannelUserProc);
			callableStatement.setInt(1, channel_id);
			callableStatement.setInt(2, user_id);
			callableStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {

			if (callableStatement != null) {
				callableStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}

		return;
	}


	public void declineChannelInvite(int channel_id, int user_id) throws SQLException {
		
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		String updateChannelUserProc = "delete from channel_user where c_id = ? and u_id = ?";
		
		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(updateChannelUserProc);

			preparedStatement.setInt(1, channel_id);
			preparedStatement.setInt(2, user_id);

			preparedStatement.executeQuery();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}

		return;
		
	}

	public String getWorkspaceUsers(int workspace_id) throws SQLException {
		
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		JSONObject finalJson = new JSONObject();
		String receiveChannelMessageProc = "select u_name, w_usertype from user natural join workspace_user  where w_id = ?";
		
		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(receiveChannelMessageProc);
			preparedStatement.setInt(1, workspace_id);
			JSONArray ja = new JSONArray();
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				
				String username = rs.getString("u_name");
				String w_usertype = rs.getString("w_usertype");
				JSONObject jo = new JSONObject();
				jo.put("user_name", username);
				jo.put("w_usertype", w_usertype);
				ja.put(jo);
				
			}
			finalJson.put("workspace_user", ja);
            
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (JSONException e) {
			
			e.printStackTrace();
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}
        
		return finalJson.toString();
	}


	public String upgradeToAdmin(String user_name, int workspace_id) throws SQLException {
		
		Connection dbConnection = null;
		CallableStatement callableStatement = null;
		String updateToAdmin = "{call update_general_to_admin(?,?)}";
		
		try {
			dbConnection = getDBConnection();
			callableStatement = dbConnection.prepareCall(updateToAdmin);
			callableStatement.setString(1, user_name);
			callableStatement.setInt(2, workspace_id);
			callableStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {

			if (callableStatement != null) {
				callableStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}

		return null;

	}

	public void addAllUser(int channel_id, int workspace_id) throws SQLException {
	
		Connection dbConnection = null;
		CallableStatement callableStatement = null;
		String updateToAdmin = "{call add_public_channel_users(?,?)}";
		
		try {
			dbConnection = getDBConnection();
			callableStatement = dbConnection.prepareCall(updateToAdmin);

			callableStatement.setInt(1, channel_id);
			callableStatement.setInt(2, workspace_id);

			callableStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {

			if (callableStatement != null) {
				callableStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}

		return;
        
	}

	public String getChannelUsers(int channel_id) throws SQLException {
		
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		JSONObject finalJson = new JSONObject();
		String receiveChannelMessageProc = "select u_name, c_usertype from user natural join channel_user  where c_id = ?";
		
		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(receiveChannelMessageProc);
			preparedStatement.setInt(1, channel_id);
			JSONArray ja = new JSONArray();
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				
				String username = rs.getString("u_name");
				String c_usertype = rs.getString("c_usertype");
				JSONObject jo = new JSONObject();
				jo.put("user_name", username);
				jo.put("c_usertype", c_usertype);
				ja.put(jo);
			}
			finalJson.put("channel_user", ja);
				
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (JSONException e) {
			
			e.printStackTrace();
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}

		return finalJson.toString();

	}

	public String getUsersToInvite(int workspace_id) throws SQLException {
        
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		JSONObject finalJson = new JSONObject();
		String receiveChannelMessageProc = "select distinct u_name , u_email from user where u_id not in (select u_id from workspace_user where w_id != ?);";
		
		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(receiveChannelMessageProc);
			preparedStatement.setInt(1, workspace_id);
			JSONArray ja = new JSONArray();
			
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				
				String username = rs.getString("u_name");
				String useremail = rs.getString("u_email");
				JSONObject jo = new JSONObject();
				jo.put("user_name", username);
				jo.put("user_email", useremail);
				ja.put(jo);	
			}
			finalJson.put("snickr_user", ja);
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}

		return finalJson.toString();
	}

	public String forgotPassword(String user_email) throws SQLException {
		
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		JSONObject jo = new JSONObject();
		String forgotPasswordProc = "select u_random_token from forgot_password where u_email = ?";
		
		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(forgotPasswordProc);
			preparedStatement.setString(1, user_email);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				
				int random_number = rs.getInt("u_random_token");
				jo.put("random_number", random_number);
				
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (JSONException e) {
		
			e.printStackTrace();
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}

		return jo.toString();
	}

}
