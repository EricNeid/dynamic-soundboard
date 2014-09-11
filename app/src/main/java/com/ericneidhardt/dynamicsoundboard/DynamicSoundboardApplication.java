package com.ericneidhardt.dynamicsoundboard;

import android.app.Application;
import android.content.Context;
import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import java.util.Random;

/**
 * Created by eric.neidhardt on 27.08.2014.
 */
@ReportsCrashes(
	formKey = "", // This is required for backward compatibility but not used
	mailTo = "eric@neidhardt-erkner.de"
)
public class DynamicSoundboardApplication extends Application
{
	private static Random random;
	private static Context applicationContext;

	@Override
	public void onCreate()
	{
		super.onCreate();
		ACRA.init(this);

		random = new Random();
		applicationContext = this;
	}

	public static Context getContext()
	{
		return applicationContext;
	}

	public static int getRandomNumber()
	{
		return random.nextInt(Integer.MAX_VALUE);
	}
}
