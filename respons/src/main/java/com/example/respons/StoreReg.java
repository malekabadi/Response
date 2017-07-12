package com.example.respons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;


public class StoreReg extends AppCompatActivity {

    ImageView iv;
    public ArrayList<String> Topics = new ArrayList<String>();
    public ArrayList<String> TopicIDs = new ArrayList<String>();
    ArrayList<String> _SubTopics = new ArrayList<String>();
    ArrayList<String> _SubTopicIDs = new ArrayList<String>();
    //	List<String> CityIDs = new ArrayList<String>();
//	List<String> Cities = new ArrayList<String>();
    List<String> ZoneIDs = new ArrayList<String>();
    List<String> Zonez = new ArrayList<String>();
    Spinner txtCity, txtZone;
    String EditType = "";
    String picturePath;
    String TopicID = "";
    Button btnObj;
    ArrayAdapter<String> dataAdapter, dataAdapter1;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_reg);
        sp = getSharedPreferences("share", MODE_PRIVATE);
        /*************************************************** Set Custom ActionBar *****/
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar2);
        View mCustomView = getSupportActionBar().getCustomView();
        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        title.setText("ایجاد فروشگاه");
        //-----------------------------------------

        Bundle extra = getIntent().getExtras();
        if (extra != null)
            EditType = extra.getString("Edit");
        else EditType = "Add";

        btnObj = (Button) findViewById(R.id.btnObj);
        btnObj.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showPopup(StoreReg.this);
            }
        });
        txtCity = (Spinner) findViewById(R.id.txtCity);
        txtZone = (Spinner) findViewById(R.id.txtZone);
        new LongOperation().execute("");

//		dataAdapter = new ArrayAdapter<String>(this,R.layout.listview_item_row, Cities);		
//		txtCity.setAdapter(dataAdapter);

        dataAdapter1 = new ArrayAdapter<String>(this, R.layout.listview_item_row, Zonez);
        txtZone.setAdapter(dataAdapter1);
        //-----------------------------------------------
        iv = (ImageView) findViewById(R.id.StoreImage);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
/*	              Intent intent = new Intent(
                          Intent.ACTION_PICK,
                          android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                  intent.setType("image/*");
                  startActivityForResult(intent,1);2
                          //Intent.createChooser(intent, "Select File"),SELECT_FILE);
				*/
            }
        });

        Button reg = (Button) findViewById(R.id.btnReg);        // ذخیره اطلاعات
        reg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                boolean reg = true;
                if (TopicID.length() < 1) {
                    btnObj.setError("لطفا دسته فروشگاه را تعیین نمایید");
                    reg = false;
                }
                EditText shop = (EditText) findViewById(R.id.txtShop);
                if (shop.getText().toString().equals("")) {
                    shop.setError("لطفا نام فروشگاه را وارد نمایید");
                    reg = false;
                }
