package com.example.respons;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ShowProducts extends AppCompatActivity {

    public List<String> pty = new ArrayList<String>();
    public List<String> val = new ArrayList<String>();

    public ArrayList<String> ID = new ArrayList<String>();
    public ArrayList<String> Name = new ArrayList<String>();
    public ArrayList<String> Price = new ArrayList<String>();
    public ArrayList<String> Discout = new ArrayList<String>();
    public ArrayList<String> Image = new ArrayList<String>();

    public ArrayList<String> CategoryID = new ArrayList<String>();
    public ArrayList<String> SubCategoryID = new ArrayList<String>();

    public String[] Category;
    public String[] SubCategory;
    public String ShopID, cid = "", sort = "";
    int lastItemPosition=0,page=0;
    static String ShpName;
    Button btnCat;
    GridView gridview;
    ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products);

/*************************************************** Set Custom ActionBar *****/
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar2);
        View mCustomView = getSupportActionBar().getCustomView();
        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        title.setText("نمایش محصولات");
        //ActionBar mActionBar = getActionBar();
        //mActionBar.setDisplayShowHomeEnabled(false);
        //mActionBar.setDisplayShowTitleEnabled(false);
        //
        //LayoutInflater mInflater = LayoutInflater.from(this);
        //View mCustomView = mInflater.inflate(R.layout.actionbar2, null);
        //
        //TextView title = (TextView) mCustomView.findViewById(R.id.title);
        //title.setText(ShpName);
        //
        //ImageView sabad=(ImageView) mCustomView.findViewById(R.id.Licon); sabad.setImageResource(R.drawable.cart);
        //sabad.setOnClickListener(new OnClickListener() {
        //	@Override
        //	public void onClick(View v) {
        //		Intent inte=new Intent(ShowProducts.this, Cart.class);
        //		startActivity(inte);
        //	}
        //});
        //
        //ImageView about=(ImageView) mCustomView.findViewById(R.id.Ricon); about.setImageResource(R.drawable.info);
        //about.setOnClickListener(new OnClickListener() {
        //	@Override
        //	public void onClick(View v) {
        //		Intent i=new Intent(ShowProducts.this,StoreInfo.class);
        //		i.putExtra("ShopID", ShopID);
        //		startActivity(i);
        //	}
        //});
        //
        //mActionBar.setCustomView(mCustomView);
        //mActionBar.setDisplayShowCustomEnabled(true);

//----------------------------------- 

        final CallSoap cs = new CallSoap();

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            ShopID = extra.getString("SID");
            ShpName = extra.getString("Name");
        } else {
            ShopID = "0";
            ShpName = "بی نام";
        }
//PageIndicator.Sabad sa;
//TextView shopName=(TextView) findViewById(R.id.shopName); 
//shopName.setText(ShpName);
//title.setText(ShpName);
        btnCat = (Button) findViewById(R.id.btnCate);
        btnCat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //String res=cs.ResiveList("CategoryList?shopID="+sid);

