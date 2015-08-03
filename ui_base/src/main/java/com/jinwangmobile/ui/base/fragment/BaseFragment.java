package com.jinwangmobile.ui.base.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment
{
    /**
     * Debug tag
     */
    private String DEBUG_TAG;
    
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		Log.i(DEBUG_TAG, "On Activity Created");
		
		super.onActivityCreated(savedInstanceState);
	}

    @Override
    public void onSaveInstanceState( Bundle outState )
    {
        Log.i(DEBUG_TAG, "On Save Instance State");

        super.onSaveInstanceState( outState );
    }

    @Override
    public void onViewStateRestored(  Bundle savedInstanceState )
    {
        Log.i(DEBUG_TAG, "On View State Restored");

        super.onViewStateRestored( savedInstanceState );
    }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		Log.i(DEBUG_TAG, "On Activity Result");
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onAttach(Activity activity)
	{
		Log.i(DEBUG_TAG, "On Attack");
		
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
        DEBUG_TAG = getClass().getSimpleName();
		Log.i(DEBUG_TAG, "On Create");
		
		super.onCreate(savedInstanceState);
//        setRetainInstance( true );
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		Log.i(DEBUG_TAG, "On Create Options Menu");
		
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		Log.i(DEBUG_TAG, "On Create View");
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroy()
	{
		Log.i(DEBUG_TAG, "On Destroy");
		
		super.onDestroy();
	}

	@Override
	public void onDestroyOptionsMenu()
	{
		Log.i(DEBUG_TAG, "On DestroyOptionsMenu");
		
		super.onDestroyOptionsMenu();
	}

	@Override
	public void onDestroyView()
	{
		Log.i(DEBUG_TAG, "On DestroyView");
		
		super.onDestroyView();
	}

	@Override
	public void onDetach()
	{
		Log.i(DEBUG_TAG, "On Detach");
		
		super.onDetach();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Log.i(DEBUG_TAG, "On OptionsItemSelected");
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPause()
	{
		Log.i(DEBUG_TAG, "On Pause");
		
		super.onPause();
	}

	@Override
	public void onResume()
	{
		Log.i(DEBUG_TAG, "On Resume");
		
		super.onResume();
	}

	@Override
	public void onStart()
	{
		Log.i(DEBUG_TAG, "On Start");
		
		super.onStart();
	}

	@Override
	public void onStop()
	{
		Log.i(DEBUG_TAG, "On Stop");
		
		super.onStop();
	}

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState )
    {
        Log.i(DEBUG_TAG, "On View Created");

        super.onViewCreated( view, savedInstanceState );
    }
}
