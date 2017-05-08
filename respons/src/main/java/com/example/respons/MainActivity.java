package com.example.respons;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
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
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MenuRight {

    public List<String> Shops = new ArrayList<String>();
    public ArrayList<String> ShopsID = new ArrayList<String>();
    public static ArrayList<String> Topics = new ArrayList<String>();
    public static ArrayList<String> TopicIDs = new ArrayList<String>();
    public ArrayList<String> Zones = new ArrayList<String>();
    public ArrayList<String> ZoneIDs = new ArrayList<String>();
    public static ArrayList<String> SubTopics = new ArrayList<String>();
    public static ArrayList<String> SubTopicIDs = new ArrayList<String>();
    public static ArrayList<String> SubParentID = new ArrayList<String>();
    ArrayList<String> _SubTopics = new ArrayList<String>();
    ArrayList<String> _SubTopicIDs = new ArrayList<String>();
    public ArrayList<String> Image = new ArrayList<String>();
    public ArrayList<String> Desc = new ArrayList<String>();

    Point p;
    String TopicID = "", ZoneID = "", FindStr = "";
    Button b1, b2;
    ImageAdapter imAdapter;
    GridView gridview;

    @Override
    public void onBackPressed() {
        // this.finish();
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
        Intent intent = new Intent(getApplicationContext(), ShowAllProducts.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", "true");
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//	getSupportActionBar().setDisplayShowHomeEnabled(true);
//	getSupportActionBar().setIcon(R.drawable.ic_launcher);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        final CallSoap cs = new CallSoap();
        String res = cs.ResiveList("TopicList");
//        appVar.main.UserID = "1";
//        appVar.main.UserName = "09156620865";
//        appVar.main.HasShop = true;
//        appVar.main.ShopID = "2";

/*************************************************** Set Custom ActionBar *****/
//ActionBar mActionBar = getActionBar();
//mActionBar.setDisplayShowHomeEnabled(false);
//mActionBar.setDisplayShowTitleEnabled(false);
//
//LayoutInflater mInflater = LayoutInflater.from(this);
//View mCustomView = mInflater.inflate(R.layout.actionbar, null);
//
//TextView shp = (TextView) mCustomView.findViewById(R.id.pro);
//shp.setOnClickListener(new OnClickListener() {
//
//	@Override
//	public void onClick(View view) {
//		Intent i = new Intent(MainActivity.this, ShowAllProducts.class);
//		i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//    	startActivity(i);
//	}
//});
//
//TextView tv = (TextView) mCustomView.findViewById(R.id.shp);
//tv.setBackgroundResource(R.color.grl);
//
//mActionBar.setCustomView(mCustomView);
//mActionBar.setDisplayShowCustomEnabled(true);

//----------------------------------- Check Internet
//if (cs.isConnectionAvailable(this)) 
//{
//	Toast.makeText(MainActivity.this, "Internet Connection failed",
//			   Toast.LENGTH_LONG).show();
//	return;
//}

//ImageView Search=(ImageView) mCustomView.findViewById(R.id.Search);
//Search.setOnClickListener(new OnClickListener() {
//	@Override
//	public void onClick(View v) {
//		Search(MainActivity.this);
//	}
//});
//
//ImageView login=(ImageView) mCustomView.findViewById(R.id.login);
//login.setOnClickListener(new OnClickListener() {
//	@Override
//	public void onClick(View v) {
//		Intent i;
//        if (!appVar.main.UserID.equals(""))
//        {
//        	if (appVar.main.HasShop)
//        	{
//	        	i = new Intent(MainActivity.this, ProfileA.class);
//	        	startActivity(i);
//        	} else {
//	        	i = new Intent(MainActivity.this, StoreReg.class);
//	        	startActivity(i);
//        	}
//
//        } else {
//        	i = new Intent(MainActivity.this, LoginActivity.class);
//        	startActivity(i);
//        }
//		imAdapter.removeall();
//		FindStr=txtSearch.getText().toString();
//		StoreList(TopicID,ZoneID,FindStr);
//		imAdapter.notifyDataSetChanged();
//		gridview.setAdapter(imAdapter);
//	}
//});


        Button p1 = (Button) findViewById(R.id.p1);
        p1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ShowCate.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        Button p2 = (Button) findViewById(R.id.p2);
        p2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ShowAllProducts.class);
//			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

//-------------------------------------------- TopicList and ZoneList
        if (Topics.size() < 1) {
            TopicIDs.add("0");
            Topics.add("همه");
            ZoneIDs.add("0");
            Zones.add("همه");

        }
//----------------------------------------------------		
        b1 = (Button) findViewById(R.id.Subject);
        b1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(MainActivity.this, p);
            }
        });

