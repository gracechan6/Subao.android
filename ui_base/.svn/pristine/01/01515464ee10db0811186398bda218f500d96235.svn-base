package com.jinwangmobile.ui.base.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.jinwangmobile.ui.base.R;

/**
 * Created by Michael on 2014/9/22.
 */
public abstract class BaseTabHostFragment extends BaseFragment
{
    /**
     * tab bar的位置是否在底部
     */
    protected boolean tabBarPositionBottom = false;

    protected FragmentTabHost tabHost;

    /*----------    Constructors ------------*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        int layout = tabBarPositionBottom ? R.layout.tab_host_bottom_ : R.layout.tab_host_top_;

        View view = inflater.inflate(layout, container, false);
        tabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
        tabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        tabHost.setOnTabChangedListener(tabChangeListener);


        TabWidget tabWidget = tabHost.getTabWidget();
        tabWidget.setBackgroundResource(R.drawable.tab_widget_shape_);
        tabWidget.setDividerDrawable(null);

        initTabs();


        return view;
    }

    /**
     * tab changed listener
     */
    private TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener()
    {
        @Override
        public void onTabChanged(String tabTag)
        {
            onTabItemChanged(tabHost, tabTag);
        }
    };

    /**
     * tab content changed call back
     * @param tabHost   tabhost holds tabs
     * @param tabTag     tab id
     */
    public void onTabItemChanged(TabHost tabHost, String tabTag)
    {
        Log.i(getClass().getName(), "On tab changed, Tab tag[" + tabTag + "]");
    }

    /**
     * 初始化
     */
    protected abstract void initTabs();
}
