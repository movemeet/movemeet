package com.sdp.movemeet;

import android.content.Context;
import android.view.View;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class FirebaseUsersLoginTest {

    public static final String mEmail = "email";
    public static final String mPassword = "password";
    public static final String shortPassword = "pass";
    //mPassword = findViewById(R.id.edit_text_password);
    //progressBar = findViewById(R.id.progressBar);
    //fAuth = FirebaseAuth.getInstance();
    //mLoginBtn = findViewById(R.id.button_login);
    //mCreateBtn = findViewById(R.id.text_view_create_account);
    //mLoginBtn.setOnClickListener(new View.OnClickListener()

    @Rule
    public ActivityScenarioRule<FirebaseUsersLogin> testRule = new ActivityScenarioRule<>(FirebaseUsersLogin.class);

    @Test
    public void Login_NonEmpty() {
        onView(withId(R.id.edit_text_email))
                .perform(typeText(mEmail), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password))
                .perform(typeText(mPassword), closeSoftKeyboard());
        onView(withId(R.id.button_login)).perform(click());

    }

    @Test
    public void Login_EmptyMail() {

        onView(withId(R.id.button_login)).perform(click());
    }

    @Test
    public void Login_EmptyPassword() {
        onView(withId(R.id.edit_text_email))
                .perform(typeText(mEmail), closeSoftKeyboard());
        onView(withId(R.id.button_login)).perform(click());
    }

    @Test
    public void Login_Password() {
        onView(withId(R.id.edit_text_email))
                .perform(typeText(mEmail), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password))
                .perform(typeText(shortPassword), closeSoftKeyboard());
        onView(withId(R.id.button_login)).perform(click());
    }

    @Test
    public void Register() {
        Intents.init();

        onView(withId(R.id.text_view_create_account)).perform(click());

        Intents.release();
    }
}
