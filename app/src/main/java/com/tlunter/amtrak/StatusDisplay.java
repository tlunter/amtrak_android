package com.tlunter.amtrak;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;


public class StatusDisplay extends ActionBarActivity implements ConnectivityTest.ConnectivityTestInterface {
    private final String LOG_TEXT = "StatusDisplay";
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
                Intent intent = new Intent(this, RouteSettingsActivity.class);
                Long routeSettingsId = RouteSettings.listAll(RouteSettings.class).get(mViewPager.getCurrentItem()).getId();
                intent.putExtra("routeSettings", routeSettingsId);
                startActivity(intent);
                return true;
            }
        } else if (id == R.id.action_add_route_settings) {
            Intent intent = new Intent(this, RouteSettingsActivity.class);
            intent.putExtras(new Bundle());
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTrainPagerFragment.setRouteSettings(RouteSettings.listAll(RouteSettings.class));
        mTrainPagerFragment.notifyDataSetChanged();

        ConnectivityTest.test(this, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void testSuccess() {}

    public void testFailure() {
        Intent intent = new Intent(this, NetworkConnectivity.class);
        startActivity(intent);
        finish();
    }

    public class TrainPagerFragment extends FragmentStatePagerAdapter {
        List<RouteSettings> routeSettings;
        public TrainPagerFragment(FragmentManager fm) {
            super(fm);
        }

        public void setRouteSettings(List<RouteSettings> routeSettings) {
            this.routeSettings = routeSettings;
        }

        @Override
        public Fragment getItem(int position) {
            return TrainListingFragment.newInstance(routeSettings.get(position));
        }

        @Override
        public int getCount() {
            return (int)RouteSettings.count(RouteSettings.class, null, null);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            RouteSettings rs = routeSettings.get(position);

            return rs.toString();
        }

        @Override
        public int getItemPosition(Object obj) {
            TrainListingFragment fragment = (TrainListingFragment)obj;
            Long recordId = fragment.getArguments().getLong("recordId");

            for (int i = 0; i < routeSettings.size(); i++) {
                RouteSettings routeSetting = routeSettings.get(i);
                if (routeSetting.getId().equals(recordId)) {
                    return i;
                }
            }
            return POSITION_NONE;
        }
    }
}
