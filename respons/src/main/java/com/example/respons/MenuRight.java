package com.example.respons;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MenuRight extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_shop) {
            if (appVar.main.HasShop) {
                Intent i = new Intent(MenuRight.this, StoreManagment.class);
                startActivity(i);
            } else {
                Intent i = new Intent(MenuRight.this, StoreReg.class);
                i.putExtra("Edit", "False");
                startActivity(i);
            }

        } else if (id == R.id.nav_info) {
            Intent i = new Intent(MenuRight.this, Account.class);
            startActivity(i);
        } else if (id == R.id.nav_password) {
            Intent i = new Intent(MenuRight.this, PasswordActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_rule) {
//            startNewActivity((MenuRight.this), "BBK");
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "http://www.techrepublic.com");
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this site!");
            startActivity(Intent.createChooser(intent, "Share"));

        } else if (id == R.id.nav_quit) {
            SharedPreferences sp = getSharedPreferences("share", MODE_PRIVATE);
            appVar.main.UserID = "";
            appVar.main.UserName = "0";
            appVar.main.HasShop = false;
            appVar.main.ShopID = "";
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("UserName", appVar.main.UserName);
            editor.putString("UserID", appVar.main.UserID);
            editor.putBoolean("HasShop", appVar.main.HasShop);
            editor.putString("ShopID", appVar.main.ShopID);
            editor.commit();
            Menu menuNav = NewActivity.navigationView.getMenu();
            menuNav.findItem(R.id.nav_password).setEnabled(false);
            menuNav.findItem(R.id.nav_cart).setEnabled(false);
            menuNav.findItem(R.id.nav_info).setEnabled(false);
            menuNav.findItem(R.id.nav_quit).setEnabled(false);
            menuNav.findItem(R.id.nav_shop).setEnabled(false);
            NewActivity.name.setText("ورود");
            NewActivity.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MenuRight.this, LoginActivity.class));
                }
            });
        } else if (id == R.id.nav_cart) {
            Intent i = new Intent(MenuRight.this, Cart.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(Gravity.RIGHT);
        return true;
    }

    public void startNewActivity(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + packageName));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

}
