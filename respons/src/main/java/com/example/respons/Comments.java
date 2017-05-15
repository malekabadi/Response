package com.example.respons;

import android.content.Context;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;


public class Comments extends AppCompatActivity {

    public ArrayList<String> Name = new ArrayList<String>();
    public ArrayList<String> Rate = new ArrayList<String>();
    public ArrayList<String> Comment = new ArrayList<String>();

    ListView view;
    ImageAdapter imageAdapter;
    public String pid = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment);
        /*************************************************** Set Custom ActionBar *****/
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar2);
        View mCustomView = getSupportActionBar().getCustomView();
        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        title.setText("نظرات کاربران");
        //-----------------------------------------

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            pid = extra.getString("PID");
        }
        new LongOperation().execute("");
        final EditText comment = (EditText) findViewById(R.id.comment);
        final RatingBar rate = (RatingBar) findViewById(R.id.rate);
        Button btnReg = (Button) findViewById(R.id.btnReg);
        btnReg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CallSoap cs = new CallSoap();
                String userID = "2";
                String url = "UserID=" + userID +
                        "&ProID=" + pid +
                        "&Desc=" + comment.getEditableText() +
                        "&Rate=" + rate.getRating();
                cs.ResiveList("AddComment?" + url);
                finish();
            }
        });

        view = (ListView) findViewById(R.id.lsView1);
        imageAdapter = new ImageAdapter(this);
        view.setAdapter(imageAdapter);

    }

    //-------------------------------------------------------------------------
    private class LongOperation extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPostExecute(Boolean result) {
            ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
            pb.setVisibility(View.INVISIBLE);
            if (Comment.size() > 0)
                imageAdapter.notifyDataSetChanged();
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
            StoreList(pid);
            return null;
        }
    }

    //----------------------------------------------------------------
    public boolean StoreList(String ProductID) {
        try {
            CallSoap cs = new CallSoap();
            String result = cs.ResiveList("GetComments?ProductID=" + ProductID);
            String[] Rows = result.split(":");
            for (int i = 0; i < Rows.length; i++) {
                String[] Field = Rows[i].split(",");
                Name.add(Field[0]);
                Comment.add(Field[1]);
                Rate.add(Field[2]);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //----------------------------------------------------------------
    public class ImageAdapter extends BaseAdapter {

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
            gridViewAndroid = inflater.inflate(R.layout.listview_comment, null);
            TextView name = (TextView) gridViewAndroid.findViewById(R.id.user);
            TextView count = (TextView) gridViewAndroid.findViewById(R.id.comment);
            if (position<Comment.size()) {
                name.setText(Name.get(position));
                count.setText(Comment.get(position));
            }

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