//				EditText name=(EditText) findViewById(R.id.txtName);
//				if (name.getText().toString().equals(""))
//				{
//					name.setError("لطفا نام صاحب فروشگاه را وارد نمایید");reg=false;
//				}
                EditText desc = (EditText) findViewById(R.id.txtDesc);
                if (desc.getText().toString().equals("")) {
                    desc.setError("لطفا توضیحات را وارد نمایید");
                    reg = false;
                }
                EditText addr = (EditText) findViewById(R.id.txtAddr);
                if (addr.getText().toString().equals("")) {
                    addr.setError("لطفا آدرس فروشگاه را وارد نمایید");
                    reg = false;
                }
                EditText tel = (EditText) findViewById(R.id.txtTel);
                if (tel.getText().toString().length() < 1) {
                    tel.setError("لطفا شماره تلفن را وارد نمایید");
                    reg = false;
                }
                EditText mobile = (EditText) findViewById(R.id.txtMobile);
                if (mobile.getText().toString().length() < 1) {
                    mobile.setError("لطفا شماره موبایل را وارد نمایید");
                    reg = false;
                }
                EditText Namad = (EditText) findViewById(R.id.Namad);
                if (reg) {
                    CallSoap cs = new CallSoap();
                    String url = "title=" + shop.getText();
                    url += "&name=" + "";//name.getText();
                    url += "&Uniqe=" + Namad.getText();;
                    url += "&topicID=" + TopicID;
                    url += "&onwer=" + appVar.main.UserID;
                    url += "&desc=" + desc.getText();
                    url += "&tel=" + tel.getText();
                    url += "&mobile=" + mobile.getText();
                    url += "&addr=" + addr.getText();
                    url += "&image=";
                    url += "&zoneID=" + ZoneIDs.get((int) txtZone.getSelectedItemId());
                    String res = "";
                    if (EditType.equals("True")) {
                        url = "?id=" + appVar.main.ShopID + "&" + url;
                        //					res=cs.ResiveList("EditShop"+url);
                        new SaveData().execute("EditShop" + url);
                    } else {
                        url = "?" + url;
                        //					res=cs.ResiveList("AddShop"+url);
                        new SaveData().execute("AddShop" + url);
                        //					appVar.main.HasShop=true;
                        //					appVar.main.ShopID=res;
                    }
                    //				if (!res.equals("false"))
                    //				{
                    //					Toast.makeText(StoreReg.this, res,
                    //							   Toast.LENGTH_LONG).show();
                    //	                res=res.replaceAll("\"", "");
                    //	                res=res.replaceAll("\n", "");
                    //					res="Shop"+res+".jpg";
                    //	                try {
                    //	               	    // Set your file path here
                    //	               	    //FileInputStream fstrm = new FileInputStream(Environment.getExternalStorageDirectory().toString()+"/New Folder/i7.png");
                    //	               	    FileInputStream fstrm = new FileInputStream(picturePath);
                    //
                    //	               	    // Set your server page url (and the file title/description)
                    //	               	    FileUpload hfu = new FileUpload(getString(R.string.WServer)+"/fileup.aspx", res,"my file description");
                    //
                    //	               	    hfu.Send_Now(fstrm);
                    //
                    //	               	  } catch (FileNotFoundException e) {
                    //	               	    // Error: File not found
                    //	               	  }
                    //				}
                    //				finish();
                }
            }
        });


    }

    //-------------------------------------------------------------------------
    private class LongOperation extends AsyncTask<String, Integer, Boolean> {
        String res = "";
        ProgressDialog Asycdialog = new ProgressDialog(StoreReg.this);

        @Override
        protected void onPostExecute(Boolean result) {
//		dataAdapter.notifyDataSetChanged();
            dataAdapter1.notifyDataSetChanged();

            EditText Shop = (EditText) findViewById(R.id.txtShop);
//		EditText name=(EditText) findViewById(R.id.txtName);
            EditText desc = (EditText) findViewById(R.id.txtDesc);
            EditText tel = (EditText) findViewById(R.id.txtTel);
            EditText mobile = (EditText) findViewById(R.id.txtMobile);
            EditText addr = (EditText) findViewById(R.id.txtAddr);
            ImageView Image = (ImageView) findViewById(R.id.StoreImage);
//		TextView city=(TextView) findViewById(R.id.txtCity);
//		TextView zone=(TextView) findViewById(R.id.txtZone);

            if (EditType.equals("True")) {
                String[] Field = res.split(",");
                btnObj.setText(Field[0]);
                Shop.setText(Field[1]);
//			name.setText(Field[2]);
                desc.setText(Field[3]);
                tel.setText(Field[4]);
                mobile.setText(Field[5]);
                addr.setText(Field[6]);
//			txtCity.setSelection(Cities.indexOf(Field[7]));
                txtZone.setSelection(Zonez.indexOf(Field[8]));
                Context Ct = getApplicationContext();
                Picasso.with(Ct) //
                        .load(getString(R.string.WServer) + "/images/" + Field[9]) //
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .error(R.drawable.i2) //
                        .fit() //
                        .tag(Ct) //
                        .into(Image);
            }
            super.onPostExecute(result);
            Asycdialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            if (! CallSoap.isConnectionAvailable(StoreReg.this))
            {
                Intent inte = new Intent(StoreReg.this, NoNet.class);
                startActivity(inte);
            }
            Asycdialog.setMessage("در حال خواندن اطلاعات ");
            Asycdialog.show();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
//		CityList();
            ZoneList("122");
            if (EditType.equals("True")) {
                res = new CallSoap().ResiveList("GetShop?ID=" + appVar.main.ShopID);
            }

            return null;
        }
    }

    //---------------------------------------------------------------------------------
    private void selectImage() {
        final CharSequence[] options = {"گرفتن عکس با دوربین", "انتخاب از گالری", "انصراف"};
        AlertDialog.Builder builder = new AlertDialog.Builder(StoreReg.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("گرفتن عکس با دوربین")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("انتخاب از گالری")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("انصراف")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //------------------------------------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }

                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    iv.setImageBitmap(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                c.close();
                try {
                    final InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                    iv.setImageBitmap(BitmapFactory.decodeStream(imageStream));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

//                 Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
//                 Log.w("path of image:", picturePath+"");
//                 iv.setImageBitmap(thumbnail);
            }
        }
    }

    /*--------------------------------------------------------------------------------------
        public  boolean CityList()
        {
            try
            {
                CallSoap cs=new CallSoap();
                String res=cs.ResiveList("CityList");
                String[] Rows = res.split(":");
                for (int i = 1; i < Rows.length-1; i++)
                {
                    String[] Field = Rows[i].split(",");
                    CityIDs.add(Field[0]);
                    Cities.add(Field[1]);
                }
                return true;
            }
            catch(Exception e)
            {
                return false;
            }
        }   */
    //--------------------------------------------------------------------------------------
    public boolean ZoneList(String CityID) {
        try {
            CallSoap cs = new CallSoap();
            String res = cs.ResiveList("ZoneList?CityID=" + CityID);
            String[] Rows = res.split(":");
            for (int i = 1; i < Rows.length - 1; i++) {
                String[] Field = Rows[i].split(",");
                ZoneIDs.add(Field[0]);
                Zonez.add(Field[1]);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //---------------------------------------------------  Topics List
    private void showPopup(final Activity context) {

        Display display = getWindowManager().getDefaultDisplay();
        int popupWidth = display.getWidth();//-(int)(display.getWidth()*0.2);
        int popupHeight = display.getHeight();//-(int)(display.getWidth()*0.6);

        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup, viewGroup);
        Topics.clear();
        TopicIDs.clear();
        for (int i = 1; i < Shops.Topics.size(); i++) {
            Topics.add(Shops.Topics.get(i));
            TopicIDs.add(Shops.TopicIDs.get(i));
        }
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

//    		String item = TopicIDs.get(position);
//    		Toast.makeText(StoreReg.this, item,
//    				   Toast.LENGTH_LONG).show();
//    		btnObj.setText(Shops.SubTopics.get(position));
//    		TopicID= Shops.SubTopicIDs.get(position);
//			lv.setVisibility(View.GONE);
//			subList(StoreReg.this);
                popup.dismiss();
                String item = TopicIDs.get(position);
                Toast.makeText(StoreReg.this, item,
                        Toast.LENGTH_LONG).show();
                if (position == 0) {
                    new LongOperation().execute("");
                    popup.dismiss();
                } else {
                    _SubTopicIDs.clear();
                    _SubTopics.clear();
                    for (int i = 0; i < ShowAllProducts.SubTopics.size(); i++) {
                        if (ShowAllProducts.SubParentID.get(i).equals(item)) {
                            _SubTopics.add(ShowAllProducts.SubTopics.get(i));
                            _SubTopicIDs.add(ShowAllProducts.SubTopicIDs.get(i));
                        }
                    }
                    if (_SubTopics.size() > 0) {
//					lv.setVisibility(0);
                        subList(StoreReg.this);
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
                btnObj.setText(_SubTopics.get(position));
                TopicID = _SubTopicIDs.get(position);
                popup.dismiss();
            }
        });
    }

    //-----------------------------------------------------------------------------
    public class SaveData extends AsyncTask<String, Integer, String> {

        ProgressDialog Asycdialog = new ProgressDialog(StoreReg.this);

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            Asycdialog.dismiss();
            NewActivity.navigationView.getMenu().findItem(R.id.nav_shop).setTitle("فروشگاه من");
            NewActivity.navigationView.getMenu().findItem(R.id.nav_shop).setEnabled(true);
            String res = result;
            finish();
        }

        @Override
        protected void onPreExecute() {
            Asycdialog.setMessage("ذخیره اطلاعات ");
            Asycdialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String res;
            CallSoap cs = new CallSoap();
            res = cs.ResiveList(params[0]);

            appVar.main.HasShop = true;
            appVar.main.ShopID = res;
            if (!res.equals("false")) {
//				Toast.makeText(StoreReg.this, res,
//						Toast.LENGTH_LONG).show();
                appVar.main.HasShop = true;
                res = res.replaceAll("\"", "");
                res = res.replaceAll("\n", "");
                res = "Shop" + res + ".jpg";
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("HasShop", appVar.main.HasShop);
                editor.putString("ShopID", appVar.main.ShopID);
                editor.commit();

                if (picturePath != null)
                    try {
                        Helper.verifyStoragePermissions(StoreReg.this);
                        // Set your file path here
                        //FileInputStream fstrm = new FileInputStream(Environment.getExternalStorageDirectory().toString()+"/New Folder/i7.png");
                        FileInputStream fstrm = new FileInputStream(picturePath);

                        // Set your server page url (and the file title/description)
                        FileUpload hfu = new FileUpload(getString(R.string.WServer) + "/fileup.aspx", res, "my file description");

                        hfu.Send_Now(fstrm);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Log.w("level1", e.getMessage());
                    }
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

//-----------------------    
}