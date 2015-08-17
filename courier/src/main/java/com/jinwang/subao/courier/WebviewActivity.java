package com.jinwang.subao.courier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jinwangmobile.ui.base.activity.BaseWebviewActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by michael on 15/8/6.
 *
 */
public class WebviewActivity extends BaseWebviewActivity {
    //这里应该配置到一个配置文件 里面
    public static int NEXT_ACTIVITY_REQUEST_CODE = 10001;

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
    }

    @Override
    public void goBack(Object o) {

        try {
            //设置回传数据
            JSONObject data = new JSONObject(o.toString());
            JSONArray callBackData = data.getJSONArray("transferData");

            Log.i(getClass().getSimpleName(), "Callback data: " + callBackData);
            if (callBackData.length() > 0)
            {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_TRANSFER_DATA, callBackData.toString());

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
        try {
            JSONObject data = new JSONObject(o.toString());

            //页面编号
            int pageNum = data.getInt("pageNum");

            //页面地址
            String url = data.getString("destUrl");

            //转换为绝对路径，复写父类该方法，默认不变返回
            url = this.pathToAbsPath(url);

            Intent destIntent = new Intent();

            //url
            destIntent.putExtra(EXTRA_URL_PATH, url);

            //填充传递的数据
            String params = data.getJSONObject("params").toString();
            destIntent.putExtra(EXTRA_PARAMS, params);

            //传递数据
            JSONArray transferData = data.getJSONArray("transferData");
            if (transferData.length() > 0) {
                destIntent.putExtra(EXTRA_TRANSFER_DATA, transferData.toString());
            }

            //设置标题
            destIntent.putExtra(BaseWebviewActivity.EXTRA_TITLE, data.getString("title"));

            switch (pageNum) {
                //按页面编号显示不同的界面
                case 0:
                    destIntent.setClass(this, NextActivity.class);
                    default:
                        destIntent.setClass(this, NextActivity.class);
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
        return "file:///android_asset/mTest/" + path;
    }
}
