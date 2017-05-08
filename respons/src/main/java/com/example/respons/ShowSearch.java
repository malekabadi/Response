package com.example.respons;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShowSearch extends AppCompatActivity {

    public ArrayList<String> ID = new ArrayList<String>();
    public ArrayList<String> Name = new ArrayList<String>();
    public ArrayList<String> Price = new ArrayList<String>();
    public ArrayList<String> Image = new ArrayList<String>();
    public ArrayList<String> Desc = new ArrayList<String>();
    public ArrayList<String> Shop = new ArrayList<String>();

    public String txtShop, Product, Detail;
    ListView gridview;
    ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders);
        /*************************************************** Set Custom ActionBar *****/
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar2);
        View mCustomView = getSupportActionBar().getCustomView();
        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        title.setText("نتیجه جستجو");
        //-----------------------------------------

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            txtShop = extra.getString("Shop");
            Product = extra.getString("Product");
            Detail = extra.getString("Detail");
        }
        if (txtShop == null) txtShop = "";
        if (Product == null) Product = "";
        if (Detail == null) Detail = "";

        gridview = (ListView) findViewById(R.id.View1);
        imageAdapter = new ImageAdapter(this);
        gridview.setAdapter(imageAdapter);

        new LongOperation().execute("");

        gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String sid = ID.get(position);
                if (Shop.get(position).equals("0")) {
                    Intent inte = new Intent(ShowSearch.this, ShowProducts.class);
                    inte.putExtra("SID", sid);
                    startActivity(inte);

                } else {
                    Intent inte = new Intent(ShowSearch.this, PageIndicator.class);
                    inte.putExtra("SID", sid);
                    startActivity(inte);
                }
            }
        });


    }

    //-------------------------------------------------------------------------
    private class LongOperation extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPostExecute(Boolean result) {
            ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
            pb.setVisibility(View.INVISIBLE);
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
            if (Product.length() > 0)
                ProductSearch();
            else
                ShopSearch();
            return null;
        }
    }

    //------------------------------------------------------------------------------
    public class ImageAdapter extends BaseAdapter {

        private Context mContext;

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

        public View getView(int position, View convertView, ViewGroup parent) {
            View gridViewAndroid;
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                ;
            } else {
                gridViewAndroid = (View) convertView;
            }

            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(R.layout.gridview_layout2, null);
            TextView name = (TextView) gridViewAndroid.findViewById(R.id.gridview_text1);
            TextView price = (TextView) gridViewAndroid.findViewById(R.id.gridview_text2);
            TextView desc = (TextView) gridViewAndroid.findViewById(R.id.gridview_text3);
            ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.gridview_image);
            name.setText(Name.get(position));
            if (Price.get(position).length() > 0)
                price.setText(Helper.GetMoney(Price.get(position)));
            desc.setText(Desc.get(position));
            Picasso.with(mContext) //
                    .load(getString(R.string.WServer) + "/images/" + Image.get(position)) //
                    .error(R.drawable.i2) //
                    .fit() //
                    .tag(mContext) //
                    .into(imageViewAndroid);

            return gridViewAndroid;
        }

        public void remove(int position) {
            Name.remove(position);
            Price.remove(position);
            Desc.remove(position);
            Image.remove(position);
            Shop.remove(position);
            notifyDataSetChanged();

        }

    }

    //------------------------------------------------------------------------------
    public boolean ProductSearch() {
        try {
            CallSoap cs = new CallSoap();
            String result = cs.ResiveList("ProductSearch?txtShop=" + txtShop + "&txtProduct=" + Product + "&txtDetail=" + Detail);
            String[] Rows = result.split(":");
            for (int i = 0; i < Rows.length; i++) {
                String[] Field = Rows[i].split(",");
                ID.add(Field[0]);
                Name.add(Field[1]);
                Price.add(Field[2]);
                Image.add(Field[3]);
                Desc.add(Field[4]);
                Shop.add(Field[5]);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //------------------------------------------------------------------------------
    public boolean ShopSearch() {
        try {
            CallSoap cs = new CallSoap();
            String result = cs.ResiveList("ShopSearch?txtShop=" + txtShop);
            String[] Rows = result.split(":");
            for (int i = 0; i < Rows.length; i++) {
                String[] Field = Rows[i].split(",");
                ID.add(Field[0]);
                Name.add(Field[1]);
                Price.add("");
                Image.add(Field[3]);
                Desc.add(Field[4]);
                Shop.add("0");
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




