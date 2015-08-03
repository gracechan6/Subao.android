package com.jinwangmobile.ui.base.webview;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.util.Log;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by michael on 14/12/24.
 */
public class BaseWebViewClient extends WebViewClient
{
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url)
    {
        Log.i(getClass().getName(), "Should Override url loading");
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon)
    {
        Log.i(getClass().getName(), "On page started");
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url)
    {
        Log.i(getClass().getName(), "On page finished");
        super.onPageFinished(view, url);
    }

    @Override
    public void onLoadResource(WebView view, String url)
    {
        Log.i(getClass().getName(), "On load resource");
        super.onLoadResource(view, url);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url)
    {
        Log.i(getClass().getName(), "Should intercept request");
        return super.shouldInterceptRequest(view, url);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request)
    {
        Log.i(getClass().getName(), "Should intercept request");
        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg)
    {
        Log.i(getClass().getName(), "On too many redirects");
        super.onTooManyRedirects(view, cancelMsg, continueMsg);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
    {
        Log.i(getClass().getName(), "On received error");
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onFormResubmission(WebView view, Message dontResend, Message resend)
    {
        Log.i(getClass().getName(), "On form resubmission");
        super.onFormResubmission(view, dontResend, resend);
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload)
    {
        Log.i(getClass().getName(), "Do update visited history");
        super.doUpdateVisitedHistory(view, url, isReload);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
    {
        Log.i(getClass().getName(), "On received ss error");
        super.onReceivedSslError(view, handler, error);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceivedClientCertRequest(WebView view, ClientCertRequest request)
    {
        Log.i(getClass().getName(), "On received client cert request");
        super.onReceivedClientCertRequest(view, request);
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm)
    {
        Log.i(getClass().getName(), "On received http auth request");
        super.onReceivedHttpAuthRequest(view, handler, host, realm);
    }

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event)
    {
        Log.i(getClass().getName(), "Should override key event");
        return super.shouldOverrideKeyEvent(view, event);
    }

    @Override
    public void onUnhandledKeyEvent(WebView view, KeyEvent event)
    {
        Log.i(getClass().getName(), "On unhandle key event");
        super.onUnhandledKeyEvent(view, event);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onUnhandledInputEvent(WebView view, InputEvent event)
    {
        Log.i(getClass().getName(), "On unhandled input event");
        super.onUnhandledInputEvent(view, event);
    }

    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale)
    {
        Log.i(getClass().getName(), "On scale changed");
        super.onScaleChanged(view, oldScale, newScale);
    }

    @Override
    public void onReceivedLoginRequest(WebView view, String realm, String account, String args)
    {
        Log.i(getClass().getName(), "On received login request");
        super.onReceivedLoginRequest(view, realm, account, args);
    }
}
