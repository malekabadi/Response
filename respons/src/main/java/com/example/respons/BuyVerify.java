package com.example.respons;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

import com.example.respons.StoreReg.SaveData;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;


public class BuyVerify extends AppCompatActivity {
	
    GridView gridview;
    ImageAdapter imageAdapter;
    String Address="";int SumDis=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payment);
		
		/*************************************************** Set Custom ActionBar *****/
		getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.actionbar2);
		View mCustomView = getSupportActionBar().getCustomView();
		TextView title = (TextView) mCustomView.findViewById(R.id.title);
		title.setText("فاکتور");
		//-----------------------------------------
		
 		final TextView Price=(TextView) findViewById(R.id.Price);
		final TextView Forward=(TextView) findViewById(R.id.Forward);
		final TextView Discount=(TextView) findViewById(R.id.Discount);
		final TextView total=(TextView) findViewById(R.id.total);
		final TextView addr=(TextView) findViewById(R.id.addr);
		int SumCount=0,SumPrice=0,SumDiscount=0;
		for(PageIndicator.Orther row:PageIndicator.Factor)
		{
			SumCount+=Integer.parseInt(row.Count) ;
			SumPrice+=Integer.parseInt(row.Price) ;
			SumDiscount+=Integer.parseInt(row.Discount) ;
		}
		Price.setText(Helper.GetMoney(String.valueOf(SumPrice)));
		Discount.setText(Helper.GetMoney(String.valueOf(SumDiscount)));
		Forward.setText(Helper.GetMoney(String.valueOf("0")));
		total.setText(Helper.GetMoney(String.valueOf(SumPrice-SumDiscount)));
		Address="نام : "+PageIndicator.HFactor.Name+"\n"+
				"تماس : "+PageIndicator.HFactor.TEL+"\n"+
				"کد پستی : "+PageIndicator.HFactor.PostCode+"\n"+
				"آدرس : "+PageIndicator.HFactor.Adress;
		addr.setText(Address);
		SumDis=SumDiscount;
		Button btn=(Button) findViewById(R.id.btn); 
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new SaveData().execute("");
			}
		});

		gridview = (GridView) findViewById(R.id.gridView1);   
		imageAdapter=new ImageAdapter(this);
		gridview.setAdapter(imageAdapter); 
		
        ViewGroup.LayoutParams params = gridview.getLayoutParams();
        float density = this.getResources().getDisplayMetrics().density;
        params.height = (int) (320 * density * imageAdapter.getCount());
        gridview.setLayoutParams(params);
		ScrollView mainScrollView=(ScrollView) findViewById(R.id.scrolv);
        mainScrollView.fullScroll(View.FOCUS_UP);
        mainScrollView.pageScroll(View.FOCUS_UP);
	}
	//-----------------------------------------------------------------------------
	public class SaveData extends AsyncTask<String, Integer, String> {

		ProgressDialog Asycdialog = new ProgressDialog(BuyVerify.this);

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
			Asycdialog.dismiss();
			PageIndicator.Factor.clear();
			finish();
		}

		@Override
		protected void onPreExecute() {
			if (! CallSoap.isConnectionAvailable(BuyVerify.this))
			{
				Intent inte = new Intent(BuyVerify.this, NoNet.class);
				startActivity(inte);
			}
			super.onPreExecute();
			Asycdialog.setMessage("ذخیره اطلاعات ");
			Asycdialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			CallSoap cs=new CallSoap();
			String url="?UserID="+"2";
			url+="&PayType="+"1";
			url+="&SendType="+"1";
			url+="&SendCost="+"0";
			url+="&Discount="+SumDis;
			String Addr=Address.replaceAll("\n", ";");
			url+="&SendAddress="+Addr;
			String res=cs.ResiveList("AddFactor"+url);
			for(PageIndicator.Orther f : PageIndicator.Factor)
			{
				String url1="?FactorID="+res;
				url1+="&ProductID="+f.proID;
				url1+="&Date=" ;
				url1+="&Count="+f.Count;
				url1+="&Price="+f.Price;
				cs.ResiveList("AddOrder"+url1);
			}
			
			try {
				Thread.sleep(3000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return res;
		}

	}
	
//----------------------------------------------------------------	
	public class ImageAdapter extends BaseAdapter{

	    private Context mContext;

	    public ImageAdapter(Context c) {
	        mContext = c;
	    }

	    public int getCount() {
	        //return Name.size();
	        return PageIndicator.Factor.size();
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

	            gridViewAndroid = new View(mContext);
	            gridViewAndroid = inflater.inflate(R.layout.gridview_verify, null);
	            TextView name = (TextView) gridViewAndroid.findViewById(R.id.cart_text1);
	            TextView desc = (TextView) gridViewAndroid.findViewById(R.id.cart_text2);
	            Button count = (Button) gridViewAndroid.findViewById(R.id.Count);
	            TextView price = (TextView) gridViewAndroid.findViewById(R.id.price);
	            TextView total = (TextView) gridViewAndroid.findViewById(R.id.total);
	            ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.cart_image);
	            //name.setText(Name.get(position));
	            //price.setText(Price.get(position));
	            //desc.setText(Desc.get(position));
	            Typeface tf = Typeface.createFromAsset(getAssets(),"B Traffic Bold.ttf"); 
	            price.setTypeface(tf);total.setTypeface(tf);
	            
	            name.setText(PageIndicator.Factor.get(position).Name);
	            count.setText(PageIndicator.Factor.get(position).Count);
	            desc.setText(PageIndicator.Factor.get(position).Desc);
	            price.setText(" قیمت :   "+Helper.GetMoney(String.valueOf(PageIndicator.Factor.get(position).Price)));
	            int tot=Integer.parseInt(PageIndicator.Factor.get(position).Count)*
	            		Integer.parseInt(PageIndicator.Factor.get(position).Price);
	            total.setText(" قیمت کل :   "+Helper.GetMoney(String.valueOf(tot)));
	            imageViewAndroid.setImageBitmap(PageIndicator.image.get(position));
//	            Picasso.with(mContext) //
//	            .load("http://192.168.1.102/images/"+Image.get(position)) //
//	            .error(R.drawable.i2) //
//	            .fit() //
//	            .tag(mContext) //
//	            .into(imageViewAndroid);
	            
//	            Button Del = (Button) gridViewAndroid.findViewById(R.id.btnDel);
//	            Del.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						remove(position);
//					}
//				});
	            
	        } else {
	            gridViewAndroid = (View) convertView;
	        }

	        return gridViewAndroid;
	    }
	    public void remove(int position) {
	    	PageIndicator.Factor.remove(position);
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
//---------------end
}