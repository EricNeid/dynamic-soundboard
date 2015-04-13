package org.neidhardt.dynamicsoundboard.mediaplayer;

import org.junit.Test;
import org.neidhardt.dynamicsoundboard.ActivityTest;
import org.neidhardt.dynamicsoundboard.testutils.TestDataGenerator;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/**
 * Created by eric.neidhardt on 13.04.2015.
 */
public class EnhancedMediaPlayerTest extends ActivityTest
{

	@Test
	public void testSetSoundUri() throws Exception
	{
		EnhancedMediaPlayer playerToTest = new EnhancedMediaPlayer(TestDataGenerator.getRandomPlayerData());

		String testUri = TestDataGenerator.getRandomString();
		playerToTest.setSoundUri(testUri);

		assertThat(playerToTest.getMediaPlayerData().getUri(), equalTo(testUri));
	}
}