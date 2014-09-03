package com.diamondshearts.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;
import com.diamondshearts.tests.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class AssetFilesTest {

	@Test
	public void badlogicLogoExists() {
		assertTrue(Gdx.files.internal("../android/assets/badlogic.jpg").exists());
	}

}