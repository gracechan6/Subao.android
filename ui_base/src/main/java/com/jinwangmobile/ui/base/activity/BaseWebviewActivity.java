package com.jinwangmobile.ui.base.activity;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;
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
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jinwangmobile.ui.base.JSInterface;
import com.jinwangmobile.ui.base.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by michael on 15/7/31.
 * Hybird Base activity
 */
public abstract class BaseWebviewActivity extends BaseActivity implements JSInterface {
    //导航栏
    protected Toolbar mActionBar;

    //浏览器
    protected WebView mWebview;

    //页面编号
    private int mPageNum;
    //缓存模式
    private int mCacheMode = WebSettings.LOAD_DEFAULT;

    //页面请求参数
    private JSONObject mParams;

    //页面绝对地址
    private String mUrlPath;
    //传递的数据
    private JSONArray mTransferData;
    //回调函数返回数据
    private JSONArray mCallbackData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base_webview_);
    }

    @Override
    @TargetApi(11)
    protected void onPause() {
        super.onPause();

        mWebview.onPause();
    }

    @Override
    @TargetApi(11)
    protected void onResume() {
        super.onResume();

        mWebview.onResume();
    }

//=================== chrome client callback methods
    /**
     * Tell the host application the current progress of loading a page.
     *
     * @param view        The WebView that initiated the callback.
     * @param newProgress Current page loading progress, represented by
     */
    public void onWebviewProgressChanged(WebView view, int newProgress) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewProgressChanged new progress: " + newProgress);
    }

    /**
     * Notify the host application of a change in the document title.
     *
     * @param view  The WebView that initiated the callback.
     * @param title A String containing the new title of the document.
     */
    public void onWebviewReceivedTitle(WebView view, String title) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewReceivedTitle" + " Title: " + title);
    }

    /**
     * Notify the host application of a new favicon for the current page.
     *
     * @param view The WebView that initiated the callback.
     * @param icon A Bitmap containing the favicon for the current page.
     */
    public void onWebviewReceivedIcon(WebView view, Bitmap icon) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewReceivedIcon");
    }

    /**
     * Notify the host application of the url for an apple-touch-icon.
     *
     * @param view        The WebView that initiated the callback.
     * @param url         The icon url.
     * @param precomposed True if the url is for a precomposed touch icon.
     */
    public void onWebviewReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewReceivedTouchIconUrl");
    }

    /**
     * Notify the host application that the current page would
     * like to show a custom View.  This is used for Fullscreen
     * video playback; see "HTML5 Video support" documentation on
     * {@link WebView}.
     *
     * @param view     is the View object to be shown.
     * @param callback is the callback to be invoked if and when the view
     */
    public void onWebviewShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewShowCustomView");
    }

    /**
     * Notify the host application that the current page would
     * like to hide its custom view.
     */
    public void onWebviewHideCustomView() {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewHideCustomView");
    }

    /**
     * Request the host application to create a new window. If the host
     * application chooses to honor this request, it should return true from
     * this method, create a new WebView to host the window, insert it into the
     * View system and send the supplied resultMsg message to its target with
     * the new WebView as an argument. If the host application chooses not to
     * honor the request, it should return false from this method. The default
     * implementation of this method does nothing and hence returns false.
     *
     * @param view          The WebView from which the request for a new window
     *                      originated.
     * @param isDialog      True if the new window should be a dialog, rather than
     *                      a full-size window.
     * @param isUserGesture True if the request was initiated by a user gesture,
     *                      such as the user clicking a link.
     * @param resultMsg     The message to send when once a new WebView has been
     *                      created. resultMsg.obj is a
     *                      {@link WebView.WebViewTransport} object. This should be
     *                      used to transport the new WebView, by calling
     *                      {@link WebView.WebViewTransport#setWebView(WebView)
     *                      WebView.WebViewTransport.setWebView(WebView)}.
     * @return This method should return true if the host application will
     * create a new window, in which case resultMsg should be sent to
     * its target. Otherwise, this method should return false. Returning
     * false from this method but also sending resultMsg will result in
     * undefined behavior.
     */
    public boolean onWebviewCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewCreateWindow");

        return true;
    }

    /**
     * Request display and focus for this WebView. This may happen due to
     * another WebView opening a link in this WebView and requesting that this
     * WebView be displayed.
     *
     * @param view The WebView that needs to be focused.
     */
    public void onWebviewRequestFocus(WebView view) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewRequestFocus");
    }

    /**
     * Notify the host application to close the given WebView and remove it
     * from the view system if necessary. At this point, WebCore has stopped
     * any loading in this window and has removed any cross-scripting ability
     * in javascript.
     *
     * @param window The WebView that needs to be closed.
     */
    public void onWebviewCloseWindow(WebView window) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewCloseWindow");
    }

    /**
     * Tell the client to display a javascript alert dialog.  If the client
     * returns true, WebView will assume that the client will handle the
     * dialog.  If the client returns false, it will continue execution.
     *
     * @param view    The WebView that initiated the callback.
     * @param url     The url of the page requesting the dialog.
     * @param message Message to be displayed in the window.
     * @param result  A JsResult to confirm that the user hit enter.
     * @return boolean Whether the client will handle the alert dialog.
     */
    public boolean onWebviewJsAlert(WebView view, String url, String message, JsResult result) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewJsAlert");

        return true;
    }

    /**
     * Tell the client to display a confirm dialog to the user. If the client
     * returns true, WebView will assume that the client will handle the
     * confirm dialog and call the appropriate JsResult method. If the
     * client returns false, a default value of false will be returned to
     * javascript. The default behavior is to return false.
     *
     * @param view    The WebView that initiated the callback.
     * @param url     The url of the page requesting the dialog.
     * @param message Message to be displayed in the window.
     * @param result  A JsResult used to send the user's response to
     *                javascript.
     * @return boolean Whether the client will handle the confirm dialog.
     */
    public boolean onWebviewJsConfirm(WebView view, String url, String message, JsResult result) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewJsConfirm");

        return true;
    }

    /**
     * Tell the client to display a prompt dialog to the user. If the client
     * returns true, WebView will assume that the client will handle the
     * prompt dialog and call the appropriate JsPromptResult method. If the
     * client returns false, a default value of false will be returned to to
     * javascript. The default behavior is to return false.
     *
     * @param view         The WebView that initiated the callback.
     * @param url          The url of the page requesting the dialog.
     * @param message      Message to be displayed in the window.
     * @param defaultValue The default value displayed in the prompt dialog.
     * @param result       A JsPromptResult used to send the user's reponse to
     *                     javascript.
     * @return boolean Whether the client will handle the prompt dialog.
     */
    public boolean onWebviewJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onJsPrompt");

        return true;
    }

    /**
     * Tell the client to display a dialog to confirm navigation away from the
     * current page. This is the result of the onbeforeunload javascript event.
     * If the client returns true, WebView will assume that the client will
     * handle the confirm dialog and call the appropriate JsResult method. If
     * the client returns false, a default value of true will be returned to
     * javascript to accept navigation away from the current page. The default
     * behavior is to return false. Setting the JsResult to true will navigate
     * away from the current page, false will cancel the navigation.
     *
     * @param view    The WebView that initiated the callback.
     * @param url     The url of the page requesting the dialog.
     * @param message Message to be displayed in the window.
     * @param result  A JsResult used to send the user's response to
     *                javascript.
     * @return boolean Whether the client will handle the confirm dialog.
     */
    public boolean onWebviewJsBeforeUnload(WebView view, String url, String message, JsResult result) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewJsBeforeUnload");

        return true;
    }

    /**
     * Notify the host application that web content from the specified origin
     * is attempting to use the Geolocation API, but no permission state is
     * currently set for that origin. The host application should invoke the
     * specified callback with the desired permission state. See
     * {@link GeolocationPermissions} for details.
     *
     * @param origin   The origin of the web content attempting to use the
     *                 Geolocation API.
     * @param callback The callback to use to set the permission state for the
     */
    public void onWebviewGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewGeolocationPermissionsShowPrompt(String, GeolocationPermissions.Callback)");
    }

    /**
     * Notify the host application that a request for Geolocation permissions,
     * made with a previous call to
     * {@link #onWebviewGeolocationPermissionsShowPrompt(String, GeolocationPermissions.Callback) onGeolocationPermissionsShowPrompt()}
     * has been canceled. Any related UI should therefore be hidden.
     */
    public void onWebviewGeolocationPermissionsHidePrompt() {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewGeolocationPermissionsHidePrompt");
    }

    /**
     * Notify the host application that web content is requesting permission to
     * access the specified resources and the permission currently isn't granted
     * or denied. The host application must invoke {@link PermissionRequest#grant(String[])}
     * or {@link PermissionRequest#deny()}.
     * <p/>
     * If this method isn't overridden, the permission is denied.
     *
     * @param request the PermissionRequest from current web content.
     */
    public void onWebviewPermissionRequest(PermissionRequest request) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewPermissionRequest");
    }

    /**
     * Notify the host application that the given permission request
     * has been canceled. Any related UI should therefore be hidden.
     *
     * @param request the PermissionRequest that needs be canceled.
     */
    public void onWebviewPermissionRequestCanceled(PermissionRequest request) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewPermissionRequestCanceled");
    }

    /**
     * Report a JavaScript console message to the host application. The ChromeClient
     * should override this to process the log message as they see fit.
     *
     * @param consoleMessage Object containing details of the console message.
     * @return true if the message is handled by the client.
     */
    public boolean onWebviewConsoleMessage(ConsoleMessage consoleMessage) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewConsoleMessage");

        return false;
    }

    /**
     * When not playing, video elements are represented by a 'poster' image. The
     * image to use can be specified by the poster attribute of the video tag in
     * HTML. If the attribute is absent, then a default poster will be used. This
     * method allows the ChromeClient to provide that default image.
     *
     * @return Bitmap The image to use as a default poster, or null if no such image is
     * available.
     */
    public Bitmap webviewGetDefaultVideoPoster() {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "webviewGetDefaultVideoPoster");

        return null;
    }

    /**
     * When the user starts to playback a video element, it may take time for enough
     * data to be buffered before the first frames can be rendered. While this buffering
     * is taking place, the ChromeClient can use this function to provide a View to be
     * displayed. For example, the ChromeClient could show a spinner animation.
     *
     * @return View The View to be displayed whilst the video is loading.
     */
    public View webviewGetVideoLoadingProgressView() {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "webviewGetVideoLoadingProgressView");

        return null;
    }

    /**
     * Obtains a list of all visited history items, used for link coloring
     *
     * @param callback
     */
    public void webviewGetVisitedHistory(ValueCallback<String[]> callback) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "getVisitedHistory");
    }

    /**
     * Tell the client to show a file chooser.
     * <p/>
     * This is called to handle HTML forms with 'file' input type, in response to the
     * user pressing the "Select File" button.
     * To cancel the request, call <code>filePathCallback.onReceiveValue(null)</code> and
     * return true.
     *
     * @param webView           The WebView instance that is initiating the request.
     * @param filePathCallback  Invoke this callback to supply the list of paths to files to upload,
     *                          or NULL to cancel. Must only be called if the
     *                          <code>showFileChooser</code> implementations returns true.
     * @param fileChooserParams Describes the mode of file chooser to be opened, and options to be
     *                          used with it.
     * @return true if filePathCallback will be invoked, false to use default handling.
     * @see WebChromeClient.FileChooserParams
     */
    public boolean onWebviewShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewShowFileChooser");

        return false;
    }
