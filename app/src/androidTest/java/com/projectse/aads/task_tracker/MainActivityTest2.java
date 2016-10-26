package com.projectse.aads.task_tracker;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest2 {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest2() {
        ViewInteraction relativeLayout = onView(
                allOf(withId(R.id.over_due_btn), isDisplayed()));
        relativeLayout.perform(click());

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.supertask_info), isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction switch_ = onView(
                allOf(withId(R.id.doneSwitch), withText("Done"), isDisplayed()));
        switch_.perform(click());

        ViewInteraction switch_2 = onView(
                allOf(withId(R.id.doneSwitch), withText("Done"), isDisplayed()));
        switch_2.perform(click());

        ViewInteraction switch_3 = onView(
                allOf(withId(R.id.timer), withText("Timer"), isDisplayed()));
        switch_3.perform(click());

        ViewInteraction imageButton = onView(
                allOf(withContentDescription("Перейти вверх"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        imageButton.perform(click());

        ViewInteraction imageButton2 = onView(
                allOf(withContentDescription("Перейти вверх"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        imageButton2.perform(click());

        ViewInteraction relativeLayout2 = onView(
                allOf(withId(R.id.add_task_btn), isDisplayed()));
        relativeLayout2.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.txtName), isDisplayed()));
        appCompatEditText.perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_addtask), withContentDescription("addtask"), isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.action_addtask), withContentDescription("addtask"), isDisplayed()));
        actionMenuItemView2.perform(click());

        ViewInteraction actionMenuItemView3 = onView(
                allOf(withId(R.id.action_addtask), withContentDescription("addtask"), isDisplayed()));
        actionMenuItemView3.perform(click());

        ViewInteraction actionMenuItemView4 = onView(
                allOf(withId(R.id.action_addtask), withContentDescription("addtask"), isDisplayed()));
        actionMenuItemView4.perform(click());

        ViewInteraction actionMenuItemView5 = onView(
                allOf(withId(R.id.action_addtask), withContentDescription("addtask"), isDisplayed()));
        actionMenuItemView5.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.txtDateDeadline),
                        withParent(withId(R.id.linearLayout)),
                        isDisplayed()));
        appCompatEditText2.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btnDateDeadline),
                        withParent(withId(R.id.linearLayout)),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.txtDateDeadline),
                        withParent(withId(R.id.linearLayout)),
                        isDisplayed()));
        appCompatEditText3.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.txtDateDeadline),
                        withParent(withId(R.id.linearLayout)),
                        isDisplayed()));
        appCompatEditText4.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btnDateDeadline),
                        withParent(withId(R.id.linearLayout)),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button1), withText("ОК"),
                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(android.R.id.button1), withText("ОК"),
                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction actionMenuItemView6 = onView(
                allOf(withId(R.id.action_addtask), withContentDescription("addtask"), isDisplayed()));
        actionMenuItemView6.perform(click());

    }

}
