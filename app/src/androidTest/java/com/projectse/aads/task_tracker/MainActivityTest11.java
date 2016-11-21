package com.projectse.aads.task_tracker;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.AdapterView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest11 {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest11() {
        ViewInteraction relativeLayout = onView(
                allOf(withId(R.id.actual_btn), isDisplayed()));
        relativeLayout.perform(click());

      //  onData(withId(R.id.supertask_info))

        //        .atPosition(0)
          //      .perform(click());

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.supertask_info), isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.txtName), withText("Prepare to Exam"), isDisplayed()));
        appCompatEditText.perform(pressImeActionButton());

        pressBack();

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.txtName), withText("Prepare to Exam"), isDisplayed()));
        appCompatEditText2.perform(replaceText("Prepare to Examm "), closeSoftKeyboard());

        pressBack();

        pressBack();

    }

}
