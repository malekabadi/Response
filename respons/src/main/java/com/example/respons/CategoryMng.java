package com.example.respons;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
public class CategoryMng extends AppCompatActivity {
	
    public ArrayList<String> ID=new ArrayList<String>();
    public List<String> Category=new ArrayList<String>();

    public String sid,title,mode="";
    Button btnNew;
    TextView editNew;
    ArrayAdapter<String> adapter;
    int pos;
    
@Override
public void onRestart()
{
    super.onRestart();
//    finish();
//    startActivity(getIntent());
    adapter.notifyDataSetChanged();
}
    
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
	setContentView(R.layout.category_mng);
	final CallSoap cs=new CallSoap();
	/*************************************************** Set Custom ActionBar *****/
	getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
	getSupportActionBar().setCustomView(R.layout.actionbar2);
	View mCustomView = getSupportActionBar().getCustomView();
	TextView title1 = (TextView) mCustomView.findViewById(R.id.title);
	title1.setText("مدیریت گروه ها");
	//-----------------------------------------

//	editNew=(TextView) findViewById(R.id.editNew);
//	editNew.setEnabled(false);

	Bundle extra = getIntent().getExtras();
	if (extra != null) 
	    sid= extra.getString("SID");
	else sid="0";
	
	//new LongOperation().execute("");
	final ListView lv= (ListView) findViewById(R.id.list_topic);
	adapter = new ArrayAdapter<String>(this,
	R.layout.listview_item_row, StoreManagment.Category);
	lv.setAdapter(adapter);
	
//	btnNew=(Button) findViewById(R.id.btnNew);
//	btnNew.setOnClickListener(new OnClickListener() {
//		@Override
//		public void onClick(View v) {
//	        Intent i = new Intent(CategoryMng.this, NewCate.class);
//	        startActivity(i);
//		}
//	});

	FloatingActionButton btnNew=(FloatingActionButton) findViewById(R.id.fab);
	btnNew.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent i = new Intent(CategoryMng.this, NewCate.class);
			startActivity(i);
		}
	});


	lv.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			// TODO Auto-generated method stub
			String cid=StoreManagment.CategoryID.get(position);
			Intent inte=new Intent(CategoryMng.this, SubCateMng.class);
			inte.putExtra("CID", cid);
			inte.putExtra("TITLE", StoreManagment.Category.get(position));
			startActivity(inte);
		}
	});
	lv.setOnItemLongClickListener(new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
			sid=StoreManagment.CategoryID.get(position);
			title=StoreManagment.Category.get(position);
			SelectCommand(position);pos=position;
			return true;
		}
	});
}
//-------------------------------------------------------------------------
private class LongOperation extends AsyncTask<String, Integer, Boolean> {
@Override
protected void onPostExecute(Boolean result) {
	ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);   
	pb.setVisibility(View.INVISIBLE);
	adapter.notifyDataSetChanged();
	super.onPostExecute(result);
}

@Override
protected void onPreExecute() {
	ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);   
	pb.setVisibility(View.VISIBLE);
	super.onPreExecute();
}

@Override
protected Boolean doInBackground(String... params) {
	StoreList();
  return null;
}
}
//-------------------------------------------------------------------
private void SelectCommand(final int pos) {
    final CharSequence[] options = { "ویرایش", "حـذف","انصراف" };
    AlertDialog.Builder builder = new AlertDialog.Builder(CategoryMng.this);
    builder.setTitle("Add Photo!");
    builder.setItems(options, new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int item) {
            if (options[item].equals("ویرایش"))
            {
    			String cid=StoreManagment.CategoryID.get(pos);
    			Intent inte=new Intent(CategoryMng.this, NewCate.class);
    			inte.putExtra("TITLE", StoreManagment.Category.get(pos));
    			inte.putExtra("CID", cid);
    			startActivity(inte);
            }
            else if (options[item].equals("حـذف"))
            {
//                CallSoap cs=new CallSoap();
//                String res=cs.ResiveList("DelCategory?ID="+sid);
                new DelData().execute("DelCategory?ID="+sid);
            }
            else if (options[item].equals("انصراف")) {
                dialog.dismiss();
            }
        }
    });
    builder.show();
 }
//-----------------------------------------------------------------------------
public  boolean StoreList()
{
   // try
	{
        CallSoap cs=new CallSoap();
		String res=cs.ResiveList("CategoryList?shopID="+appVar.main.ShopID);
		String[] Rows = res.split(":");
		for (int i = 0; i < Rows.length; i++) 
		{
			String[] Field = Rows[i].split(",");
			if (Field.length > 1)
			{
				ID.add(Field[0]);
				Category.add(Field[1]);
			}
		}
		return true;
	}
	//catch(Exception e)
	//{
	//	return false;
	//}

}

public class DelData extends AsyncTask<String, Integer, String> {
	
	ProgressDialog Asycdialog = new ProgressDialog(CategoryMng.this);
    @Override
    protected void onPostExecute(String result) {
    	
    	super.onPostExecute(result);
    	Asycdialog.dismiss();
    	String res=result;
        if (!res.equals("False"))
        {
            StoreManagment.Category.remove(pos);
            StoreManagment.CategoryID.remove(pos);
            adapter.notifyDataSetChanged();
        }
        else
        	Toast.makeText(CategoryMng.this, "این دسته شامل کالا است و قابل حذف نمی باشد",
					   Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPreExecute() {
    	Asycdialog.setMessage("حذف اطلاعات ");
        Asycdialog.show();	    	
    	super.onPreExecute();
    }
    
    @Override
    protected String doInBackground(String... params) {
    	String res;
    	CallSoap cs=new CallSoap();
    	res=cs.ResiveList(params[0]);    	
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
//-------- End Activity
}




