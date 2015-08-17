package com.jinwang.subao.normal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Html;
import android.util.Log;
import android.webkit.WebSettings;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jinwang.subao.normal.view.NextActivity;
import com.jinwangmobile.ui.base.activity.BaseWebviewActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by michael on 15/8/6.
 *
 */
public class WebviewActivity extends BaseWebviewActivity {
    //这里应该配置到一个配置文�? 里面
    public static int NEXT_ACTIVITY_REQUEST_CODE = 10001;
    public static int pageNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebSettings settings = mWebview.getSettings();
        settings.setDomStorageEnabled(true);
    }

    @Override
    public void transferData(Object o) {
        /*
        这个方法暂时无用
         */
//        try {
//            setTransferData(new JSONArray(o.toString()));
//        } catch (JSONException e) {
//            Log.e(getClass().getSimpleName(), e.getMessage());
//        }
//        mWalkView.load("javascript:jwGobal.goback()", null);
    }

    @Override
    public void goBack(Object o) {
        Log.i(getClass().getSimpleName(), "Callback data: "+"返回");
        mActionBar.setNavigationIcon(null);
        try {
            //设置回传数据
            JSONObject data = new JSONObject(o.toString());
            JSONArray callBackData = data.getJSONArray("transferData");

            Log.i(getClass().getSimpleName(), "Callback data: " + callBackData);
            if (callBackData.length() > 0)
            {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_TRANSFER_DATA, callBackData.toString());
                Log.i(getClass().getSimpleName(), "设置EXTRA_TRANSFER_DATA");
                setResult(Activity.RESULT_OK, intent);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            finish();
        }
    }

    @Override
    public void showPage(Object o) {
        Log.i(getClass().getSimpleName(), "showPage" + o.toString());
        try {
            JSONObject data = new JSONObject(o.toString());
            Log.i(getClass().getSimpleName(),"showPage"+data);
            //菜单按钮
            Log.i(getClass().getSimpleName(), "开始确定showMenu值");

            //页面编号
            pageNum = data.getInt("pageNum");

            //页面地址
            String url = data.getString("destUrl");

            Log.i(getClass().getSimpleName(), "获取URL的值" + url);
            Intent destIntent = new Intent();
            //转换为绝对路径，复写父类该方法，默认不变返回
            if(url.indexOf("kaidi100")>-1){
                url="http://m.kuaidi100.com";
            }else {
                url = this.pathToAbsPath(url);
            }
            //url
            destIntent.putExtra(EXTRA_URL_PATH, url);

            //填充传�?�的数据
            String params = data.getJSONObject("params").toString();
            Log.i(getClass().getSimpleName(),"params"+params);
            destIntent.putExtra(EXTRA_PARAMS, params);
            //传�?�数�?
            JSONArray transferData = data.getJSONArray("transferData");
            if (transferData.length() > 0) {
                destIntent.putExtra(EXTRA_TRANSFER_DATA, transferData.toString());
            }

            //设置标题

            destIntent.putExtra(BaseWebviewActivity.EXTRA_TITLE,data.getString("title"));
//            destIntent.putExtra(BaseWebviewActivity.EXTRA_TITLE,R.drawable.icon_back);
            Log.i(getClass().getSimpleName(), "pageNum是多少"+pageNum);
            switch (pageNum) {
                //按页面编号显示不同的界面
                case 0:
                    destIntent.setClass(this, NextActivity.class);
//                    mWebview.loadUrl("http://m.kuaidi100.com");
                    break;
                default:
                    destIntent.setClass(this, NextActivity.class);
                    break;
            }

            startActivityForResult(destIntent, MainActivity.NEXT_ACTIVITY_REQUEST_CODE);

        }
        //数据格式错误
        catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getMessage());
        }
    }

    @Override
    public String pathToAbsPath(String path) {
        return "file:///android_asset/mPuTong/" + path;
    }
}
