package com.example.respons;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;

public class ProfileA extends Activity {
	
	static String[] UserInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		/*************************************************** Set Custom ActionBar *****/
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);

		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(R.layout.actionbar2, null);

		TextView title = (TextView) mCustomView.findViewById(R.id.title);
		title.setText("مدیریت حساب کاربری");

		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
		//-----------------------------------------

        Typeface ir = Typeface.createFromAsset(getAssets(),"iransans.ttf"); 
		//new LongOperation().execute("");
		Button id=(Button) findViewById(R.id.id);id.setTypeface(ir);
		id.setText(appVar.main.UserName);
		View btn1=(View) findViewById(R.id.btn1);
		TextView t=(TextView) findViewById(R.id.t1);t.setTypeface(ir);
		btn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        Intent i = new Intent(ProfileA.this, StoreManagment.class);
		        startActivity(i);
			}
		});
		
		View btn2=(View) findViewById(R.id.btn2);
		t=(TextView) findViewById(R.id.t2);t.setTypeface(ir);
		btn2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        Intent i = new Intent(ProfileA.this, PasswordActivity.class);
		        startActivity(i);
			}
		});
		View btn3=(View) findViewById(R.id.btn3);
		t=(TextView) findViewById(R.id.t3);t.setTypeface(ir);
		btn3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        Intent i = new Intent(ProfileA.this, Account.class);
		        i.putExtra("Type", "0"); //Order
		        startActivity(i);
			} 
		});
		View btn4=(View) findViewById(R.id.btn4);
		t=(TextView) findViewById(R.id.t4);t.setTypeface(ir);
		btn4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        Intent i = new Intent(ProfileA.this, GetAdress.class);
		        i.putExtra("Type", "1"); //Sent
		        startActivity(i);
			}
		});
	}
	/*-----------------------------------------------------------------------------
	private class LongOperation extends AsyncTask<String, Integer, Boolean> {
		ProgressDialog Asycdialog = new ProgressDialog(ProfileA.this);
	    @Override
	    protected void onPostExecute(Boolean result) {
			Asycdialog.dismiss();
	    	super.onPostExecute(result);
	    }

	    @Override
	    protected void onPreExecute() {
	    	Asycdialog.setMessage("در حال خواندن اطلاعات ");
	        Asycdialog.show();	    	
	    	super.onPreExecute();
	    }
	    
	    @Override
	    protected Boolean doInBackground(String... params) {
	    	CallSoap cs=new CallSoap();
			String res=cs.ResiveList("GetUserInfo?id="+appVar.main.UserID);
			UserInfo = res.split(",");
	        return null;
	    }
	}*/
//------------- End of Activity	
}