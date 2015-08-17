package com.jinwang.subao.courier;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.TextView;

import com.jinwang.subao.courier.activity.SettingActivity;
import com.jinwang.subao.courier.utils.PreferenceUtils;
import com.jinwangmobile.ui.base.activity.BaseWebviewActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends WebviewActivity {
    private TextView mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebSettings settings = mWebview.getSettings();
        settings.setDomStorageEnabled(true);
        initToolBar();
        setUrlPath("file:///android_asset/mCourier/index.html");
        intoIndex();
        webviewLoadData();
    }
    public void intoIndex(){
        SharedPreferences sp=getSharedPreferences(PreferenceUtils.PREFERENCE, MODE_APPEND);
        final String mUuid = sp.getString(PreferenceUtils.PREFERENCE_MUUID, "");
        Log.i(getClass().getSimpleName(),"获取到的UUID"+mUuid);
        JSONObject jb=new JSONObject();
        try {
            jb.put("Muuid",mUuid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setParams(jb);
    }

    protected void initToolBar()
    {
        //设置自定义标题
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;

        mTitle = new TextView(this);
        mTitle.setTextColor(Color.WHITE);
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        mTitle.setText(getString(R.string.app_name));
        mActionBar.addView(mTitle, lp);
        //消息按钮绑定
        mActionBar.setNavigationIcon(R.drawable.icon_message);
        mActionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, ChatMsgListActivity.class);
//                startActivity(intent);
            }
        });
        //设置按钮绑定
        mActionBar.inflateMenu(R.menu.menu_settings);
        mActionBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEXT_ACTIVITY_REQUEST_CODE && null != data) {
            String transferData = data.getStringExtra(EXTRA_TRANSFER_DATA);

            //有回传数据
            if (null != transferData) {

                Log.i(getClass().getSimpleName(), "Transfer callback data: " + transferData);

                //传递参数
                mWebview.loadUrl("javascript:jwGobal.transferData('" + transferData + "')");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
