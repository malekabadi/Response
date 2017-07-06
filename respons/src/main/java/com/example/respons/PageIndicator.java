package com.example.respons;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PageIndicator extends AppCompatActivity {

    public class ClsFactor {
        String Name;
        String TEL;
        String PostCode;
        String Adress;
        String UserID;
    }

    public class Orther {

        String proID;
        String Name;
        String Price;
        String Desc;
        String Count;
        String Discount;
        Bitmap image;
    }

    ArrayList<String> curImageList = new ArrayList<String>();

    public List<String> pty = new ArrayList<String>();
    public List<String> val = new ArrayList<String>();
    String id;
    int imagePosition = 0;
    TextView[] PagerIndicator = new TextView[7];
    ImageView curImage;
    CallSoap cs = new CallSoap();
    TAdapter adapter;
    ViewPager gallery_pager;
    String[] Field;
    Orther order = new Orther();
    public static ClsFactor HFactor;
    public static List<Orther> Factor = new ArrayList<Orther>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.indicator);

//	/*************************************************** Set Custom ActionBar *****/
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar2);
        View mCustomView = getSupportActionBar().getCustomView();
        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        title.setText("مشخصات کالا");
        //	ActionBar mActionBar = getActionBar();
        //	mActionBar.setDisplayShowHomeEnabled(false);
        //	mActionBar.setDisplayShowTitleEnabled(false);
        //
        //	LayoutInflater mInflater = LayoutInflater.from(this);
        //	View mCustomView = mInflater.inflate(R.layout.actionbar2, null);
        //
        //	TextView title = (TextView) mCustomView.findViewById(R.id.title);
        //
        //
        //	ImageView sabad=(ImageView) mCustomView.findViewById(R.id.Licon); sabad.setImageResource(R.drawable.cart32);
        //	sabad.setOnClickListener(new OnClickListener() {
        //		@Override
        //		public void onClick(View v) {
        //			Intent inte=new Intent(PageIndicator.this, Cart.class);
        //			startActivity(inte);
        //		}
        //	});
        //
        //	mActionBar.setCustomView(mCustomView);
        //	mActionBar.setDisplayShowCustomEnabled(true);
        //
        //-----------------------------------

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            id = extra.getString("SID");
        }
        //---------- Button Comment

//	title.setText(ShowProducts.ShpName);
        Button btnComm = (Button) findViewById(R.id.btnComm);
        btnComm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent inte = new Intent(PageIndicator.this, Comments.class);
                inte.putExtra("PID", id);
                startActivity(inte);
            }
        });

        new LongOperation().execute("");
        final TextView name = (TextView) findViewById(R.id.title);
        final TextView price = (TextView) findViewById(R.id.Price);
        final TextView desc = (TextView) findViewById(R.id.Descr);
//		String count=Field[3];
//		String call=Field[4];
//		String download=Field[5];
        //final String discount=Field[6];
