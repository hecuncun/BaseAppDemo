package com.study.hcc.baseappdemo.ui.activity;

import android.os.Bundle;

import com.study.hcc.baseappdemo.R;
import com.study.hcc.baseappdemo.base.BaseActivity;
import com.study.hcc.baseappdemo.presenter.LoginPresenter;


/**
 * Created by hecuncun on 2018/8/30
 */

public class LoginActivity extends BaseActivity<LoginPresenter> {

    @Override
    public void initData(Bundle savedInstanceState) {
       initToolBar("登陆");
    }

    @Override
    protected void initToolBar(String title) {
        super.initToolBar(title);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public LoginPresenter newP() {
        return null;
    }

}
