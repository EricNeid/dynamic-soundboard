package org.neidhardt.dynamicsoundboard;

import android.os.Environment;
import org.apache.maven.artifact.ant.shaded.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.neidhardt.robolectricutils.CustomTestRunner;
import org.robolectric.shadows.ShadowEnvironment;

import java.io.File;

import static junit.framework.TestCase.assertTrue;

/**
 * File created by eric.neidhardt on 18.04.2015.
 */
@RunWith(CustomTestRunner.class)
public abstract class BaseTest
{

	@Before
	public void setUp() throws Exception
	{
		ShadowEnvironment.setExternalStorageState(Environment.MEDIA_MOUNTED);
		// nothing to be done
	}

	@After
	public void tearDown() throws Exception
	{
		FileUtils.cleanDirectory(Environment.getExternalStorageDirectory());

		ShadowEnvironment.setExternalStorageState(Environment.MEDIA_UNMOUNTED);
	}

	public static File createFile(String fileName) throws Exception
	{
		File newFile = new File(ShadowEnvironment.getExternalStorageDirectory(), fileName);
		if (!newFile.exists())
			assertTrue(newFile.createNewFile());
		return newFile;
	}

}
