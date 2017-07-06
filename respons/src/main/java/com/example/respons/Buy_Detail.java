package com.example.respons;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;


public class Buy_Detail extends AppCompatActivity {
	
    public ArrayList<String> Name=new ArrayList<String>();
    public ArrayList<String> Price=new ArrayList<String>();
    public ArrayList<String> Count=new ArrayList<String>();
    public ArrayList<String> SumPrice=new ArrayList<String>();

    ListView view;
    ImageAdapter imageAdapter;
    public String fid;
    float density;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buy_detail);
		/*************************************************** Set Custom ActionBar *****/
		getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.actionbar2);
		View mCustomView = getSupportActionBar().getCustomView();
		TextView title = (TextView) mCustomView.findViewById(R.id.title);
		title.setText("جزئیات خرید");
		//-----------------------------------------
		String customer="",price="",addr="";
		String ListType="";
		Bundle extra = getIntent().getExtras();
		if (extra != null) 
		{
		    fid= extra.getString("FID");
			customer=extra.getString("customer");
			price=extra.getString("price");
			addr=extra.getString("addr");
			ListType= extra.getString("Type");
		}
		else 
		{
			fid="0";
			ListType="0";
		}
		if (! ListType.equals("0"))
		{
			LinearLayout lay=(LinearLayout)  findViewById(R.id.Footer);
			LayoutParams params = lay.getLayoutParams();
			// Changes the height and width to the specified *pixels*
			params.height = 0;
			params.width = 10;
			lay.setLayoutParams(params);
		}

		Button btnSend=(Button) findViewById(R.id.btnSend); 
		btnSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
		        CallSoap cs=new CallSoap();
		        cs.ResiveList("SetStatus?FactorID="+fid+"&Status=1");
		        finish();
		        //Orders
			}
		});
		
		Button btnDel=(Button) findViewById(R.id.btnDel); 
		btnDel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//		        CallSoap cs=new CallSoap();
//		        cs.ResiveList("SetStatus?FactorID="+fid+"&Status=2");
//		        finish();
				imageAdapter.notifyDataSetChanged();
			}
		});

		final TextView Price=(TextView) findViewById(R.id.Price);Price.setText(Helper.GetMoney(price));
		final TextView Customer=(TextView) findViewById(R.id.customer);Customer.setText(customer);
		final TextView num=(TextView) findViewById(R.id.num);num.setText(fid);
		final TextView PayType=(TextView) findViewById(R.id.PayType);
		String temp[]=addr.split(";");
		//addr=addr.replaceAll(";", "\n");
		if (temp.length > 0)
			addr="نام : "+temp[0]+"\n";
		if (temp.length > 1)
			addr=addr+"شماره تماس : "+temp[1]+"\n";
		if (temp.length > 2)
			addr=addr+"کد پستی : "+temp[2]+"\n";
		if (temp.length > 3)
			addr=addr+"آدرس : "+ temp[3];
		final TextView Addr=(TextView) findViewById(R.id.addr);Addr.setText(addr);

		new LongOperation().execute("");
		
		view = (ListView) findViewById(R.id.gridView1);   
		imageAdapter=new ImageAdapter(this);
		view.setAdapter(imageAdapter);  
		
        ViewGroup.LayoutParams params = view.getLayoutParams();
        density = this.getResources().getDisplayMetrics().density;
//        params.height = (int) (170 * density * imageAdapter.getCount());
//        view.setLayoutParams(params);
        
		//LayoutParams lp = (LayoutParams) view.getLayoutParams();
		//lp.height = 420;
		//view.setLayoutParams(lp);
		//Helper.getListViewSize(view);
		ScrollView mainScrollView=(ScrollView) findViewById(R.id.scrollView1);
        mainScrollView.fullScroll(View.FOCUS_UP);
        mainScrollView.pageScroll(View.FOCUS_UP);

	}
	//-------------------------------------------------------------------------
	private class LongOperation extends AsyncTask<String, Integer, Boolean> {
	@Override
	protected void onPostExecute(Boolean result) {
		ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);   
		pb.setVisibility(View.INVISIBLE);
		imageAdapter.notifyDataSetChanged();
		ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = (int) (170 * density * imageAdapter.getCount());
        view.setLayoutParams(params);

		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);   
		pb.setVisibility(View.VISIBLE);
		if (! CallSoap.isConnectionAvailable(Buy_Detail.this))
		{
			Intent inte = new Intent(Buy_Detail.this, NoNet.class);
			startActivity(inte);
		}
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(String... params) {
		StoreList(fid);
		
	    return null;
	}
	}	
//----------------------------------------------------------------	
	public  boolean StoreList(String FactorID)
	{
	    try
		{
	        CallSoap cs=new CallSoap();
	        String result=cs.ResiveList("GetOrders?FactorID="+FactorID);
	        String[] Rows = result.split(":");
			for (int i = 0; i < Rows.length; i++)
			{
				String[] Field = Rows[i].split(",");
				Name.add(Field[0]);
				Count.add(Field[1]);
				Price.add(Field[2]);
				SumPrice.add(Field[3]);
			}
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}	
//----------------------------------------------------------------	
	public class ImageAdapter extends BaseAdapter{

	    private Context mContext;
	    LayoutInflater inflater;

	    public ImageAdapter(Context c) {
	        mContext = c;
	    }

	    public int getCount() {
	        return Name.size();
	    }

	    public Object getItem(int position) {
	        return position;
	    }

	    public long getItemId(int position) {
	        return position;
	    }
    
	    public View getView(final int position, View convertView, ViewGroup parent) {
	        View gridViewAndroid;
	        LayoutInflater inflater = (LayoutInflater) mContext
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        if (convertView == null) {
	        } else {
	            gridViewAndroid = (View) convertView;
	        }
	            gridViewAndroid = new View(mContext);
	            gridViewAndroid = inflater.inflate(R.layout.listview_detail, null);
	            TextView name = (TextView) gridViewAndroid.findViewById(R.id.Name);
	            TextView count = (TextView) gridViewAndroid.findViewById(R.id.Count);
	            TextView price = (TextView) gridViewAndroid.findViewById(R.id.Price);
	            TextView sumPrice = (TextView) gridViewAndroid.findViewById(R.id.SumPrice);
	            name.setText(Name.get(position));
	            count.setText(Count.get(position));
	            price.setText(Helper.GetMoney(Price.get(position)));
	            sumPrice.setText(Helper.GetMoney(SumPrice.get(position)));

	        return gridViewAndroid;
	    }
	    public void remove(int position) {
	    	Name.remove(position);
	    	notifyDataSetChanged();

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