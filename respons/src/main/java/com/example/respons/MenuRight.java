package com.example.respons;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
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
        	Intent i = new Intent(MenuRight.this, StoreManagment.class);
            startActivity(i);
        } else if (id == R.id.nav_info) {
            Intent i = new Intent(MenuRight.this, Account.class);
            startActivity(i);
        } else if (id == R.id.nav_password) {
            Intent i = new Intent(MenuRight.this, PasswordActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_password) {
            appVar.main.UserID = "";
            appVar.main.UserName = "";
            appVar.main.HasShop = false;
            appVar.main.ShopID = "";
        } else if (id == R.id.nav_cart) {
            Intent i = new Intent(MenuRight.this, Cart.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

}
