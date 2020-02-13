package com.example.oblig1;

import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.view.KeyEvent;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.Iterator;


import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class QuizTest {

    @Rule
    public ActivityTestRule<MainActivity> startMain = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void onBefore(){
        Intents.init();
    }

    @Test
    public void startQuiz(){
     try{
         onView(withId(R.id.startButton)).perform(click());
     }catch(NoMatchingViewException e){
         onView(withId(999)).perform(typeText("Tester"));
         onView(withText("OK")).perform(click());
         onView(withId(R.id.startButton)).perform(click());

     }
    Quiz quizActivity = (Quiz) getActivityInstance();
    Image image = quizActivity.getCurrentImage();
    onView(withId(R.id.submitText)).perform(typeText("Feil svar")).perform(pressKey(KeyEvent.KEYCODE_ENTER));

    }

    @After
    public void onAfter() {
        Intents.release();
    }

    /**
     *
     * @return Current activity
     */
    private Activity getActivityInstance() {
        final Activity[] currentActivity = {null};

        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection<Activity> resumedActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                Iterator<Activity> it = resumedActivity.iterator();
                currentActivity[0] = it.next();
            }
        });

        return currentActivity[0];
    }
}
