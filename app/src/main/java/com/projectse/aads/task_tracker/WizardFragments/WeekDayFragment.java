package com.projectse.aads.task_tracker.WizardFragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.projectse.aads.task_tracker.Interfaces.ParentFragment;
import com.projectse.aads.task_tracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by smith on 4/19/16.
 */
public class WeekDayFragment extends Fragment{

    public int getDayOfWeek() {
        return day_of_week;
    }

    private int day_of_week;
    private int score = 0;
    private TextView scoreLabel;
    private TextView nameLabel;
    private onWeekDayEventListener weed_day_listener;

    public interface onWeekDayEventListener {
        public void scoreUpdated();
    }

    public WeekDayFragment(int day_of_week){
        super();
        this.day_of_week = day_of_week;
        if(!(day_of_week == Calendar.SATURDAY || day_of_week == Calendar.SUNDAY))
            score = 8;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wizzard_week_day, null);
        nameLabel = (TextView) view.findViewById(R.id.txtDay);
        nameLabel.setText(getDayNameByCalendarInt(day_of_week));
        scoreLabel = (TextView) view.findViewById(R.id.txtScore);
        ImageButton btnUp = (ImageButton )view.findViewById(R.id.btnUp);
        btnUp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                score = 8;
                updateScore();
                return true;
            }
        });
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incScore();
                updateScore();
            }
        });
        ImageButton btnDown = (ImageButton )view.findViewById(R.id.btnDown);
        btnDown.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                score = 0;
                updateScore();
                return true;
            }
        });
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decScore();
                updateScore();
            }
        });

        updateScore();

        return view;
    }

    public int getScore(){
        return score;
    }

    public void incScore(){
        if( (score + 1) <= 8 )
            score++;
    }

    public void decScore(){
        if( (score - 1) >= 0 )
            score--;
    }

    private void updateScore() {
        if(scoreLabel == null)
            return;
        scoreLabel.setText(String.valueOf(score));
        if(weed_day_listener != null)
            weed_day_listener.scoreUpdated();
    }

    private static String getDayNameByCalendarInt(int day){
        if(day < Calendar.SUNDAY && day > Calendar.SATURDAY)
            throw new IllegalArgumentException("Must be between Calendar.SUNDAY and Calendar.SATURDAY");

        Calendar date = Calendar.getInstance();
        date.set(Calendar.DAY_OF_WEEK, day);
        SimpleDateFormat sdl = new SimpleDateFormat("EEEE");
        return sdl.format(date.getTime());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Fragment parent = getParentFragment();
        if(parent instanceof ParentFragment)
            ((ParentFragment) parent).onChildCreated();
        if(parent instanceof onWeekDayEventListener)
            this.weed_day_listener = (onWeekDayEventListener) parent;
    }
}
