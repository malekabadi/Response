package com.example.respons;

import java.util.ArrayList;
import java.util.List;

import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;


public class StoreInfo extends AppCompatActivity {
	
	ImageView iv;
	List<String> CityIDs = new ArrayList<String>();
	List<String> Cities = new ArrayList<String>();
	List<String> ZoneIDs = new ArrayList<String>();
	List<String> Zonez = new ArrayList<String>();
	Spinner txtCity,txtZone;
	String ShopID="";
	String picturePath;
	String TopicID="";
	Button btnObj;
	ArrayAdapter<String> dataAdapter,dataAdapter1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_info);
		/*************************************************** Set Custom ActionBar *****/
		getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.actionbar2);
		View mCustomView = getSupportActionBar().getCustomView();
		TextView title = (TextView) mCustomView.findViewById(R.id.title);
		title.setText("مشخصات فروشگاه");
		//-----------------------------------------
		
		Bundle extra = getIntent().getExtras();
		if (extra != null) 
			ShopID= extra.getString("ShopID");
		else ShopID="0";
		
		txtCity = (Spinner) findViewById(R.id.txtCity);
		txtZone = (Spinner) findViewById(R.id.txtZone);
		new LongOperation().execute("");
		
		dataAdapter = new ArrayAdapter<String>(this,
				R.layout.listview_item_row, Cities);		
		//dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		txtCity.setAdapter(dataAdapter);

		dataAdapter1 = new ArrayAdapter<String>(this,
				R.layout.listview_item_row, Zonez);		
		txtZone.setAdapter(dataAdapter1);
		//-----------------------------------------------

		
	
	}
	//-------------------------------------------------------------------------
	private class LongOperation extends AsyncTask<String, Integer, Boolean> {
			String res="";
			ProgressDialog Asycdialog = new ProgressDialog(StoreInfo.this);
		@Override
		protected void onPostExecute(Boolean result) {
	//		dataAdapter.notifyDataSetChanged();
	//		dataAdapter1.notifyDataSetChanged();

			btnObj=(Button) findViewById(R.id.btnObj);
			TextView Shop=(TextView) findViewById(R.id.txtShop);
			EditText name=(EditText) findViewById(R.id.txtName);
			EditText desc=(EditText) findViewById(R.id.txtDesc);
			EditText tel=(EditText) findViewById(R.id.txtTel);
			EditText mobile=(EditText) findViewById(R.id.txtMobile);
			EditText addr=(EditText) findViewById(R.id.txtAddr);
			ImageView Image=(ImageView) findViewById(R.id.StoreImage);
	//		TextView city=(TextView) findViewById(R.id.txtCity);
	//		TextView zone=(TextView) findViewById(R.id.txtZone);

				String[] Field = res.split(",");
				btnObj.setText(Field[0]);
				Shop.setText(Field[1]);
				name.setText(Field[2]);
				desc.setText(Field[3]);
				tel.setText(Field[4]);
				mobile.setText(Field[5]);
				addr.setText(Field[6]);
				txtCity.setSelection(Cities.indexOf(Field[7]));
				txtZone.setSelection(Zonez.indexOf(Field[8]));
				Context Ct=getApplicationContext();
				Picasso.with(Ct) //
				.load(getString(R.string.WServer)+"/images/"+Field[9]) //
				.error(R.drawable.i2) //
				.fit() //
				.tag(Ct) //
				.into(Image);

			super.onPostExecute(result);
			Asycdialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			if (! CallSoap.isConnectionAvailable(StoreInfo.this))
			{
				Intent inte = new Intent(StoreInfo.this, NoNet.class);
				startActivity(inte);
			}
			Asycdialog.setMessage("در حال خواندن اطلاعات ");
			Asycdialog.show();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(String... params) {
				res=new CallSoap().ResiveList("GetShop?ID="+ShopID);

			return null;
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

//-----------------------    
}