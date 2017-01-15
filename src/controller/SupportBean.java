package controller;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;


public class SupportBean {
private ArrayList <String> al = new ArrayList<String>();
	public ArrayList<String> listMembers(String s)
{	if(s.length()<=2){return al;}
		System.out.println(s);
		s=s.substring(1,s.length());
s=s.substring(0,s.length()-1);
s=s.replaceAll("[\"w]", "");		
String indv[] =s.split(",") ;
for(int i=0;i<indv.length;i++)
{
	System.out.println(indv[i]);
	String lap[] =indv[i].split(":");
	String id = lap[0];
	String status = lap[1];
	if(lap[1].equals("true"))
	{
		al.add(lap[0]);
	}
}
	System.out.println(s+al.size());
		return al;
	}
public boolean listexsist(String output,String name)
{    JSONObject json;
name = name+"@javacrunch.in";
boolean flag=true;
try {
	JSONObject obj = new JSONObject(output);
	JSONArray arr = obj.getJSONArray("items");
	for (int i = 0; i < arr.length(); i++)
	{
	    String post_id = arr.getJSONObject(i).getString("address");
	if(post_id.equals(name))
	{
		flag=false;
	}
	}	

	}
catch(Exception e)
{
	}
return flag;
}
}
