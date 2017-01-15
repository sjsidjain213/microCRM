package controller;
import javax.ws.rs.core.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class MailList {
private static String ListName,MemberList,message,MemberName,subject;
    // for setting and getting message of mail
    public void setMessage(String message)
    {
	this.message=message;
	}
    
    public String getMessage()
    {
	return message;
	}
    // for setting  and getting name of list
    public void setListName(String name)
    {
	this.ListName = name;
	}
	
    public String getListName()
	{
		return ListName;
	}
	//for setting and getting name of Members inside a List
    public void setMemberName(String name)
	{
		MemberName = name;
	}
	
    public String getMemberName()
	{
		return MemberName;
	}
	//for setting and getting List of Members in form of JSON formatted String
    public void setMembersList(String s)
	{
		MemberList = s;
	}
    public String getMemberList()
    {
    	return MemberList;
    }
    // for setting and getting subject of Mail
    public void setSubject(String subject)
    {
    this.subject=subject;	
    }
    public String getSubject()
    {
    	return subject;
    }
    
	//Create MailList, ListName is need to be  supplied for setting List name 
    public static ClientResponse CreateMailingList() {
	       Client client = Client.create();
	       client.addFilter(new HTTPBasicAuthFilter("api",
	                       "key-2146143b75b920c2a85df48a75427f49"));
	       WebResource webResource =
	               client.resource("https://api.mailgun.net/v3/lists");
	       MultivaluedMapImpl formData = new MultivaluedMapImpl();
	       formData.add("address", ListName+"@javacrunch.in");
	       formData.add("description", "Mailgun developers list");
	       return webResource.type(MediaType.APPLICATION_FORM_URLENCODED).
	               post(ClientResponse.class, formData);
                                                        }
    
    // Add member in list,variable  MemberList is needed to be supplied for providing name of all member to add 
	public static ClientResponse AddListMember()
	{
	    System.out.println(ListName+";;"+MemberList);   
		Client client = Client.create();
	       client.addFilter(new HTTPBasicAuthFilter("api",
	                       "key-2146143b75b920c2a85df48a75427f49"));
	       WebResource webResource =
	               client.resource("https://api.mailgun.net/v3/lists/" +
	                               ListName+"@javacrunch.in/members.json");
	       MultivaluedMapImpl formData = new MultivaluedMapImpl();
	       formData.add("members",MemberList);
	       formData.add("upsert", true);
	       return webResource.type(MediaType.APPLICATION_FORM_URLENCODED).
	               post(ClientResponse.class, formData);
	}
	
	//show all available list
	public static ClientResponse ListingMembers()
	{
	       Client client = Client.create();
	       client.addFilter(new HTTPBasicAuthFilter("api",
	                       "key-2146143b75b920c2a85df48a75427f49"));
	       WebResource webResource =
	               client.resource("https://api.mailgun.net/v3/lists/pages");
	       return webResource.get(ClientResponse.class);
	}
	
	// Send Message, variable ListName and Message is required to be supplied
	public static ClientResponse SendSimpleMessage() 
	{
	       Client client = Client.create();
	       client.addFilter(new HTTPBasicAuthFilter("api",
	                       "key-2146143b75b920c2a85df48a75427f49"));
	       WebResource webResource =
	               client.resource("https://api.mailgun.net/v3/javacrunch.in" +
	                               "/messages");
	       MultivaluedMapImpl formData = new MultivaluedMapImpl();
	       formData.add("from", "Excited User <mailgun@javacrunch.in>");
	       formData.add("to", ListName);
	       formData.add("to", "YOU@javacrunch.in");
	       formData.add("subject", subject);
	       formData.add("text", message);
	       return webResource.type(MediaType.APPLICATION_FORM_URLENCODED).
	               post(ClientResponse.class, formData);
	}
}
