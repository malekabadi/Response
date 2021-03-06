package com.example.respons;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Cate_Product extends AppCompatActivity {

    public ArrayList<String> ID = new ArrayList<String>();
    public ArrayList<String> Name = new ArrayList<String>();
    public ArrayList<String> Price = new ArrayList<String>();
    public ArrayList<String> Discout = new ArrayList<String>();
    public ArrayList<String> Image = new ArrayList<String>();

    ArrayList<String> _SubTopics = new ArrayList<String>();
    ArrayList<String> _SubTopicIDs = new ArrayList<String>();

    Point p;float density;
    String TopicID = "", Topic = "";
    Button b1, b2;
    ImageAdapter imAdapter;
    GridView gridview;
    ListView listview;
    CateAdapter adapter;
    int Select=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cate_product);
        /*************************************************** Set Custom ActionBar *****/
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar2);
        View mCustomView = getSupportActionBar().getCustomView();
        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        title.setText("لیست محصولات");
        //-----------------------------------------

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            TopicID = extra.getString("SID");
            Topic = extra.getString("Name");
        } else {
            TopicID = "0";
            Topic = "بی نام";
        }

        _SubTopicIDs.clear();
        _SubTopics.clear();
        for (int i = 0; i < ShowAllProducts.SubTopics.size(); i++) {
            if (ShowAllProducts.SubParentID.get(i).equals(TopicID)) {
                _SubTopics.add(ShowAllProducts.SubTopics.get(i));
                _SubTopicIDs.add(ShowAllProducts.SubTopicIDs.get(i));
            }
        }
        if (_SubTopics.size() < 1) return;

        //Products(TopicID);
        new AsyncTask<String, String, Boolean>() {
            ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);

            @Override
            protected void onPreExecute() {
                pb.setVisibility(View.VISIBLE);
            }

            @Override
            protected Boolean doInBackground(String... params) {
                Products(params[0]);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                pb.setVisibility(View.INVISIBLE);
                imAdapter.notifyDataSetChanged();
                Helper.setGridViewHeightBasedOnChildren(gridview,2);            }
        }.execute(TopicID);


        listview = (ListView) findViewById(R.id.listView1);
//        adapter = new ArrayAdapter<String>(this,
//                R.layout.listview_item_row, _SubTopics);
        adapter =new CateAdapter(this);
        listview.setAdapter(adapter);
        Helper.getListViewSize(listview);

        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                new LongOperation().execute(_SubTopicIDs.get(position));
                Select = position;
                adapter.notifyDataSetChanged();
            }
        });

        gridview = (GridView) findViewById(R.id.gridView1);
        imAdapter = new ImageAdapter(this);
        gridview.setAdapter(imAdapter);

        ScrollView mainScrollView=(ScrollView) findViewById(R.id.scrollView);
//        mainScrollView.fullScroll(View.FOCUS_UP);
//        mainScrollView.pageScroll(View.FOCUS_UP);


        gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String SelectID = ID.get(position);
                Intent inte = new Intent(Cate_Product.this, PageIndicator.class);
                inte.putExtra("SID", SelectID);
                //inte.putExtra("PID", SelectID);
                startActivity(inte);
            }
        });

    }

    //----------------------------------------------------------------------
    public boolean Products(String topicID) {
        try {
            CallSoap cs = new CallSoap();
            String result = cs.ResiveList("TopicProducts?topicID=" + topicID );
            ID.clear();
            Name.clear();
            Price.clear();
            Image.clear();//Desc.clear();
            String[] Rows = result.split(":");
            if (Rows.length > 0) {
                for (int i = 0; i < Rows.length; i++) {
                    String[] Field = Rows[i].split(",");
                    ID.add(Field[0]);
                    Name.add(Field[1]);
                    Price.add(Field[2]);
                    Image.add(Field[3]);
                    //Desc.add(Field[4]);
                }
            } else
                Toast.makeText(Cate_Product.this, "هیچ کالایی در این گروه موجود نمی باشد",
                        Toast.LENGTH_LONG).show();

            imAdapter.notifyDataSetChanged();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //-------------------------------------------------------------------------
    private class LongOperation extends AsyncTask<String, String, Boolean> {
        @Override
        protected void onPostExecute(Boolean result) {
            ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
            pb.setVisibility(View.INVISIBLE);
            imAdapter.notifyDataSetChanged();
            Helper.setGridViewHeightBasedOnChildren(gridview,2);
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            if (! CallSoap.isConnectionAvailable(Cate_Product.this))
            {
                Intent inte = new Intent(Cate_Product.this, NoNet.class);
                startActivity(inte);
            }
            ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
            pb.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            StoreList(params[0]);
            return true;
        }
    }

    //--------------------------------------------------------------------------------
    public class CateAdapter extends BaseAdapter {
        private Context mContext;

        public CateAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return _SubTopics.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View gridViewAndroid;
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
            } else {
                gridViewAndroid = (View) convertView;
            }

            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(R.layout.listview_item_row, null);
            TextView title = (TextView) gridViewAndroid.findViewById(R.id.Text1);
            title.setText(_SubTopics.get(position));
            if (position == Select)
                title.setBackgroundColor(0xffddddff);

            return gridViewAndroid;
        }

    }

    //--------------------------------------------------------------------------------
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return Name.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View gridViewAndroid;
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
            } else {
                gridViewAndroid = (View) convertView;
            }

            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(R.layout.gridview_product, null);
            TextView name = (TextView) gridViewAndroid.findViewById(R.id.gridview_text1);
            TextView price = (TextView) gridViewAndroid.findViewById(R.id.Price);
            TextView dis = (TextView) gridViewAndroid.findViewById(R.id.dis);
            //dis.setText(Discout.get(position) + "%" + " تخفیف");
            //if (Integer.parseInt(Discout.get(position)) > 0) dis.setVisibility(View.VISIBLE);
            //else dis.setVisibility(View.GONE);
            //price.setBackgroundColor(Color.parseColor("#76c4a5"));
            ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.gridview_image);
            name.setText(Name.get(position));
            price.setText(Helper.GetMoney(Price.get(position)));
            final ProgressBar pb = (ProgressBar) gridViewAndroid.findViewById(R.id.progressBar1);
            pb.setVisibility(View.VISIBLE);
            Picasso.with(mContext) //
                    .load(getString(R.string.WServer) + "/images/" + Image.get(position)) //
                    .error(R.drawable.i2) //
                    .fit() //
                    .tag(mContext) //
                    .into(imageViewAndroid,new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            pb.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            pb.setVisibility(View.GONE);
                        }
                    });

            return gridViewAndroid;
        }

    }

    //----------------------------------------------------------------------
    public boolean StoreList(String topicID) {
        try {
            CallSoap cs = new CallSoap();
            String result = "";
                result = cs.ResiveList("AllProducts?topicID=" + topicID);
            ID.clear();
            Name.clear();
            Price.clear();
            Image.clear();
            Discout.clear();
            String[] Rows = result.split(":");
            for (int i = 0; i < Rows.length; i++) {
                String[] Field = Rows[i].split(",");
                ID.add(Field[0]);
                Name.add(Field[1]);
                Price.add(Field[2]);
                Image.add(Field[3]);
                Discout.add(Field[5]);
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

//-------- End Activity
}




