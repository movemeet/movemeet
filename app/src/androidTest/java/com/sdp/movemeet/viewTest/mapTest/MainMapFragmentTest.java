package com.sdp.movemeet.viewTest.mapTest;

import android.Manifest;
import android.location.Location;

import com.android21buttons.fragmenttestrule.FragmentTestRule;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.R;
import com.sdp.movemeet.models.Sport;
import com.sdp.movemeet.view.map.MainMapFragment;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.sdp.movemeet.models.Sport.Badminton;
import static com.sdp.movemeet.models.Sport.Boxing;
import static com.sdp.movemeet.models.Sport.Climbing;
import static com.sdp.movemeet.models.Sport.Dancing;
import static com.sdp.movemeet.models.Sport.Golf;
import static com.sdp.movemeet.models.Sport.Gym;
import static com.sdp.movemeet.models.Sport.Hockey;
import static com.sdp.movemeet.models.Sport.Pingpong;
import static com.sdp.movemeet.models.Sport.Rugby;
import static com.sdp.movemeet.models.Sport.Running;
import static com.sdp.movemeet.models.Sport.Soccer;
import static com.sdp.movemeet.models.Sport.Swimming;
import static com.sdp.movemeet.models.Sport.Tennis;
import static com.sdp.movemeet.models.Sport.Trekking;
import static com.sdp.movemeet.models.Sport.VolleyBall;
import static com.sdp.movemeet.models.Sport.Windsurfing;
import static com.sdp.movemeet.models.Sport.Yoga;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class MainMapFragmentTest {

    private UiDevice uiDevice;
    private FirebaseAuth fAuth;
    private FirebaseUser user;

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

   @Rule
   public FragmentTestRule<?, MainMapFragment> fragmentTestRule =
           FragmentTestRule.create(MainMapFragment.class);


    @Before
    public void setUp() {
        //The map is tested when the user is logged in
        fAuth = FirebaseAuth.getInstance();
        fAuth.signInWithEmailAndPassword("test@test.com", "password").addOnCompleteListener(task -> {
            user = fAuth.getCurrentUser();
            uiDevice = UiDevice.getInstance(getInstrumentation());
        });
    }

    @Test
    public void mainMapFragmentIsDisplayedAndGMapsNotNull() throws InterruptedException {
        onView(withId(R.id.fragment_map)).check(matches((isDisplayed())));
    }

    @Test
    public void mainMapFragment_MarkerOnMapForUser() throws UiObjectNotFoundException, InterruptedException {
        waitFor(5000);
        UiObject marker = uiDevice.findObject(new UiSelector().descriptionContains("I am here !"));
        assertNotNull(marker);
    }


    @Test
    public void testChooseIcons() throws InterruptedException {
        MainMapFragment mapFragment = fragmentTestRule.getFragment();
        waitFor(2000);
        assertEquals(R.drawable.icon_boxing, setSportIcon(Boxing, mapFragment), 0);
        assertEquals(R.drawable.icon_windsurfing, setSportIcon(Windsurfing, mapFragment), 0);
        assertEquals(R.drawable.icon_dancing, setSportIcon(Dancing, mapFragment), 0);
        assertEquals(R.drawable.icon_yoga, setSportIcon(Yoga, mapFragment), 0);
        assertEquals(R.drawable.icon_climbing, setSportIcon(Climbing, mapFragment), 0);
        assertEquals(R.drawable.icon_golf, setSportIcon(Golf, mapFragment), 0);
        assertEquals(R.drawable.icon_gym, setSportIcon(Gym, mapFragment), 0);
        assertEquals(R.drawable.icon_soccer, setSportIcon(Soccer, mapFragment), 0);
        assertEquals(R.drawable.icon_tennis, setSportIcon(Tennis, mapFragment), 0);
        assertEquals(R.drawable.icon_volleyball, setSportIcon(VolleyBall, mapFragment), 0);
        assertEquals(R.drawable.icon_hockey, setSportIcon(Hockey, mapFragment), 0);
        assertEquals(R.drawable.icon_pingpong, setSportIcon(Pingpong, mapFragment), 0);
        assertEquals(R.drawable.icon_trekking, setSportIcon(Trekking, mapFragment), 0);
        assertEquals(R.drawable.icon_rugby, setSportIcon(Rugby, mapFragment), 0);
        assertEquals(R.drawable.icon_badminton, setSportIcon(Badminton, mapFragment), 0);
        assertEquals(R.drawable.icon_running, setSportIcon(Running, mapFragment), 0);
        assertEquals(R.drawable.icon_swim, setSportIcon(Swimming, mapFragment), 0);
    }

    private int setSportIcon(Sport sport, @NotNull MainMapFragment mapFragment) {
        Activity activity = new Activity("activity id", "organizer id", "title", 2, new ArrayList<>(), 0, 0,
                "description", "documentPath", new Date(), 1, sport,"here", new Date());
        return mapFragment.chooseIcon(activity);
    }

    @Test
    public void mainMapFragment_userClickingOnMapAddsNewActivity() throws UiObjectNotFoundException, InterruptedException {
        waitFor(5000);
        //User must be logged to add new activity
        assertNotNull(user);

        //fragmentTestRule.launchFragment(new MainMapFragment());

        waitFor(2000);

        uiDevice.click(200, 500);

        waitFor(1000);

        UiObject marker = uiDevice.findObject(new UiSelector().descriptionContains("new activity"));
        assertNotNull(marker);

        uiDevice.click(200, 400);
    }

    @After
    public void after() {
        fAuth.signOut();
    }


    private void waitFor(int duration) throws InterruptedException {
        Thread.sleep(duration);
    }
}