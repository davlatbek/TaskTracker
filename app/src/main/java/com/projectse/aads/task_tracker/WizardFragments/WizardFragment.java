package com.projectse.aads.task_tracker.WizardFragments;

import android.app.Activity;
import android.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.projectse.aads.task_tracker.DrawableHelper;
import com.projectse.aads.task_tracker.Interfaces.WizardManager;
import com.projectse.aads.task_tracker.R;
import com.projectse.aads.task_tracker.WizardActivity;

import java.io.IOException;

/**
 * Created by smith on 4/20/16.
 */
public abstract class WizardFragment extends Fragment{

    protected WizardActivity wizardActivity;
    protected WizardManager wizardManager;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof WizardActivity){
            wizardActivity = (WizardActivity) activity;
        }
        if(activity instanceof WizardManager){
            wizardManager = (WizardManager) activity;
        }
    }

    public void setDrawablesToImageViews(View view){
        if(view == null)
            view = getView();
        try{

            ImageView koala = (ImageView) view.findViewById(R.id.koala);
            if(koala != null)
                koala.setImageDrawable(DrawableHelper.getAssetImage(getActivity(),"koala"));

            ImageView cloud = (ImageView) view.findViewById(R.id.cloud);
            if(cloud != null)
                cloud.setImageDrawable(DrawableHelper.getAssetImage(getActivity(),"cloud"));
        } catch (IOException e) {
//            e.printStackTrace();
            Log.d("MyImageError",e.getLocalizedMessage());
            Log.d("MyImageError",e.toString());
        }
    }

}
