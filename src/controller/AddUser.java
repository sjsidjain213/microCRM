package controller;

import java.io.BufferedReader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@WebServlet("/AddUser")
public class AddUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public AddUser() {
        super();
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// process for reading and storing data came with request form option.html
		// data is stored in StringBuilder sb
		StringBuilder sb = new StringBuilder();
	    try 
	    {
	      BufferedReader reader = request.getReader();
	      String line = null;
	      while ((line = reader.readLine()) != null)
	      { sb.append(line);
	      }
	    } catch (Exception e) { e.printStackTrace(); }
	     /* Data came from HTML page is in JSON format
	     / Format of JSON Data received --> {"name":"value","email":"value"} */   
	
	     /*JSONObject and JSONParser belong to externaly added jar json-simple-1.1.1.jar 
	     (http://juliusdavies.ca/json-simple-1.1.1-javadocs/org/json/simple/JSONObject.html)
	     these belong to org.json.simple.JSONObject and org.json.simple.parser.JSONParser class respectively
	     These 2 class provide various methods which make parsing over JSON Object optimized
	      */
	    JSONParser parser = new JSONParser();
	    JSONObject joUser = null;
        String name="";
        String email="";	 
try
	    {
	    	//creating JSONObject out of JSON formatted String
	      joUser = (JSONObject) parser.parse(sb.toString());
	      name= joUser.get("name").toString();
	       email = joUser.get("email").toString();

	    } catch (Exception e) { e.printStackTrace(); }
	       
	        // retrieving and storing data received along with request from option.html
	     
// check for if name or email is empty or email is not appropriate format	      
if(name.trim().length()<=0||email.trim().length()<=0||!(email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")))
{
// in case of invalid input	
response.getWriter().println("1");
}else{
	
		try{
			//Establishing connection with MySQL Database
			DriverManager.registerDriver(new com.mysql.jdbc.Driver() );
			// name of schema test , name of table coursecode
			/*Schema of table coursecode
			 table coursecode(
			 ID int unsigned NOT NULL,
             Name varchar(255) NOT NULL,
             Email varchar(255) NOT NULL,
             PRIMARY KEY (P_Id)
			 )
			 */
			String url="jdbc:mysql://localhost:3306/test?user=root&password=sidsql";
			Connection con = DriverManager.getConnection(url); 
			String query = " insert into coursecode (Name,Email)"
			        + " values (?, ?)";
			      // adding data in database 
			      // create the mysql insert preparedstatement
			      PreparedStatement ps = con.prepareStatement(query);
			      ps.setString (1, name);
			      ps.setString (2, email);
			      ps.execute();
	        }
			catch(SQLException e)
			{    
				e.printStackTrace();
			}
}
	}

}
