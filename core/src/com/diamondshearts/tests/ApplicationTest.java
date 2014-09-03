package com.diamondshearts.tests;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.diamondshearts.DiamondsHearts;

public class ApplicationTest {

	@Mock
	DiamondsHearts listener;

	HeadlessApplication app;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		app = new HeadlessApplication(listener);

		try {
			Thread.sleep(100);
		}
		catch (InterruptedException e) {
		}

	}

	@Test
	public void renderMethodIsInvokedAtLeastOnce() {
		verify(listener, atLeast(1)).render();
	}

	@Test
	public void createMethodIsInvokedAtLeastOnce() {
		verify(listener, atLeast(1)).create();
	}

	@Test
	public void pauseIsNotInvoked() {
		verify(listener, times(0)).pause();
		app.exit();
		try {
			Thread.sleep(100);
		}
		catch (InterruptedException e) {
		}
		verify(listener, times(1)).pause();
	}

	@Test
	public void disposeIsNotInvoked() {
		verify(listener, times(0)).dispose();
		app.exit();
		try {
			Thread.sleep(100);
		}
		catch (InterruptedException e) {
		}

		verify(listener, times(1)).dispose();
	}

}
