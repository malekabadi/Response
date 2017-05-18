package com.example.respons;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.devsmart.android.ui.HorizontalListView;
import com.example.respons.StoreManagment.MyTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;


public class ProductReg extends AppCompatActivity {

	private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;
	public ArrayList<String> CategoryID=new ArrayList<String>();
    public ArrayList<String> SubCategoryID=new ArrayList<String>();
    public String[] Category;
    public ArrayList<String> SubCategory=new ArrayList<String>();
    public String sid;
    Button btnCat;

	public List<String> pty=new ArrayList<String>();
	public List<String> val=new ArrayList<String>();
    public ArrayList<ImageView> iv=new ArrayList<ImageView>();
    public ArrayList<String> OldFiles=new ArrayList<String>();
    public ArrayList<String> Dels=new ArrayList<String>();
    public ArrayList<String> Files=new ArrayList<String>();
    String[] Images;ImageView tempIm;
    HorizontalListView hListView;ListView listPty;
    HAdapter p;TAdapter tap;
	String id="",cid="-1";
	CallSoap cs=new CallSoap();
	public ArrayList<ImageView> ivs=new ArrayList<ImageView>();
	int index=0,neW=0,Old=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_reg);
		 //pty.add("Size");
		 //val.add("No value");
		/*************************************************** Set Custom ActionBar *****/
		getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.actionbar2);
		View mCustomView = getSupportActionBar().getCustomView();
		TextView title = (TextView) mCustomView.findViewById(R.id.title);
		title.setText("کالای جدید");
		ImageButton imb = (ImageButton) mCustomView.findViewById(R.id.imageButton);
		imb.setVisibility(View.VISIBLE);
		//-----------------------------------------
		
		hListView = (HorizontalListView) findViewById(R.id.hlistview);
		p=new HAdapter(getApplicationContext());
		hListView.setAdapter(p);
		 
		EditText txtPrice=(EditText)findViewById(R.id.txtPrice);
		txtPrice.addTextChangedListener(new txtWatcher(txtPrice));
		EditText txtDiscount=(EditText)findViewById(R.id.txtDiscount);
		txtDiscount.addTextChangedListener(new txtWatcher(txtDiscount));

		Button bo=(Button)findViewById(R.id.btnAx);
		bo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectImage();
			}
		});
		btnCat=(Button)findViewById(R.id.btnObj);
		btnCat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPopup(ProductReg.this);
			}
		});
		
		Bundle extra = getIntent().getExtras();
		if (extra != null) 
		{
			cid= extra.getString("CID");
			String cname= extra.getString("CNAME");
  			btnCat.setText(cname);
			id= extra.getString("ID");
			tempIm=new ImageView(this);
			new MyTask().execute();
//			if (id != null)
//			{
//				String res=cs.ResiveList("GetProduct?id="+id);
//				String[] Row = res.split(":");
//				String[] Field = Row[0].split(",");
//				Images = Row[1].split(",");
//				EditText name=(EditText) findViewById(R.id.txtName);name.setText(Field[0]);
//				EditText price=(EditText) findViewById(R.id.txtPrice);price.setText(Field[1]);
//				EditText desc=(EditText) findViewById(R.id.txtDesc);desc.setText(Field[2]);
//				EditText count=(EditText) findViewById(R.id.txtCount);count.setText(Field[3]);
//				CheckBox call=(CheckBox) findViewById(R.id.chkCall);call.setChecked(Boolean.valueOf(Field[4]));
//				CheckBox download=(CheckBox) findViewById(R.id.chkDownload);download.setChecked(Boolean.valueOf(Field[5]));
//				EditText discount=(EditText) findViewById(R.id.txtDiscount);discount.setText(Field[6]);
//				EditText min=(EditText) findViewById(R.id.txtMin);min.setText(Field[7]);
//				EditText forward=(EditText) findViewById(R.id.txtForward);forward.setText(Field[8]);
//				EditText weight=(EditText) findViewById(R.id.txtWeight);weight.setText(Field[9]);
//				final ImageView im1=(ImageView) findViewById(R.id.imageView1);
//				tempIm=new ImageView(this);
//				for(int i=0;i<Images.length;i++)
//				{
//					iv.add(tempIm);
//					OldFiles.add(Images[i]);
//					index++;
//				}
//				String res2=cs.ResiveList("ProProperty?id="+id);
//				String[] Pro=res2.split(":");
//				for(int i=0;i<Pro.length;i++)
//				{
//					String[] Fields=Pro[i].split(",");
//					if (Fields.length>1)
//					{
//						val.add(Fields[0]);
//						pty.add(Fields[1]);
//					}
//				}
//			} else {	
//				id="-1";
//				String res2=cs.ResiveList("CateProperty?id="+"10022");
//				String[] Pro=res2.split(",");
//				for(int i=0;i<Pro.length;i++)
//				{
//					pty.add(Pro[i]);
//					val.add("");
//				}
//			}
		} else return;
		 listPty= (ListView) findViewById(R.id.listView);
		 tap=new TAdapter(this);
		 listPty.setAdapter(tap);
		 Helper.getListViewSize(listPty);
		
		//------------------------------- btnReg Click
		Button reg= (Button)findViewById(R.id.btnReg);reg.setVisibility(View.GONE);
		imb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean Ok=true;
				EditText name=(EditText) findViewById(R.id.txtName);
				if (name.getText().toString().equals(""))
					{	name.setError("لطفا نام کالا را وارد نمایید");Ok=false; }
				EditText price=(EditText) findViewById(R.id.txtPrice);
				if (price.getText().toString().equals(""))
				{	price.setError("لطفا مبلغ کالا را وارد نمایید");Ok=false; }
				if (cid.equals("-1"))
				{	btnCat.setError("لطفا گروه کالایی را تعیین نمایید");Ok=false; }
				EditText desc=(EditText) findViewById(R.id.txtDesc);
				EditText count=(EditText) findViewById(R.id.txtCount);
				CheckBox call=(CheckBox) findViewById(R.id.chkCall);
				CheckBox download=(CheckBox) findViewById(R.id.chkDownload);
				EditText discount=(EditText) findViewById(R.id.txtDiscount);
				EditText min=(EditText) findViewById(R.id.txtMin);
				EditText forward=(EditText) findViewById(R.id.txtForward);
				EditText weight=(EditText) findViewById(R.id.txtWeight);
				String url="";//?cid="+cid
				url+="&name="+name.getText();
				url+="&descr="+desc.getText();
				url+="&Price="+price.getText();
				url+="&discount="+discount.getText();
				url+="&count="+count.getText();
				url+="&min="+min.getText();
				url+="&weight="+weight.getText();
				url+="&forward="+forward.getText();
				url+="&download=";//+download.getText();
				url+="&call=";//+call.getText();
				url+="&ImgCnt="+(Files.size()-1);
				String py="";
				for(int i=0;i<pty.size();i++)
					py+=pty.get(i)+","+val.get(i)+":";
				url+="&Property="+py;
				//--------------------------------------------- Add Product
				if (Ok)
				if (Integer.parseInt(id) < 0 )
				{
					url="?cid=" + cid + url;
					new SaveData().execute("AddProduct"+url);
//					String res=cs.ResiveList("AddProduct"+url);
//					Toast.makeText(ProductReg.this, res,
//							   Toast.LENGTH_LONG).show();
//					String[] images=res.split(",");
//					for(int i=0;i<images.length;i++)
//					{
//						try {
//							FileInputStream fstrm = new FileInputStream(Files.get(i));
//							FileUpload hfu = new FileUpload(getString(R.string.WServer)+"/fileup.aspx", images[i],"description");
//							hfu.Send_Now(fstrm);
//	               	  	} catch (FileNotFoundException e) {
//	               	    // Error: File not found
//	               	  	}
//					}
				}
				else
				{	//------------------------------------------- Edit Product
					String s="";
					if (Dels.size()>0)
					{
						for(int i=0;i<Dels.size()-1;i++)
							s+=Dels.get(i)+",";
						s+=Dels.get(Dels.size()-1);
					}
					url="?id=" + id + url;
					url+="&DelFiles="+s;
					new SaveData().execute("EditProduct"+url);
//					String res=cs.ResiveList("EditProduct"+url);
//					Toast.makeText(ProductReg.this, res,
//							   Toast.LENGTH_LONG).show();
//					if (res.indexOf(",")>0)
//					{
//						String[] images=res.split(",");
//						for(int i=0;i<images.length;i++)
//						{
//							try {
//								FileInputStream fstrm = new FileInputStream(Files.get(i));
//								FileUpload hfu = new FileUpload(getString(R.string.WServer)+"/fileup.aspx", images[i],"description");
//								hfu.Send_Now(fstrm);
//		               	  	} catch (FileNotFoundException e) {
//		               	    // Error: File not found
//		               	  	}
//						}
//					}
				}
				//finish();
			}
		});
	}
	//-----------------------------------------------------------------------------
	class MyTask extends AsyncTask<Void, Void, Void> {

	        ProgressDialog Asycdialog = new ProgressDialog(ProductReg.this);
	        String[] Field;
	        @Override
	        protected void onPreExecute() {

	            super.onPreExecute();
	            Asycdialog.setMessage("بارگذاری مشخصه ها ");
	            Asycdialog.show();
	        }

	        @Override
	        protected Void doInBackground(Void... arg0) {

	            // do the task you want to do. This will be executed in background.
				if (id != null)
				{
		        	String res=cs.ResiveList("GetProduct?id="+id);
					String[] Row = res.split(":");
					Field = Row[0].split(",");
					if (Row.length>1)
					{
						Images = Row[1].split(",");
						for(int i=0;i<Images.length;i++)
						{
							iv.add(tempIm);
							OldFiles.add(Images[i]);
							index++;
						}
					}
					String res2=cs.ResiveList("ProProperty?id="+id);
					String[] Pro=res2.split(":");
					for(int i=0;i<Pro.length;i++)
					{
						String[] Fields=Pro[i].split(",");
						if (Fields.length>1)
						{
							val.add(Fields[0]);
							pty.add(Fields[1]);
						}
					}
				} else {	
					id="-1";
					String res2=cs.ResiveList("CateProperty?id="+cid);
					if (! res2.equals("No"))
					{
						String[] Pro=res2.split(",");
						for(int i=0;i<Pro.length;i++)
						{
							pty.add(Pro[i]);
							val.add("");
						}
					}
				}
	            return null;
	        }

	        @Override
	        protected void onPostExecute(Void result) {

	            super.onPostExecute(result);
				tap.notifyDataSetChanged();
				Helper.getListViewSize(listPty);
				if (! id.equals("-1"))
				{
		            EditText name=(EditText) findViewById(R.id.txtName);name.setText(Field[0]);
					EditText price=(EditText) findViewById(R.id.txtPrice);price.setText(Field[1]);
					EditText desc=(EditText) findViewById(R.id.txtDesc);desc.setText(Field[2]);
					EditText count=(EditText) findViewById(R.id.txtCount);count.setText(Field[3]);
					CheckBox call=(CheckBox) findViewById(R.id.chkCall);call.setChecked(Boolean.valueOf(Field[4]));
					CheckBox download=(CheckBox) findViewById(R.id.chkDownload);download.setChecked(Boolean.valueOf(Field[5]));
					EditText discount=(EditText) findViewById(R.id.txtDiscount);discount.setText(Field[6]);
					EditText min=(EditText) findViewById(R.id.txtMin);min.setText(Field[7]);
					EditText forward=(EditText) findViewById(R.id.txtForward);forward.setText(Field[8]);
					EditText weight=(EditText) findViewById(R.id.txtWeight);weight.setText(Field[9]);
				}
	            Asycdialog.dismiss();
	        }
	    }
	//--------------------------------------------------------------------------
    private void selectImage() {
        final CharSequence[] options = { "گرفتن عکس با دوربین", "انتخاب از گالری","انصراف" };
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductReg.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
 
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("گرفتن عکس با دوربین"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("انتخاب از گالری"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
				}
                else if (options[item].equals("انصراف")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
     }	
//----------------------------------------------------------------------------------
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
                    ImageView temp=new ImageView(this);iv.add(temp);
                    iv.get(index).setImageBitmap(bitmap);
                    index++;
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
                ImageView temp=new ImageView(this);iv.add(temp);
				Bitmap selectedImage=null;
				try {
					final Uri imageUri = data.getData();

                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(imageUri,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
					String picturePath = c.getString(columnIndex);
				Files.add(picturePath);

					final InputStream imageStream = getContentResolver().openInputStream(imageUri);
					selectedImage = BitmapFactory.decodeStream(imageStream);
					temp.setImageBitmap(selectedImage);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
                //iv.get(index).setImageBitmap(bitmap);
                iv.get(index).setImageBitmap(selectedImage);
                p.notifyDataSetChanged();
				hListView.invalidate();
                index++;
            }
        }
    }
//-------------------------------------------------------------------------------------
    private class HAdapter extends BaseAdapter {

        LayoutInflater inflater;

        public HAdapter(Context context) {
            inflater = LayoutInflater.from(context);

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return iv.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            HViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.listview_image, null);
                holder = new HViewHolder();
                convertView.setTag(holder);

            } else {
                holder = (HViewHolder) convertView.getTag();
            }
            holder.img = (ImageView) convertView.findViewById(R.id.ImageV);
            TextView txt = (TextView) convertView.findViewById(R.id.gridview_text1);
            txt.setText("حذف");
            txt.setOnClickListener(new OnClickListener() {
				
				@Override  // Delete image from List
				public void onClick(View v) {
					// TODO Auto-generated method stub
					remove(position);
					if (position<OldFiles.size())
					{
						Dels.add(OldFiles.get(position));
						OldFiles.remove(position);
					}
					else
						Files.remove(position-OldFiles.size());
					index--;
				}
			});
            
            if (iv.get(position).getDrawable() != null)
            	holder.img.setImageDrawable(iv.get(position).getDrawable());
            else
            if (Images[position]!= null)
            {
				Context mContext=getBaseContext();
				Picasso.with(mContext) //
	            .load("http://192.168.1.102/images/"+Images[position]) //
	            //.fit()
	            .into(holder.img);
            }
            
            return convertView;
        }
        public void remove(int position) {
        	iv.remove(position);
        	notifyDataSetChanged();
	    }

    }
