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
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
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


public class OneFragment extends Fragment{


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
    View rootView;

    public OneFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_one, container, false);

//-------------------------------------------- TopicList and ZoneList
        if (Topics.size() < 1) {
            TopicIDs.add("0");
            Topics.add("همه");
            ZoneIDs.add("0");
            Zones.add("همه");

        }
//----------------------------------------------------
        b1 = (Button) rootView.findViewById(R.id.Subject);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(getActivity(), p);
            }
        });

//StoreList(TopicID,ZoneID,FindStr);
        new LongOperation().execute("");
        gridview = (GridView) rootView.findViewById(R.id.gridView1);
        imAdapter = new ImageAdapter(getActivity());
        gridview.setAdapter(imAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String sid = ShopsID.get(position);
                String Name = Shops.get(position);
                Intent inte = new Intent(getActivity(), ShowProducts.class);
                inte.putExtra("SID", sid);
                inte.putExtra("Name", Name);
                startActivity(inte);
            }
        });


    return rootView;
}

    //-------------------------------------------------------------------------
    private class LongOperation extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPostExecute(Boolean result) {
            ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.progressBar1);
            pb.setVisibility(View.INVISIBLE);
            imAdapter.notifyDataSetChanged();
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

    //---------------------------------------------------  Topics List
    private void showPopup(final Activity context, Point p) {

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

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String item = TopicIDs.get(position);
                Toast.makeText(getActivity(), item,
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

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int popupWidth = display.getWidth() - 100;
        int popupHeight = display.getHeight() - 200;

        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup, viewGroup);

        final ListView lv = (ListView) layout.findViewById(R.id.list_topic);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
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

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        dialog.setContentView(R.layout.search);
        dialog.show();


        final EditText Shop = (EditText) dialog.findViewById(R.id.txtShop);
        final EditText Product = (EditText) dialog.findViewById(R.id.txtProduct);
        final EditText Detail = (EditText) dialog.findViewById(R.id.txtDetail);
        Button Search = (Button) dialog.findViewById(R.id.btnSearch);
        Search.setOnClickListener(new View.OnClickListener() {
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



}