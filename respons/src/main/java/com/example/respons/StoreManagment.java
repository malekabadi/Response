package com.example.respons;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.support.v7.app.ActionBar;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;


public class StoreManagment extends AppCompatActivity {
	
    public static ArrayList<String> CategoryID=new ArrayList<String>();
    public static ArrayList<String> SubCategoryID=new ArrayList<String>();
    public static ArrayList<String> SubCategoryPID=new ArrayList<String>();
    public static ArrayList<String> Category=new ArrayList<String>();
    public static ArrayList<String> SubCategory=new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_mng);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.actionbar2);
		getSupportActionBar().setTitle("مدیریت فروشگاه");
		/*************************************************** Set Custom ActionBar *****/
//		ActionBar mActionBar = getActionBar();
//		mActionBar.setDisplayShowHomeEnabled(false);
//		mActionBar.setDisplayShowTitleEnabled(false);
//
//		LayoutInflater mInflater = LayoutInflater.from(this);
//		View mCustomView = mInflater.inflate(R.layout.actionbar2, null);
//
//		TextView title = (TextView) mCustomView.findViewById(R.id.title);
//		title.setText("مدیریت فروشگاه");
//
//		mActionBar.setCustomView(mCustomView);
//		mActionBar.setDisplayShowCustomEnabled(true);
		//-----------------------------------------
		
		if (Category.size()<1)
		{	
			new MyTask().execute();
		}
		View btn1=(View) findViewById(R.id.btn1);
		btn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        Intent i = new Intent(StoreManagment.this, ProductMng.class);
		        startActivity(i);
			}
		});
		
		View btn2=(View) findViewById(R.id.btn2);
		btn2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        Intent i = new Intent(StoreManagment.this, CategoryMng.class);
		        startActivity(i);
			}
		});
		View btn3=(View) findViewById(R.id.btn3);
		btn3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        Intent i = new Intent(StoreManagment.this, Orders.class);
		        i.putExtra("Type", "0"); //Order
		        startActivity(i);
			} 
		});
		View btn4=(View) findViewById(R.id.btn4);
		btn4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        Intent i = new Intent(StoreManagment.this, Orders.class);
		        i.putExtra("Type", "1"); //Sent
		        startActivity(i);
			}
		});
		View btn5=(View) findViewById(R.id.btn5);
		btn5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        Intent i = new Intent(StoreManagment.this, Orders.class);
		        i.putExtra("Type", "2"); //Delete
		        startActivity(i);
			}
		});
		View btn6=(View) findViewById(R.id.btn6);
		btn6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        Intent i = new Intent(StoreManagment.this, StoreReg.class);
		        i.putExtra("Edit", "True"); //Delete
		        startActivity(i);
			}
		});
		View btn7=(View) findViewById(R.id.btn7);
		btn7.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				appVar.main.UserID="";
				appVar.main.HasShop=false;
				finish();
			}
		});
		View btn8=(View) findViewById(R.id.btn8);
		btn8.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        Intent i = new Intent(StoreManagment.this, Request.class);
		        startActivity(i);
			}
		});
		View btn9=(View) findViewById(R.id.btn9);
		btn9.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        Intent i = new Intent(StoreManagment.this, Requests.class);
		        startActivity(i);
			}
		});
	}
//-----------------------------------------------------------------------------
	class MyTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog Asycdialog = new ProgressDialog(StoreManagment.this);

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            Asycdialog.setMessage("Loading...");
            Asycdialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // do the task you want to do. This will be executed in background.
        	StoreList();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            Asycdialog.dismiss();
        }
    }
	
//------------------------------------------------------------------------
public void StoreList()
{	
	CallSoap cs=new CallSoap();
	String res=cs.ResiveList("CategoryList?shopID="+appVar.main.ShopID);
	String[] Rows = res.split(":");
	if (Rows.length > 1)
	{
//		CategoryID.add("-1");
//		Category.add("همه");
		for (int i = 0; i < Rows.length; i++) 
		{
			String[] Field = Rows[i].split(",");
			if (Field.length > 1)
			{
				CategoryID.add(Field[0]);
				Category.add(Field[1]);
			}
		}
	}
	res=cs.ResiveList("CategoryShop?shopID="+appVar.main.ShopID);
	String[] Rows2 = res.split(":");
	if (Rows2.length > 1)
	{
		for (int i = 0; i < Rows2.length; i++) 
		{
			String[] Field = Rows2[i].split(",");
			if (Field.length > 1)
			{
				SubCategoryID.add(Field[0]);
				SubCategory.add(Field[1]);
				SubCategoryPID.add(Field[2]);
			}
		}
	}
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
//------------- End of Activity
}