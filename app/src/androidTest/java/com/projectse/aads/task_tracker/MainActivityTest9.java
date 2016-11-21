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

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest9 {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest9() {
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
                allOf(withId(R.id.over_due_btn), isDisplayed()));
        relativeLayout2.perform(click());



       // onData(withId(R.id.supertask_info))

          //      .atPosition(0)
          //      .perform(click());


/*
        ViewInteraction linearLayout2 = onView(
                allOf(withId(R.id.supertask_info), isDisplayed()));
        linearLayout2.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.txtName), withText("Scala overview"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.flContent),
                                        0),
                                5),
                        isDisplayed()));
        editText.check(matches(withText("Scala overview")));

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_edittask), withContentDescription("edittask"), isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.txtName), withText("Scala overview"), isDisplayed()));
        appCompatEditText.perform(replaceText("Scala overviewq"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.txtName), withText("Scala overviewq"), isDisplayed()));
        appCompatEditText2.perform(pressImeActionButton());

        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.action_edittask), withContentDescription("savetask"), isDisplayed()));
        actionMenuItemView2.perform(click());

        pressBack();
*/
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
