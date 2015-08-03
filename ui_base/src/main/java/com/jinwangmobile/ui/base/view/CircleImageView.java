package com.jinwangmobile.ui.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.jinwangmobile.ui.base.R;

public class CircleImageView extends ImageView {


  private static final int DEFAULT_BORDER_WIDTH = 0;
  private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
  private  RectF mDrawableRect = new RectF();
  private  RectF mBorderRect = new RectF();

  private final Paint mBitmapPaint = new Paint();
  private final Paint mBorderPaint = new Paint();
  
  private final Matrix mShaderMatrix = new Matrix();
  private int mBitmapWidth;
  private int mBitmapHeight;

  private int mBorderColor ;
  private int mBorderWidth ;

  private Bitmap mBitmap;
  private BitmapShader mBitmapShader;
  private float mDrawableRadius;
  private float mBorderRadius;

  private boolean mReady;

  public CircleImageView(Context context) {
    super(context);
  }

  public CircleImageView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0);

    mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_border_width_, DEFAULT_BORDER_WIDTH);
    mBorderColor = a.getColor(R.styleable.CircleImageView_border_color_, DEFAULT_BORDER_COLOR);
    a.recycle();
    mReady = true;
  }



  @Override
  protected void onDraw(Canvas canvas) {
    if (getDrawable() == null) {
      return;
    }

    canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius, mBitmapPaint);
    canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius, mBorderPaint);
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    setup();
  }

  @Override
  public void setImageDrawable(Drawable drawable) {
    super.setImageDrawable(drawable);
    mBitmap = getBitmapFromDrawable(drawable);
    setup();
  }

  private Bitmap getBitmapFromDrawable(Drawable drawable) {
    if (drawable == null) {
      return null;
    }

    if (drawable instanceof BitmapDrawable) {
      return ((BitmapDrawable) drawable).getBitmap();
    }

      Bitmap bitmap = null;
      Canvas canvas = new Canvas(bitmap);
      drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
      drawable.draw(canvas);
      return bitmap;
  }

  private void setup() {
    if (!mReady) {
      return;
    }
    if (mBitmap == null) {
      return;
    }

    mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

    mBitmapPaint.setAntiAlias(true);
    mBitmapPaint.setShader(mBitmapShader);

    mBorderPaint.setStyle(Paint.Style.STROKE);
    mBorderPaint.setAntiAlias(true);
    mBorderPaint.setColor(mBorderColor);
    mBorderPaint.setStrokeWidth(mBorderWidth);
    
    mBitmapHeight = mBitmap.getHeight();
    mBitmapWidth = mBitmap.getWidth();

    mBorderRect.set(0, 0, getWidth(), getHeight());
    mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2, (mBorderRect.width() - mBorderWidth) / 2);

    mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);
    mDrawableRadius = Math.min(mDrawableRect.height() / 2, mDrawableRect.width() / 2);
    
    updateShaderMatrix();
    invalidate();
  }
  
  private void updateShaderMatrix() {
	    float scale;
	    float dx = 0;
	    float dy = 0;

	    mShaderMatrix.set(null);

	    if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
	      scale = mDrawableRect.height() / (float) mBitmapHeight;
	      dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
	    } else {
	      scale = mDrawableRect.width() / (float) mBitmapWidth;
	      dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
	    }

	    mShaderMatrix.setScale(scale, scale);
	    mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth, (int) (dy + 0.5f) + mBorderWidth);

	    mBitmapShader.setLocalMatrix(mShaderMatrix);
	  }
}
