package com.example.respons;

import java.util.ArrayList;
import java.util.List;

import com.example.respons.StoreReg.SaveData;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
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

public class Account extends AppCompatActivity {

	ImageView iv;
	List<String> CityIDs = new ArrayList<String>();
	List<String> Cities = new ArrayList<String>();
	List<String> StateIDs = new ArrayList<String>();
	List<String> States = new ArrayList<String>();
	List<String> ZoneIDs = new ArrayList<String>();
	List<String> Zonez = new ArrayList<String>();
	Spinner txtCity, txtState, txtSex;
	String ShopID = "";
	String picturePath;
	String TopicID = "";
	Button btnObj;
	ArrayAdapter<String> dataAdapter, dataAdapter1, dataAdapter2;
	String[] Sex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account);
		/*************************************************** Set Custom ActionBar *****/
		getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.actionbar2);
		View mCustomView = getSupportActionBar().getCustomView();
		TextView title = (TextView) mCustomView.findViewById(R.id.title);
		title.setText("مشخصات فردی");
		//-----------------------------------------

		Sex = getResources().getStringArray(R.array.ssex);

		Bundle extra = getIntent().getExtras();
		if (extra != null)
			ShopID = extra.getString("ShopID");
		else
			ShopID = "0";

		txtCity = (Spinner) findViewById(R.id.txtCity);
		txtState = (Spinner) findViewById(R.id.txtState);
		txtSex = (Spinner) findViewById(R.id.txtSex);
		new LongOperation().execute("");

//		Zonez.add("مرد");
//		Zonez.add("زن");
		
		dataAdapter = new ArrayAdapter<String>(this, R.layout.listview_item_row, Sex);
		txtSex.setAdapter(dataAdapter);

		dataAdapter1 = new ArrayAdapter<String>(this, R.layout.listview_item_row, Cities);
		//txtCity.setAdapter(dataAdapter1);

		dataAdapter2 = new ArrayAdapter<String>(this, R.layout.listview_item_row, States);
		//txtState.setAdapter(dataAdapter2);
		// -----------------------------------------------

		Button reg = (Button) findViewById(R.id.btnReg); // ذخیره اطلاعات
		reg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean reg = true;
				EditText name = (EditText) findViewById(R.id.txtName);
				if (name.getText().toString().equals("")) {
					name.setError("لطفا نام را وارد نمایید");
					reg = false;
				}
				EditText desc = (EditText) findViewById(R.id.txtCode);
				if (desc.getText().toString().equals("")) {
					desc.setError("لطفا کد ملی را وارد نمایید");
					reg = false;
				}
				EditText addr = (EditText) findViewById(R.id.txtTel);
				if (addr.getText().toString().equals("")) {
					addr.setError("شماره همراه خالی می باشد");
					reg = false;
				}
				if (reg) {
					// CallSoap cs=new CallSoap();
					// String url="title="+shop.getText();
					// url+="&name="+name.getText();
					// url+="&topicID="+TopicID;
					// url+="&onwer="+appVar.main.UserID;
					// url+="&desc="+desc.getText();
					// url+="&tel="+tel.getText();
					// url+="&mobile="+mobile.getText();
					// url+="&addr="+addr.getText();
					// url+="&image=";
					// url+="&zoneID="+ZoneIDs.get((int)txtZone.getSelectedItemId());
					// String res="";
					// if (EditType.equals("True"))
					// {
					// url="?id="+appVar.main.ShopID+"&"+url;
					// res=cs.ResiveList("EditShop"+url);
					// new SaveData().execute("EditShop"+url);
					// }
					// else
					// {
					// url="?"+url;
					// res=cs.ResiveList("AddShop"+url);
					// new SaveData().execute("AddShop"+url);
					// }
				}
			}
		});

	}

	// -------------------------------------------------------------------------
	private class LongOperation extends AsyncTask<String, Integer, Boolean> {
		String res = "";
		String[] Field;
		ProgressDialog Asycdialog = new ProgressDialog(Account.this);

		@Override
		protected void onPostExecute(Boolean result) {

			final TextView name = (TextView) findViewById(R.id.txtName);
			final TextView code = (TextView) findViewById(R.id.txtCode);
			final TextView tel = (TextView) findViewById(R.id.txtTel);
			final TextView addr = (TextView) findViewById(R.id.txtAddr);
			final Spinner sex = (Spinner) findViewById(R.id.txtSex);
			final Spinner city = (Spinner) findViewById(R.id.txtState);
			final Spinner state = (Spinner) findViewById(R.id.txtCity);
			final Spinner zone = (Spinner) findViewById(R.id.txtZone);
			if (Field.length > 1)
				name.setText(Field[1]);
			if (Field.length > 2)
				sex.setSelection(Cities.indexOf(Field[2]));
			if (Field.length > 3)
				code.setText(Field[3]);
			if (Field.length > 4)
				tel.setText(Field[4]);
			if (Field.length > 5)
				addr.setText(Field[5]);
			// if (Field.length>6) post.setText(Field[6]);
//			if (Field.length > 7)
//				city.setSelection(CityIDs.indexOf(Field[7]));
//			if (Field.length > 8)
//				state.setSelection(StateIDs.indexOf(Field[8]));
			txtCity.setAdapter(dataAdapter1);
			txtState.setAdapter(dataAdapter2);

			super.onPostExecute(result);
			Asycdialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			Asycdialog.setMessage("در حال خواندن اطلاعات ");
			Asycdialog.show();
			if (! CallSoap.isConnectionAvailable(Account.this))
			{
				Intent inte = new Intent(Account.this, NoNet.class);
				startActivity(inte);
			}
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			LoadStates();
			String res = new CallSoap().ResiveList("GetUserInfo?id=" + appVar.main.UserID);
			Field = res.split(",");
			LoadCities(Field[8]);

			return null;
		}
	}
	//--------------------------------------------------------------------
	public boolean LoadStates() {
		try {
			CallSoap cs = new CallSoap();
			String res = cs.ResiveList("StateList");
			String[] Rows = res.split(":");
			for (int i = 1; i < Rows.length - 1; i++) {
				String[] Field = Rows[i].split(",");
				StateIDs.add(Field[0]);
				States.add(Field[1]);
			}
			return true;
		} catch (Exception e) {
			return false;
		}

	}
	//--------------------------------------------------------------------
	public boolean LoadCities(String StateID) {
		try {
			CallSoap cs = new CallSoap();
			String res = cs.ResiveList("CityList?StateID=" + StateID);
			String[] Rows = res.split(":");
			for (int i = 1; i < Rows.length - 1; i++) {
				String[] Field = Rows[i].split(",");
				CityIDs.add(Field[0]);
				Cities.add(Field[1]);
			}
			return true;
		} catch (Exception e) {
			return false;
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
	// -----------------------
}