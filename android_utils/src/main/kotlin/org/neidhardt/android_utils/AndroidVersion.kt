package org.neidhardt.android_utils

import android.os.Build

/**
 * Created by eric.neidhardt on 30.11.2016.
 */
object AndroidVersion
{
	val IS_LOLLIPOP_AVAILABLE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
}