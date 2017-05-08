package com.example.respons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.ActionBar;
//import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
//import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class PasswordActivity extends AppCompatActivity {

	EditText phone, password, newpassword;
	TextView tv;
	SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password);
		/*************************************************** Set Custom ActionBar *****/
		getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.actionbar2);
		View mCustomView = getSupportActionBar().getCustomView();
		TextView title = (TextView) mCustomView.findViewById(R.id.title);
		title.setText("تغییر رمز ورود");
		//-----------------------------------------
		sp = getSharedPreferences("share", 0);
		phone = (EditText) findViewById(R.id.tel);
		phone.setText(appVar.main.UserName);
//		final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater()
//				.inflate(R.layout.login_action_bar, null);
//
//		// Set up my ActionBar
//		final ActionBar actionBar = getActionBar();
//		actionBar.setDisplayShowHomeEnabled(false);
//		actionBar.setDisplayShowTitleEnabled(false);
//		actionBar.setDisplayShowCustomEnabled(true);
//		actionBar.setCustomView(actionBarLayout);
//
//		// my customization
//		final int actionBarColor = getResources().getColor(R.color.action_bar);
//		actionBar.setBackgroundDrawable(new ColorDrawable(actionBarColor));
//
//		final TextView actionBarSent = (TextView) findViewById(R.id.Login_action_bar_text);
//		actionBarSent.setText("register");
//		final ImageButton actionBarbotton = (ImageButton) findViewById(R.id.Login_action_bar_back);
//
	}
	// ثبت نام وبررسی درست وارد کردن اطلاعات
	public void onclick(View btn) {
		if(btn == findViewById(R.id.button1))
		{
		password = (EditText) findViewById(R.id.pass);
		newpassword = (EditText) findViewById(R.id.npass);
		String regexStr = "(0|\\+98)?([ ]|-|[()]){0,2}9[1|2|3|4]([ ]|-|[()]){0,2}(?:[0-9]([ ]|-|[()]){0,2}){8}";
		String number = phone.getText().toString();
		if (phone.getText().toString().length() != 10
				|| number.matches(regexStr) == false) {
			Toast.makeText(this,
					"Please enter " + "\n" + " valid phone number",
					Toast.LENGTH_SHORT).show();
		} else {
			if (password.getText().toString()
					.equalsIgnoreCase(newpassword.getText().toString().trim())) {
				tv = (TextView) findViewById(R.id.textView2);
				new Thread(new Runnable() {
					@Override
					public void run() {
						register();
					}
				}).start();
			} else {

				DisplayResult("password and confirmpassword do not match to each other");
			}
		}
	}
//		else if(btn == findViewById(R.id.Login_action_bar_back))
//		{
//			super.onBackPressed();
//		}
	}

	// وصل شدن به سرور
	public void register() {

			String result = "";
	        CallSoap cs=new CallSoap();
	        result=cs.ResiveList("ChangePassword?name="+phone.getText().toString()+
	        		"&pass="+password.getText().toString());
			DisplayResult(result);
	}

	// نمایش نتیجه
	public void DisplayResult(final String result) {

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (result.substring(0, 2).equals("ok")) {
					Toast.makeText(getApplicationContext(), "خوش آمدید",
							Toast.LENGTH_SHORT).show();
					appVar.main.UserName=phone.getText().toString();
					appVar.main.UserID=result.substring(2);
					appVar.main.HasShop=true;
					Editor editor = sp.edit();
					editor.putString("phonekey", phone.getText().toString());
					//editor.apply();
					finish();
				} else {
					Toast.makeText(getApplicationContext(),
							"یوز یا پسورد درست وارد نشده است" + result,
							Toast.LENGTH_SHORT).show();
				}

			}
		});
	}
	//------------------------------------------------------ Action Bar Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.action, menu);
		return true;
	}

	//-----------------------------
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.arrow_back:
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}
