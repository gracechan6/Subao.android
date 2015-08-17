package com.jinwang.subao.courier;

import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.HttpAuthHandler;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jinwangmobile.ui.base.activity.BaseWebviewActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by michael on 15/7/31.
 *
 */
public class TestBaseWebviewActivity extends BaseWebviewActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        setUrlPath(pathToAbsPath(null));
        Map<String, Object> params = new HashMap<>();
        params.put("content", "abc");
        params.put("size", 150);

        setParams(new JSONObject(params));

        super.onCreate(savedInstanceState);
    }

    @Override
    public void setWebviewClient() {

    }

    @Override
    public String pathToAbsPath(String path) {
        String url = "http://www.baidu.com";

//        return url;
        return "file:///android_asset/subao_kdy/mCourier/userInfoQrcode.html";
    }

    @Override
    public void transferData(Object o) {

    }

    @Override
    public void goBack(Object o) {

    }

    @Override
    public void showPage(Object o) {

    }
}
