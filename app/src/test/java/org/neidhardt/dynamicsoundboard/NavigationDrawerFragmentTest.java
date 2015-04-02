package org.neidhardt.dynamicsoundboard;

import android.widget.TextView;
import com.neidhardt.testutils.CustomTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neidhardt.dynamicsoundboard.soundlayouts.SoundLayoutsManager;
import org.robolectric.Robolectric;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/**
 * Created by eric.neidhardt on 02.04.2015.
 */
@RunWith(CustomTestRunner.class)
public class NavigationDrawerFragmentTest
{
	BaseActivity activity;
	NavigationDrawerFragment fragment;

	@Before
	public void setUp() throws Exception
	{
		this.activity = Robolectric.setupActivity(BaseActivity.class);
		this.fragment = (NavigationDrawerFragment) activity.getFragmentManager().findFragmentByTag(NavigationDrawerFragment.TAG);
	}

	@Test
	public void testSetLayoutName() throws Exception
	{
		assertNotNull(this.fragment);

		TextView textView = (TextView) this.activity.findViewById(R.id.tv_current_sound_layout_name);
		assertThat(textView.getText().toString(), equalTo(activity.getResources().getString(R.string.sound_layout_default)));

		String testString = "test";
		this.fragment.setLayoutName(testString);
		assertThat(textView.getText().toString(), equalTo(testString));
	}
}