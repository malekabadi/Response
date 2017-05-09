package com.example.respons;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
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

//import android.widget.AdapterView.OnItemClickListener;

public class ProductMng extends AppCompatActivity {

    public ArrayList<String> ID = new ArrayList<String>();
    public ArrayList<String> Name = new ArrayList<String>();
    public ArrayList<String> Price = new ArrayList<String>();
    public ArrayList<String> Image = new ArrayList<String>();
    public ArrayList<String> Desc = new ArrayList<String>();

    public ArrayList<String> CategoryID = new ArrayList<String>();
    public ArrayList<String> SubCategoryID = new ArrayList<String>();
    //public ArrayList<String> Category=new ArrayList<String>();
    public ArrayList<String> SubCategory = new ArrayList<String>();

    public String[] Category;
    //public String[] SubCategory;
    public String sid, cid = "";
    Button btnCat;
    int pos = -1;
    Point p;
    GridView gridview;
    ImageAdapter imageAdapter;
    Boolean SelectOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_mng);
        /*************************************************** Set Custom ActionBar *****/
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar2);
        View mCustomView = getSupportActionBar().getCustomView();
        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        title.setText("مدیریت کالاها");
        //-----------------------------------------

        //Bundle extra = getIntent().getExtras();
        //if (extra != null)
        //    sid= extra.getString("SID");
        //else sid="0";

        StoreManagment.CategoryID.add(0,"-1");
        StoreManagment.Category.add(0,"همه");
        final CallSoap cs = new CallSoap();
        FloatingActionButton btnNew = (FloatingActionButton) findViewById(R.id.fab);
        btnNew.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductMng.this, ProductReg.class);
                i.putExtra("CNAME", btnCat.getText());
                i.putExtra("CID", cid);
                startActivity(i);
            }
        });

//Button btnNew=(Button) findViewById(R.id.btnNew);
//btnNew.setOnClickListener(new OnClickListener() {
//	@Override
//	public void onClick(View v) {
//        Intent i = new Intent(ProductMng.this, ProductReg.class);
//        i.putExtra("CNAME", btnCat.getText());
//        i.putExtra("CID", cid);
//        startActivity(i);
//
//	}
//});

        btnCat = (Button) findViewById(R.id.btnCat);
        btnCat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//		String res=cs.ResiveList("CategoryList?shopI="+appVar.main.ShopID);
//		String[] Rows = res.split(":");
//		if (Rows.length > 1)
//		{
//			Category=new String[Rows.length];
//			for (int i = 0; i < Rows.length; i++) 
//			{
//				String[] Field = Rows[i].split(",");
//				if (Field.length > 1)
//				{
//					CategoryID.add(Field[0]);
//					Category[i]=Field[1];
//				}
//			}
                showPopup(ProductMng.this);
            }
        });
        new LongOperation().execute("");
        gridview = (GridView) findViewById(R.id.gridView1);
        imageAdapter = new ImageAdapter(this);
        gridview.setAdapter(imageAdapter);


        gridview.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                sid = ID.get(position);
                pos = position;
                SelectCommand();
                return true;
            }
        });

