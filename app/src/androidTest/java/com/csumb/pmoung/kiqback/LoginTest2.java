package com.csumb.pmoung.kiqback;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;


/**
 * Created by BRX01 on 11/7/2016.
 */

public class LoginTest2 {
    @Rule
    public ActivityTestRule<Login> mLoginActivityTestRule = new ActivityTestRule<Login>(Login.class);

    @Test
    public void mainScreen_loads() {
        onView(withId(R.id.text_details)).check(matches(withText("Welcome!")));
    }

    @Test
    public void loginFacebook(){
        onView(withId(R.id.login_button)).perform(click());
    }
}
