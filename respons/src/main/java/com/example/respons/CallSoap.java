package com.example.respons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.commons.lang3.StringEscapeUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Xml;

public class CallSoap
{
	String Resv="";
//---------------------------------------------
public static boolean isConnectionAvailable(Context context) {
	 
    ConnectivityManager connectivityManager = (ConnectivityManager) context
            .getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivityManager != null) {
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()
                && netInfo.isConnectedOrConnecting()
                && netInfo.isAvailable()) {
            return true;
        }
    }
    return false;
}
//-----------------------------------------------
public  String convertStreamToString(InputStream is)
{
	BufferedReader reader = null;
		reader = new BufferedReader(new InputStreamReader(is));
	StringBuilder sb = new StringBuilder();

	String line = null;
	try {
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
	}
	catch (IOException e) {
		e.printStackTrace();
	}
	finally {
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	return sb.toString();
}
//--------------------------------------------------------
public String ResiveListSync(String addr) throws Exception{
    
	String bnds=
    new AsyncTask<String, Void, String>(){
        @Override
        protected void onPreExecute() {
        };
        @Override
        protected String doInBackground(String... params) {
            return  ResiveList (params[0]);
        }
        @Override
        protected void onPostExecute(String result)
        {
        }
    }.execute(addr).get();
    return bnds;
}
//--------------------------------------------------------
public  String ResiveList (String addr)
{
    try
	{
		System.out.println("guru");
		DefaultHttpClient httpClient=new DefaultHttpClient();
		addr=addr.replaceAll(" " ,"%20");
//		HttpGet httpGet=new HttpGet("http://10.0.2.2/RestServiceImpl.svc/"+addr);
		HttpGet httpGet=new HttpGet("http://wcf.kapma.ir/RestServiceImpl.svc/"+addr);
		//Get the response
		HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();
    	InputStream stream=httpEntity.getContent();
    	//Convert the stream to readable format
        String result= convertStreamToString(stream);
        result=result.replaceAll("\"", "");
        result=result.replaceAll("\n", "");
    	return result;
	}
	catch(Exception e)
	{
		return null;
	}
}

	public List<TopicList> GetTopics(String SID) throws Exception {
		JSONObject jsonResponse;
		List<TopicList> brands = new ArrayList<TopicList>();

		try {
			String Content = ResiveListSync("Topiclist2");
			String part1=Content.substring(0,Content.indexOf("[{"));
			String part2=Content.substring(Content.indexOf("[{"),Content.length());
			//String sssss = StringEscapeUtils.escapeJava(Content);
			String JsonStr="{\"TopicList2Result\":"+convertStandardJSONString(part2);
			jsonResponse = new JSONObject(JsonStr);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("TopicList2Result");

			for (int i = 0; i < jsonMainNode.length(); i++) {
				/****** Get Object for each JSON node. ***********/
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

				// جدا کننده سه رقم
				// DecimalFormat f = new DecimalFormat("#,###");
				// DecimalFormatSymbols fs = f.getDecimalFormatSymbols();
				// fs.setGroupingSeparator(',');
				// f.setDecimalFormatSymbols(fs);
				TopicList company = new TopicList();

				/******* Fetch node values **********/
				// String min =
				// f.format(Integer.parseInt(jsonChildNode.optString("min").toString()));
				// String max =
				// f.format(Integer.parseInt(jsonChildNode.optString("max").toString()));
				company.ID = jsonChildNode.optString("ID").toString();
				company.Topic = jsonChildNode.optString("Topic1").toString();
				company.Image = jsonChildNode.optString("Image").toString();
				company.ParentID = jsonChildNode.optString("ParentID").toString();
				company.Step = jsonChildNode.optString("Step").toString();
				brands.add(company);
			}
		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
		return brands;
	}

	public String convertStandardJSONString(String data_json){
//		data_json = data_json.replace("\\", "");
		data_json = data_json.replace("{", "{\"");
		data_json = data_json.replace("},", "\"},");
		data_json = data_json.replace(":", "\":\"");
		data_json = data_json.replace(",", "\",\"");
		data_json = data_json.replace("}\",\"{", "},{");
		data_json = data_json.replace("}]", "\"}]");
		return data_json;
	}
//-------End Class
}

class TopicList {

	public  String ID="";
	public  String Topic="";
	public  String Image="";
	public  String ParentID="";
	public  String Step="";
}
