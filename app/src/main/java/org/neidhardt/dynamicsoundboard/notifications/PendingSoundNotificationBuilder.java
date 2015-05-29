package org.neidhardt.dynamicsoundboard.notifications;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import org.neidhardt.dynamicsoundboard.R;
import org.neidhardt.dynamicsoundboard.mediaplayer.EnhancedMediaPlayer;
import org.neidhardt.dynamicsoundboard.misc.IntentRequest;
import org.neidhardt.dynamicsoundboard.misc.Util;
import org.neidhardt.dynamicsoundboard.soundactivity.SoundActivity;

/**
 * File created by eric.neidhardt on 04.12.2014.
 */
public class PendingSoundNotificationBuilder extends Notification.Builder
{
	private String playerId;
	public String getPlayerId()
	{
		return this.playerId;
	}

	private int notificationId;
	public int getNotificationId()
	{
		return this.notificationId;
	}

	public PendingSoundNotificationBuilder(Context context, EnhancedMediaPlayer player)
	{
		this(context, player, player.getMediaPlayerData().getPlayerId().hashCode());
	}

	public PendingSoundNotificationBuilder(Context context, EnhancedMediaPlayer player, int notificationId)
	{
		this(context, player, notificationId, player.getMediaPlayerData().getLabel(), null);
	}

	public PendingSoundNotificationBuilder(Context context, EnhancedMediaPlayer player, int notificationId, String title, String message)
	{
		super(context);
		this.playerId = player.getMediaPlayerData().getPlayerId();
		this.notificationId = notificationId;

		boolean isLollipopStyleAvailable = Util.IS_LOLLIPOP_AVAILABLE;
		this.setActionStop(context, isLollipopStyleAvailable);
		if (player.isPlaying())
		{
			this.setOngoing(true);
			this.setActionFadeOut(context, isLollipopStyleAvailable);
		}
		else
		{
			this.setOngoing(false);
			this.setActionPlay(context, isLollipopStyleAvailable);
		}

		this.setSmallIcon(R.drawable.ic_stat_pending_sounds);
		this.setDeleteIntent(this.getPendingIntent(context, NotificationIds.ACTION_DISMISS));
		this.setContentIntent(this.getOpenActivityIntent(context));

		this.setContentTitle(title);
		this.setContentText(message);
		this.setScaledLargeIcon(this.getLargeIcon(context, player.getMediaPlayerData().getUri()));

		if (isLollipopStyleAvailable)
			this.addStyleLollipop();
	}

	private Bitmap getLargeIcon(Context context, String uri)
	{
		MediaMetadataRetriever mediaDataReceiver = new MediaMetadataRetriever();
		mediaDataReceiver.setDataSource(context, Uri.parse(uri));

		Resources resources = context.getResources();
		int requiredHeight = resources.getDimensionPixelSize(android.R.dimen.notification_large_icon_height);
		int requiredWidth = resources.getDimensionPixelSize(android.R.dimen.notification_large_icon_width);

		byte [] data = mediaDataReceiver.getEmbeddedPicture();
		if (data != null)
		{
			Point size = BitmapUtil.getBitmapSize(data);
			int sampleSize = BitmapUtil.getSampleFactor(size.x, size.y, requiredWidth, requiredHeight);
			return BitmapUtil.getBitmap(data, sampleSize);
		}
		else
		{
			Point size = BitmapUtil.getBitmapSize(resources, R.drawable.ic_notification_large);
			int sampleSize = BitmapUtil.getSampleFactor(size.x, size.y, requiredWidth, requiredHeight);
			return BitmapUtil.getBitmap(context, R.drawable.ic_notification_large, sampleSize);
		}
	}

	private void setScaledLargeIcon(Bitmap bitmap)
	{
		if (bitmap == null)
			return;
		this.setLargeIcon(bitmap);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private void addStyleLollipop()
	{
		Notification.MediaStyle style = new Notification.MediaStyle();
		style.setShowActionsInCompactView(1); // index of action play/pause
		this.setStyle(style);
		this.setCategory(Notification.CATEGORY_TRANSPORT);
	}

	private void setActionStop(Context context, boolean isLollipopStyleAvailable)
	{
		this.addAction(R.drawable.ic_notification_stop, isLollipopStyleAvailable ? context.getString(R.string.notification_stop_sound) : "",
				this.getPendingIntent(context, NotificationIds.ACTION_STOP));
	}

	private void setActionPlay(Context context, boolean isLollipopStyleAvailable)
	{
		this.addAction(R.drawable.ic_notification_play, isLollipopStyleAvailable ? context.getString(R.string.notification_play_sound) : "",
				this.getPendingIntent(context, NotificationIds.ACTION_PLAY));
	}

	private void setActionFadeOut(Context context, boolean isLollipopStyleAvailable)
	{
		this.addAction(R.drawable.ic_notification_pause, isLollipopStyleAvailable ? context.getString(R.string.notification_pause_sound) : "",
				this.getPendingIntent(context, NotificationIds.ACTION_FADE_OUT));
	}

	private PendingIntent getPendingIntent(Context context, String action)
	{
		Intent intent = new Intent(action);
		intent.putExtra(NotificationIds.KEY_PLAYER_ID, this.playerId);
		intent.putExtra(NotificationIds.KEY_NOTIFICATION_ID, this.notificationId);
		return PendingIntent.getBroadcast(context, this.getRequestCode(), intent, 0);
	}

	private PendingIntent getOpenActivityIntent(Context context)
	{
		Intent intent = new Intent(context, SoundActivity.class);
		return PendingIntent.getActivity(context, IntentRequest.NOTIFICATION_OPEN_ACTIVITY, intent, 0);
	}

	private int getRequestCode()
	{
		if (this.notificationId != NotificationIds.NOTIFICATION_ID_PLAYLIST)
			return this.notificationId;
		return Integer.toString(this.notificationId + this.playerId.hashCode()).hashCode();
	}

	public static IntentFilter getNotificationIntentFilter()
	{
		IntentFilter filter = new IntentFilter();
		filter.addAction(NotificationIds.ACTION_DISMISS);
		filter.addAction(NotificationIds.ACTION_PLAY);
		filter.addAction(NotificationIds.ACTION_STOP);
		filter.addAction(NotificationIds.ACTION_FADE_OUT);
		return filter;
	}
}
