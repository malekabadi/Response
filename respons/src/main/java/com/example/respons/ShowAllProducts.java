package com.example.respons;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ShowAllProducts extends Fragment {

    public List<String> pty = new ArrayList<String>();
    public List<String> val = new ArrayList<String>();

    public ArrayList<String> ID = new ArrayList<String>();
    public ArrayList<String> Name = new ArrayList<String>();
    public ArrayList<String> Price = new ArrayList<String>();
    public ArrayList<String> Discout = new ArrayList<String>();
    public ArrayList<String> Image = new ArrayList<String>();

    public static ArrayList<String> Topics = new ArrayList<String>();
    public static ArrayList<String> TopicIDs = new ArrayList<String>();
    public static ArrayList<String> SubTopics = new ArrayList<String>();
    public static ArrayList<String> SubTopicIDs = new ArrayList<String>();
    public static ArrayList<String> SubParentID = new ArrayList<String>();
    ArrayList<String> _SubTopics = new ArrayList<String>();
    ArrayList<String> _SubTopicIDs = new ArrayList<String>();

    public String ShopID, cid = "", Res = "";
    static String ShpName;
    Button btnCat;
    GridView gridview;
    ImageAdapter imageAdapter;
    String sort = "",min="",max="";
    View rootView;

    public ShowAllProducts() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.content_menu_right, container, false);

//        Bundle extra = getIntent().getExtras();
//        if (extra != null)
//            if (extra.getString("EXIT") != null) {
//                finishAffinity();
//                finish();
//            }

        appVar.main.UserID = "1";
        appVar.main.UserName = "09156620865";
        appVar.main.HasShop = true;
        appVar.main.ShopID = "2";
//-------------------------------------------- TopicList and ZoneList
        if (Topics.size() < 1) {
            TopicIDs.add("0");
            Topics.add("همه");
        }

        GridView gridview1 = (GridView) rootView.findViewById(R.id.gridView1);
        imageAdapter = new ImageAdapter(getActivity());
        gridview1.setAdapter(imageAdapter);

        GridView gridview2 = (GridView) rootView.findViewById(R.id.gridView2);
        gridview2.setAdapter(imageAdapter);

        GridView gridview3 = (GridView) rootView.findViewById(R.id.gridView3);
        gridview3.setAdapter(imageAdapter);

        gridview1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String SelectID = ID.get(position);
                Intent inte = new Intent(getActivity(), PageIndicator.class);
                inte.putExtra("SID", SelectID);
                //inte.putExtra("PID", SelectID);
                startActivity(inte);
            }
        });

/*****************************************************  Tabs Host *******/
        final TabHost tabs = (TabHost) rootView.findViewById(R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec tab1 = tabs.newTabSpec("tag 1");
        tab1.setIndicator("جدیدترین ها");
        tab1.setContent(R.id.tab1);
        tabs.addTab(tab1);

        TabHost.TabSpec tab2 = tabs.newTabSpec("tag 2");
        tab2.setIndicator("حراجی ها");
        tab2.setContent(R.id.tab2);
        tabs.addTab(tab2);

        TabHost.TabSpec tab3 = tabs.newTabSpec("tag 3");
        tab3.setIndicator("محبوب ترین");
        tab3.setContent(R.id.tab3);
        tabs.addTab(tab3);


        tabs.getTabWidget().getChildAt(tabs.getCurrentTab())
                .setBackgroundResource(R.color.grl); // selected

//for(int i=0;i<tabs.getTabWidget().getChildCount();i++) 
//{
//    TextView tv = (TextView) tabs.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
//    tv.setTextColor(color.white);
//} 

        tabs.setOnTabChangedListener(new OnTabChangeListener() {

            public void onTabChanged(String arg0) {
                for (int i = 0; i < tabs.getTabWidget().getChildCount(); i++) {
                    tabs.getTabWidget().getChildAt(i)
                            .setBackgroundResource(R.color.white); // unselected
                }
                tabs.getTabWidget().getChildAt(tabs.getCurrentTab())
                        .setBackgroundResource(R.color.grl); // selected

                switch (tabs.getCurrentTab()) {
                    case 0:
                        new LongOperation().execute("");
                        break;
                    case 1:
                        new LongOperation().execute("Discount");
                        break;
                    case 2:
                        new LongOperation().execute("MaxVisit");
                        break;
                }
                imageAdapter.notifyDataSetChanged();

            }
        });

//---------------------- Start
        btnCat = (Button) rootView.findViewById(R.id.Subject);
        btnCat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(getActivity());
            }
        });


        final CallSoap cs = new CallSoap();

        Button Sort = (Button) rootView.findViewById(R.id.btnsort);
        Sort.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Sort(getActivity());
            }
        });
        new LongOperation().execute("");

