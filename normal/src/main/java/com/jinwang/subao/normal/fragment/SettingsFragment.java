package com.jinwang.subao.normal.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.jinwang.subao.normal.R;
import com.jinwangmobile.ui.base.fragment.BaseFragment;

/**
 * Created by dreamy on 2015/6/24.
 */
public class SettingsFragment extends BaseFragment {
    private RelativeLayout relAbout;
    private RelativeLayout relAgreement;
    private RelativeLayout relSuggestion;
    private RelativeLayout changePasswd;
    private RelativeLayout relAddress;
    private RelativeLayout relUpdate;
    private Button btnExit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_setting, null);
        initView(view);
        initUserInfo();
        return view;
    }

    private void initView(View view){
        relAbout = (RelativeLayout) view.findViewById(R.id.rel_about);
        relAbout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        relAgreement = (RelativeLayout) view.findViewById(R.id.rel_agreement);
        relAgreement.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        relSuggestion = (RelativeLayout) view.findViewById(R.id.rel_suggestion);
        relSuggestion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        changePasswd = (RelativeLayout) view.findViewById(R.id.rel_change_passwd);
        changePasswd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        relAddress = (RelativeLayout) view.findViewById(R.id.rel_address);
        relAddress.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        relUpdate = (RelativeLayout) view.findViewById(R.id.rel_update);
        relUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        btnExit = (Button) view.findViewById(R.id.exit_btn);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 初始化用户信息
     */
    private void initUserInfo()
    {
//        setHeadPhoto();
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState ){
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

}
