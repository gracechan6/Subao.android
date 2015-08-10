package com.jinwang.subao.normal.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.jinwang.subao.normal.R;
import com.jinwangmobile.ui.base.activity.BaseActivity;
import com.jinwang.subao.normal.config.ConstantConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
        Log.i(getClass().getSimpleName(), "enter setOnCropedListener");
        mOnCropedListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.i(getClass().getSimpleName(), "enter onCreate");
        setContentView(R.layout.activity_crop);
        initView();
    }

    private void initView(){
        Log.i(getClass().getSimpleName(), "enter initView");
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
                cancel();
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
        Log.i(getClass().getSimpleName(), "enter upload "+(ConstantConfig.TEMP_FILE_PATH + File.separator + System.currentTimeMillis() + ".png"));
        //保存图片到文件，传递文件名给调用者
        Log.i(getClass().getSimpleName(), "enter upload");
        File file2 = new File(ConstantConfig.TEMP_FILE_PATH);
        Log.i(getClass().getSimpleName(), "enter upload"+(file2.exists()));
        if (!file2.exists()) {
            file2.mkdir();
        }
        Log.i(getClass().getSimpleName(), "enter upload"+(file2.exists()));
        File file = new File(ConstantConfig.TEMP_FILE_PATH + File.separator + System.currentTimeMillis() + ".png");
        Log.i(getClass().getSimpleName(), "enter upload 0"+file2.exists());
        try
        {
            Log.i(getClass().getSimpleName(), "enter upload 1"+file.exists());
            file.createNewFile();
            Log.i(getClass().getSimpleName(), "enter upload 2"+file.exists());
            OutputStream outputStream = new FileOutputStream(file);
            Log.i(getClass().getSimpleName(), "enter upload 3"+file.exists());;
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
        Log.i(getClass().getSimpleName(), "enter takePhoto");
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
        Log.i(getClass().getSimpleName(), "enter pic");
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

    //将图片放大缩小独立出来
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(getClass().getSimpleName(), "enter onActivityResult");
        //图片选择后返回选择文件名，按文件名称打开图片文件，进行裁剪
        //如果图片太大，图片会以适应屏幕宽度进行重新转换大小，以显示
        if (requestCode == PIC_REQUEST_CODE && resultCode == RESULT_OK && null != data)
        {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Log.i(getClass().getSimpleName(), "cropdata!=null" + picturePath);
            crop(picturePath);
        }
        else if (requestCode == TAKE_PHOTO && resultCode == RESULT_OK)
        {
            String path = Environment.getExternalStorageDirectory().toString()+"/tmp";
            Log.i(getClass().getSimpleName(), "cropdata==null" + (path + File.separator + "face.jpg"));
            crop(path + File.separator + "face.jpg");
        }else if(resultCode==Activity.RESULT_CANCELED){
            finish();
            return;
        }

        /*Log.i(getClass().getSimpleName(), "Request ok with code[" + requestCode + "]");
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
        }*/
    }

    private void crop(String picturePath)
    {
        Log.i(getClass().getSimpleName(), "enter crop"+picturePath);
        int inSize = 1;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = inSize;
        options.inScreenDensity = getWindowInfo(this).densityDpi;
        options.inDensity = getWindowInfo(this).densityDpi;
        options.inTargetDensity = getWindowInfo(this).densityDpi;
        options.inScaled = true;

        InputStream inputStream = null;
        try
        {
            inputStream = new FileInputStream(new File(picturePath));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        if (null != inputStream)
        {
            mBitMap = BitmapFactory.decodeStream(inputStream, new Rect(0, 0, 0, 0), options);

            float scale = (float) getScreenWidth(this) / mBitMap.getWidth();

            //图片太大，进行缩放，屏幕大小
            if (scale < 1)
            {
                Matrix matrix = new Matrix();
                matrix.setScale(scale, scale);
                mBitMap = Bitmap.createBitmap(mBitMap, 0, 0, mBitMap.getWidth(), mBitMap.getHeight(), matrix, true);
            }

            Log.i(getClass().getSimpleName(), "With[" + mBitMap.getWidth() + "], Height[" + mBitMap.getHeight() + "]");
            mCropImageView.setImageBitmap(mBitMap);
        }
    }
    /**
     * 获取屏幕宽度
     */
    public static final int getScreenWidth(Context context)
    {
        DisplayMetrics metric = getWindowInfo(context);
        return metric.widthPixels;
    }
    /**
     * 获取屏幕属性
     */
    public static final DisplayMetrics getWindowInfo(Context context)
    {
        DisplayMetrics metric = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metric);

        return metric;
    }
    /**
     * 获取屏幕高度
     */
    public static final int getScreenHeight(Context context)
    {
        DisplayMetrics metric = getWindowInfo(context);

        return metric.heightPixels;
    }
    /**
     * 上传头像到服务器
     * @param bitmap    头像
     */
    private void uploadPhoto(String bitmap)
    {

    }
}
