package com.example.respons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.ActionBar;
//import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
//import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends MenuRight {

	Button logbtn;
	EditText phone;
	EditText pass;
	SharedPreferences sp;
	ProgressDialog progressDialog;
	Handler handler = new Handler();
	static  AppCompatActivity lgin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		lgin=this;
		/*************************************************** Set Custom ActionBar *****/
		getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.actionbar2);
		View mCustomView = getSupportActionBar().getCustomView();
		TextView title = (TextView) mCustomView.findViewById(R.id.title);
		title.setText("ورود به حساب کاربری");
		//-----------------------------------------

		progressDialog = new ProgressDialog(LoginActivity.this);
		phone = (EditText) findViewById(R.id.tel);
		pass = (EditText) findViewById(R.id.pass);
		logbtn = (Button) findViewById(R.id.button1);
		sp = getSharedPreferences("share", 0);
		String user = sp.getString("share", "");
		Button Register = (Button) findViewById(R.id.Register);
		Register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
			}
		});
//		final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater()
//				.inflate(R.layout.login_action_bar, null);
//
//		// Set up your ActionBar
//		final ActionBar actionBar = getActionBar();
//		actionBar.setDisplayShowHomeEnabled(false);
//		actionBar.setDisplayShowTitleEnabled(false);
//		actionBar.setDisplayShowCustomEnabled(true);
//		actionBar.setCustomView(actionBarLayout);
//
//		// You customization
//		final int actionBarColor = getResources().getColor(R.color.action_bar);
//		actionBar.setBackgroundDrawable(new ColorDrawable(actionBarColor));
//
//		final TextView actionBarSent = (TextView) findViewById(R.id.Login_action_bar_text);
//		actionBarSent.setText("Login");
//		final ImageButton actionBarbotton = (ImageButton) findViewById(R.id.Login_action_bar_back);

		if (!user.equals("")) // لاگین نگهداشتن کاربر
		{
			startActivity(new Intent(LoginActivity.this,
					NewActivity.class));
			finish();
		}
	}

	// لاگین کردن کاربر و بررسی اعتبار سنجی ورود اطلاعات
	@SuppressWarnings("static-access")
	public void onclick(View btn) {
		if (btn == findViewById(R.id.button1)) {
			phone = (EditText) findViewById(R.id.tel);
			pass = (EditText) findViewById(R.id.editText2);

			String regexStr = "(0|\\+98)?([ ]|-|[()]){0,2}9[1|2|3|4]([ ]|-|[()]){0,2}(?:[0-9]([ ]|-|[()]){0,2}){8}";
			String number = phone.getText().toString();
			try {
				if (phone.getText().toString().length() != 0
						& pass.getText().toString().length() != 0) {
					if (phone.getText().toString().length() != 11
							|| number.matches(regexStr) == false) {
						Toast.makeText(this, "شماره تلفن را بدرستی وارد کنید",
								Toast.LENGTH_SHORT).show();
					} else {

						btn.setEnabled(false);
						new AsyncTask<Integer, Integer, Boolean>() {
							ProgressDialog progressDialog = null;

							@Override
							protected void onPreExecute() {
								progressDialog = ProgressDialog.show(
										LoginActivity.this, "", "Loading...");
							}

							@Override
							protected Boolean doInBackground(Integer... params) {
								if (params == null) {
									return false;
								}
								try {

									Thread.sleep(2000);
									login();

								} catch (Exception e) {

									return false;
								}

								return true;
							}

							@Override
							protected void onPostExecute(Boolean result) {
								progressDialog.dismiss();
							}
						}.execute();
					}
				}

				else {
					Toast.makeText(this, "شماره تلفن خود را وارد کنید",
							Toast.LENGTH_SHORT).show();

				}
			} catch (Exception e) {
				Toast.makeText(this, "مشکل در ارتباط با سرور",
						Toast.LENGTH_SHORT).show();
				handler.sendEmptyMessage(0);
				progressDialog.dismiss();
			}
		} else if (btn == findViewById(R.id.textView2)) {
			Toast.makeText(getApplicationContext(), "asasa", Toast.LENGTH_SHORT)
					.show();
		} //else if (btn == findViewById(R.id.Login_action_bar_back)) {
			//super.onBackPressed();
			//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//}

	}

	// وصل شدن به سرور
	public void login() throws MalformedURLException, IOException {

		//			URL url = new URL(app.main.URL + app.main.REGISTER
		//					+ "/login?phone=" + phone.getText().toString() + "&pass="
		//					+ pass.getText().toString());
		//			HttpURLConnection huConnection = (HttpURLConnection) url
		//					.openConnection();
		//			huConnection.connect();
		//			InputStream is = huConnection.getInputStream();
		//			BufferedReader bf = new BufferedReader(new InputStreamReader(is,
		//					"UTF-8"));
					String result = "", lines = "";
		//
		//			while ((lines = bf.readLine()) != null)
		//				result += lines;// + "\n";

			        CallSoap cs=new CallSoap();
			        result=cs.ResiveList("Login?name="+phone.getText().toString()+
			        					"&pass="+pass.getText().toString());
					String res=new CallSoap().ResiveList("HasShop?UserID="+phone.getText().toString());
					if (res.equals("no"))
						appVar.main.HasShop=false;
					else
					{
						appVar.main.HasShop=true;
						appVar.main.ShopID=res;
					}

					DisplayResult(result);

	}

	// نمایش نتیجه و رفتن به صفحه کاربری
	public void DisplayResult(final String result) {

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				String[] res=result.split(":");
				if (res[0].substring(0, 2).equals("ok")) {
					Toast.makeText(getApplicationContext(), "خوش آمدید",
							Toast.LENGTH_SHORT).show();
					Editor editor = sp.edit();
					editor.putString("phonekey", phone.getText().toString());
					appVar.main.UserName=phone.getText().toString();
					appVar.main.UserID=res[0].substring(2);
					if (res.length > 1)
						if (res[1].substring(0, 4).equals("shop")) {
							appVar.main.HasShop = true;
							appVar.main.ShopID = res[1].substring(4);
						} else
							NewActivity.navigationView.getMenu().findItem(R.id.nav_shop).setTitle("ایجاد فروشگاه");
					else
						NewActivity.navigationView.getMenu().findItem(R.id.nav_shop).setTitle("ایجاد فروشگاه");
					//editor.apply();
					//startActivity(new Intent(LoginActivity.this,NewActivity.class));

					Menu menuNav=NewActivity.navigationView.getMenu();
					menuNav.findItem(R.id.nav_password).setEnabled(true);
					menuNav.findItem(R.id.nav_cart).setEnabled(true);
					menuNav.findItem(R.id.nav_info).setEnabled(true);
					menuNav.findItem(R.id.nav_quit).setEnabled(true);
					menuNav.findItem(R.id.nav_shop).setEnabled(true);
					NewActivity.name.setText(appVar.main.UserName);

					logbtn.setEnabled(true);
					progressDialog.dismiss();
					handler.sendEmptyMessage(0);
					finish();
				} else {
					Toast.makeText(getApplicationContext(),
							"یوز یا پسورد درست وارد نشده است" + result,
							Toast.LENGTH_SHORT).show();
					if (progressDialog != null) {
						progressDialog.dismiss();
						progressDialog.cancel();
					}
					logbtn.setEnabled(true);
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
