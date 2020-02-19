package com.example.oblig1;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.Iterator;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import static androidx.test.espresso.action.ViewActions.longClick;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    @Rule
    public ActivityTestRule<MainActivity> startMain = new ActivityTestRule<>(MainActivity.class);

    private ImageRepository repo;

    @Before
    public void onBefore(){
            repo = new ImageRepository(startMain.getActivity().getApplication());
            nukeTable();
            Bitmap bm1 = BitmapFactory.decodeResource(startMain.getActivity().getResources(), R.drawable.jake);
            Image img1 = new Image("Jake", bm1);
            addImageToDB(repo,img1);
            MainActivity.dataBaseIsEmpty = false;
        }

    @Test
    public void startDatabase(){
        try{
            onView(withId(R.id.DB)).perform(click());
        }catch(NoMatchingViewException e){
            onView(withId(999)).perform(typeText("Tester"));
            onView(withText("OK")).perform(click());
            onView(withId(R.id.DB)).perform(click());
        }
        Database databaseActivity = (Database) getActivityInstance();
        int countBefore = databaseActivity.getCount();
        onView(withId(R.id.button5)).perform(click());
        Bitmap bm2 = BitmapFactory.decodeResource(databaseActivity.getResources(), R.drawable.hitchcock);
        Image img2 = new Image("Hitchcock", bm2);
        addImageToDB(repo,img2);
        Bitmap bm3 = BitmapFactory.decodeResource(databaseActivity.getResources(), R.drawable.boyle);
        Image img3 = new Image("Boyle", bm3);
        addImageToDB(repo,img3);
        onView(withId(R.id.DB)).perform(click());
        Database databaseActivity2 = (Database) getActivityInstance();
        int countAfter = databaseActivity2.getCount();
        assertEquals(countBefore+2, countAfter);
    }

    @Test
    public void checkDelete(){
        try{
            onView(withId(R.id.DB)).perform(click());
        }catch(NoMatchingViewException e){
            onView(withId(999)).perform(typeText("Tester"));
            onView(withText("OK")).perform(click());
            onView(withId(R.id.DB)).perform(click());
        }
        Database databaseActivity = (Database) getActivityInstance();
        int countBefore = databaseActivity.getCount();
        onView(withText("Jake")).perform(longClick());
        onView(withText("Confirm")).perform(click());
        int countAfter = databaseActivity.getCount();
        assertEquals(countAfter, countBefore-1);
    }

    public static void addImageToDB(final ImageRepository repo, Image image){

    final Image imagetoAdd = image;
        class SaveImage extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {

                repo.getImageDao().addImage(imagetoAdd);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

            }
        }
        SaveImage si = new SaveImage();
        si.execute();
    }

    private void nukeTable(){

        class SaveImage extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                repo.getImageDao().nukeTable();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

            }
        }
        SaveImage si = new SaveImage();
        si.execute();
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
