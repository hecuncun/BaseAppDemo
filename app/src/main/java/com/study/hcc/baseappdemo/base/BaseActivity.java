package com.study.hcc.baseappdemo.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.study.hcc.baseappdemo.R;

import cn.droidlover.xdroidmvp.mvp.IPresent;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * Created by hecuncun on 2018/8/30
 *
 * 所有Activity的基类
 */


public abstract class BaseActivity<P extends IPresent> extends XActivity<P> {
    //==========================状态栏=================================

    private boolean isAddStatusBar = true;//是否需要填充状态栏颜色
    private boolean isAutoDealStatusBar = true;//是否需要填充状态栏颜色

    public void cancelAddStatusBar() {
        this.isAddStatusBar = false;
    }

    public void setAutoDealStatusBar() {
        this.isAutoDealStatusBar = false;
    }

    /**
     * 判断是否是快速点击
     */
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;

    }

    /**
     * 判断触摸时间派发间隔
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isFastDoubleClick()) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isAutoDealStatusBar) {
            translucentStatusBar();
        }
    }

    /**
     * 状态栏透明化
     */
    public void translucentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        if (isAddStatusBar) {
            addStatusBarView();
        }
    }

    /**
     * 使用android:fitsSystemWindows="true"属性，不让布局延伸到状态栏，这时状态栏就是透明的，
     * 然后添加一个和状态栏高、宽相同的指定颜色View来覆盖被透明化的状态栏。
     */
    public void addStatusBarView() {
        //版本在4.4以上才做状态栏操作
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View view = new View(this);
            //View transparentView = new View(this);
            //transparentView.setBackgroundColor(Color.parseColor("#50000000"));//状态栏半透明化效果
            //view.setBackgroundDrawable(getActionBarBackground(this));
            view.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.colorPrimary));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeightById());

            ViewGroup decorView = (ViewGroup) findViewById(android.R.id.content);

            decorView.addView(view, params);
            //decorView.addView(transparentView, params);
        }
    }

    /**
     * 通过系统尺寸资源获取状态栏高度
     * 状态栏高度定义在Android系统尺寸资源中status_bar_height，但这并不是公开可直接使用的，
     * 例如像通常使用系统资源那样android.R.dimen.status_bar_height。
     * 但是系统给我们提供了一个Resource类，通过这个类可以获取资源文件，借此可以获取到status_bar_height
     */
    public int getStatusBarHeightById() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        return getResources().getDimensionPixelSize(resourceId);
    }

    protected void initToolBar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            //设置NavigationIcon
            toolbar.setNavigationIcon(R.drawable.register_btn_return);//设置页面返回的图标
            // 设置navigation button 点击事件
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            // 设置 toolbar 背景色
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            ((TextView)toolbar.findViewById(R.id.tv_title)).setText(title);//设置页面的标题
            //设置 Toolbar menu
            //toolbar.inflateMenu(R.menu.setting_menu);
            // 设置menu item 点击事件
          /*  toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.item_setting:
                            //点击设置
                            break;
                    }
                    return false;
                }
            });*/

        }
    }

   /* protected RecyclerView initRecyclerView(RecyclerView.LayoutManager mLayoutManager,
                                            RecyclerView.Adapter adapter,
                                            RecyclerView.ItemAnimator itemAnimator) {
        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(itemAnimator);
        mRecyclerView.setAdapter(adapter);
        return mRecyclerView;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private static Toast mToast;

    protected void showToast(String str) {
        if (mToast == null) {
            mToast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(str);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    protected void showLongToast(String str) {
        if (mToast == null) {
            mToast = Toast.makeText(this, str, Toast.LENGTH_LONG);
        } else {
            mToast.setText(str);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

}
