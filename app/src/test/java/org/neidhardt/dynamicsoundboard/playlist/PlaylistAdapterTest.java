package org.neidhardt.dynamicsoundboard.playlist;

import junit.framework.TestCase;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.mockito.Mockito;
import org.neidhardt.dynamicsoundboard.AbstractBaseActivityTest;
import org.neidhardt.dynamicsoundboard.dao.MediaPlayerData;
import org.neidhardt.dynamicsoundboard.mediaplayer.EnhancedMediaPlayer;
import org.neidhardt.dynamicsoundboard.mediaplayer.MediaPlayerCompletedEvent;
import org.neidhardt.dynamicsoundboard.testutils.TestDataGenerator;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;

/**
 * Created by eric.neidhardt on 20.04.2015.
 */
public class PlaylistAdapterTest extends AbstractBaseActivityTest
{

	@Test
	public void testGetValues() throws Exception
	{
		PlaylistAdapter adapter = spy(new PlaylistAdapter());
		adapter.setServiceManagerFragment(this.serviceManagerFragment);

		assertTrue(adapter.getValues().isEmpty());

		this.serviceManagerFragment.getPlayList().add(mock(EnhancedMediaPlayer.class));
		this.serviceManagerFragment.getPlayList().add(mock(EnhancedMediaPlayer.class));
		assertThat(adapter.getValues().size(), equalTo(2));
	}

	@Test
	public void testGetItemCount() throws Exception
	{
		PlaylistAdapter adapter = spy(new PlaylistAdapter());
		when(adapter.getValues()).thenReturn(this.serviceManagerFragment.getPlayList());

		assertThat(adapter.getItemCount(), equalTo(0));

		this.serviceManagerFragment.getPlayList().add(mock(EnhancedMediaPlayer.class));
		this.serviceManagerFragment.getPlayList().add(mock(EnhancedMediaPlayer.class));
		assertThat(adapter.getItemCount(), equalTo(2));
	}

	@Test
	public void testGetCurrentItemIndex() throws Exception
	{
		PlaylistAdapter adapter = new PlaylistAdapter();
		assertThat(adapter.getCurrentItemIndex(), equalTo(null));

		adapter.setCurrentItemIndex(1);
		assertThat(adapter.getCurrentItemIndex(), equalTo(1));
	}

	@Test
	public void testOnEvent() throws Exception
	{
		PlaylistAdapter adapter = spy(new PlaylistAdapter());
		when(adapter.getValues()).thenReturn(this.serviceManagerFragment.getPlayList());

		MediaPlayerData data = TestDataGenerator.getMediaPlayerData("player1", "");
		EnhancedMediaPlayer player = new EnhancedMediaPlayer(data);
		this.serviceManagerFragment.getPlayList().add(player);

		assertThat(adapter.getItemCount(), equalTo(1));
		assertThat(adapter.getCurrentItemIndex(), equalTo(null));
	}

	@Test
	public void testStartOrStopPlayList() throws Exception
	{
		PlaylistAdapter adapter = spy(new PlaylistAdapter());
		when(adapter.getValues()).thenReturn(this.serviceManagerFragment.getPlayList());

		EnhancedMediaPlayer playerA = mock(EnhancedMediaPlayer.class);

		boolean wasExceptionThrown = false;
		try {
			adapter.startOrStopPlayList(playerA); // should throw exception cause player is not in playlist
		}
		catch (IllegalStateException e)
		{
			wasExceptionThrown = true;
		}
		assertTrue(wasExceptionThrown);
	}

