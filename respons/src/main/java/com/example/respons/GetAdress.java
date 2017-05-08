package com.example.respons;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;


public class GetAdress extends AppCompatActivity {
	
	String UserID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addr);
		/*************************************************** Set Custom ActionBar *****/
		getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.actionbar2);
		View mCustomView = getSupportActionBar().getCustomView();
		TextView title = (TextView) mCustomView.findViewById(R.id.title);
		title.setText("ثبت آدرس");
		//-----------------------------------------
		if (appVar.main.UserID==null)
		{
			//PageIndicator.HFactor.UserID="1";
			//UserID=PageIndicator.HFactor.UserID;
	        Intent inte = new Intent(GetAdress.this, LoginActivity.class);
	        startActivity(inte);
		}
//		PageIndicator.HFactor.UserID=appVar.main.UserID;
//		UserID=PageIndicator.HFactor.UserID;
//		final CallSoap cs=new CallSoap();
//		String res=cs.ResiveList("GetUserInfo?id="+appVar.main.UserID);
//		String[] Field = res.split(",");
		new LongOperation().execute("");
		final TextView name1=(TextView) findViewById(R.id.name1);
		final TextView tel1=(TextView) findViewById(R.id.tel1);
		final TextView code1=(TextView) findViewById(R.id.code1);
		final TextView addr1=(TextView) findViewById(R.id.addr1);
		final TextView name2=(TextView) findViewById(R.id.name2);
		final TextView tel2=(TextView) findViewById(R.id.tel2);
		final TextView code2=(TextView) findViewById(R.id.code2);
		final TextView addr2=(TextView) findViewById(R.id.addr2);
		final RadioButton info1=(RadioButton) findViewById(R.id.info1);
		final RadioButton info2=(RadioButton) findViewById(R.id.info2);
		
		Button btn=(Button) findViewById(R.id.btn); 
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean reg=true;
				if (info1.isChecked())
				{
					//Check Entery1
					if (name1.getText().toString().equals(""))
					{
						name1.setError("لطفا نام را وارد نمایید");reg=false;
					} 
					if (tel1.getText().toString().equals(""))
					{
						tel1.setError("لطفا شماره تماس را وارد نمایید");reg=false;
					} 
					if (code1.getText().toString().equals(""))
					{
						code1.setError("لطفا کد پستی را وارد نمایید");reg=false;
					} 
					if (addr1.getText().toString().equals(""))
					{
						addr1.setError("لطفا آدرس را وارد نمایید");reg=false;
					} 
					String url="ID="+UserID;
					url+="&Name="+name1.getText();
					url+="&Tel="+tel1.getText();
					url+="&Adress="+addr1.getText();
					url+="&PostalCode="+code1.getText();
					//String res=cs.ResiveList("SaveUserInfo?"+url);
					PageIndicator.HFactor.Name=name1.getText().toString();
					PageIndicator.HFactor.TEL=tel1.getText().toString();
					PageIndicator.HFactor.PostCode=code1.getText().toString();
					PageIndicator.HFactor.Adress=addr1.getText().toString();
				}
				if (info2.isChecked())
				{
					//Check Entery2
					if (name2.getText().toString().equals(""))
					{
						name2.setError("لطفا نام را وارد نمایید");reg=false;
					} 
					if (tel2.getText().toString().equals(""))
					{
						tel2.setError("لطفا شماره تماس را وارد نمایید");reg=false;
					} 
					if (code2.getText().toString().equals(""))
					{
						code2.setError("لطفا کد پستی را وارد نمایید");reg=false;
					} 
					if (addr2.getText().toString().equals(""))
					{
						addr2.setError("لطفا آدرس را وارد نمایید");reg=false;
					} 
//					PageIndicator.HFactor.Name=name2.getText().toString();
//					PageIndicator.HFactor.TEL=tel2.getText().toString();
//					PageIndicator.HFactor.PostCode=code2.getText().toString();
//					PageIndicator.HFactor.Adress=addr2.getText().toString();
				}
				if (reg)
				{
			        Intent inte = new Intent(GetAdress.this, BuyVerify.class);
			        startActivity(inte);
				}
			}
		});
	}
	//-------------------------------------------------------------------------
	private class LongOperation extends AsyncTask<String, Integer, Boolean> {
		String[] Field;
		ProgressDialog Asycdialog = new ProgressDialog(GetAdress.this);
	    @Override
	    protected void onPostExecute(Boolean result) {
			Asycdialog.dismiss();
			final TextView name1=(TextView) findViewById(R.id.name1);
			final TextView tel1=(TextView) findViewById(R.id.tel1);
			final TextView code1=(TextView) findViewById(R.id.code1);
			final TextView addr1=(TextView) findViewById(R.id.addr1);
			if (Field.length>2) name1.setText(Field[1]);
			if (Field.length>3) tel1.setText(Field[2]);
			if (Field.length>4) code1.setText(Field[3]);
			if (Field.length>5) addr1.setText(Field[4]);
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
			Field = res.split(",");
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
}