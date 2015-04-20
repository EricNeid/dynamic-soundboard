package org.neidhardt.dynamicsoundboard.misc;

import android.net.Uri;
import org.junit.Test;
import org.neidhardt.dynamicsoundboard.AbstractBaseActivityTest;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

/**
 * Created by eric.neidhardt on 14.04.2015.
 */
public class FileUtilsTest extends AbstractBaseActivityTest
{

	@Test
	public void testGetFileForUri() throws Exception
	{
		File file = new File("test.mp3");
		assertFalse(file.exists());
		File testFile = FileUtils.getFileForUri(this.activity, Uri.fromFile(file));
		assertNull(testFile);

		file = this.createFile("test.mp3");
		assertNotNull(file);
		assertTrue(file.exists());

		testFile = FileUtils.getFileForUri(this.activity, Uri.fromFile(file));
		assertNotNull(testFile);
		assertEquals(file.getAbsolutePath(), testFile.getAbsolutePath());
		assertEquals(Uri.fromFile(file), Uri.fromFile(testFile));
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void testStripFileTypeFromName() throws Exception
	{
		String testName = null;
		boolean wasExceptionThrown = false;
		try
		{
			FileUtils.stripFileTypeFromName(testName);
		}
		catch (NullPointerException e) {
			wasExceptionThrown = true;
		}
		assertTrue(wasExceptionThrown);

		testName = "test";
		assertThat(FileUtils.stripFileTypeFromName(testName), equalTo("test"));

		testName = "test.mp3";
		assertThat(FileUtils.stripFileTypeFromName(testName), equalTo("test"));

		testName = "test.test.mp3";
		assertThat(FileUtils.stripFileTypeFromName(testName), equalTo("test.test"));
	}
}