	@Test
	public void testStartOrStopPlayList1() throws Exception
	{
		PlaylistAdapter adapter = spy(new PlaylistAdapter());
		when(adapter.getValues()).thenReturn(this.serviceManagerFragment.getPlayList());

		EnhancedMediaPlayer playerA = mock(EnhancedMediaPlayer.class);
		EnhancedMediaPlayer playerB = mock(EnhancedMediaPlayer.class);

		this.serviceManagerFragment.getPlayList().add(playerA);
		this.serviceManagerFragment.getPlayList().add(playerB);

		when(playerA.isPlaying()).thenReturn(true);

		adapter.startOrStopPlayList(playerA);
		verify(playerA, atLeastOnce()).pauseSound();
		verify(playerB, atLeastOnce()).stopSound();
		assertThat(adapter.getCurrentItemIndex(), equalTo(0));
	}

	@Test
	public void testStartOrStopPlayList2() throws Exception
	{
		PlaylistAdapter adapter = spy(new PlaylistAdapter());
		when(adapter.getValues()).thenReturn(this.serviceManagerFragment.getPlayList());

		EnhancedMediaPlayer playerA = mock(EnhancedMediaPlayer.class);
		EnhancedMediaPlayer playerB = mock(EnhancedMediaPlayer.class);

		this.serviceManagerFragment.getPlayList().add(playerA);
		this.serviceManagerFragment.getPlayList().add(playerB);

		when(playerA.isPlaying()).thenReturn(false);

		adapter.startOrStopPlayList(playerA);
		verify(playerA, atLeastOnce()).playSound();
		verify(playerB, atLeastOnce()).stopSound();
		assertThat(adapter.getCurrentItemIndex(), equalTo(0));
	}

	@Test
	public void testStartOrStopPlayList3() throws Exception
	{
		PlaylistAdapter adapter = spy(new PlaylistAdapter());
		when(adapter.getValues()).thenReturn(this.serviceManagerFragment.getPlayList());

		EnhancedMediaPlayer playerA = mock(EnhancedMediaPlayer.class);
		EnhancedMediaPlayer playerB = mock(EnhancedMediaPlayer.class);

		this.serviceManagerFragment.getPlayList().add(playerA);
		this.serviceManagerFragment.getPlayList().add(playerB);

		when(playerB.isPlaying()).thenReturn(true);

		adapter.startOrStopPlayList(playerB);
		verify(playerA, atLeastOnce()).stopSound();
		verify(playerB, atLeastOnce()).pauseSound();
		assertThat(adapter.getCurrentItemIndex(), equalTo(1));
	}

	@Test
	public void testStartOrStopPlayList4() throws Exception
	{
		PlaylistAdapter adapter = spy(new PlaylistAdapter());
		when(adapter.getValues()).thenReturn(this.serviceManagerFragment.getPlayList());

		EnhancedMediaPlayer playerA = mock(EnhancedMediaPlayer.class);
		EnhancedMediaPlayer playerB = mock(EnhancedMediaPlayer.class);

		this.serviceManagerFragment.getPlayList().add(playerA);
		this.serviceManagerFragment.getPlayList().add(playerB);

		when(playerB.isPlaying()).thenReturn(false);

		adapter.startOrStopPlayList(playerB);
		verify(playerA, atLeastOnce()).stopSound();
		verify(playerB, atLeastOnce()).playSound();
		assertThat(adapter.getCurrentItemIndex(), equalTo(1));
	}

	@Test
	public void testStartOrStopPlayList5() throws Exception
	{
		PlaylistAdapter adapter = spy(new PlaylistAdapter());
		when(adapter.getValues()).thenReturn(this.serviceManagerFragment.getPlayList());

		EnhancedMediaPlayer playerA = mock(EnhancedMediaPlayer.class);
		EnhancedMediaPlayer playerB = mock(EnhancedMediaPlayer.class);

		this.serviceManagerFragment.getPlayList().add(playerA);
		this.serviceManagerFragment.getPlayList().add(playerB);

		when(playerB.isPlaying()).thenReturn(false);

		adapter.setCurrentItemIndex(1);
		adapter.startOrStopPlayList(playerA);
		verify(playerA, atLeastOnce()).playSound();
		verify(playerB, atLeastOnce()).stopSound();
		assertThat(adapter.getCurrentItemIndex(), equalTo(0));
	}
}