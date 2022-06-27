package com.tianxin.activity.meun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.Toashow;

import butterknife.BindView;

/**
 * 安卓侧滑菜单DrawerLayout
 * 我们最常使用DrawerLayout + NavigationView 实现侧滑菜单，而NavigationView用来实现侧滑导航的布局
 * https://blog.csdn.net/weixin_42046829/article/details/110396733
 */
public class MEUN_MainActivity extends BasActivity2 implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;


    @Override
    protected int getview() {
        return R.layout.activity_meun_main;
    }

    @Override
    public void iniview() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


//        drawer.openDrawer(Gravity.START);
//        drawer.openDrawer(Gravity.END);
//        drawer.closeDrawer(Gravity.START);
//        drawer.closeDrawers();

        //2.4 监听事件
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });




    }

    @Override
    public void initData() {

    }

    @Override
    public void OnClick(View v) {

    }

    @Override
    public void OnEorr() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activitymain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.id1:
                Toast.makeText(this, "MenuItem1", Toast.LENGTH_LONG).show();
                return true;
            case R.id.id2:
                Toast.makeText(this, "MenuItem2", Toast.LENGTH_LONG).show();
                return true;
            case R.id.id3:
                Toast.makeText(this, "MenuItem3", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_camera:
                Toashow.show("导航摄像机");
                break;
            case R.id.nav_gallery:
                Toashow.show("导航列表");
                break;
            case R.id.nav_slideshow:
                Toashow.show("导航幻灯片");
                break;
            case R.id.nav_manage:
                Toashow.show("导航管理");
                break;
            case R.id.nav_share:
                Toashow.show("导航分享");
                break;
            case R.id.nav_send:
                Toashow.show("导航发送");
                break;
            case R.id.imageView:
                Toashow.show("图像视图");
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}