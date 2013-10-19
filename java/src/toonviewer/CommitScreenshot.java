package toonviewer;

import java.applet.AppletContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import com.sun.corba.se.pept.transport.Connection;

public class CommitScreenshot {

	public static void commit(String response, String toon_id,AppletContext context)
	{
		try 
		{
			String base_url="http://www.3darmory.com/commit_ss";
			String xml_params=URLEncoder.encode(response,"UTF-8");
			base_url+= "?t=" + toon_id + "&x=" +xml_params;
						
			URL url = new URL(base_url);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		    connection.setDoInput(true);  // We want feedback!
		    connection.setUseCaches(false); // No cache, it is (supposed to be) a new image each time, even if URL is always the same
		    connection.setRequestProperty("User-Agent", "3DArmory.java.screenshot_uploader");
		    
		    int response_code=connection.getResponseCode();
		    
		    if(response_code!=500)
		    {
		    	BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		    	String line="";
		    	String output="";
		    	while ((line = in.readLine()) != null) {
		    		output+=line;
		    	}			
		    	
		    	// open the result url
		    	URL result_url=null;
		    	try 
		    	{
		    		result_url=new URL(output);
		    		context.showDocument(result_url,"_blank");
		    	}
		    	catch (MalformedURLException e) {System.out.println("malformed url:" + e.getMessage());}
		    }
		    else
		    {
		    	System.out.println("Error processing screenshot:\n");
		    	BufferedReader error = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
		    	String error_line="";
		    	while((error_line=error.readLine()) != null) {System.out.println(error_line);}
		    }   	
		} 
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}

/*
	connection.setDoOutput(true); // We output stuff
	connection.setRequestMethod("POST");  // With POST method		    
	OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
	out.write(params);
	out.close();
*/


	//URL url=null;
//try {
//		url=new URL(response);
//			applet_context.showDocument(url,"_blank");
//			System.out.println(url);
	//}
	//catch (MalformedURLException e) {System.out.println("malformed url:" + e.getMessage());}