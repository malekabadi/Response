package com.example.respons;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Category extends Fragment {

    public static ArrayList<String> Topics = new ArrayList<String>();
    public static ArrayList<String> TopicIDs = new ArrayList<String>();
    public static ArrayList<String> SubTopics = new ArrayList<String>();
    public static ArrayList<String> SubTopicIDs = new ArrayList<String>();
    public static ArrayList<String> SubParentID = new ArrayList<String>();
    ArrayList<String> _SubTopics = new ArrayList<String>();
    ArrayList<String> _SubTopicIDs = new ArrayList<String>();
    public ArrayList<String> Image = new ArrayList<String>();

    Point p;
    String TopicID = "", ZoneID = "", FindStr = "";
    Button b1, b2;
    ImageAdapter imAdapter;
    GridView gridview;
    View rootView;

    public Category() {
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
        rootView = inflater.inflate(R.layout.category, container, false);

        //StoreList(TopicID,ZoneID,FindStr);
        new LongOperation().execute("");
        gridview = (GridView) rootView.findViewById(R.id.gridView1);
        imAdapter = new ImageAdapter(getActivity());
        gridview.setAdapter(imAdapter);

        gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String sid = TopicIDs.get(position);
                String Name = Topics.get(position);
                Intent inte = new Intent(getActivity(), Cate_Product.class);
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
                    Image.add(Field[2]);
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
            return Topics.size();
        }

        public Object getItem(int position) {
            return Topics.get(position);
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
            gridViewAndroid = inflater.inflate(R.layout.gridview_cate, null);
            TextView text1 = (TextView) gridViewAndroid.findViewById(R.id.title);
            ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.image);
            text1.setText(Topics.get(position));
            int img = mContext.getResources().getIdentifier(Image.get(position),
                    "drawable", mContext.getPackageName());
            Drawable res = getResources().getDrawable(img);
            Picasso.with(mContext) //
                    .load(img) //
                    //.placeholder(R.drawable.i1) //
                    .error(R.drawable.i2) //
                    .fit() //
                    .tag(mContext) //
                    .into(imageViewAndroid);
            imageViewAndroid.setImageResource(img);
            return gridViewAndroid;
        }

        public void removeall() {
            Topics.clear();
            TopicIDs.clear();
            Image.clear();
        }
    }
//-------- End Activity
}




