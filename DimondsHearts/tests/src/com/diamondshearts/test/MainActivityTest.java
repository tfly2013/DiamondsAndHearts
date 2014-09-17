package com.diamondshearts.test;

import android.test.ActivityInstrumentationTestCase2;

import com.diamondshearts.MainActivity;
import com.diamondshearts.R;
import com.google.android.gms.common.SignInButton;

public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity mainActivity;
	private SignInButton signInButton;

	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mainActivity = getActivity();
		signInButton = (SignInButton) mainActivity
				.findViewById(R.id.sign_in_button);
	}
	
	public void testSimple() {
		assertTrue(true);
	}

	public void testSignInButton() {
		assertTrue(signInButton != null);
	}
}
