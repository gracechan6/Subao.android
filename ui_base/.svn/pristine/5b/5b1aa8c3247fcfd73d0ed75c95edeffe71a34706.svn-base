package com.jinwangmobile.ui.base.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinwangmobile.ui.base.R;


/**
 * Created by michael on 14/12/19.
 */
public class TabItemView extends RelativeLayout
{
    /**
     * tab item view
     */
    private TextView itemView;
    /**
     * badger view
     */
    private TextView badgerView;

    /**
     * 默认badger的大小
     */
    private static final int DEFAULT_BADGER_SIZE = 20;

    /**
     * 默认badger字体的大小
     */
    private static final int DEFAULT_BADGER_TEXT_SIZE = 12;

    /**
     * 默认badger字体的颜色
     */
    private static final int DEFAULT_BADGER_TEXT_COLOR = Color.WHITE;

    /*-------       Constructors    -------*/
    public TabItemView(Context context)
    {
        this(context, null, 0);
    }

    public TabItemView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public TabItemView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabItemView, 0, defStyleAttr);

        itemView = new TextView(context);
        itemView.setGravity(Gravity.CENTER);
        itemView.setId(android.R.id.text1);


        /*-----------------------Badger view attrs  -------------------*/
        badgerView = new TextView(context);
        badgerView.setGravity(Gravity.CENTER);
        // badger view的大小
        int badgerW = typedArray.getDimensionPixelSize(R.styleable.TabItemView_badgerWidth_, DEFAULT_BADGER_SIZE);
        int badgerH = typedArray.getDimensionPixelSize(R.styleable.TabItemView_badgerHeight_, DEFAULT_BADGER_SIZE);
        setBadgerSize(badgerW, badgerH);

        //badger background
        int bbg = typedArray.getResourceId(R.styleable.TabItemView_badgerBackground_, R.drawable.badger_bg_);
        setBadgerBackground(bbg);

        //是否显示badger
        boolean showBadger = typedArray.getBoolean(R.styleable.TabItemView_showBadger_, false);
        if (showBadger)
        {
            showBadger();
        }

        //badger 字体大小
        int textSize = typedArray.getDimensionPixelSize(R.styleable.TabItemView_badgerTextSize_, DEFAULT_BADGER_TEXT_SIZE);
        setBadgerTextSize(textSize);

        //badger 字体的颜色
        int textColor = typedArray.getColor(R.styleable.TabItemView_badgerTextColor_, DEFAULT_BADGER_TEXT_COLOR);
        setBadgerTextColor(textColor);

        //badger 的值
        int bvalue = typedArray.getInteger(R.styleable.TabItemView_badgerValue_, 0);
        setBadgerValue(bvalue);
        /*--------------------- Badger view attrs   -----------------*/


        /*--------------------- item view attrs ---------------------*/
        //tab item top drawable
        int itemDRes = typedArray.getResourceId(R.styleable.TabItemView_itemIcon_, 0);
        Drawable drawable = typedArray.getDrawable(R.styleable.TabItemView_itemIcon_);
        setItemDrawable(drawable);

        //tab item text
        String text = typedArray.getString(R.styleable.TabItemView_itemText_);
        setItemText(text);

        //drawable padding
        int dime = typedArray.getDimensionPixelSize(R.styleable.TabItemView_itemDrawablePadding_, 0);
        setItemDrawablePadding(dime);

        //item padding
        dime = typedArray.getDimensionPixelSize(R.styleable.TabItemView_itemPadding_, 0);
        int dimeL = typedArray.getDimensionPixelSize(R.styleable.TabItemView_itemPaddingLeft_, 0);
        int dimeT = typedArray.getDimensionPixelSize(R.styleable.TabItemView_itemPaddingTop_, 0);
        int dimeR = typedArray.getDimensionPixelSize(R.styleable.TabItemView_itemPaddingRight_, 0);
        int dimeB = typedArray.getDimensionPixelSize(R.styleable.TabItemView_itemPaddingBottom_, 0);
        if (0 != dime)
        {
            dimeL = dimeT = dimeR = dimeB = dime;
        }

        setItemPadding(dimeL, dimeT, dimeR, dimeB);

        //item text color
        ColorStateList color = typedArray.getColorStateList(R.styleable.TabItemView_itemTextColor_);
        if (null != color)
        {
            itemView.setTextColor(color);
        }


        /*--------------------- item view attrs ---------------------*/

        //add to root view
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(itemView, lp);

        lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(ALIGN_PARENT_TOP);
        lp.addRule(ALIGN_RIGHT, itemView.getId());

        //badger view margins
        int bMargin = typedArray.getDimensionPixelSize(R.styleable.TabItemView_badgerMargin_, -1);
        if (-1 != bMargin)
        {
            lp.leftMargin = lp.topMargin = lp.rightMargin = lp.leftMargin = bMargin;
        }
        else
        {
            bMargin = typedArray.getDimensionPixelSize(R.styleable.TabItemView_badgerMarginLeft_, 0);
            lp.leftMargin = bMargin;

            bMargin = typedArray.getDimensionPixelSize(R.styleable.TabItemView_badgerMarginTop_, 0);
            lp.topMargin = bMargin;

            bMargin = typedArray.getDimensionPixelSize(R.styleable.TabItemView_badgerMarginRight_, 0);
            lp.rightMargin = bMargin;

            bMargin = typedArray.getDimensionPixelSize(R.styleable.TabItemView_badgerMarginBottom_, 0);
            lp.bottomMargin = bMargin;
        }

        addView(badgerView, lp);
    }
    /*-------------------------------------*/

    /**
     * 设置badger的大小
     * @param width     width in px
     * @param height    height in px
     */
    public void setBadgerSize(int width, int height)
    {
        badgerView.setWidth(width);
        badgerView.setHeight(height);
    }

    /**
     * 设置badger的数字
     * @param value 值
     */
    public void setBadgerValue(int value)
    {
        if (0 == value)
        {
            hideBadger();
            badgerView.setText("");
        }
        else
        {
            showBadger();
            badgerView.setText(String.valueOf(value));
        }
    }

    /**
     * 设置badger字体的颜色
     * @param color 颜色
     */
    public void setBadgerTextColor(int color)
    {
        badgerView.setTextColor(color);
    }

    /**
     * 设置gadger的背景
     * @param res
     */
    public void setBadgerBackground(int res)
    {
        badgerView.setBackgroundResource(res);
    }

    /**
     * 显示Badger
     */
    public void showBadger()
    {
        badgerView.setVisibility(VISIBLE);
    }

    /**
     * 隐藏Badger
     */
    public void hideBadger()
    {
        badgerView.setVisibility(GONE);
    }

    /**
     * 设置badger字体大小
     * @param size 字体大小（PX)
     */
    public void setBadgerTextSize(int size)
    {
        badgerView.setTextSize(size);
    }

    /**
     * 设置item的内容边距
     * @param l 左边距
     * @param t 上边距
     * @param r 右边距
     * @param b 下边距
     */
    public void setItemPadding(int l, int t, int r, int b)
    {
        itemView.setPadding(l, t, r, b);
    }

    /**
     * 设置item图片和文字的间距
     * @param padding   间距
     */
    public void setItemDrawablePadding(int padding)
    {
        itemView.setCompoundDrawablePadding(padding);
    }

    /**
     * 设置item的文字
     * @param text  文字
     */
    public void setItemText(String text)
    {
        itemView.setText(text);
    }

    /**
     * 设置item文字的颜色
     * @param color 颜色
     */
    public void setItemTextColor(int color)
    {
        itemView.setTextColor(color);
    }

    /**
     * 设置item的图标
     * @param drawable   图标ID
     */
    public void setItemDrawable(Drawable drawable)
    {
        itemView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
    }
}
