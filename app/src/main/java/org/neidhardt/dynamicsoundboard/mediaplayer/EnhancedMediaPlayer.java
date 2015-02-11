package org.neidhardt.dynamicsoundboard.mediaplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import de.greenrobot.event.EventBus;
import org.neidhardt.dynamicsoundboard.DynamicSoundboardApplication;
import org.neidhardt.dynamicsoundboard.dao.MediaPlayerData;
import org.neidhardt.dynamicsoundboard.misc.Logger;
import org.neidhardt.dynamicsoundboard.playlist.Playlist;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class EnhancedMediaPlayer extends MediaPlayer implements MediaPlayer.OnCompletionListener, Runnable
{
	private static final String TAG = EnhancedMediaPlayer.class.getName();

	private static final int FADE_OUT_DURATION = 100;
	private static final int INT_VOLUME_MAX = 100;
	private static final int INT_VOLUME_MIN = 0;
	private static final float FLOAT_VOLUME_MAX = 1;
	private static final float FLOAT_VOLUME_MIN = 0;

	private enum State
	{
		IDLE,
		INIT,
		PREPARED,
		STARTED,
		STOPPED,
		PAUSED,
		DESTROYED
	}

	private State currentState;
	private MediaPlayerData rawData;
	private int duration;
	private int volume;

	private Set<OnMediaPlayerStateChangedListener> onMediaPlayerChangedListeners;
	private Handler handler = new Handler();

	public EnhancedMediaPlayer(Context context, MediaPlayerData data) throws IOException
	{
		super();

		this.onMediaPlayerChangedListeners = new HashSet<>();
		this.rawData = data;
		this.setLooping(data.getIsLoop());

		this.currentState = State.IDLE;
		this.init(context);
	}

	public static EnhancedMediaPlayer getInstanceForPlayList(Context context, MediaPlayerData data) throws IOException
	{
		MediaPlayerData playListData = new MediaPlayerData();
		playListData.setId(data.getId());
		playListData.setIsInPlaylist(true);
		playListData.setPlayerId(data.getPlayerId());
		playListData.setFragmentTag(Playlist.TAG);
		playListData.setIsLoop(false);
		playListData.setLabel(data.getLabel());
		playListData.setUri(data.getUri());

		return new EnhancedMediaPlayer(context, playListData);
	}

	public static MediaPlayerData getMediaPlayerData(String fragmentTag, Uri uri, String label)
	{
		MediaPlayerData data = new MediaPlayerData();

		String playerId = Integer.toString((uri.toString() + DynamicSoundboardApplication.getRandomNumber()).hashCode());
		data.setPlayerId(playerId);
		data.setFragmentTag(fragmentTag);
		data.setLabel(label);
		data.setUri(uri.toString());
		data.setIsInPlaylist(false);
		data.setIsLoop(false);

		return data;
	}

	private void init(Context context) throws IOException
	{
		if (this.rawData.getUri() == null)
			throw new NullPointerException("cannot init media player, sound uri is null");

		this.setAudioStreamType(AudioManager.STREAM_MUSIC);
		this.setDataSource(context, Uri.parse(this.rawData.getUri()));
		this.setLooping(this.rawData.getIsLoop());
		this.prepare();
		this.currentState = State.PREPARED;

		this.volume = INT_VOLUME_MAX;
		this.duration = super.getDuration();
		this.setOnCompletionListener(this);
	}

	public void destroy()
	{
		this.handler.removeCallbacks(this);
		this.currentState = State.DESTROYED;
		this.reset();
		this.release();
		this.postEvent(false);
	}

	public MediaPlayerData getMediaPlayerData()
	{
		return this.rawData;
	}

	@Override
	public int getDuration()
	{
		return this.duration;
	}

	@Override
	public boolean isPlaying()
	{
		return currentState != State.DESTROYED && super.isPlaying();
	}

	@Override
	public void setLooping(boolean looping)
	{
		super.setLooping(looping);
		this.rawData.setIsLoop(looping);
	}

	public void setIsInPlaylist(boolean inPlaylist)
	{
		this.rawData.setIsInPlaylist(inPlaylist);
	}

	public boolean playSound()
	{
		try
		{
			if (this.currentState == State.INIT || this.currentState == State.STOPPED)
				this.prepare();

			this.volume = INT_VOLUME_MAX;
			this.updateVolume(this.volume);

			this.start();
			this.currentState = State.STARTED;

			this.postEvent(true);
			return true;
		}
		catch (IOException e)
		{
			Logger.e(TAG, e.toString());
			return false;
		}
		catch (IllegalStateException e)
		{
			Logger.e(TAG, e.toString());
			return false;
		}
	}

	public boolean stopSound()
	{
		if (this.pauseSound())
		{
			this.seekTo(0);
			return true;
		}
		return false;
	}

	public boolean pauseSound()
	{
		try
		{
			switch (this.currentState)
			{
				case INIT:
					this.prepare();
					this.start();
					break;
				case PREPARED:
					this.start();
					break;
				case STARTED:
					break;
				case PAUSED:
					break;
				case STOPPED:
					this.prepare();
					this.start();
					break;
				default:
					return false; // should not be reached
			}
			this.pause();
			this.currentState = State.PAUSED;
			this.triggerOnMediaPlayerStateChangedListeners(false);

			this.postEvent(false);
			return true;
		}
		catch (IOException e)
		{
			Logger.e(TAG, e.getMessage());
			return false;
		}
		catch (IllegalStateException e)
		{
			Logger.e(TAG, e.getMessage());
			return false;
		}
	}

	public void fadeOutSound()
	{
		this.updateVolume(0);
		this.scheduleNextVolumeChange();
	}

	private void scheduleNextVolumeChange()
	{
		int delay = FADE_OUT_DURATION / INT_VOLUME_MAX;
		this.handler.postDelayed(this, delay);
	}

	@Override
	public void run()
	{
		updateVolume(-1);
		if (volume == INT_VOLUME_MIN)
		{
			updateVolume(INT_VOLUME_MAX);
			pauseSound();
			triggerOnMediaPlayerStateChangedListeners(false);
		}
		else
			scheduleNextVolumeChange();
	}

	private void updateVolume(int change)
	{
		this.volume = this.volume + change;

		//ensure volume within boundaries
		if (this.volume < INT_VOLUME_MIN)
			this.volume = INT_VOLUME_MIN;
		else if (this.volume > INT_VOLUME_MAX)
			this.volume = INT_VOLUME_MAX;

		//convert to float value
		float fVolume = 1 - ((float) Math.log(INT_VOLUME_MAX - this.volume) / (float) Math.log(INT_VOLUME_MAX));

		//ensure fVolume within boundaries
		if (fVolume < FLOAT_VOLUME_MIN)
			fVolume = FLOAT_VOLUME_MIN;
		else if (fVolume > FLOAT_VOLUME_MAX)
			fVolume = FLOAT_VOLUME_MAX;

		this.setVolume(fVolume, fVolume);
	}

	public boolean setPositionTo(int timePosition)
	{
		try
		{
			switch (this.currentState)
			{
				case INIT:
					this.prepare();
					this.seekTo(timePosition);
					this.currentState = State.PREPARED;
					return true;
				case PREPARED:
					this.seekTo(timePosition);
					return true;
				case STARTED:
					this.seekTo(timePosition);
					return true;
				case PAUSED:
					this.seekTo(timePosition);
					return true;
				case STOPPED:
					this.prepare();
					this.seekTo(timePosition);
					this.currentState = State.PREPARED;
					return true;
				default:
					return false;
			}
		}
		catch (IOException e)
		{
			Logger.e(TAG, e.getMessage());
			return false;
		}
		catch (IllegalStateException e)
		{
			Logger.e(TAG, e.getMessage());
			return false;
		}
	}

	public void addOnMediaPlayerStateChangedListener(OnMediaPlayerStateChangedListener listener)
	{
		this.onMediaPlayerChangedListeners.add(listener);
	}

	@Override
	public void onCompletion(MediaPlayer mp)
	{
		this.triggerOnMediaPlayerStateChangedListeners(true);
		this.postEvent(true);
	}

	private void triggerOnMediaPlayerStateChangedListeners(boolean hasPlayerCompleted)
	{
		for (OnMediaPlayerStateChangedListener listener : this.onMediaPlayerChangedListeners)
			listener.onMediaPlayerStateChanged(this, hasPlayerCompleted);
	}

	private void postEvent(boolean isAlive)
	{
		EventBus.getDefault().post(new MediaPlayerStateChangedEvent(isAlive, this.rawData.getPlayerId()));
	}

	public static interface OnMediaPlayerStateChangedListener
	{
		public void onMediaPlayerStateChanged(MediaPlayer player, boolean hasPlayerCompleted);
	}

}
