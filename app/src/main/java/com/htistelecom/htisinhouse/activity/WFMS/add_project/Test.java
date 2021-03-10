   package com.htistelecom.htisinhouse.activity.WFMS.add_project;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.htistelecom.htisinhouse.R;

public class Test extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_team_wfms);
        LinearLayout llMapFragmentTeamWFMS=(LinearLayout)findViewById(R.id.llMapFragmentTeamWFMS);
        llMapFragmentTeamWFMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

    }
}