//StoreList(TopicID,ZoneID,FindStr);
        new LongOperation().execute("");
        gridview = (GridView) findViewById(R.id.gridView1);
        imAdapter = new ImageAdapter(this);
        gridview.setAdapter(imAdapter);

        gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String sid = ShopsID.get(position);
                String Name = Shops.get(position);
                Intent inte = new Intent(MainActivity.this, ShowProducts.class);
                inte.putExtra("SID", sid);
                inte.putExtra("Name", Name);
                startActivity(inte);
            }
        });

    }

    //-------------------------------------------------------------------------
    private class LongOperation extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPostExecute(Boolean result) {
            ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
            pb.setVisibility(View.INVISIBLE);
            imAdapter.notifyDataSetChanged();
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
            CallSoap cs = new CallSoap();
            String res = "";
            String[] Rows;
            if (Topics.size() < 2) {
                res = cs.ResiveList("TopicList");
                Rows = res.split(":");
                for (int i = 0; i < Rows.length - 1; i++) {
                    String[] Field = Rows[i].split(",");
                    TopicIDs.add(Field[0]);
                    Topics.add(Field[1]);
                }
                res = cs.ResiveList("SubTopic?id=" + "all");
                Rows = res.split(":");
                for (int i = 0; i < Rows.length - 1; i++) {
                    String[] Field = Rows[i].split(",");
                    SubTopicIDs.add(Field[0]);
                    SubTopics.add(Field[1]);
                    SubParentID.add(Field[2]);
                }
            }

            res = cs.ResiveList("ZoneList?CityID=122");
            Rows = res.split(":");
            for (int i = 0; i < Rows.length - 1; i++) {
                String[] Field = Rows[i].split(",");
                ZoneIDs.add(Field[0]);
                Zones.add(Field[1]);
            }
            StoreList(TopicID, ZoneID, FindStr);

            return null;
        }
    }

    //-------------------------------------------------------------------------
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return Shops.size();
        }

        public Object getItem(int position) {
            return Shops.get(position);
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
            gridViewAndroid = inflater.inflate(R.layout.gridview_layout, null);
            TextView text1 = (TextView) gridViewAndroid.findViewById(R.id.gridview_text1);
            TextView text2 = (TextView) gridViewAndroid.findViewById(R.id.gridview_text2);
            ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.gridview_image);
            text1.setText(Shops.get(position));
            text2.setText(Desc.get(position));
            text2.setTextSize(10);
            final ProgressBar pb = (ProgressBar) gridViewAndroid.findViewById(R.id.progressBar1);
            pb.setVisibility(View.VISIBLE);
            Picasso.with(mContext) //
                    .load(getString(R.string.WServer) + "/images/" + Image.get(position)) //
                    //.placeholder(R.drawable.i1) //
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

        public void removeall() {
            Shops.clear();
            ShopsID.clear();
            Image.clear();
            Desc.clear();
        }
    }

    //-----------------------------------------------------------
    public boolean StoreList(final String topicID, final String zoneID, final String findStr) {
        try {
//		new AsyncTask<String, Integer, Boolean>() {
//		final String res="";
//		@Override
//		protected Boolean doInBackground(String... params) {
//			if (params == null) {
//				return false;
//			}
//			try {
//
//				Thread.sleep(2000);
//				CallSoap cs=new CallSoap();
//		        String res=cs.ResiveList("ShopList?TopicID="+topicID+"&ZoneID="+zoneID+"&Search="+findStr);
//				String[] Rows = res.split(":");
//				for (int i = 1; i < Rows.length-1; i++) 
//				{
//					String[] Field = Rows[i].split(",");
//					ShopsID.add(Field[0]);
//					Shops.add(Field[1]);
//					if (Field.length>2) Image.add(Field[2]);
//					if (Field.length>3) Desc.add(Field[3]); else Desc.add("");
//				}
//
//			} catch (Exception e) {
//
//				return false;
//			}
//
//			return true;
//		}
//		protected void onPostExecute(Boolean result) {
//		}
//	}.execute();

            CallSoap cs = new CallSoap();
            String res = cs.ResiveList("ShopList?TopicID=" + topicID + "&ZoneID=" + zoneID + "&Search=" + findStr);
            String[] Rows = res.split(":");
            for (int i = 1; i < Rows.length - 1; i++) {
                String[] Field = Rows[i].split(",");
                ShopsID.add(Field[0]);
                Shops.add(Field[1]);
                if (Field.length > 2) Image.add(Field[2]);
                if (Field.length > 3) Desc.add(Field[3]);
                else Desc.add("");
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //---------------------------------------------------------------
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        int[] location = new int[2];
//Button button = (Button) findViewById(R.id.Position);

// location[0] = x, location[1] = y.
//button.getLocationOnScreen(location);

        p = new Point();
        p.x = location[0];
        p.y = location[1];
    }

    //---------------------------------------------------  Topics List
    private void showPopup(final Activity context, Point p) {

        Display display = getWindowManager().getDefaultDisplay();
        int popupWidth = display.getWidth();//-(int)(display.getWidth()*0.2);
        int popupHeight = display.getHeight();//-(int)(display.getWidth()*0.6);

        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup, viewGroup);

        final ListView lv = (ListView) layout.findViewById(R.id.list_topic);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.listview_item_row, Topics);
        lv.setAdapter(adapter);

        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

        int OFFSET_X = 0;
        int OFFSET_Y = 30;

        popup.setBackgroundDrawable(new ColorDrawable(0xa0000000));
        popup.getContentView().setBackgroundResource(android.R.color.transparent);

//popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y +  OFFSET_Y);
        popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String item = TopicIDs.get(position);
                Toast.makeText(MainActivity.this, item,
                        Toast.LENGTH_LONG).show();
                if (position == 0) {
                    Shops.clear();
                    ShopsID.clear();
                    TopicID = "";
                    //StoreList(TopicID,ZoneID,FindStr);
                    new LongOperation().execute("");
                    imAdapter.notifyDataSetChanged();
                    popup.dismiss();
                } else {
                    _SubTopicIDs.clear();
                    _SubTopics.clear();
                    for (int i = 0; i < SubTopics.size(); i++) {
                        if (SubParentID.get(i).equals(item)) {
                            _SubTopics.add(SubTopics.get(i));
                            _SubTopicIDs.add(SubTopicIDs.get(i));
                        }
                    }
                    if (_SubTopics.size() > 0) {
//				lv.setVisibility(0);
                        subList(MainActivity.this);
                    }
                    popup.dismiss();
                }
            }
        });

    }

    //----------------------------------------------------- SubTopics List
    private void subList(final Activity context) {
        Display display = getWindowManager().getDefaultDisplay();
        int popupWidth = display.getWidth();//-100;
        int popupHeight = display.getHeight();//-200;

        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup, viewGroup);

        final ListView lv = (ListView) layout.findViewById(R.id.list_topic);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.listview_item_row, _SubTopics);
        lv.setAdapter(adapter);

        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);


        int OFFSET_X = 0;
        int OFFSET_Y = 30;

