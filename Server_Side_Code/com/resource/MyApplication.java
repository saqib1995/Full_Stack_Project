package com.resource;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import org.json.JSONObject;
import java_jdbc.Driver;

@Path("/user")
public class MyApplication {
	Driver d = new Driver();
    
	@POST
	@Path("/addAllUser")
	public Response addAllUser(String jsonString) {
		int channel_id;
		int workspace_id;
			
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			channel_id = jsonObject.getInt("channel_id");
			workspace_id = jsonObject.getInt("workspace_id");
			d.addAllUser(channel_id, workspace_id);
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Response.status(200).entity("add all users").build();
	}

	@POST
	@Path("/upgradeToAdmin")
	public Response upgradeToAdmin(String jsonString) {
		String user_name;
		int workspace_id;
	
		try {
			JSONObject jsonObject = new JSONObject(jsonString);	
			user_name = jsonObject.getString("user_name");
			workspace_id = jsonObject.getInt("workspace_id");
			d.upgradeToAdmin(user_name, workspace_id);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Response.status(200).entity("user updated").build();
		
	}
	
	@POST
	@Path("/sendChannelMessages")
	public Response sendChannelMessages(String jsonString) {
		int user_id;
		int channel_id;
		String message;
		String jsonValue = null;
		
		try {
			JSONObject jsonObject = new JSONObject(jsonString);		
			user_id = jsonObject.getInt("user_id");
			channel_id = jsonObject.getInt("channel_id");
			message = jsonObject.getString("message");
			jsonValue = d.sendChannelMessage(user_id, channel_id, message);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(jsonValue).build();
		
	}
	
	@POST
	@Path("/getWorkspaceUsers")
	public Response getWorkspaceUsers(String jsonString) {
		int workspace_id;
		String jsonValue = null;
		
		try {
			JSONObject jsonObject = new JSONObject(jsonString);	
			workspace_id = jsonObject.getInt("workspace_id");
			jsonValue = d.getWorkspaceUsers(workspace_id);
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Response.status(200).entity(jsonValue).build();
	}
	
	@POST
	@Path("/forgotpassword")
	public Response forgotPassword(String jsonString) {
		String user_email;	
		String jsonValue = null;
		
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			user_email = jsonObject.getString("email");
			jsonValue = d.forgotPassword(user_email);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Response.status(200).entity(jsonValue).build();
	}
	
	@POST
	@Path("/getUsersToInvite")
	public Response getUsersToInvite(String jsonString) {
		int workspace_id;
		String jsonValue = null;
		
		try {
			JSONObject jsonObject = new JSONObject(jsonString);	
			workspace_id = jsonObject.getInt("workspace_id");
			jsonValue = d.getUsersToInvite(workspace_id);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(jsonValue).build();
	}
	
	@POST
	@Path("/getChannelUsers")
	public Response getChannelUsers(String jsonString) {
		int channel_id;
		String jsonValue = null;
		
		try {
			JSONObject jsonObject = new JSONObject(jsonString);		
			channel_id = jsonObject.getInt("channel_id");
			jsonValue = d.getChannelUsers(channel_id);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Response.status(200).entity(jsonValue).build();
	}
	
	@POST
	@Path("/receiveChannelMessages")
	public Response receiveChannelMessages(String jsonString) {
		int user_id;
		int channel_id;
		int workspace_id;
		String jsonValue = null;
		
		try {
			JSONObject jsonObject = new JSONObject(jsonString);		
			user_id = jsonObject.getInt("user_id");
			channel_id = jsonObject.getInt("channel_id");
			workspace_id = jsonObject.getInt("workspace_id");
			jsonValue = d.receiveChannelMessage(user_id, channel_id, workspace_id);
						
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Response.status(200).entity(jsonValue).build();
	}
	
	@POST
	@Path("/acceptChannelInvite")
	public Response accChannelInvite(String jsonString) {
		int channel_id;
		int user_id;
	
		try {
			JSONObject jsonObject = new JSONObject(jsonString);	
			channel_id = jsonObject.getInt("channel_id");
			user_id = jsonObject.getInt("user_id");
			d.acceptChannelInvite(channel_id, user_id);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return Response.status(200).entity("Welcome to the Channel").build();
	}
	
	@POST
	@Path("/declineChannelInvite")
	public Response decChannelInvite(String jsonString) {
		int channel_id;
		int user_id;

		try {			
			JSONObject jsonObject = new JSONObject(jsonString);
			channel_id = jsonObject.getInt("channel_id");
			user_id = jsonObject.getInt("user_id");
			d.declineChannelInvite(channel_id, user_id);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return Response.status(200).entity("Declined the invite!").build();
	}
	
	@POST
	@Path("/adduser")
	public Response addUser(String receivedJson) {
		String name;
		String password;
		String email;
		String nickname;
		JSONObject jsonObject;
		
		try {
			jsonObject = new JSONObject(receivedJson);	
			name = jsonObject.getString("name");
			nickname = jsonObject.getString("nickname");
			email = jsonObject.getString("email");
			password = jsonObject.getString("password");
			d.insertUserDatabase(name, nickname, email, password);
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Response.status(200).entity("user added succesfully").build();
	}

	@POST
	@Path("/verifyuser")
	public Response verifyUser(String jsonString) {
		String email;
		String password;
		StringBuilder sb;
		boolean status = false;
		String n_pass;
		String hashtext;
		
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			email = jsonObject.getString("email");
			password = jsonObject.getString("password");
			n_pass = d.verifyUserFromDatabase(email);
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hashInBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

			sb = new StringBuilder();
			for (byte b : hashInBytes) {
				sb.append(String.format("%02x", b));
			}
			hashtext = sb.toString();

			if (hashtext.equals(n_pass)) {

				status = true;
			} else {
				status = false;
			}
			
			if(n_pass == null) {
				return Response.status(200).entity(null).build();
			}
			
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(status).build();

	}

	@POST
	@Path("/createWorkspace")
	public Response createWorkspace(String jsonString) {
		int user_id;
		String workspace_name;
		String workspace_description;
	
		try {
			JSONObject jsonObject = new JSONObject(jsonString);	
			user_id = jsonObject.getInt("user_id");
			workspace_name = jsonObject.getString("workspace_name");
			workspace_description = jsonObject.getString("workspace_description");
			d.insertWorkspaceDatabase(user_id, workspace_name, workspace_description);
		} catch (SQLException | JSONException e) {
			e.printStackTrace();
		}

		return Response.status(200).entity("created workspace").build();
	}

	@POST
	@Path("/createChannel")
	public Response createChannel(String jsonString) {
		int user_id = 0;
		int workspace_id = 0;
		String channel_name = null;
		String channel_type = null;
		
		try {
			
			JSONObject jsonObject = new JSONObject(jsonString);
			user_id = jsonObject.getInt("user_id");
			workspace_id = jsonObject.getInt("workspace_id");
			channel_name = jsonObject.getString("channel_name");
			channel_type = jsonObject.getString("channel_type");
		
			d.insertChannelDatabase(user_id, workspace_id, channel_name, channel_type);
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return Response.status(200).entity("created channel").build();
	}

	@POST
	@Path("/sendWorkspaceInvite")
	public Response sendWorkspaceInvite(String jsonString) {
		int user_id;
		int workspace_id;
		
		try {
			JSONObject jsonObject = new JSONObject(jsonString);		
			user_id = jsonObject.getInt("user_id");
			workspace_id = jsonObject.getInt("workspace_id");
			d.insertIntoWorkspaceUser(user_id, workspace_id);
					
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Response.status(200).entity("Invited User Succesfully").build();

	}
	
	@POST
	@Path("/sendChannelInvite")
	public Response sendChannelInvite(String jsonString) {
		int user_id;
		int channel_id;
		int workspace_id;
	
		try {
			JSONObject jsonObject = new JSONObject(jsonString);		
			user_id = jsonObject.getInt("user_id");
			workspace_id = jsonObject.getInt("workspace_id");
			channel_id = jsonObject.getInt("channel_id");
			d.insertIntoChannelUser(user_id, channel_id, workspace_id);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Response.status(200).entity("Invited User Succesfully").build();

	}

	@POST
	@Path("/checkWorkspaceInvites")
	public Response checkWorkspaceInv(String jsonString) {
		int user_id;		
		String jsonValue = null;
		
		try {	
			JSONObject jsonObject = new JSONObject(jsonString);
			user_id = jsonObject.getInt("user_id");
			jsonValue = d.checkWorkspaceInvites(user_id);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return Response.status(200).entity(jsonValue).build();
	}
	
	@POST
	@Path("/checkChannelInvites")
	public Response checkChannelInv(String jsonString) {
		int user_id;
		int workspace_id;
		String jsonValue = null;
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			user_id = jsonObject.getInt("user_id");
			workspace_id = jsonObject.getInt("workspace_id");
			jsonValue = d.checkChannelInvites(user_id, workspace_id);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return Response.status(200).entity(jsonValue).build();
	}

	@POST
	@Path("/acceptWorkspaceInvite")
	public Response accWorkspaceInvite(String jsonString) {		
		int workspace_id;
		int user_id;
		
		try {
			JSONObject jsonObject = new JSONObject(jsonString);	
			workspace_id = jsonObject.getInt("workspace_id");
			user_id = jsonObject.getInt("user_id");
			d.acceptWorkspaceInvite(workspace_id, user_id);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return Response.status(200).entity("Welcome to the workspace").build();
	}
	
	@POST
	@Path("/declineWorkspaceInvite")
	public Response decWorkspaceInvite(String jsonString) {
		int workspace_id;
		int user_id;
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			workspace_id = jsonObject.getInt("workspace_id");
			user_id = jsonObject.getInt("user_id");
			d.declineWorkspaceInvite(workspace_id, user_id);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return Response.status(200).entity("Declined the invite!").build();
	}
	
	@POST
	@Path("/getCurrentChannels")
	public Response getChannelNames(String jsonString) {
		int user_id;
		int workspace_id;
		String returnedJson = null;
		JSONObject jsonObject;
		
		try {
			jsonObject = new JSONObject(jsonString);
			user_id = jsonObject.getInt("user_id");
			workspace_id = jsonObject.getInt("workspace_id");
			returnedJson = d.getCurrentChannels(user_id, workspace_id);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Response.status(200).entity(returnedJson).build();
	}
	
	@POST
	@Path("/getCurrentWorkspaces")
	public Response getWorkspaceNames(String jsonString) {
		int user_id;
		String returnedJson = null;
		JSONObject jsonObject;
		
		try {
			jsonObject = new JSONObject(jsonString);
			user_id = jsonObject.getInt("user_id");
			returnedJson = d.getCurrentWorkspaces(user_id);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Response.status(200).entity(returnedJson).build();
	}
	
	@POST
	@Path("/getUserId")
	public Response getUserId(String jsonString) {
		String user_email;
		String jsonValue = null;
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			user_email = jsonObject.getString("user_email");
			jsonValue = d.getUserId(user_email);
		}  catch (JSONException e) {
			e.printStackTrace();
		}
		
		return Response.status(200).entity(jsonValue).build();
	}
	
	
	@POST
	@Path("/getWorkspaceId")
	public Response getWorkspaceId(String jsonString) {
		String workspace_name;
		String jsonValue = null;
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			workspace_name = jsonObject.getString("workspace_name");
			jsonValue = d.getWorkspaceId(workspace_name);
		}  catch (JSONException e) {
			e.printStackTrace();
		}
		
		return Response.status(200).entity(jsonValue).build();
	}
	
	@POST
	@Path("/getChannelId")
	public Response getChannelId(String jsonString) {
		String channel_name;
		int workspace_id;
		String jsonValue = null;
		
		try {
			JSONObject jsonObject = new JSONObject(jsonString);	
			channel_name = jsonObject.getString("channel_name");
			workspace_id = jsonObject.getInt("workspace_id");
			jsonValue = d.getChannelId(channel_name, workspace_id);
		}  catch (JSONException e) {
			e.printStackTrace();
		}
		
		return Response.status(200).entity(jsonValue).build();
	}
}
