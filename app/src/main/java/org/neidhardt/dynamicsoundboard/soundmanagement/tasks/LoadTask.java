package org.neidhardt.dynamicsoundboard.soundmanagement.tasks;

import de.greenrobot.event.EventBus;
import org.neidhardt.dynamicsoundboard.misc.Logger;
import org.neidhardt.dynamicsoundboard.misc.progressbar.LongTermTaskEvent;
import org.neidhardt.dynamicsoundboard.misc.safeasyncTask.SafeAsyncTask;

import java.util.List;

/**
 * Created by eric.neidhardt on 24.03.2015.
 */

public abstract class LoadTask<T> extends SafeAsyncTask<List<T>>
{
	private static final String TAG = LoadTask.class.getName();

	@Override
	protected void onPreExecute() throws Exception
	{
		super.onPreExecute();
		EventBus.getDefault().postSticky(new LongTermTaskEvent(true));
	}

	@Override
	protected void onSuccess(List<T> ts) throws Exception
	{
		super.onSuccess(ts);
		Logger.d(TAG, "onSuccess: with " + ts.size() + " sounds loaded");
		//EventBus.getDefault().post(new LongTermTaskEvent(false));
	}

	@Override
	protected void onException(Exception e) throws RuntimeException
	{
		super.onException(e);
		Logger.e(TAG, e.getMessage());
		//EventBus.getDefault().post(new LongTermTaskEvent(false));
		throw new RuntimeException(e);
	}

}
