package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.sun.jersey.api.client.ClientResponse;

import controller.MailList;
import controller.SupportBean;

@WebServlet("/ListCreation")
public class ListCreation extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ListCreation() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    
		//This will receive a GET request from newlist.html.
	    //Main objective of this method is to display name and email id of all the registered users 
		//on newlist.html	
		try{
			//Establishing connection with MySQL Database
			DriverManager.registerDriver(new com.mysql.jdbc.Driver() );
			String url="jdbc:mysql://localhost:3306/test?user=root&password=sidsql";
			Connection con = DriverManager.getConnection(url); 
			Statement   stmt = (Statement) con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from coursecode");
		
/* Table coursecode have all the entries made by user on page option.html 	
		  schema of table coursecode -->
		TABLE coursecode
(
ID int unsigned NOT NULL,
Name varchar(255) NOT NULL,
Email varchar(255) NOT NULL,
PRIMARY KEY (P_Id)
)
		*/	
			
		//String json --> This String will contain name and email id of all the registered users  
		// This String is created in JSON Format in order to make it compatible with AngularJS at front 
			String json="[";
			rs.beforeFirst();
			while(rs.next())
{			 json=json+"{\""+"id"+"\":"+"\""+rs.getString("id")+"\","+"\""+"name"+"\":"+"\""+rs.getString("name")+"\","+"\""+"email"+"\":"+"\""+rs.getString("email")+"\"},";
}
		    json= json.substring(0,json.length()-1);
			json= json+"]";
			System.out.println(json);
			PrintWriter out = response.getWriter();
if(json.equals("]")){json="";System.out.println("hello");}
else{out.println(json);}	        
       //send JSON formatted response to newlist.html
   
	}
			catch(SQLException e)
			{    
				e.printStackTrace();
			}
	}

// method for receving post request form newlist.html	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	// Main Objective of this method is to create a List and Add all the members selected by user  to the list.  

		// process for reading and storing data came with request form option.html
		// data is stored in StringBuilder sb
	
		StringBuilder sb = new StringBuilder();
	    try 
	    {
	      BufferedReader reader = request.getReader();
	      String line = null;
	      while ((line = reader.readLine()) != null)
	      {System.out.println(line);
	        sb.append(line);
	      }
	    } catch (Exception e) { e.printStackTrace(); }
	
	    /* Data came from HTML page is in JSON format
	     / Format of JSON Data received --> {"key":"value","key":"value"} */   
	
	     /*JSONObject and JSONParser belong to externaly added jar json-simple-1.1.1.jar 
	     (http://juliusdavies.ca/json-simple-1.1.1-javadocs/org/json/simple/JSONObject.html)
	     these belong to org.json.simple.JSONObject and org.json.simple.parser.JSONParser class respectively
	     These 2 class provide various methods which make parsing over JSON Object optimized
	      */
	    JSONParser parser = new JSONParser();
	    JSONObject joUser = null;
	    try
	    {
	      joUser = (JSONObject) parser.parse(sb.toString());
	    } catch (Exception e) { e.printStackTrace(); }
	    
	      String name= joUser.get("name").toString();
	      String listname = joUser.get("filename").toString();
         
	     /* Retrieve name of all exsisting list from MailGun API
	      Maillist is seprate class which contains all the method responsible for communicating with
	      MailGun API
	      */
	      ClientResponse cr  = MailList.ListingMembers();
	      String output = cr.getEntity(String.class);

	      /*listMembers --> return arraylist of all mail id selected by user from newlist page 
	      SupportBean is seprate class which contain method listMembers which convert JSON Formatted
	      String into arrayList of String. This arraylist contain name of all the list present 
	      on  MailGun
	      */
	      SupportBean supportb = new SupportBean(); 
	      ArrayList<String> eligiblemembers = supportb.listMembers(name);
	
         // Check if No mail Id have been selected by user or Listname is empty or Listname already Exsist  
        if(eligiblemembers.size()==0||listname.trim().length()<=0||!supportb.listexsist(output, listname))
        {// in case of invalid input	
    	 response.getWriter().println("1");
         }
        else{
	           try{
	   			//Establishing connection with MySQL Database
	        	DriverManager.registerDriver(new com.mysql.jdbc.Driver() ); // this will give error if path of sql is not set
		        String url="jdbc:mysql://localhost:3306/test?user=root&password=sidsql";
	            Connection con = DriverManager.getConnection(url); 
	            Statement   stmt = (Statement) con.createStatement();
				// name of schema test , name of table coursecode
				/*Schema of table coursecode
				 table coursecode(
				 ID int unsigned NOT NULL,
	             Name varchar(255) NOT NULL,
	             Email varchar(255) NOT NULL,
	             PRIMARY KEY (P_Id)
				 )
				 */
	            ResultSet rs = stmt.executeQuery("select * from coursecode");

	// String json, will conatins all the email address which are to be added in List	and this String is in JSON
	// to make it compatible with mailgun API
	String json="[";
	rs.beforeFirst();
	while(rs.next())
  {// The condition in if loop will be true if ID's received from newlist.html matches with database ID 
   // In this loop email address in database is compared with email address selected by user
   // if loop will be true only for those email address which are selected by user		
		if(eligiblemembers.contains(rs.getString("ID")))
    {json=json+"{\""+"address"+"\":"+"\""+rs.getString("Email")+"\","+"\""+"name"+"\":"+"\""+rs.getString("name")+"\"},";	 }
  }
	json =json.substring(0,json.length()-1);
	json=json+"]";

  // MailList class contains all the method required for different operaions with Mailgun Api.
   MailList m = new MailList();
   
   // setter methods of MailList class
   m.setListName(listname);
   m.setMembersList(json);
   
   // MailGun API methods for creating list and adding members
   MailList.CreateMailingList();
   MailList.AddListMember();
	           }
catch(Exception e)
{System.out.println(e.getMessage());
	}}	}