//==================================================

    /**
     * 设置Webview Chrome client
     */
    public void setWebviewChromeClient()
    {
        mWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                onWebviewProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                onWebviewReceivedTitle(view, title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);

                onWebviewReceivedIcon(view, icon);
            }

            @Override
            public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
                super.onReceivedTouchIconUrl(view, url, precomposed);
                onWebviewReceivedTouchIconUrl(view, url, precomposed);
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                onWebviewShowCustomView(view, callback);
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
                onWebviewHideCustomView();
            }


            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
                return onWebviewCreateWindow(view, isDialog, isUserGesture, resultMsg);
            }

            @Override
            public void onRequestFocus(WebView view) {
                super.onRequestFocus(view);
                onWebviewRequestFocus(view);
            }

            @Override
            public void onCloseWindow(WebView window) {
                super.onCloseWindow(window);
                onWebviewCloseWindow(window);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onJsAlert");

                return onWebviewJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return onWebviewJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return onWebviewJsPrompt(view, url, message, defaultValue, result);
            }

            @Override
            public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
                return onWebviewJsBeforeUnload(view, url, message, result);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
                onWebviewGeolocationPermissionsShowPrompt(origin, callback);
            }

            @Override
            public void onGeolocationPermissionsHidePrompt() {
                super.onGeolocationPermissionsHidePrompt();
                onWebviewGeolocationPermissionsHidePrompt();
            }

            @Override
            public void onPermissionRequest(PermissionRequest request) {
                super.onPermissionRequest(request);

                onWebviewPermissionRequest(request);
            }

            @Override
            public void onPermissionRequestCanceled(PermissionRequest request) {
                super.onPermissionRequestCanceled(request);

                onWebviewPermissionRequestCanceled(request);
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return onWebviewConsoleMessage(consoleMessage);
            }

            @Override
            public Bitmap getDefaultVideoPoster() {
                return webviewGetDefaultVideoPoster();
            }

            @Override
            public View getVideoLoadingProgressView() {
                return webviewGetVideoLoadingProgressView();
            }

            @Override
            public void getVisitedHistory(ValueCallback<String[]> callback) {
                super.getVisitedHistory(callback);
                webviewGetVisitedHistory(callback);
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                return onWebviewShowFileChooser(webView, filePathCallback, fileChooserParams);
            }
        });
    }

