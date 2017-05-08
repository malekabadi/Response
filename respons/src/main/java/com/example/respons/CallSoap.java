package com.example.respons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
	BufferedReader reader = new BufferedReader(new InputStreamReader(is));
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

//-------End Class
}

