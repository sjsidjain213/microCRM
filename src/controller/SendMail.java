package controller;

import java.io.BufferedReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.jersey.api.client.ClientResponse;

import controller.MailList;
/**
 * Servlet implementation class SendMail
 */
@WebServlet("/SendMail")
   public class SendMail extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
    public SendMail() {
        super();

    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	// MailList class contains all the method required for different operaions with Mailgun Api.
	  MailList m = new MailList();
    
	  /* Retrieve name of all exsisting list from MailGun API
      Maillist is seprate class which contains all the method responsible for communicating with
      MailGun API
      */
      ClientResponse cr  = MailList.ListingMembers();
      String output = cr.getEntity(String.class);
	
      try {
		  //listdisplay is JSON formatted String which will contain address of all the
    	  //user present in selected list  
    	  String listdisplay="[";
		  
    	  /*JSONObject and JSONParser belong to externaly added jar json-simple-1.1.1.jar 
		     (http://juliusdavies.ca/json-simple-1.1.1-javadocs/org/json/simple/JSONObject.html)
		     these belong to org.json.simple.JSONObject and org.json.simple.parser.JSONParser class respectively
		     These 2 class provide various methods which make parsing over JSON Object optimized
		      */
		   
		    JSONParser parser = new JSONParser();
		    JSONObject joUser = null;
		    JSONArray ja = null;
		    joUser = (JSONObject) parser.parse(output.toString());
		      ja = (JSONArray)joUser.get("items");
		  
		  /*This for loop convert response received from MailGun's ListingMember method
		  this method when invoked return's a JSON object which contains all list available on MailGun
		    This loop convert response of ListingMember method in this format->
		   ["address":"list@domain.com"]
		    
		    This is then stored in variable listdisplay in order to send this as reposne to maillist.html
		   */
			for (int i = 0; i < ja.size(); i++)
			{   joUser = (JSONObject) parser.parse(ja.get(i).toString());		  
			   String post_id = joUser.get("address").toString();
         	    listdisplay = listdisplay + "{\"address\":"+"\""+post_id+"\"},";
			}	
			listdisplay = listdisplay.substring(0,listdisplay.length()-1);
			listdisplay = listdisplay+"]";
	        PrintWriter out = response.getWriter();
	       //variable listdispay send to maillist.html as response
	       if(listdisplay.equals("]")){}
	       else{  out.println(listdisplay);}
		     } 
		 catch (Exception e) 
		     {
			e.printStackTrace();
		     }
		}
	
// this method is used for sending mail to list selected by users	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// process for reading and storing data came with request form option.html
		// data is stored in StringBuilder sb
		StringBuffer sb = new StringBuffer();
	    try 
	    {
	      BufferedReader reader = request.getReader();
	      String line = null;
	      while ((line = reader.readLine()) != null)
	      {
	        sb.append(line);
	      }
	    } catch (Exception e) { e.printStackTrace(); }
	
	    /* Data came from HTML page is in JSON format
	     / Format of JSON Data received --> {"listaddess":"value","listaddress":"value"} */   
	
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
	 /*
variable listname -> contains all the name of list selected by user for sending mail
variable message -> contains message to be sent
variable subject -> contains subject of message to be sent
	  */
	    String listname= joUser.get("name").toString();
	    String message = joUser.get("message").toString();
	   String subject = joUser.get("subject").toString();
// in case of no subject and message body
	   if(message.length()==0){message=" ";}
	if(subject.length()==0){subject=" ";}
	   /*SupportBean is seprate class which contain method listMembers which convert JSON Formatted
	      String into arrayList of String. This arraylist contain name of all the list to whom mail is to be
	      send 
	   */
	   SupportBean support = new SupportBean();
	    ArrayList<String> al =  support.listMembers(listname);
	    // Check if No mail list have been selected by user  
	  if(al.size()==0)
	 {
	response.getWriter().println("1");	 
	 }else{
		// MailList class contains all the method required for different operaions with Mailgun Api.
	 MailList m = new MailList();
	 
	 // setter methods of MailList class
	 m.setMessage(message);
	 m.setSubject(subject);
	 
	 // loop for sending mail to all the list selected by users
	 // arraylist al conatin name of all the list for sending 
	 // SendSimpleMessage method of MailGun API for sending mail
	 for(int i=0;i<al.size();i++)
	  {m.setListName(al.get(i));
	   MailList.SendSimpleMessage();
	  }
	 }
   }
}

