package com.example.respons;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

//import android.widget.AdapterView.OnItemLongClickListener;


public class SubCateMng extends AppCompatActivity {

    public ArrayList<String> SubCategoryID = new ArrayList<String>();
    public ArrayList<String> SubCategory = new ArrayList<String>();

    public String cid, ind, mode = "";
    Button btnNew;
    TextView editNew;
    ArrayAdapter<String> adapter;
    int pos;

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subcat_mng);
        /*************************************************** Set Custom ActionBar *****/
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar2);
        View mCustomView = getSupportActionBar().getCustomView();
        TextView title1 = (TextView) mCustomView.findViewById(R.id.title);
        title1.setText("گروه بندی سطح دو");
        //-----------------------------------------

        final CallSoap cs = new CallSoap();
        String title = "";
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            cid = extra.getString("CID");
            title = extra.getString("TITLE");
        } else cid = "0";

//	editNew=(TextView) findViewById(R.id.editNew);
//	editNew.setEnabled(false);


        StoreList();
        final TextView tv = (TextView) findViewById(R.id.title);
        tv.setText(title);
        final ListView lv = (ListView) findViewById(R.id.list_topic);
        adapter = new ArrayAdapter<String>(this,
                R.layout.listview_item_row, SubCategory);
        lv.setAdapter(adapter);

        FloatingActionButton btnNew=(FloatingActionButton) findViewById(R.id.fab);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
	        Intent i = new Intent(SubCateMng.this, NewCate.class);
            i.putExtra("mode", "add");
			i.putExtra("PID", cid);
	        startActivity(i);
            }
        });

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ind = SubCategoryID.get(position);
                pos = position;
                SelectCommand(position);

            }
        });
    }


    private void SelectCommand(final int pos) {
        final CharSequence[] options = {"ویرایش", "حـذف", "انصراف"};
        AlertDialog.Builder builder = new AlertDialog.Builder(SubCateMng.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, int item) {
                if (options[item].equals("ویرایش")) {
                    String id = SubCategoryID.get(pos);
                    Intent inte = new Intent(SubCateMng.this, NewCate.class);
                    inte.putExtra("TITLE", SubCategory.get(pos));
                    inte.putExtra("CID", id);
                    inte.putExtra("PID", cid);
                    inte.putExtra("mode", "edit");
                    startActivity(inte);
                } else if (options[item].equals("حـذف")) {

                    new AsyncTask<String, Integer, Boolean>() {
                        final String res = "";

                        @Override
                        protected Boolean doInBackground(String... params) {
                            if (params == null) {
                                return false;
                            }
                            try {
                                CallSoap cs = new CallSoap();
                                String res = cs.ResiveList("DelCategory?ID=" + ind);
                                if (!res.equals("False")) {
                                    SubCategory.remove(pos);
                                    SubCategoryID.remove(pos);
                                } else
                                    Toast.makeText(SubCateMng.this, "این دسته شامل کالا است و قابل حذف نمی باشد",
                                            Toast.LENGTH_LONG).show();
                            } catch (Exception e) {

                                dialog.dismiss();
                                return false;
                            }

                            return true;
                        }

                        protected void onPostExecute(Boolean result) {
                            adapter.notifyDataSetChanged();
                        }
                    }.execute();

                } else if (options[item].equals("انصراف")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void StoreList() {
        SubCategory.clear();
        SubCategoryID.clear();
        for (int i = 0; i < StoreManagment.SubCategoryID.size(); i++)
            if (StoreManagment.SubCategoryPID.get(i).equals(cid)) {
                SubCategory.add(StoreManagment.SubCategory.get(i));
                SubCategoryID.add(StoreManagment.SubCategoryID.get(i));
            }
    }

    public boolean StoreList_OLD() {
        // try
        {
            CallSoap cs = new CallSoap();
            String res = cs.ResiveList("SubCategoryList?CID=" + cid);
            String[] Rows = res.split(":");
            for (int i = 0; i < Rows.length; i++) {
                String[] Field = Rows[i].split(",");
                SubCategoryID.add(Field[0]);
                SubCategory.add(Field[1]);
            }
            return true;
        }
        //catch(Exception e)
        //{
        //	return false;
        //}

    }


//-------- End Activity
}