//--------------------------------------------------------------------------------
    class HViewHolder {
        ImageView img;
    }  
//--------------------------------------------------------------------------
  	public class TAdapter extends BaseAdapter{
  		
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

  	        	convertView = inflater.inflate(R.layout.listview_wedit, null);
  	            TextView item = (TextView) convertView.findViewById(R.id.Item);
  	            EditText value = (EditText) convertView.findViewById(R.id.Value);
  	            value.setText(val.get(position));
  	            item.setText(pty.get(position));
  	            

  	        return convertView;
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

  	final ListView lv= (ListView) layout.findViewById(R.id.list_topic);
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
  	popup.showAtLocation(layout, Gravity.CENTER,0, 0);

  	lv.setOnItemClickListener(new OnItemClickListener() {

  		@Override
  		public void onItemClick(AdapterView<?> parent, View view, int position,
  	        long id) {

  			String item = StoreManagment.CategoryID.get(position);
  			//btnCat.setText(StoreManagment.Category.get(position));
  			//cid=item;
  			SubCategory.clear();SubCategoryID.clear();
  			for(int i=0 ; i<StoreManagment.SubCategoryID.size();i++)
  				if (StoreManagment.SubCategoryPID.get(i).equals(item))
  				{
  					SubCategory.add(StoreManagment.SubCategory.get(i));
  					SubCategoryID.add(StoreManagment.SubCategoryID.get(i));
  				}
  			subList(ProductReg.this);
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

  	final ListView lv= (ListView) layout.findViewById(R.id.list_topic);
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
  			String CategorySelect=SubCategoryID.get(position);
  			cid=CategorySelect;
  			new MyTask2().execute();
  			popup.dismiss();	
  		}
  	});
  	}    
	//-----------------------------------------------------------------------------
	class MyTask2 extends AsyncTask<Void, Void, Void> {

        ProgressDialog Asycdialog = new ProgressDialog(ProductReg.this);
        String[] Field;
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            Asycdialog.setMessage("بارگذاری مشخصه ها ");
            Asycdialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // do the task you want to do. This will be executed in background.
			String res2=cs.ResiveList("CateProperty?id="+cid);
			String[] Pro=res2.split(",");
			//Log.e("String : ", Pro.length+"");
			pty.clear();val.clear();
			for(int i=0;i<Pro.length;i++)
			{
				pty.add(Pro[i]);
				val.add("");
			}
			return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
  			tap.notifyDataSetChanged();
  			Helper.getListViewSize(listPty);
            Asycdialog.dismiss();
        }
    }
	public class SaveData extends AsyncTask<String, Integer, String> {
		
		ProgressDialog Asycdialog = new ProgressDialog(ProductReg.this);
	    @Override
	    protected void onPostExecute(String result) {
	    	
	    	super.onPostExecute(result);
	    	Asycdialog.dismiss();
	    	finish();
	    	String res=result;
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
	    	CallSoap cs=new CallSoap();
	    	res=cs.ResiveList(params[0]);
			if (res.indexOf(",")>0)
			{
				String[] images=res.split(",");
				for(int i=0;i<images.length;i++)
				{
					try {
						FileInputStream fstrm = new FileInputStream(Files.get(i));
						FileUpload hfu = new FileUpload(getString(R.string.WServer)+"/fileup.aspx", images[i],"description");
						hfu.Send_Now(fstrm);
               	  	} catch (FileNotFoundException e) {
               	    // Error: File not found
               	  	}
				}
			}
	    	
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
		getMenuInflater().inflate(R.menu.register, menu);
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

//------------ End Activity	
}

