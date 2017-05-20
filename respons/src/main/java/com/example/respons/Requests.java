package com.example.respons;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class Requests extends AppCompatActivity {

    public ArrayList<String> ID = new ArrayList<String>();
    public ArrayList<String> Title = new ArrayList<String>();
    public ArrayList<String> Status = new ArrayList<String>();
    public ArrayList<String> Date = new ArrayList<String>();

    public String fid;
    ListView view;
    TextAdapter tAdapter;
    String ListType = "";

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requests);
        /*************************************************** Set Custom ActionBar *****/
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar2);
        View mCustomView = getSupportActionBar().getCustomView();
        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        title.setText("لیست درخواست ها");
        //-----------------------------------------

        new LongOperation().execute("");
        view = (ListView) findViewById(R.id.View1);
        tAdapter = new TextAdapter(this);
        view.setAdapter(tAdapter);
    }

    //-----------------------------------------------------------------------------
    private class LongOperation extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPostExecute(Boolean result) {
            ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
            pb.setVisibility(View.INVISIBLE);
            tAdapter.notifyDataSetChanged();
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

    //------------------------------------------------------------------------------
    public class TextAdapter extends BaseAdapter {

        private Context mContext;

        public TextAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return Title.size();
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
            gridViewAndroid = inflater.inflate(R.layout.listview_request, null);
            final TextView stat = (TextView) gridViewAndroid.findViewById(R.id.stat);
            final TextView item = (TextView) gridViewAndroid.findViewById(R.id.Item);
            final TextView date = (TextView) gridViewAndroid.findViewById(R.id.date);

            stat.setText(Status.get(position));
            date.setText(Date.get(position));
            item.setText(Title.get(position));

//	            Button btnDet=(Button) gridViewAndroid.findViewById(R.id.btnDet); 
//	            btnDet.setOnClickListener(new OnClickListener() {
//	          	@Override
//	          	public void onClick(View v) {
//	                  Intent i = new Intent(Requests.this, Buy_Detail.class);
//	                  i.putExtra("FID", ID.get(position));
//	                  startActivity(i);
//	                  }
//	            });

            return gridViewAndroid;
        }
    }

    //------------------------------------------------------------------------------
    public boolean StoreList() {
        try {
            CallSoap cs = new CallSoap();
            String result = cs.ResiveList("Requsets?userID=" + appVar.main.UserID);
            String[] Rows = result.split(":");
            for (int i = 0; i < Rows.length; i++) {
                String[] Field = Rows[i].split(",");
                ID.add(Field[0]);
                Title.add(Field[2]);
                Date.add(Field[3]);
                Status.add(Field[4]);
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

    //------------------------------------------------------
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




