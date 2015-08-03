package com.jinwangmobile.ui.base;

/**
 * Created by michael on 15/7/31.
 * 混合开发JS与原生交互接口
 */
public interface JSInterface {
    /**
     * 设置标题
     * @param title 标题
     */
    public void jsSetTitle(String title);

    /**
     * 显示新的页面
     * @param data  数据，包括传递的参数和其它数据
     */
    public void jsShowPage(Object data);

    /**
     * 返回到上一界面
     * @param data  数据，包括传递的参数和其它数据
     */
    public void jsGoBack(Object data);

    /**
     * java script 传递数据给原生
     *
     * @param data: 数据，包括传递的参数和其它数据
     */
    public void jsTransferData(Object data);
}