//		String min=Field[7];
//		String forward=Field[8];
//		String weight=Field[9];


        gallery_pager = (ViewPager) findViewById(R.id.gallery_pager);
        ListView lv = (ListView) findViewById(R.id.listView);
        adapter = new TAdapter(this);
        lv.setAdapter(adapter);
        //Helper.getListViewSize(lv);

        PagerIndicator[0] = (TextView) findViewById(R.id.dot1);
        PagerIndicator[1] = (TextView) findViewById(R.id.dot2);
        PagerIndicator[2] = (TextView) findViewById(R.id.dot3);
        PagerIndicator[3] = (TextView) findViewById(R.id.dot4);
        PagerIndicator[4] = (TextView) findViewById(R.id.dot5);
        PagerIndicator[5] = (TextView) findViewById(R.id.dot6);
        PagerIndicator[6] = (TextView) findViewById(R.id.dot7);


        Button buy = (Button) findViewById(R.id.btnBuy);
        buy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                order.proID = id;
                order.Name = name.getText().toString();
                //order.Price=(String) price.getText();
                order.Price = Field[1];
                order.Desc = desc.getText().toString();
                order.Count = "1";
                order.image = getBitmapFromView(gallery_pager.getChildAt(0));
                order.Discount = Field[6];
                Factor.add(order);
                HFactor = new ClsFactor();
                Intent inte = new Intent(PageIndicator.this, Cart.class);
                startActivity(inte);
            }
        });

        //-----------------------------------------------------------------
    }

    //-----------------------------------------------------------------
    private class LongOperation extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPostExecute(Boolean result) {
            ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
            pb.setVisibility(View.INVISIBLE);

            TextView name = (TextView) findViewById(R.id.title);
            name.setText(Field[0]);
            TextView price = (TextView) findViewById(R.id.Price);
            price.setText(Helper.GetMoney(Field[1]));
            String numPrice = Field[1];
            TextView desc = (TextView) findViewById(R.id.Descr);
            desc.setText(Field[2]);
//    		String count=Field[3];
//    		String call=Field[4];
//    		String download=Field[5];
            String discount = Field[6];
//    		String min=Field[7];
//    		String forward=Field[8];
//    		String weight=Field[9];

            for (int i = 0; i < curImageList.size(); i++) {
                PagerIndicator[i].setVisibility(View.VISIBLE);
                PagerIndicator[i].setTextColor(0xffaaaaaa);
            }
            for (int i = curImageList.size(); i < 7; i++) {
                ViewGroup layout = (ViewGroup) PagerIndicator[i].getParent();
                if (null != layout)
                    layout.removeView(PagerIndicator[i]);
            }
            PagerIndicator[0].setTextColor(0xffff0000);

            final MyPagerAdapter ad = new MyPagerAdapter(PageIndicator.this.getSupportFragmentManager(),
                    curImageList, imagePosition);
            gallery_pager.setAdapter(ad);
            gallery_pager.setCurrentItem(imagePosition);
            gallery_pager.setOnPageChangeListener(new OnPageChangeListener() {

                @Override
                public void onPageScrollStateChanged(int arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onPageSelected(int index) {
                    // TODO Auto-generated method stub
                    for (int i = 0; i < curImageList.size(); i++) {
                        PagerIndicator[i].setVisibility(View.VISIBLE);
                        PagerIndicator[i].setTextColor(0xffaaaaaa);

                        if (i == index) {
                            PagerIndicator[i].setTextColor(0xffff0000);
                        }
                    }
                }

            });
            adapter.notifyDataSetChanged();
            ListView lv = (ListView) findViewById(R.id.listView);
            Helper.getListViewSize(lv);
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            if (! CallSoap.isConnectionAvailable(PageIndicator.this))
            {
                Intent inte = new Intent(PageIndicator.this, NoNet.class);
                startActivity(inte);
            }
            ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
            pb.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String res = cs.ResiveList("GetProduct?id=" + id);
            String[] Row = res.split(":");
            Field = Row[0].split(",");
            //curImageList = (ArrayList<String>) Arrays.asList(Row[1].split(","));
            if (Row.length > 1) {
                String[] Temp = Row[1].split(",");
                for (String s : Temp)
                    curImageList.add(s);
            }

            String res2 = cs.ResiveList("ProProperty?id=" + id);
            if (res2.length() > 1) {
                String[] Pro = res2.split(":");
                for (int i = 0; i < Pro.length; i++) {
                    String[] Fields = Pro[i].split(",");
                    if (Fields.length > 0) val.add(Fields[0]);
                    else val.add("");
                    if (Fields.length > 1) pty.add(Fields[1]);
                    else pty.add("");
                }
            }
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //-----------------------------------------------------------------
    public static Bitmap getBitmapFromView(View view) {
        view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
        return bitmap;
    }

    //---------------------------------------------------------------------
    @SuppressLint("ValidFragment")
    public class GalleryFragment extends Fragment {

        private Context context;
        private String imageUrl;

        public GalleryFragment(String imageurl) {
            this.imageUrl = imageurl;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            context = GalleryFragment.this.getActivity();

            ImageView image = new ImageView(context);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            image.setLayoutParams(params);
            image.setScaleType(ScaleType.FIT_CENTER);

            //TODO use the imageUrl to load and display the image;
            Picasso.with(context) //
                    .load(getString(R.string.WServer) + "/images/" + imageUrl) //
                    //.placeholder(R.drawable.i1) //
                    .error(R.drawable.i3) //
                    .fit() //
                    .tag(context) //
                    .into(image);


            LinearLayout layout = new LinearLayout(context);
            layout.setGravity(Gravity.CENTER);
            layout.addView(image);

            return layout;
        }
    }

    //------------------------------------------------------------------
    public class MyPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<String> imageList;
        private int imagePosition;

        public MyPagerAdapter(FragmentManager fragmentManager, ArrayList<String> imageList, int imagePosition) {
            super(fragmentManager);

            this.imageList = imageList;
            this.imagePosition = imagePosition;
        }

        @Override
        public Fragment getItem(int index) {
            return new GalleryFragment(imageList.get(index));
        }

        @Override
        public int getCount() {
            return imageList.size();
        }

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

        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = inflater.inflate(R.layout.listview_2item, null);
            TextView item = (TextView) convertView.findViewById(R.id.Item);
            TextView value = (TextView) convertView.findViewById(R.id.Value);
            value.setText(pty.get(position));
            item.setText(val.get(position));
            //value.setText("ظ…ظ‚ط¯ط§ط±");


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
}