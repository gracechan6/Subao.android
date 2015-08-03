package com.jinwang.subao.normal.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jinwang.subao.normal.R;
import com.jinwangmobile.ui.base.activity.BaseActivity;
import com.jinwang.subao.normal.config.ConstantConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import app.frame.cropper.CropImageView;

/**
 * Created by dreamy on 2015/6/30.
 */
public class CropActivity extends BaseActivity{
    public static final int PIC_REQUEST_CODE = 0x01;
    private static final int TAKE_PHOTO = 0x02;
    private static int CROP_REQUEST_CODE = 10001;
    private CropImageView mCropImageView;
    private Button btnOk;
    private Button btnCancel;
    private Bitmap mBitMap;
    private OnCropedListener mOnCropedListener;

    /**
     * 保存图片
     */
    public static interface OnCropedListener
    {
        public void onCroped(Bitmap bitmap);
    }

    public void setOnCropedListener(OnCropedListener listener)
    {
        mOnCropedListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        initView();
    }

    private void initView(){
        btnOk = (Button)findViewById(R.id.ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
        btnCancel = (Button)findViewById(R.id.cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mCropImageView = (CropImageView)findViewById(R.id.CropImageView);
        mCropImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));

        int type = getIntent().getIntExtra(SettingActivity.PHOTO_TYPE, 1);

        if (type == SettingActivity.PHOTO_TYPE_0)
        {
            takePhoto();
        }
        else
        {
            pic();
        }

    }

    protected final void upload()
    {
        //保存截图，返回保存文件路径
        Log.i(getClass().getSimpleName(), "Confirm");

        //保存图片到文件，传递文件名给调用者
        File file = new File(ConstantConfig.TEMP_FILE_PATH + File.separator + System.currentTimeMillis() + ".png");
        try
        {
            file.createNewFile();
            OutputStream outputStream = new FileOutputStream(file);

            mCropImageView.getCroppedImage().compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

        }catch (IOException e)
        {
            Log.e(getClass().getSimpleName(), e.toString());
        }

        Intent data = new Intent();
        data.putExtra("BITMAP_DATA", file.getAbsolutePath());

        setResult(Activity.RESULT_OK, data);

        finish();
    }

    protected final void cancel()
    {
        finish();
    }

    private void takePhoto()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //文件夹tmp
        String path = Environment.getExternalStorageDirectory().toString()+"/tmp";
        File path1 = new File(path);
        if(!path1.exists()){
            path1.mkdirs();
        }
        File file = new File(path1,"face"+".jpg");
        Uri photUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photUri);

        startActivityForResult(intent, TAKE_PHOTO);
    }

    public void pic()
    {
        //清除Bitmap
        if (null != mBitMap)
        {
            mBitMap.recycle();
            System.gc();
        }


        Intent intent = new Intent(Intent.ACTION_PICK);//ACTION_OPEN_DOCUMENT
        //        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/jpeg");

        startActivityForResult(intent, PIC_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.i(getClass().getSimpleName(), "Request ok with code[" + requestCode + "]");
        if (CROP_REQUEST_CODE == requestCode && resultCode == Activity.RESULT_OK)
        {
            String bitmap = data.getStringExtra("BITMAP_DATA");

            if (null != bitmap)
            {
                uploadPhoto(bitmap);
            }
            else
            {
                Toast.makeText(this, "", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 上传头像到服务器
     * @param bitmap    头像
     */
    private void uploadPhoto(String bitmap)
    {

    }
}