//		String res=Res;
//		String[] Rows = res.split(":");
//		Category=new String[Rows.length];
//		for (int i = 0; i < Rows.length; i++) 
//		{
//			String[] Field = Rows[i].split(",");
//			CategoryID.add(Field[0]);
//			Category[i]=Field[1];
//		}
                if (Category != null)
                    showPopup(ShowProducts.this);
            }
        });

        Button Filter = (Button) findViewById(R.id.Filter);
        Filter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter(ShowProducts.this);
            }
        });

        Button Sort = (Button) findViewById(R.id.Sort);
        Sort.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Sort(ShowProducts.this);
            }
        });
        new LongOperation().execute();
        GridView gridview = (GridView) findViewById(R.id.gridView1);
        imageAdapter = new ImageAdapter(this);
        gridview.setAdapter(imageAdapter);
        gridview.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastItem = firstVisibleItem + visibleItemCount;
                if (imageAdapter.getCount() >= 30 && lastItem  > imageAdapter.getCount() - 4) {
                    boolean isLoading = false;
                    if (!isLoading) {
                        if(lastItem > lastItemPosition){
                            lastItemPosition = imageAdapter.getCount();
                            page++;
                            new LongOperation().execute("True");
                        }
                        isLoading = true;
                    }
                }
            }
            public void onScrollStateChanged(AbsListView view, int scrollState) {}

        });

        gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String SelectID = ID.get(position);
                Intent inte = new Intent(ShowProducts.this, PageIndicator.class);
                inte.putExtra("SID", SelectID);
                //inte.putExtra("PID", SelectID);
                startActivity(inte);
            }
        });

    }

    //--------------------------------------------------------------------------------
    private class LongOperation extends AsyncTask<String, Integer, Boolean> {
        String res;

        @Override
        protected void onPostExecute(Boolean result) {
            ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
            pb.setVisibility(View.INVISIBLE);
            imageAdapter.notifyDataSetChanged();
            res = "-1,همه:" + res;
            String[] Rows = res.split(":");
            Category = new String[Rows.length];
            for (int i = 0; i < Rows.length; i++) {
                String[] Field = Rows[i].split(",");
                if (Field.length > 1) {
                    CategoryID.add(Field[0]);
                    Category[i] = Field[1];
                }
            }

            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            if (! CallSoap.isConnectionAvailable(ShowProducts.this))
            {
                Intent inte = new Intent(ShowProducts.this, NoNet.class);
                startActivity(inte);
            }
            ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
            pb.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Products("", "", "","");
            res = new CallSoap().ResiveList("CategoryList?shopID=" + ShopID);
            return null;
        }
    }

    //--------------------------------------------------------------------------------
    private class ProductsList extends AsyncTask<String, Integer, Boolean> {
        String res;
        ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);

        @Override
        protected void onPostExecute(Boolean result) {
            pb.setVisibility(View.INVISIBLE);
            imageAdapter.notifyDataSetChanged();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            if (! CallSoap.isConnectionAvailable(ShowProducts.this))
            {
                Intent inte = new Intent(ShowProducts.this, NoNet.class);
                startActivity(inte);
            }
            ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
            pb.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
                Products( params[0], params[1], params[2],params[3]);
            return null;
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
            dis.setText(Discout.get(position) + "%" + " تخفیف");
            if (Integer.parseInt(Discout.get(position)) > 0) dis.setVisibility(View.VISIBLE);
            else dis.setVisibility(View.GONE);
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
    public boolean Products(String Fields, String str, String min, String max) {
        try {
            CallSoap cs = new CallSoap();
            String result="";
            if (cid.equals(""))
                result = cs.ResiveList("ProductsShop?cid=all" + "&shopID=" + ShopID + "&Sort=" + sort + "&field=" + Fields + "&str=" + str
                        + "&min=" + min+ "&max=" + max + "&page=" + page);
            else
                result = cs.ResiveList("ProductFilter?cid=" + cid + "&topicID" + ShopID + "&Sort=" + sort + "&field=" + Fields + "&str=" + str
                        + "&min=" + min+ "&max=" + max + "&page=" + page);

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
                    Discout.add(Field[5]);
                }
            } else
                Toast.makeText(ShowProducts.this, "هیچ کالایی در این گروه موجود نمی باشد",
                        Toast.LENGTH_LONG).show();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //------------------------------------------------------------
    private void showPopup(final Activity context) {

        Display display = getWindowManager().getDefaultDisplay();
        int popupWidth = display.getWidth();//-(int)(display.getWidth()*0.2);
        int popupHeight = display.getHeight();//-(int)(display.getWidth()*0.6);

        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup, viewGroup);

        final ListView lv = (ListView) layout.findViewById(R.id.list_topic);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.listview_item_row, Category);
        lv.setAdapter(adapter);

        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

        popup.setBackgroundDrawable(new ColorDrawable(0xa0000000));
        popup.getContentView().setBackgroundResource(android.R.color.transparent);

        popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String item = CategoryID.get(position);
                btnCat.setText(Category[position]);
                if (item.equals("-1")) {
                    new LongOperation().execute("");
                    popup.dismiss();
                    return;
                }
                cid = item;
                CallSoap cs = new CallSoap();
                String res = "";
                try {
                    res = cs.ResiveListSync("SubCategoryList?CID=" + item);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (res.indexOf(":") >= 0) {
                    String[] Rows = res.split(":");
                    SubCategory = new String[Rows.length];
                    for (int i = 0; i < Rows.length; i++) {
                        String[] Field = Rows[i].split(",");
                        SubCategoryID.add(Field[0]);
                        SubCategory[i] = Field[1];
                    }
                    lv.setVisibility(View.GONE);
                    subList(ShowProducts.this);
                } else
                    new ProductsList().execute("","","","");
                popup.dismiss();
            }
        });

    }

    //------------------------------------------------------------
    private void subList(final Activity context) {
        Display display = getWindowManager().getDefaultDisplay();
        int popupWidth = display.getWidth();//-100;
        int popupHeight = display.getHeight();//-200;

        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.filter);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup, viewGroup);

        final ListView lv = (ListView) layout.findViewById(R.id.list_topic);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.listview_item_row, SubCategory);
        lv.setAdapter(adapter);

        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

        popup.setBackgroundDrawable(new ColorDrawable(0xa0000000));
        popup.getContentView().setBackgroundResource(android.R.color.transparent);
        popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                btnCat.setText(SubCategory[position]);
                String CategorySelect = SubCategoryID.get(position);
                cid = CategorySelect;
                new ProductsList().execute("","","","");
                popup.dismiss();
            }
        });
    }

    //------------------------------------------------------------
    private void Sort(final Activity context) {
        Display display = getWindowManager().getDefaultDisplay();
        int popupWidth = display.getWidth();//-100;
        int popupHeight = display.getHeight();//-200;

        RelativeLayout viewGroup = (RelativeLayout) context.findViewById(R.id.sort);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.sort, viewGroup);
        float density = this.getResources().getDisplayMetrics().density;
        //popupHeight=(int) (265 * density);

        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

        popup.setBackgroundDrawable(new ColorDrawable(0xa0000000));
        popup.getContentView().setBackgroundResource(android.R.color.transparent);
        popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

        final RadioGroup radio = (RadioGroup) layout.findViewById(R.id.radioGroup1);
        radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = radio.findViewById(checkedId);
                int index = radio.indexOfChild(radioButton);
                ID.clear();
                Name.clear();
                Price.clear();
                Image.clear();


                // Add logic here

                switch (index) {
                    case 0: // New
                        sort = "";
                        break;
                    case 1: // Min Price
                        sort = "MaxPrice";
                        break;
                    case 2: // Max Price
                        sort = "MinPrice";
                        break;
                    case 3: // Max Discount
                        sort = "";
                        break;
                    case 4: // Max Buy
                        sort = "";
                        break;
                    case 5: // Max Show
                        sort = "";
                        break;
                }
                if (cid.length() > 0)
                    new ProductsList().execute("","","","");
                    //Products(cid, sort);
                else
                    //StoreList(ShopID, sort);
                    new ProductsList().execute("","","","");
                popup.dismiss();
            }
        });

    }

    ////------------------------------------------------------------
    private void Filter(final Activity context) {
//	Display display = getWindowManager().getDefaultDisplay();
//	int popupWidth = display.getWidth()-100;
//	int popupHeight = display.getHeight()-200;
//	float density = this.getResources().getDisplayMetrics().density;
//	popupHeight=(int) (265 * density);
//
//	LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.filter);
//	LayoutInflater layoutInflater = (LayoutInflater) context
//	.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	View layout = layoutInflater.inflate(R.layout.filter, viewGroup);
//	
//	final PopupWindow popup = new PopupWindow(context);
//	popup.setContentView(layout);
//	popup.setWidth(popupWidth);
//	popup.setHeight(popupHeight);
//	popup.setFocusable(true);
//	
//	//popup.setBackgroundDrawable(new BitmapDrawable());
//	//popup.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//	popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter);
        dialog.show();

        final ListView listPty = (ListView) dialog.findViewById(R.id.listView);
        final TAdapter tap = new TAdapter(this);
        listPty.setAdapter(tap);
        Helper.getListViewSize(listPty);


        new AsyncTask<String, Integer, Boolean>() {
            final String res = "";

            @Override
            protected Boolean doInBackground(String... params) {
                if (params == null) {
                    return false;
                }
                try {

                    Thread.sleep(2000);
                    CallSoap cs = new CallSoap();
                    String res2 = cs.ResiveList("CateProperty?id=" + cid);
                    String[] Pro = res2.split(",");
                    pty.clear();
                    val.clear();
                    for (int i = 0; i < Pro.length; i++) {
                        pty.add(Pro[i]);
                        val.add("");
                    }
                } catch (Exception e) {

                    return false;
                }

                return true;
            }

            protected void onPostExecute(Boolean result) {
                tap.notifyDataSetChanged();
                Helper.getListViewSize(listPty);
            }
        }.execute();


//	CallSoap cs= new CallSoap();
//	String res2=cs.ResiveList("CateProperty?id="+cid);
//	String[] Pro=res2.split(",");
//	pty.clear();val.clear();
//	for(int i=0;i<Pro.length;i++)
//	{
//		pty.add(Pro[i]);
//		val.add("");
// 	}
        final EditText min = (EditText) dialog.findViewById(R.id.txtMin);
        final EditText max = (EditText) dialog.findViewById(R.id.txtMax);
        Button Filter = (Button) dialog.findViewById(R.id.Filter);
        Filter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String fields = "", str = "";
                for (int i = 0; i < val.size(); i++) {
                    if (val.get(i).length() > 0) {
                        fields += pty.get(i) + ";";
                        str += val.get(i) + ";";
                    }
                }
                new ProductsList().execute(fields, str, min.getText().toString(),max.getText().toString());
                dialog.dismiss();
            }
        });

    }

    //-----------------------------------------------------------------
    public class TAdapter extends BaseAdapter {

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

            convertView = inflater.inflate(R.layout.listview_wedit, null);
            TextView item = (TextView) convertView.findViewById(R.id.Item);
            EditText value = (EditText) convertView.findViewById(R.id.Value);
            item.setText(pty.get(position));
            value.setText(val.get(position));
            value.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                    val.set(position, arg0.toString());
                }
            });

            return convertView;
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




