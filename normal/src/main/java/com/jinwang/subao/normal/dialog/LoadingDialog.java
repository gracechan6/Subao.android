package com.jinwang.subao.normal.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.jinwang.subao.normal.R;


/**
 * Created by Zengjp on 2015/3/9.
 */
public class LoadingDialog extends Dialog{
    private int layoutId = R.layout.loading_a_dialog;
    private int alpha = 200;
    private boolean cancelable = false;

    public LoadingDialog(Context context) {
        super(context, R.style.LoadingDialog);
    }

    public LoadingDialog(Context context, int layoutId) {
        super(context, R.style.LoadingDialog);
        this.layoutId = layoutId;
    }

    public LoadingDialog(Context context, int layoutId, int alpha, boolean cancelable) {
        super(context, R.style.LoadingDialog);
        this.layoutId = layoutId;
        this.alpha = alpha;
        this.cancelable = cancelable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);
        View v = findViewById(R.id.loading);
        v.getBackground().setAlpha(alpha);
        setCancelable(cancelable);
    }
}
