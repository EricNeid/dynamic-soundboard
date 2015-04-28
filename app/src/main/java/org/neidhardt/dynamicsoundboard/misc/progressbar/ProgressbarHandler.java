package org.neidhardt.dynamicsoundboard.misc.progressbar;

import de.greenrobot.event.EventBus;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import org.neidhardt.dynamicsoundboard.misc.Logger;

/**
 * Created by eric.neidhardt on 28.04.2015.
 */
public class ProgressbarHandler
{
	private static final String TAG = ProgressbarHandler.class.getName();

	private SmoothProgressBar progressBar;
	private int pendingEventCounter;
	private boolean isActive;

	public ProgressbarHandler(SmoothProgressBar progressBar)
	{
		this.progressBar = progressBar;
		this.pendingEventCounter = 0;
		this.isActive = false;
	}

	public void showProgressBar(boolean showProgressBar)
	{
		Logger.d(TAG, "showProgressBar() " + showProgressBar);
		if (showProgressBar)
		{
			//if (this.progressBar.getVisibility() != View.VISIBLE)
			//	this.progressBar.setVisibility(View.VISIBLE);

			//this.progressBar.progressiveStart();

			// TODO maybe play progressive Start

			this.isActive = true;
		}
		else
		{
			this.progressBar.progressiveStop();

			//this.progressBar.setVisibility(View.GONE);
			this.isActive = false;
		}
	}

	public void onEvent(LongTermTaskStartedEvent event)
	{
		Logger.d(TAG, "onEvent() " + event);
		EventBus.getDefault().removeStickyEvent(event);
		this.pendingEventCounter++;
		this.showProgressBar(true);
	}

	public void onEvent(LongTermTaskStoppedEvent event)
	{
		Logger.d(TAG, "onEvent() " + event);
		EventBus.getDefault().removeStickyEvent(event);
		if (this.pendingEventCounter > 0)
			this.pendingEventCounter--;
		if (this.pendingEventCounter == 0)
			this.showProgressBar(false);
	}

	public int getPendingEventCounter()
	{
		return pendingEventCounter;
	}
}