//==================Web view client callback methods
    /**
     * Give the host application a chance to take over the control when a new
     * url is about to be loaded in the current WebView. If WebViewClient is not
     * provided, by default WebView will ask Activity Manager to choose the
     * proper handler for the url. If WebViewClient is provided, return true
     * means the host application handles the url, while return false means the
     * current WebView handles the url.
     * This method is not called for requests using the POST "method".
     *
     * @param view The WebView that is initiating the callback.
     * @param url  The url to be loaded.
     * @return True if the host application wants to leave the current WebView
     * and handle the url itself, otherwise return false.
     */
    public boolean webviewShouldOverrideUrlLoading(WebView view, String url) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "webviewShouldOverrideUrlLoading");

        return false;
    }

    /**
     * Notify the host application that a page has started loading. This method
     * is called once for each main frame load so a page with iframes or
     * framesets will call onPageStarted one time for the main frame. This also
     * means that onPageStarted will not be called when the contents of an
     * embedded frame changes, i.e. clicking a link whose target is an iframe.
     *
     * @param view    The WebView that is initiating the callback.
     * @param url     The url to be loaded.
     * @param favicon The favicon for this page if it already exists in the
     */
    public void onWebviewPageStarted(WebView view, String url, Bitmap favicon) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewPageStarted url: " + url);
    }

    /**
     * Notify the host application that a page has finished loading. This method
     * is called only for main frame. When onPageFinished() is called, the
     * rendering picture may not be updated yet. To get the notification for the
     * new Picture, use {@link WebView.PictureListener#onNewPicture}.
     *
     * @param view The WebView that is initiating the callback.
     * @param url  The url of the page.
     */
    public void onWebviewPageFinished(WebView view, String url) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewPageFinished: " + url);

        if (url.matches("^javascript.*"))
        {
            return;
        }
        //传递参数
        view.loadUrl("javascript:jwGobal.transferData('" + getParams() + "')");
    }

    /**
     * Notify the host application that the WebView will load the resource
     * specified by the given url.
     *
     * @param view The WebView that is initiating the callback.
     * @param url  The url of the resource the WebView will load.
     */
    public void onWebviewLoadResource(WebView view, String url) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewLoadResource");
    }

    /**
     * Notify the host application of a resource request and allow the
     * application to return the data.  If the return value is null, the WebView
     * will continue to load the resource as usual.  Otherwise, the return
     * response and data will be used.  NOTE: This method is called on a thread
     * other than the UI thread so clients should exercise caution
     * when accessing private data or the view system.
     *
     * @param view    The {@link WebView} that is requesting the
     *                resource.
     * @param request Object containing the details of the request.
     * @return A {@link WebResourceResponse} containing the
     * response information or null if the WebView should load the
     * resource itself.
     */
    public WebResourceResponse webviewShouldInterceptRequest(WebView view, WebResourceRequest request) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "webviewShouldInterceptRequest, request: " + request.toString());

        return null;
    }

    /**
     * Report an error to the host application. These errors are unrecoverable
     * (i.e. the main resource is unavailable). The errorCode parameter
     * corresponds to one of the ERROR_* constants.
     *
     * @param view        The WebView that is initiating the callback.
     * @param errorCode   The error code corresponding to an ERROR_* value.
     * @param description A String describing the error.
     * @param failingUrl  The url that failed to load.
     */
    public void onWebviewReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewReceivedError, error:" + description);
    }

    /**
     * As the host application if the browser should resend data as the
     * requested page was a result of a POST. The default is to not resend the
     * data.
     *
     * @param view       The WebView that is initiating the callback.
     * @param dontResend The message to send if the browser should not resend
     * @param resend     The message to send if the browser should resend data
     */
    public void onWebviewFormResubmission(WebView view, Message dontResend, Message resend) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewFormResubmission" );
    }

    /**
     * Give the host application a chance to handle the key event synchronously.
     * e.g. menu shortcut key events need to be filtered this way. If return
     * true, WebView will not handle the key event. If return false, WebView
     * will always handle the key event, so none of the super in the view chain
     * will see the key event. The default behavior returns false.
     *
     * @param view  The WebView that is initiating the callback.
     * @param event The key event.
     * @return True if the host application wants to handle the key event
     * itself, otherwise return false
     */
    public boolean webviewShouldOverrideKeyEvent(WebView view, KeyEvent event) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "webviewShouldOverrideKeyEvent");

        return false;
    }

    /**
     * Notifies the host application that the WebView received an HTTP
     * authentication request. The host application can use the supplied
     * {@link HttpAuthHandler} to set the WebView's response to the request.
     * The default behavior is to cancel the request.
     *
     * @param view    the WebView that is initiating the callback
     * @param handler the HttpAuthHandler used to set the WebView's response
     * @param host    the host requiring authentication
     * @param realm   the realm for which authentication is required
     * @see WebView#getHttpAuthUsernamePassword
     */
    public void onWebviewReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewReceivedHttpAuthRequest");
    }

    /**
     * Notify the host application to handle a SSL client certificate
     * request. The host application is responsible for showing the UI
     * if desired and providing the keys. There are three ways to
     * respond: proceed(), cancel() or ignore(). Webview remembers the
     * response if proceed() or cancel() is called and does not
     * call onReceivedClientCertRequest() again for the same host and port
     * pair. Webview does not remember the response if ignore() is called.
     * <p/>
     * This method is called on the UI thread. During the callback, the
     * connection is suspended.
     * <p/>
     * The default behavior is to cancel, returning no client certificate.
     *
     * @param view    The WebView that is initiating the callback
     * @param request An instance of a {@link ClientCertRequest}
     */
    public void onWebviewReceivedClientCertRequest(WebView view, ClientCertRequest request) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewReceivedClientCertRequest");
    }

    /**
     * Notify the host application that an SSL error occurred while loading a
     * resource. The host application must call either handler.cancel() or
     * handler.proceed(). Note that the decision may be retained for use in
     * response to future SSL errors. The default behavior is to cancel the
     * load.
     *
     * @param view    The WebView that is initiating the callback.
     * @param handler An SslErrorHandler object that will handle the user's
     *                response.
     * @param error   The SSL error object.
     */
    public void onWebviewReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewReceivedSslError");
    }

    /**
     * Notify the host application to update its visited links database.
     *
     * @param view     The WebView that is initiating the callback.
     * @param url      The url being visited.
     * @param isReload True if this url is being reloaded.
     */
    public void webviewdoUpdateVisitedHistory(WebView view, String url, boolean isReload) {

        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "webviewdoUpdateVisitedHistory");
    }

    /**
     * Notify the host application that a input event was not handled by the WebView.
     * Except system keys, WebView always consumes input events in the normal flow
     * or if shouldOverrideKeyEvent returns true. This is called asynchronously
     * from where the event is dispatched. It gives the host application a chance
     * to handle the unhandled input events.
     * <p/>
     * Note that if the event is a {@link MotionEvent}, then it's lifetime is only
     * that of the function call. If the WebViewClient wishes to use the event beyond that, then it
     * <i>must</i> create a copy of the event.
     * <p/>
     * It is the responsibility of overriders of this method to call
     * {@link WebViewClient#onUnhandledKeyEvent(WebView, KeyEvent)}
     * when appropriate if they wish to continue receiving events through it.
     *
     * @param view  The WebView that is initiating the callback.
     * @param event The input event.
     */
    public void onWebviewUnhandledInputEvent(WebView view, InputEvent event) {

        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewUnhandledInputEvent");
    }

    /**
     * Notify the host application that the scale applied to the WebView has
     * changed.
     *
     * @param view     he WebView that is initiating the callback.
     * @param oldScale The old scale factor
     * @param newScale The new scale factor
     */
    public void onWebviewScaleChanged(WebView view, float oldScale, float newScale) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onWebviewScaleChanged");
    }

    /**
     * Notify the host application that a request to automatically log in the
     * user has been processed.
     *
     * @param view    The WebView requesting the login.
     * @param realm   The account realm used to look up accounts.
     * @param account An optional account. If not null, the account should be
     *                checked against accounts on the device. If it is a valid
     *                account, it should be used to log in the user.
     * @param args    Authenticator specific arguments used to log in the user.
     */
    public void onWebviewReceivedLoginRequest(WebView view, String realm, String account, String args) {
        Log.i(getClass().getSimpleName(), "Web view Chrome Client: " + "onReceivedLoginRequest");
    }
