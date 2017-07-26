package com.example.respons;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class NewCate extends AppCompatActivity {
    
    public List<String> pty=new ArrayList<String>();
    TAdapter adapter;
    String id,cid,pid,mode="add",text;
    int  pos;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_cat);
		
		/*************************************************** Set Custom ActionBar *****/
		getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.actionbar2);
		View mCustomView = getSupportActionBar().getCustomView();
		TextView title = (TextView) mCustomView.findViewById(R.id.title);
		title.setText("گروه جدید");
		//-----------------------------------------
		
		final CallSoap cs=new CallSoap();

		final EditText editNew=(EditText) findViewById(R.id.txtName);
		
		// For Edit mode
		Bundle extra = getIntent().getExtras();
		if (extra != null) 
		{
		    cid= extra.getString("CID");
	    	pid=extra.getString("PID");
			mode=extra.getString("mode");
			if (mode.equals("edit")) {
				editNew.setText(extra.getString("TITLE"));
				pos=extra.getInt("pos");
				String py = null;
				try {
					py = cs.ResiveListSync("CateProperty?ID=" + cid);
				} catch (Exception e) {
					e.printStackTrace();
				}
				String[] listp = py.split(",");
				for (int i = 0; i < listp.length; i++)
					pty.add(listp[i]);
			} else
			{
				String py = null;
				try {
					py = cs.ResiveListSync("CateProperty?ID=" + pid);
				} catch (Exception e) {
					e.printStackTrace();
				}
				String[] listp = py.split(",");
				for (int i = 0; i < listp.length; i++)
					pty.add(listp[i]);
			}
		}
		else cid="0";
		if (pid == null) pid="0";

		ImageButton imb = (ImageButton) mCustomView.findViewById(R.id.imageButton);
		imb.setVisibility(View.VISIBLE);
		imb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String strPty="";
				text = editNew.getText().toString();
				if (text.equals(""))
					editNew.setError("لطفا نام دسته را وارد نمایید");
				else
				{
					for (int i=0;i<pty.size();i++)
					{
						strPty+=pty.get(i)+",";
					}

					if (mode.equals("edit"))
					{
						String url="?ID="+cid;
						url+="&name="+editNew.getText();
						url+="&Property="+strPty;
						//id=cs.ResiveList("EditCategory"+url);
						new SaveData().execute("EditCategory"+url);
					}
					else
					{
						String url="?name="+editNew.getText();
						url+="&Image=";
						url+="&ParentID="+pid;
						url+="&ShopID="+appVar.main.ShopID;
						url+="&Property="+strPty;
						//id=cs.ResiveList("AddCategory"+url);
						new SaveData().execute("AddCategory"+url);
					}
				}

				//finish();
			}
		});
		final EditText editPty=(EditText) findViewById(R.id.editPty);
		Button btnAdd=(Button) findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pty.add(editPty.getText().toString());
				adapter.notifyDataSetChanged();
				editPty.setText("");
			}
		});

		ListView lv = (ListView) findViewById(R.id.list_topic);
		adapter= new TAdapter(this);
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//				R.layout.listview_wdel, Name);
		lv.setAdapter(adapter);
	}

//-----------------------------------------------------------------
	public class TAdapter extends BaseAdapter{
		
		LayoutInflater inflater;

	    public TAdapter(Context c) {
	    	inflater = LayoutInflater.from(c);
	    }

	    public int getCount() {
	        return pty.size();
	    }

	    public Object getItem(int position) {
	        return position;
	    }

	    public long getItemId(int position) {
	        return position;
	    }
    
	    public View getView(final int position, View convertView, ViewGroup parent) {

	        	convertView = inflater.inflate(R.layout.listview_wdel, null);
	            TextView value = (TextView) convertView.findViewById(R.id.textView1);
	            value.setText(pty.get(position));

	            ImageView img = (ImageView) convertView.findViewById(R.id.imageView1);
	            img.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						pty.remove(position);
						adapter.notifyDataSetChanged();
					}
				});

	        return convertView;
	    }
    }

//---------------------------------------------------------------------	
	public class SaveData extends AsyncTask<String, Integer, String> {
		
		ProgressDialog Asycdialog = new ProgressDialog(NewCate.this);
	    @Override
	    protected void onPostExecute(String result) {
	    	
	    	super.onPostExecute(result);
	    	Asycdialog.dismiss();
	    	if (mode.equals("add"))
	    	if (pid.equals("0"))
			{
				StoreManagment.Category.add(text);
				StoreManagment.CategoryID.add(id);
			} else {
				StoreManagment.SubCategory.add(text);
				StoreManagment.SubCategoryID.add(id);
				StoreManagment.SubCategoryPID.add(pid);
			}
			else
			{
				if (pid.equals("0"))
				{
					StoreManagment.Category.set(pos,text);
					StoreManagment.CategoryID.set(pos,id);
				} else {
					StoreManagment.SubCategory.set(pos,text);
					StoreManagment.SubCategoryID.set(pos,id);
					StoreManagment.SubCategoryPID.set(pos,pid);
				}

			}
	    	finish();
 	    }

	    @Override
	    protected void onPreExecute() {
			if (! CallSoap.isConnectionAvailable(NewCate.this))
			{
				Intent inte = new Intent(NewCate.this, NoNet.class);
				startActivity(inte);
			}
	    	Asycdialog.setMessage("ذخیره اطلاعات ");
            Asycdialog.show();	    	
	    	super.onPreExecute();
	    }
	    
	    @Override
	    protected String doInBackground(String... params) {
	    	String res;
	    	CallSoap cs=new CallSoap();
	    	res=cs.ResiveList(params[0]);   
	    	id = res;
	        try {
	            Thread.sleep(3000);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    	return res;
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
/// End of Activity
}