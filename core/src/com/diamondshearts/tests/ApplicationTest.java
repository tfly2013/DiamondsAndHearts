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
	DiamondsHearts game;

	HeadlessApplication app;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		app = new HeadlessApplication(game);

		try {
			Thread.sleep(100);
		}
		catch (InterruptedException e) {
		}

	}

	@Test
	public void renderMethodIsInvokedAtLeastOnce() {
		verify(game, atLeast(1)).render();
	}

	@Test
	public void createMethodIsInvokedAtLeastOnce() {
		verify(game, atLeast(1)).create();
	}

	@Test
	public void pauseIsNotInvoked() {
		verify(game, times(0)).pause();
		app.exit();
		try {
			Thread.sleep(100);
		}
		catch (InterruptedException e) {
		}
		verify(game, times(1)).pause();
	}

	@Test
	public void disposeIsNotInvoked() {
		verify(game, times(0)).dispose();
		app.exit();
		try {
			Thread.sleep(100);
		}
		catch (InterruptedException e) {
		}

		verify(game, times(1)).dispose();
	}

}
