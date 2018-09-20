package com.hdpsolution.quanlychitieu;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.hdpsolution.quanlychitieu.adapter.SectionsPagerAdapter;
import com.hdpsolution.quanlychitieu.ads.MyBaseMainActivity;
import com.hdpsolution.quanlychitieu.fragment.FragmentThongKe;
import com.kobakei.ratethisapp.RateThisApp;

public class MainActivity extends MyBaseMainActivity implements FragmentThongKe.GetFragmentThongKe, View.OnClickListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private Toolbar toolbar;
    private ImageView imgRate;
    private ImageView imgShare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsPagerAdapter=new SectionsPagerAdapter(this,getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"));
        tabLayout.setSelectedTabIndicatorHeight((int) (4 * getResources().getDisplayMetrics().density));
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(mSectionsPagerAdapter.getTabView(i));
        }
        toolbar=findViewById(R.id.toolbar_top);
        imgRate=toolbar.findViewById(R.id.imgRate);
        imgShare=toolbar.findViewById(R.id.imgShare);
        imgRate.setOnClickListener(this);
        imgShare.setOnClickListener(this);

        //DuyLH - code Firebase Analytics

        FirebaseAnalytics anl = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString("action", "vao_app");

        anl.logEvent("vao_app", params);
        //end

        RateThisApp.Config config = new RateThisApp.Config(1, 5);
        config.setTitle(R.string.my_own_title);
        config.setMessage(R.string.my_own_message);
        config.setYesButtonText(R.string.my_own_rate);
        config.setNoButtonText(R.string.my_own_thanks);
        config.setCancelButtonText(R.string.my_own_cancel);

        String urlRate = "https://play.google.com/store/apps/details?id=" + getPackageName();
        config.setUrl(urlRate);
        RateThisApp.init(config);
        RateThisApp.onCreate(this);

        try {
            RateThisApp.showRateDialogIfNeeded(this);
        } catch (Exception e) {

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    FragmentThongKe fragmentThongKe;
    @Override
    public void getFragment(FragmentThongKe fragmentThongKe) {
        this.fragmentThongKe = fragmentThongKe;
    }
    public void updateData(){
        if(fragmentThongKe!= null){
            //update
            fragmentThongKe.setThongkeTatCa();

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgRate:
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                Log.e("appPackageName", appPackageName );
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    //Toast.makeText(this, "Đang xây dựng", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.imgShare:
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Quản lý chi tiêu");
                    String sAux = "\nỨng dụng quản lí chi tiêu tiện lợi,dễ dàng sử dụng với người sử dụng\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.hdpsolution.quanlychitieu \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "Chia sẻ với"));
                } catch(Exception e) {
                    //e.toString();
                    //Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
