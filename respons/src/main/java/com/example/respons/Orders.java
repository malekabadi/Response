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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class Orders extends AppCompatActivity {

    public ArrayList<String> ID = new ArrayList<String>();
    public ArrayList<String> Name = new ArrayList<String>();
    public ArrayList<String> Price = new ArrayList<String>();
    public ArrayList<String> Addr = new ArrayList<String>();

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
        setContentView(R.layout.orders);

        //Button btnDet=(Button) findViewById(R.id.btnDet);
        //btnDet.setOnClickListener(new OnClickListener() {
        //	@Override
        //	public void onClick(View v) {
        //        Intent i = new Intent(Orders.this, ProductReg.class);
        //        i.putExtra("FID", fid);
        //        startActivity(i);
        //
        //	}
        //});
        Bundle extra = getIntent().getExtras();
        if (extra != null)
            ListType = extra.getString("Type");
        else ListType = "0";
        /*************************************************** Set Custom ActionBar *****/
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar2);
        View mCustomView = getSupportActionBar().getCustomView();
        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        title.setText("لیست گزارش");
        //-----------------------------------------

        //TextView title = (TextView) findViewById(R.id.title);
        //switch (Integer.parseInt(ListType)) {
        //case 1: title.setText("لیست خرید");break;
        //case 2: title.setText("فاکتورهای ابطال شده ");break;
        //default : title.setText("لیست سفارشات");
        //}
        new LongOperation().execute("");
        view = (ListView) findViewById(R.id.View1);
        tAdapter = new TextAdapter(this);
        view.setAdapter(tAdapter);

    }

    //-------------------------------------------------------------------------
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
            if (! CallSoap.isConnectionAvailable(Orders.this))
            {
                Intent inte = new Intent(Orders.this, NoNet.class);
                startActivity(inte);
            }
            ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
            pb.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            StoreList(appVar.main.ShopID, ListType);
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
            gridViewAndroid = inflater.inflate(R.layout.listview_orther, null);
            final TextView num = (TextView) gridViewAndroid.findViewById(R.id.number);
            final TextView price = (TextView) gridViewAndroid.findViewById(R.id.price);
            final TextView customer = (TextView) gridViewAndroid.findViewById(R.id.customer);
            num.setText(ID.get(position));
            customer.setText(Name.get(position));
            price.setText(Helper.GetMoney(Price.get(position)));
            Button btnDet = (Button) gridViewAndroid.findViewById(R.id.btnDet);
            btnDet.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Orders.this, Buy_Detail.class);
                    i.putExtra("FID", ID.get(position));
                    i.putExtra("price", Price.get(position));
                    i.putExtra("customer", Name.get(position));
                    i.putExtra("addr", Addr.get(position));
                    i.putExtra("Type", ListType);
                    startActivity(i);
                }
            });

            return gridViewAndroid;
        }
    }

    //------------------------------------------------------------------------------
    public boolean StoreList(String shopID, String ListType) {
        try {
            CallSoap cs = new CallSoap();
            String result = cs.ResiveList("GetShopFactors?shopID=" + shopID + "&Type=" + ListType);
            String[] Rows = result.split(":");
            for (int i = 0; i < Rows.length; i++) {
                String[] Field = Rows[i].split(",");
                ID.add(Field[0]);
                Name.add(Field[1]);
                Price.add(Field[2]);
                Addr.add(Field[3]);
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