//==================================================
    /**
     * 设置Webview client
     */
    public void setWebviewClient()
    {
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return webviewShouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                onWebviewPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                onWebviewPageFinished(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);

                onWebviewLoadResource(view, url);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return webviewShouldInterceptRequest(view, request);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                onWebviewReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                super.onFormResubmission(view, dontResend, resend);
                onWebviewFormResubmission(view, dontResend, resend);
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return webviewShouldOverrideKeyEvent(view, event);
            }

            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                super.onReceivedHttpAuthRequest(view, handler, host, realm);
                onWebviewReceivedHttpAuthRequest(view, handler, host, realm);
            }

            @Override
            public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
                super.onReceivedClientCertRequest(view, request);
                onWebviewReceivedClientCertRequest(view, request);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                onWebviewReceivedSslError(view, handler, error);
            }

            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                super.doUpdateVisitedHistory(view, url, isReload);
                webviewdoUpdateVisitedHistory(view, url, isReload);
            }

            @Override
            public void onUnhandledInputEvent(WebView view, InputEvent event) {
                super.onUnhandledInputEvent(view, event);
                onWebviewUnhandledInputEvent(view, event);
            }

            @Override
            public void onScaleChanged(WebView view, float oldScale, float newScale) {
                super.onScaleChanged(view, oldScale, newScale);
                onWebviewScaleChanged(view, oldScale, newScale);
            }

            @Override
            public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
                super.onReceivedLoginRequest(view, realm, account, args);
                onWebviewReceivedLoginRequest(view, realm, account, args);
            }
        });
    }


    /**
     * 加载数据
     */
    protected void webviewLoadData()
    {
        String requestParam = "";

        if (null != mUrlPath)
        {
            try {
                requestParam = convertRequestParams(mParams);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(getClass().getSimpleName(), "JSON Error: " + e.toString());
            }
        }
        mWebview.loadUrl(mUrlPath + requestParam);
        Log.i(getClass().getSimpleName(), "Request path: " + mUrlPath + " params: " + requestParam);
    }

    /**
     * 请求JSON参数转换为请求字符串参数
     * @param params    请求参数JSON
     * @return          请求字符串
     * @throws JSONException
     */
    protected String convertRequestParams(JSONObject params) throws JSONException {
        if (null == params)
        {
            return null;
        }


        Iterator<String> keys = params.keys();
        if (!keys.hasNext())
        {
            return null;
        }

        String result = "?";

        while (keys.hasNext())
        {
            String key = keys.next();
            result += (key + "=");
            result += (params.get(key) + "&");
        }

        return result.substring(0, result.length()-1);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        mActionBar = (Toolbar) findViewById(R.id.actionBar_);
        mWebview = (WebView) findViewById(R.id.webview_);

        initWebview();

        if (null != mUrlPath)
        {
            webviewLoadData();
        }
    }

    /**
     * 初始化Webview
     */
    public void initWebview()
    {
        WebSettings settings = mWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);

        if (Build.VERSION.SDK_INT > 15) {
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        settings.setCacheMode(mCacheMode);
        settings.setAppCacheEnabled(true);

        setWebviewChromeClient();
        setWebviewClient();
    }

    /**
     * 设置导航栏背景着色
     * @param color 背影着色
     */
    public void setActionBarBackgroundColor(int color)
    {
        mActionBar.setBackgroundColor(color);
    }

    /**
     * 设置导航栏背景
     * @param drawable 背影
     */
    @TargetApi(16)
    public void setActionBarBackground(Drawable drawable)
    {
        mActionBar.setBackground(drawable);
    }

    /**
     * 设置导航栏背景
     * @param res 背影资源
     */
    public void setActionBarBackgroundResourse(int res)
    {
        mActionBar.setBackgroundResource(res);
    }

    /**
     * 获取页面编号
     * @return  页面编号
     */
    public int getPageNum()
    {
        return mPageNum;
    }

    /**
     * 设置页面缓存模式
     * @param cacheMode 缓存模式
     */
    public void setWebCacheMode(int cacheMode)
    {
        mCacheMode = cacheMode;
    }

    /**
     * 设置页面编号
     * @param pageNum   页面编号
     */
    public void setPageNum(int pageNum)
    {
        mPageNum = pageNum;
    }

    /**
     * 页面名称转换为页面绝对路径
     * 默认返回Path，按需实现
     *
     * @param path: 相对路径
     *
     * @return 页面对应的绝对路径
     */
    public String pathToAbsPath(String path)
    {
        return path;
    }

    /**
     * 设置页面路径
     * @param path  页面绝对路径
     */
    public void setUrlPath(String path)
    {
        mUrlPath = path;
    }

    @Override
    public void jsSetTitle(String title) {
        mActionBar.setTitle(title);
    }

    /**
     * 设置请求参数
     * @param params    JSON
     */
    public void setParams(JSONObject params)
    {
        mParams = params;
    }

    /**
     * 获取请求参数
     * @return  请求参数
     */
    public JSONObject getParams()
    {
        return mParams;
    }

    /**
     * 设置传递参数
     * @param transferData  传递的数据
     */
    public void setTransferData(JSONArray transferData)
    {
        mTransferData = transferData;
    }

    /**
     * 获取传递参数
     * @return  要传递的参数
     */
    public JSONArray getTransferData()
    {
        return mTransferData;
    }

    /**
     * 设置回传数据
     * @param callbackData  回传数据
     */
    public void setCallbackData(JSONArray callbackData)
    {
        mCallbackData = callbackData;
    }

    /**
     * 获取回传数据
     * @return  回传数据
     */
    public JSONArray getCallbackData()
    {
        return mCallbackData;
    }
}
