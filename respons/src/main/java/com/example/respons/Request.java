package com.example.respons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.respons.StoreReg.SaveData;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class Request extends AppCompatActivity {
	
	ImageView iv;
	public List<String> objs=new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.request);
		/*************************************************** Set Custom ActionBar *****/
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar2);
        View mCustomView = getSupportActionBar().getCustomView();
        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        title.setText("ثبت درخواست");
		//-----------------------------------------
		
		final Spinner obj=(Spinner) findViewById(R.id.btnObj);
		final EditText Title=(EditText) findViewById(R.id.txtTitle);
		final EditText Desc=(EditText) findViewById(R.id.txtDesc);
//		final EditText Pari=(EditText) findViewById(R.id.txtPari);
		objs.add("پشتیبانی");
		objs.add("فنی");
		objs.add("مالی");
		final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				R.layout.listview_item_row, objs);		
		//dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		obj.setAdapter(dataAdapter);
		
//----------------------------------------------------------------------------------
		iv = (ImageView) findViewById(R.id.RImage); 
		iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectImage();
			}
		});

		Button reg= (Button)findViewById(R.id.btnReg);
		reg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CallSoap cs=new CallSoap();
				String url="?PID=1";
				url+="&userID="+appVar.main.UserID;
				url+="&title="+Title.getText();
				url+="&desc="+Desc.getText();
				//url+="&Priority="+Pari.getText();
				//String res=cs.ResiveList("AddRequest"+url);
				new SaveData().execute("AddRequest"+url);

				finish();
			}
		});
	}

	// -----------------------------------------------------------------------------
	public class SaveData extends AsyncTask<String, Integer, String> {

		ProgressDialog Asycdialog = new ProgressDialog(Request.this);

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
			Asycdialog.dismiss();
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
			Toast.makeText(Request.this, res, Toast.LENGTH_LONG).show();
			res = res.replaceAll("\"", "");
			res = res.replaceAll("\n", "");
			res = "Request" + res + ".jpg";
			try {
				// Set your file path here
				FileInputStream fstrm = new FileInputStream(
						Environment.getExternalStorageDirectory().toString() + "/New Folder/i7.png");

				// Set your server page url (and the file title/description)
				FileUpload hfu = new FileUpload(R.string.WServer + "/fileup.aspx", res, "my file description");

				hfu.Send_Now(fstrm);

			} catch (FileNotFoundException e) {
				// Error: File not found
			}

			try {
				Thread.sleep(3000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return res;
		}

	}

	//---------------------------------------------------------------------------------	
    private void selectImage() {
        final CharSequence[] options = { "گرفتن عکس با دوربین", "انتخاب از گالری","انصراف" };
        AlertDialog.Builder builder = new AlertDialog.Builder(Request.this);
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

//-------------------------------------------------------------------------------------
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
                 String[] filePath = { MediaStore.Images.Media.DATA };
                 Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                 c.moveToFirst();
                 int columnIndex = c.getColumnIndex(filePath[0]);
                 String picturePath = c.getString(columnIndex);
                 c.close();
                 Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                 //Log.w("path of image from gallery......******************.........", picturePath+"");
                 iv.setImageBitmap(thumbnail);
             }
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

//---------------------------------------------------------------------------    
}