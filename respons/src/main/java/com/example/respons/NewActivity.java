package com.example.respons;

        import android.app.Activity;
        import android.app.Dialog;
        import android.app.DownloadManager;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.content.pm.PackageManager;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.design.widget.NavigationView;
        import android.support.design.widget.TabLayout;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentPagerAdapter;
        import android.support.v4.view.ViewPager;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBarDrawerToggle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.Window;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;

        import java.util.ArrayList;
        import java.util.List;

//import info.androidhive.materialtabs.R;
//import info.androidhive.materialtabs.fragments.Shops;
//import info.androidhive.materialtabs.fragments.ThreeFragment;
//import info.androidhive.materialtabs.fragments.TwoFragment;

public class NewActivity extends MenuRight {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    int AppVersion=0,CurrnetVersion=0;
    private int[] tabIcons = {
            R.drawable.cate,
            R.drawable.prod,
            R.drawable.shop24
    };
    public static TextView name;
    public static NavigationView navigationView;
    static AppCompatActivity MainAvtivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

//        appVar.main.UserID = "1";
//        appVar.main.UserName = "09156620865";
//        appVar.main.HasShop = true;
//        appVar.main.ShopID = "2";
        MainAvtivity=this;
        if (! CallSoap.isConnectionAvailable(this)) {
            Intent inte = new Intent(this, NoNet.class);
            startActivity(inte);
        }
        if (NoNet._NoNet != null) NoNet._NoNet.finish();
        //------------------------------- Version Control and Update
        try {
            AppVersion=getPackageManager().getPackageInfo(getPackageName(),0).versionCode;
            //new CallSoap().ResiveListSync("GetCurrentVersion");

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (AppVersion < CurrnetVersion )
        {
            Intent updateIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://some-public-url/deploy/MyApplication.apk"));
            startActivity(updateIntent);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        try {
//            List<TopicList> brands=new CallSoap().GetTopics("");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.closeDrawer(Gravity.RIGHT);
                } else {
                    drawer.openDrawer(Gravity.RIGHT);
                }
            }
        });

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //View header= navigationView.inflateHeaderView(R.layout.nav_header_menu_right);
        View header=navigationView.getHeaderView(0);
        name = (TextView)header.findViewById(R.id.Name);
        if (! appVar.main.UserName.equals("0")) {
            name.setText(appVar.main.UserName);
            navigationView.getMenu().findItem(R.id.nav_shop).setEnabled(true);
        }
        else
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewActivity.this,LoginActivity.class));
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        viewPager.setCurrentItem(2);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        } else {
            super.onBackPressed();
        }
    }

    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("کالاها");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.prod, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("فروشگاه ها");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.shop24, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("دسته بندی ها");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.cate, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabThree);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Category(), "one");
        adapter.addFrag(new Shops(), "two");
        adapter.addFrag(new ShowAllProducts(), "three");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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

                Intent i = new Intent(context, ShowSearch.class);
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

    //------------------------------------------------------


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Search(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}