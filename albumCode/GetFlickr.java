//
// File: GetFlickr.java
// 
// Instructions:
// 1) replace ****YOUR API KEY HERE**** with your flickr API key
// 2) compile and run
// 3) returns response string with flickr images with tag SFSUCS413F16Test
// 4) parse into JSON object with gson

package albumCode;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import com.google.gson.Gson;

class GetFlickr {
	int perPageCount;
	String searchKey;
	final String API  = "https://api.flickr.com/services/rest/?method=flickr.photos.search";
	
	
    public GetFlickr() {
    	perPageCount = 16;
    	searchKey = "";
    }
    
    public int getPerPageCount() {
		return perPageCount;
	}

	public void setPerPageCount(int perPageCount) {
		this.perPageCount = perPageCount;
	}
	
	
	
	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public Response search() throws IOException{
		String request = API + "&per_page=" + perPageCount;
		request += "&format=json&nojsoncallback=1&extras=geo";
		request += "&api_key=" + "edc03e3cea1d82a5ff485eb5138d063d";
		    
		    
		if (searchKey.length() != 0) {
		    request += "&tags="+searchKey;
		}
		
		
		// open http connection
		URL obj = new URL(request);
		    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		// send GET request
		con.setRequestMethod("GET");
		
		// get response
		int responseCode = con.getResponseCode();
		
		
		// read and construct response String
	    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	    String inputLine;
	    StringBuffer response = new StringBuffer();
	
	    while ((inputLine = in.readLine()) != null) {
	        response.append(inputLine);
	    }
	    in.close();
		
		Gson gson = new Gson();
		String s = response.toString();
		
		Response responseObject = gson.fromJson(s, Response.class);
//		int farm = responseObject.photos.photo[0].farm;
//		String server = responseObject.photos.photo[0].server;
//		String id = responseObject.photos.photo[0].id;
//		String secret = responseObject.photos.photo[0].secret;
//		String photoUrl = "http://farm"+farm+".static.flickr.com/"
//		+server+"/"+id+"_"+secret+".jpg";
//		System.out.println(photoUrl);

		return responseObject;
	}
}
