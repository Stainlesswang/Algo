package edu.tjut.algo.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.File;
import java.io.InputStream;
import java.util.Scanner;

import edu.tjut.algo.R;
import edu.tjut.algo.fragment.DashboardFragment;
import edu.tjut.algo.fragment.EditFragment;
import edu.tjut.algo.fragment.HomeFragment;

public class MainActivity extends FragmentActivity implements DashboardFragment.OnFragmentInteractionListener,EditFragment.OnFragmentInteractionListener
        ,HomeFragment.OnFragmentInteractionListener{
    private TextView mTextMessage;
    private FrameLayout fl;
    private Fragment[] fragments;
    private int lastShowFragment = 0;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (lastShowFragment != 0) {
                        switchFrament(lastShowFragment, 0);
                        lastShowFragment = 0;
                    }
                    return true;
                case R.id.navigation_dashboard:
                    if (lastShowFragment != 1) {
                        switchFrament(lastShowFragment, 1);
                        lastShowFragment = 1;
                    }
                    return true;
                case R.id.navigation_notifications:
                    if (lastShowFragment != 2) {
                        switchFrament(lastShowFragment, 2);
                        lastShowFragment = 2;
                    }
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fl = (FrameLayout) findViewById(R.id.content);
//        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initFragments();
    }
    /**
     * 切换Fragment
     *
     * @param lastIndex 上个显示Fragment的索引
     * @param index     需要显示的Fragment的索引
     */
    public void switchFrament(int lastIndex, int index) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.hide(fragments[lastIndex]);

        if (!fragments[index].isAdded()) {

            transaction.add(R.id.content, fragments[index]);
        }

        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

    private void initFragments() {
        HomeFragment homeFragment = new HomeFragment();
        DashboardFragment dashboardFragment = new DashboardFragment();
        EditFragment editFragment = new EditFragment();

        fragments = new Fragment[]{homeFragment, dashboardFragment, editFragment};
        lastShowFragment = 0;
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, homeFragment)
                .show(homeFragment)
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