//---------------------- Start
        btnCat = (Button) rootView.findViewById(R.id.Subject);
        btnCat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(getActivity());
            }
        });

        Button filter = (Button) rootView.findViewById(R.id.btnfilter);
        filter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Filter(getActivity());
            }
        });
        new LongOperation().execute("");

        return rootView;
    }

    //--------------------------------------------------------------------------------
    private class LongOperation extends AsyncTask<String, Integer, Boolean> {
        String res;

        @Override
        protected void onPostExecute(Boolean result) {
            ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.progressBar1);
            pb.setVisibility(View.INVISIBLE);
            imageAdapter.notifyDataSetChanged();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.progressBar1);
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

            StoreList(params[0]);
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
            //dis.setText(Discout.get(position) + "%" + " تخفیف");
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
    public boolean StoreList(String Sort) {
        try {
            CallSoap cs = new CallSoap();
            String result = "";
            if (Sort.length() > 0)
                result = cs.ResiveList("Products?Sort=" + Sort);
            else
                result = cs.ResiveList("Products");
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

    //----------------------------------------------------------------------
    public boolean Products() {
        try {
            CallSoap cs = new CallSoap();
            String result = cs.ResiveListSync("AllProducts?topicID=" + cid + "&Sort=" + sort+ "&MinP=" + min + "&MaxP=" + max);
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
                Toast.makeText(getActivity(), "هیچ کالایی در این گروه موجود نمی باشد",
                        Toast.LENGTH_LONG).show();

            imageAdapter.notifyDataSetChanged();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //----------------------------------------------------------------------
    private void Sort(final Activity context) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
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
                        sort = "MinPrice";
                        break;
                    case 2: // Max Price
                        sort = "MaxPrice";
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
                    Products();
                else
                    StoreList(sort);
                imageAdapter.notifyDataSetChanged();
                popup.dismiss();
            }
        });

    }

    ////------------------------------------------------------------
    private void Filter(final Activity context) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pfilter);
        dialog.show();

        Button Filter = (Button) dialog.findViewById(R.id.Filter);
        Filter.setOnClickListener(new OnClickListener() {
              @Override
              public void onClick(View v) {
                  EditText Min = (EditText) dialog.findViewById(R.id.min);min=Min.getText().toString();
                  EditText Max = (EditText) dialog.findViewById(R.id.max);max=Max.getText().toString();
                  Products();
                  imageAdapter.notifyDataSetChanged();
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

    //---------------------------------------------------  Topics List
    private void showPopup(final Activity context) {

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int popupWidth = display.getWidth();//-(int)(display.getWidth()*0.2);
        int popupHeight = display.getHeight();//-(int)(display.getWidth()*0.6);

        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup, viewGroup);

        final ListView lv = (ListView) layout.findViewById(R.id.list_topic);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
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
                Toast.makeText(getActivity(), item,
                        Toast.LENGTH_LONG).show();
                if (position == 0) {
                    new LongOperation().execute("");
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
//					lv.setVisibility(0);
                        subList(getActivity());
                    }
                    popup.dismiss();
                }
            }
        });

    }

    //----------------------------------------------------- SubTopics List
    private void subList(final Activity context) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int popupWidth = display.getWidth();//-100;
        int popupHeight = display.getHeight();//-200;

        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup, viewGroup);

        final ListView lv = (ListView) layout.findViewById(R.id.list_topic);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
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
                btnCat.setText(SubTopics.get(position));
                cid=SubTopicIDs.get(position);
                if (cid.length() > 0) {
                    ID.clear();
                    Name.clear();
                    Price.clear();
                    Image.clear();
                    Products();
                    imageAdapter.notifyDataSetChanged();
                }
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

                Intent i = new Intent(getActivity(), ShowSearch.class);
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

    //------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Search(getActivity());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//-------- End Activity
}




