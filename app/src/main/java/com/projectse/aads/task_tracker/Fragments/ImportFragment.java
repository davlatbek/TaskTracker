package com.projectse.aads.task_tracker.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.projectse.aads.task_tracker.Adapters.CoursesAdapter;
import com.projectse.aads.task_tracker.Adapters.DayPlanOverviewAdapter;
import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Interfaces.ActualTasksCaller;
import com.projectse.aads.task_tracker.Interfaces.AddTaskCaller;
import com.projectse.aads.task_tracker.Interfaces.DoneTasksCaller;
import com.projectse.aads.task_tracker.Interfaces.OverdueTasksCaller;
import com.projectse.aads.task_tracker.MainActivity;
import com.projectse.aads.task_tracker.Models.CheckableCourseModel;
import com.projectse.aads.task_tracker.Models.CourseModel;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by smith on 4/20/16.
 */
public class ImportFragment extends Fragment {
    private AddTaskCaller addTaskCaller;
    private ActualTasksCaller actualTasksCaller;
    private DoneTasksCaller doneTasksCaller;
    private OverdueTasksCaller overdueTasksCaller;
    private DatabaseHelper db;
    private CoursesAdapter candidates_adapter;
    private CoursesAdapter exists_adapter;
    private List<CheckableCourseModel> candidates = new ArrayList<>();
    private List<CheckableCourseModel> exists = new ArrayList<>();
    private ImportFragment my;
    private TaskCategoriesCaller categoriesCaller;

    public interface TaskCategoriesCaller{
        public void callTasksCategory();
    }

    View.OnClickListener confirm_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(candidates_adapter.checkedCount() == 1 && exists_adapter.checkedCount()== 1){
                deleteChecked(candidates);
                candidates_adapter.notifyDataSetChanged();
                deleteChecked(exists);
                exists_adapter.notifyDataSetChanged();
            }else{
                Toast.makeText(getActivity(),"Check from both sides",Toast.LENGTH_SHORT);
            }
            if(candidates.size() == 0)
                categoriesCaller.callTasksCategory();
        }
    };

    private void deleteChecked(List<CheckableCourseModel> src){
        for(int i=0; i< src.size();i++){
            if(src.get(i).getChecked() && src.get(i).getCourseName() != "CREATE NEW")
                src.remove(i);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseHelper.getsInstance(getActivity().getApplicationContext());
        my = this;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof TaskCategoriesCaller)
            categoriesCaller = (TaskCategoriesCaller) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Import");
        View view = inflater.inflate(R.layout.fragment_import, container, false);

        ListView listCand = (ListView) view.findViewById(R.id.listCandidates);
        ListView listEx = (ListView) view.findViewById(R.id.listExists);

        candidates_adapter = new CoursesAdapter(getActivity(), R.id.listCandidates, candidates);
        listCand.setAdapter(candidates_adapter);

        exists_adapter = new CoursesAdapter(getActivity(), R.id.listExists, exists);
        listEx.setAdapter(exists_adapter);

        View confirm = view.findViewById(R.id.btnConfirm);
        confirm.setOnClickListener(confirm_listener);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setData(Map<String,List<TaskModel>> src){
        candidates.clear();
        for(String course_name : src.keySet()){
            CheckableCourseModel c = new CheckableCourseModel(course_name);
            c.setTasks(src.get(course_name));
            candidates.add(c);
        }
        if(candidates.size() > 0)
            candidates.get(0).setChecked(true);
        candidates_adapter.notifyDataSetChanged();
        exists.clear();
        exists.add(new CheckableCourseModel("CREATE NEW"));
        exists.get(0).setChecked(true);
        for(CourseModel course: db.getCourseModelList()){
            CheckableCourseModel c = new CheckableCourseModel(course);
            exists.add(c);
        }
        exists_adapter.notifyDataSetChanged();
    }
}