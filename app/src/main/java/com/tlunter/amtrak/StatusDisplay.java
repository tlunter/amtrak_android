package com.tlunter.amtrak;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;


public class StatusDisplay extends ActionBarActivity implements ConnectivityTest.ConnectivityTestInterface {
    private final String LOG_TEXT = "com.tlunter.amtrak.StatusDisplay";
    private ViewPager mViewPager;
    private TrainPagerFragment mTrainPagerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_status_display);

        mTrainPagerFragment = new TrainPagerFragment(getFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(mTrainPagerFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_status_display, menu);
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
            if (mViewPager.getCurrentItem() > -1) {
                Intent intent = new Intent(this, EditRouteSettingsActivity.class);
                Long routeSettingsId = RouteSettings.listAll(RouteSettings.class).get(mViewPager.getCurrentItem()).getId();
                intent.putExtra("routeSettings", routeSettingsId);
                startActivity(intent);
                return true;
            }
        } else if (id == R.id.action_add_route_settings) {
            Intent intent = new Intent(this, EditRouteSettingsActivity.class);
            intent.putExtras(new Bundle());
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTrainPagerFragment.notifyDataSetChanged();

        ConnectivityTest.test(this, this);
    }

    public void testSuccess() {}

    public void testFailure() {
        Intent intent = new Intent(this, NetworkConnectivity.class);
        startActivity(intent);
        finish();
    }

    public class TrainPagerFragment extends FragmentStatePagerAdapter {
        public TrainPagerFragment(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            List<RouteSettings> rss = RouteSettings.listAll(RouteSettings.class);

            return TrainListingFragment.newInstance(rss.get(position));
        }

        @Override
        public int getCount() {
            return (int)RouteSettings.count(RouteSettings.class, null, null);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            List<RouteSettings> rss = RouteSettings.listAll(RouteSettings.class);

            RouteSettings rs = rss.get(position);

            return rs.toString();
        }
    }
}
