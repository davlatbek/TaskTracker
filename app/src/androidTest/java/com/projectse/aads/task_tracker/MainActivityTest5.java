package com.projectse.aads.task_tracker;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest5 {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest5() {
        ViewInteraction relativeLayout = onView(
                allOf(withId(R.id.add_task_btn), isDisplayed()));
        relativeLayout.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.txtName), withText("Type a name for a task..."),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.flContent),
                                        0),
                                2),
                        isDisplayed()));
        editText.check(matches(withText("NewTask")));

        ViewInteraction button = onView(
                allOf(withId(R.id.btnDateDeadline),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                                2)),
                                3),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_addtask), withContentDescription("addtask"), isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.action_addtask), withContentDescription("addtask"), isDisplayed()));
        actionMenuItemView2.perform(click());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
