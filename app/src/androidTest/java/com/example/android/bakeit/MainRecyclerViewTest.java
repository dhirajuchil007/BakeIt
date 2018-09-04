package com.example.android.bakeit;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class MainRecyclerViewTest {

    public static final String TEST_TEXT="Step:1";
@Rule
    public ActivityTestRule<MainActivity> mActivityTestRule=new ActivityTestRule<>(MainActivity.class,true,true       );
private IdlingResource idlingResource;



@Before
public void registerIdlingResource(){


       idlingResource=mActivityTestRule.getActivity().getIdlingResource();
    Espresso.registerIdlingResources(idlingResource);
}
    @Test
    public  void clickRecyclerViewItem(){

        onView(withId(R.id.recipe_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.recipe_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.step_no)).check(matches(withText(TEST_TEXT)));


    }
    @After
    public void unregisterIdlingResource(){
   if(idlingResource!=null)
        Espresso.unregisterIdlingResources(idlingResource);
   }

}
