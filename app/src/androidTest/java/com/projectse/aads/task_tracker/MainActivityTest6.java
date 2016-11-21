package com.projectse.aads.task_tracker;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import com.projectse.aads.task_tracker.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest6 {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest6() {
        ViewInteraction relativeLayout = onView(
allOf(withId(R.id.over_due_btn),
childAtPosition(
childAtPosition(
withId(R.id.tasks_categories_buttons),
0),
0),
isDisplayed()));
        relativeLayout.check(matches(isDisplayed()));

        ViewInteraction relativeLayout2 = onView(
allOf(withId(R.id.over_due_btn),
childAtPosition(
childAtPosition(
withId(R.id.tasks_categories_buttons),
0),
0),
isDisplayed()));
        relativeLayout2.check(matches(isDisplayed()));

        ViewInteraction relativeLayout3 = onView(
allOf(withId(R.id.over_due_btn),
childAtPosition(
childAtPosition(
withId(R.id.tasks_categories_buttons),
0),
0),
isDisplayed()));
        relativeLayout3.check(matches(isDisplayed()));

        ViewInteraction relativeLayout4 = onView(
allOf(withId(R.id.over_due_btn), isDisplayed()));
        relativeLayout4.perform(click());

        ViewInteraction view = onView(
allOf(withId(R.id.toolbar),
childAtPosition(
childAtPosition(
withId(R.id.drawer_layout),
0),
0),
isDisplayed()));
        view.check(matches(isDisplayed()));

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
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
    }
