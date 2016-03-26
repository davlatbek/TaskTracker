package com.projectse.aads.task_tracker.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.AttributeSet;

public class XMLFragment extends Fragment {

    @Override
    public void onInflate(Activity activity, AttributeSet attrs,
                          Bundle savedInstanceState) {

        FragmentManager fm = getFragmentManager();
        if (fm != null) {
            fm.beginTransaction().remove(this).commit();
        }

        super.onInflate(activity, attrs, savedInstanceState);
    }
}
