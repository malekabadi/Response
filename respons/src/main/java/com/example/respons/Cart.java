package com.example.respons;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

//import android.widget.AdapterView.OnItemClickListener;

public class Cart extends AppCompatActivity {

    public ArrayList<String> ID = new ArrayList<String>();
    public ArrayList<String> Name = new ArrayList<String>();
    public ArrayList<String> Price = new ArrayList<String>();
    public ArrayList<String> Image = new ArrayList<String>();
    public ArrayList<String> Desc = new ArrayList<String>();

    public ArrayList<String> CategoryID = new ArrayList<String>();
    public ArrayList<String> SubCategoryID = new ArrayList<String>();

    public String[] Category;
    public String[] SubCategory;
    public String sid, cid;
    Button btnCat;
    int pos = -1;
    Point p;
    GridView gridview;
    ImageAdapter imageAdapter;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar2);
        View mCustomView = getSupportActionBar().getCustomView();
        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        title.setText("سبد خرید");
        sp = getSharedPreferences("share", MODE_PRIVATE);
        //Bundle extra = getIntent().getExtras();
        //if (extra != null)
        //    sid= extra.getString("SID");
        //else sid="0";

        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(Cart.this, GetAdress.class);
                startActivity(inte);
            }
        });

        gridview = (GridView) findViewById(R.id.gridView1);
        imageAdapter = new ImageAdapter(this);
        gridview.setAdapter(imageAdapter);
        StoreList("2");

    }


    public class ImageAdapter extends BaseAdapter {

        private Context mContext;
        int Number;

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
                gridViewAndroid = inflater.inflate(R.layout.gridview_cart, null);
                TextView name = (TextView) gridViewAndroid.findViewById(R.id.cart_text1);
                TextView desc = (TextView) gridViewAndroid.findViewById(R.id.cart_text2);
                Button count = (Button) gridViewAndroid.findViewById(R.id.num);
                TextView price = (TextView) gridViewAndroid.findViewById(R.id.price);
                final TextView total = (TextView) gridViewAndroid.findViewById(R.id.total);
                ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.cart_image);
                //name.setText(Name.get(position));
                //price.setText(Price.get(position));
                //desc.setText(Desc.get(position));
                Typeface tf = Typeface.createFromAsset(getAssets(), "B Traffic Bold.ttf");
                //price.setTypeface(tf);total.setTypeface(tf);

                Typeface ir = Typeface.createFromAsset(getAssets(), "iransans.ttf");
                price.setTypeface(tf);
                total.setTypeface(tf);

                name.setText(PageIndicator.Factor.get(position).Name);
                count.setText(PageIndicator.Factor.get(position).Count);
                desc.setText(PageIndicator.Factor.get(position).Desc);
                price.setText(" قیمت :   " + Helper.GetMoney(PageIndicator.Factor.get(position).Price));
                int tot = Integer.parseInt(PageIndicator.Factor.get(position).Count) *
                        Integer.parseInt(PageIndicator.Factor.get(position).Price);
                total.setText(" قیمت کل :   " + Helper.GetMoney(String.valueOf(tot)));
                if (PageIndicator.image.size() > position)
                    imageViewAndroid.setImageBitmap(PageIndicator.image.get(position));
                else
                    Picasso.with(mContext) //
                    .load(getString(R.string.WServer) + "/images/"+PageIndicator.Factor.get(position).image) //
                    .error(R.drawable.i2) //
                    .fit() //
                    .tag(mContext) //
                    .into(imageViewAndroid);

                Button Del = (Button) gridViewAndroid.findViewById(R.id.btnDel);
                Del.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        remove(position);
                    }
                });

                /****************************************************************
                 * Set Dec , Inc Button
                 ***/
                Typeface ye = Typeface.createFromAsset(getAssets(), "iransans.ttf");
                final TextView num = (TextView) gridViewAndroid.findViewById(R.id.num);
                num.setTypeface(ye);
                TextView inc = (TextView) gridViewAndroid.findViewById(R.id.inc);
                inc.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        Number = Integer.parseInt(num.getText().toString());
                        Number++;
                        PageIndicator.Factor.get(position).Count = Number + "";
                        num.setText(Number + "");
                        PageIndicator.Factor.get(position).Count = Number + "";
                        int tot = Number * Integer.parseInt(PageIndicator.Factor.get(position).Price);
                        total.setText(" قیمت کل :   " + Helper.GetMoney(String.valueOf(tot)));
                    }
                });
                TextView dec = (TextView) gridViewAndroid.findViewById(R.id.dec);
                dec.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        if (Number > 1) {
                            Number = Integer.parseInt(num.getText().toString());
                            Number--;
                            PageIndicator.Factor.get(position).Count = Number + "";
                            num.setText(Number + "");
                            PageIndicator.Factor.get(position).Count = Number + "";
                            int tot = Number * Integer.parseInt(PageIndicator.Factor.get(position).Price);
                            total.setText(" قیمت کل :   " + Helper.GetMoney(String.valueOf(tot)));
                        }
                    }
                });

            } else {
                gridViewAndroid = (View) convertView;
            }

            return gridViewAndroid;
        }

        public void remove(int position) {
            PageIndicator.Factor.remove(position);
            SharedPreferences.Editor editor = sp.edit();
            Gson gson = new Gson();
            String json = gson.toJson(PageIndicator.Factor);
            editor.putString("Factor", json);
            editor.commit();
            notifyDataSetChanged();

        }

    }

    //--------------------------------------------------------
    public boolean StoreList(String shopID) {
        try {
            CallSoap cs = new CallSoap();
            String result = cs.ResiveList("ProductsShop?shopID=" + shopID);
            String[] Rows = result.split(":");
            for (int i = 0; i < Rows.length; i++) {
                String[] Field = Rows[i].split(",");
                ID.add(Field[0]);
                Name.add(Field[1]);
                Price.add(Field[2]);
                Image.add(Field[3]);
                Desc.add(Field[4]);
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