//gridview.setOnItemClickListener(new OnItemClickListener() {
//	@Override
//	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//        SelectOn=true;
//        pos=position;
//        imageAdapter.notifyDataSetChanged();
//	}
//});

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
            StoreList(appVar.main.ShopID);

            return null;
        }
    }

    //--------------------------------------------------------------------
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
            //CheckBox sele=(CheckBox) gridViewAndroid.findViewById(R.id.checkBox1);
            //if (SelectOn)sele.setVisibility(View.VISIBLE); else sele.setVisibility(View.INVISIBLE);
            //if (position == pos) sele.setChecked(true);
            ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.gridview_image);
            name.setText(Name.get(position));
            price.setText(Helper.GetMoney(Price.get(position)));
            desc.setText(Desc.get(position));
            Log.e("file:", getString(R.string.WServer) + "/images/" + Image.get(position));
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
            notifyDataSetChanged();

        }

    }

    private void SelectCommand() {
        final CharSequence[] options = {"ویرایش", "حـذف", "انصراف"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductMng.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("ویرایش")) {
                    Intent inte = new Intent(ProductMng.this, ProductReg.class);
                    inte.putExtra("ID", sid);
                    inte.putExtra("CID", cid);
                    startActivity(inte);
                } else if (options[item].equals("حـذف")) {
                    CallSoap cs = new CallSoap();
                    //String Res=cs.ResiveList("DelProduct?ID="+sid);
                    new DelData().execute("DelProduct?ID=" + sid);
                    //String Res="True";
//                if (Res.equals("True"))
//                {
//            		imageAdapter.remove(pos);
//            		gridview.setAdapter(imageAdapter);
//
//                }                	//imageAdapter.remove(pos);
                } else if (options[item].equals("انصراف")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

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
                if (Field.length > 4)
                    Desc.add(Field[4]);
                else
                    Desc.add(" ");
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean Products(String CategoryID) {
//    try
//	{
        CallSoap cs = new CallSoap();
        String result = "";
        try {
            result = cs.ResiveListSync("ProductsList?cid=" + CategoryID);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ID.clear();
        Name.clear();
        Price.clear();
        Image.clear();
        Desc.clear();
        String[] Rows = result.split(":");
        if (Rows.length > 0) {
            for (int i = 0; i < Rows.length; i++) {
                String[] Field = Rows[i].split(",");
                ID.add(Field[0]);
                Name.add(Field[1]);
                Price.add(Field[2]);
                Image.add(Field[3]);
                if (Field.length > 4)
                    Desc.add(Field[4]);
                else
                    Desc.add(" ");
            }
        } else
            Toast.makeText(ProductMng.this, "هیچ کالایی در این گروه موجود نمی باشد",
                    Toast.LENGTH_LONG).show();

        imageAdapter.notifyDataSetChanged();
        return true;
//	}
//	catch(Exception e)
//	{
//		return false;
//	}
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
                R.layout.listview_item_row, StoreManagment.Category);
        lv.setAdapter(adapter);

        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

//popup.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        popup.setBackgroundDrawable(new ColorDrawable(0xa0000000));
        popup.getContentView().setBackgroundResource(android.R.color.transparent);

//popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y +  OFFSET_Y);
        popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String item = StoreManagment.CategoryID.get(position);
                btnCat.setText(StoreManagment.Category.get(position));
                cid = item;
                if (item.equals("-1")) {
                    ID.clear();
                    Name.clear();
                    Price.clear();
                    Image.clear();
                    Desc.clear();
                    new LongOperation().execute("");
                    popup.dismiss();
                    return;
                }
//		CallSoap cs=new CallSoap();
//		String res=cs.ResiveList("SubCategoryList?CID="+item);
//		if (res.indexOf(":")>= 0)
//		{
//			String[] Rows = res.split(":");
//			SubCategory=new String[Rows.length];
//			for (int i = 0; i < Rows.length; i++) 
//			{
//				String[] Field = Rows[i].split(",");
//				SubCategoryID.add(Field[0]);
//				SubCategory[i]=Field[1];
//			}
//			lv.setVisibility(0);
//			subList(ProductMng.this);
//		} else 
//			Products(item);
                SubCategory.clear();
                SubCategoryID.clear();

                for (int i = 0; i < StoreManagment.SubCategoryID.size(); i++)
                    if (StoreManagment.SubCategoryPID.get(i).equals(item)) {
                        SubCategory.add(StoreManagment.SubCategory.get(i));
                        SubCategoryID.add(StoreManagment.SubCategoryID.get(i));
                    }
                subList(ProductMng.this);
                popup.dismiss();
            }
        });

/*Button close = (Button) layout.findViewById(R.id.close);
close.setOnClickListener(new OnClickListener() {

@Override
public void onClick(View v) {
popup.dismiss();
}
});*/
    }

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
                R.layout.listview_item_row, SubCategory);
        lv.setAdapter(adapter);

        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

//popup.setBackgroundDrawable(new BitmapDrawable());
        popup.setBackgroundDrawable(new ColorDrawable(0xa0000000));
        popup.getContentView().setBackgroundResource(android.R.color.transparent);

        popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                btnCat.setText(SubCategory.get(position));
                String CategorySelect = SubCategoryID.get(position);
                cid = CategorySelect;
                Products(CategorySelect);
                popup.dismiss();
            }
        });
    }

    public class DelData extends AsyncTask<String, Integer, String> {

        ProgressDialog Asycdialog = new ProgressDialog(ProductMng.this);

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            Asycdialog.dismiss();
            String res = result;
            if (res.equals("True")) {
                imageAdapter.remove(pos);
                gridview.setAdapter(imageAdapter);

            }
        }

        @Override
        protected void onPreExecute() {
            Asycdialog.setMessage("حذف اطلاعات ");
            Asycdialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String res;
            CallSoap cs = new CallSoap();
            res = cs.ResiveList(params[0]);
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
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




