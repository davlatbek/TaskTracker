package com.projectse.aads.task_tracker.WizzardFragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.projectse.aads.task_tracker.Adapters.DayPlanOverviewAdapter;
import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Fragments.WeekSliderFragment;
import com.projectse.aads.task_tracker.Interfaces.ParentFragment;
import com.projectse.aads.task_tracker.Interfaces.WizzardManager;
import com.projectse.aads.task_tracker.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class WeekFragment extends Fragment implements WeekSliderFragment.onWeekSliderEventListener,WeekDayFragment.onWeekDayEventListener, ParentFragment {
    private HashMap<Integer, Integer> scores = new HashMap<>(7);
    private List<WeekDayFragment> daysFrgments = new ArrayList<>();

    @Override
    public void scoreUpdated() {
        updateTotal();
    }

    public interface onWeekViewEventListener{
        public void callPlanFragment(Calendar first_day, int day_of_week);
    }

    private DatabaseHelper db;

    List<DayPlanOverviewAdapter> adapters = new ArrayList<>();
    private static View view;
    private WeekSliderFragment sliderFragment;

    private WizzardManager wizzardManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = DatabaseHelper.getsInstance(getActivity().getApplicationContext());

        FragmentManager fm = getChildFragmentManager();
        sliderFragment = new WeekSliderFragment();
        sliderFragment.setSomeEventListener(this);
        fm.beginTransaction().replace(R.id.fragment_week_slider, sliderFragment).commit();
        fm.executePendingTransactions();

        scores.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(getResources().getString(R.string.creating_plan_title));
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_wizzard_week, null);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        for(int day = Calendar.SUNDAY; day <= Calendar.SATURDAY; day++) {
            int id = -1;
            switch (day) {
                case Calendar.MONDAY:
                    id = R.id.monday;
                    break;
                case Calendar.TUESDAY:
                    id = R.id.tuesday;
                    break;
                case Calendar.WEDNESDAY:
                    id = R.id.wednesday;
                    break;
                case Calendar.THURSDAY:
                    id = R.id.thursday;
                    break;
                case Calendar.FRIDAY:
                    id = R.id.friday;
                    break;
                case Calendar.SATURDAY:
                    id = R.id.saturday;
                    break;
                case Calendar.SUNDAY:
                    id = R.id.sunday;
                    break;
            }
            WeekDayFragment df = new WeekDayFragment(day);
            daysFrgments.add(df);
            ft.replace(id, df);
        }
        ft.commit();
        fm.executePendingTransactions();

        TextView tooltip = (TextView) view.findViewById(R.id.txtTooltip);
        tooltip.setText(getResources().getString(R.string.week_tooltip));

        Button prev = (Button) view.findViewById(R.id.btnPrev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wizzardManager.callIntroFragment();
            }
        });
        Button next = (Button) view.findViewById(R.id.btnNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wizzardManager.callTasksFragment();
            }
        });

        return view;
    }

    private void updateTotal() {
        Integer total = 0;
        for(WeekDayFragment df : daysFrgments)
            total += df.getScore();

        TextView textTotal = (TextView) getView().findViewById(R.id.txtTotal);
        textTotal.setText(String.valueOf(total));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof WizzardManager){
            wizzardManager = (WizzardManager) activity;
        }
    }

    @Override
    public void setWeek(Calendar week_first_date) {
        if(sliderFragment != null)
            sliderFragment.updateLabel();

    }

    /**
     * Child fragments are creating async-ly.
     * To manage async views creation call this method in onViewCreated() of child fragment.
     */
    @Override
    public void onChildCreated(){
        if(sliderFragment.getView() != null) {
            Calendar week_first_day = Calendar.getInstance();
            week_first_day.setFirstDayOfWeek(Calendar.MONDAY);
            week_first_day.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            setWeek(week_first_day);
        }
        if(daysFrgments.size() == 7)
            updateTotal();
    }
}