//popup.setBackgroundDrawable(new BitmapDrawable());
        popup.setBackgroundDrawable(new ColorDrawable(0xa0000000));
        popup.getContentView().setBackgroundResource(android.R.color.transparent);

        popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                b1.setText(SubTopics.get(position));
                Shops.clear();
                ShopsID.clear();
                TopicID = SubTopicIDs.get(position);
                //StoreList(TopicID,ZoneID,FindStr);
                new LongOperation().execute("");
                imAdapter.notifyDataSetChanged();
                popup.dismiss();
            }
        });
    }

    //----------------------------------------------------- SubTopics List
    private void ZoneList(final Activity context) {
        Display display = getWindowManager().getDefaultDisplay();
        int popupWidth = display.getWidth() - 100;
        int popupHeight = display.getHeight() - 200;

        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup, viewGroup);

        final ListView lv = (ListView) layout.findViewById(R.id.list_topic);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.listview_item_row, Zones);
        lv.setAdapter(adapter);

        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);


        int OFFSET_X = 0;
        int OFFSET_Y = 30;

        popup.setBackgroundDrawable(new BitmapDrawable());

        popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                b2.setText(Zones.get(position));
                Shops.clear();
                ShopsID.clear();
                ZoneID = ZoneIDs.get(position);
                StoreList(TopicID, ZoneID, FindStr);
                imAdapter.notifyDataSetChanged();
                popup.dismiss();
            }
        });
    }

    //------------------------------------------------------------
    private void Search(final Activity context) {
//	Display display = getWindowManager().getDefaultDisplay();
//	int popupWidth = display.getWidth();
//	int popupHeight = display.getHeight();
//	float density = this.getResources().getDisplayMetrics().density;
//	popupHeight=(int) (235 * density);
//
//	LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.search);
//	LayoutInflater layoutInflater = (LayoutInflater) context
//	.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	View layout = layoutInflater.inflate(R.layout.search, viewGroup);
//	
//	final PopupWindow popup = new PopupWindow(context);
//	popup.setContentView(layout);
//	popup.setWidth(popupWidth);
//	popup.setHeight(popupHeight);
//	popup.setFocusable(true);
//	
//	popup.setBackgroundDrawable(new BitmapDrawable());
//	popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        dialog.setContentView(R.layout.search);
        dialog.show();


        final EditText Shop = (EditText) dialog.findViewById(R.id.txtShop);
        final EditText Product = (EditText) dialog.findViewById(R.id.txtProduct);
        final EditText Detail = (EditText) dialog.findViewById(R.id.txtDetail);
        Button Search = (Button) dialog.findViewById(R.id.btnSearch);
        Search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent i = new Intent(MainActivity.this, ShowSearch.class);
                String p = Shop.getText().toString();
                i.putExtra("Shop", p);
                p = Product.getText().toString();
                p = Product.getEditableText().toString();
                i.putExtra("Product", Product.getEditableText().toString());
                i.putExtra("Detail", Detail.getText());
                startActivity(i);
                dialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Search(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
//-------- End Activity
}




