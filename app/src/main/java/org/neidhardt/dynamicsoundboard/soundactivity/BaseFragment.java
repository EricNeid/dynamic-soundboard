package org.neidhardt.dynamicsoundboard.soundactivity;


import android.app.Fragment;
import org.neidhardt.dynamicsoundboard.R;
import org.neidhardt.dynamicsoundboard.navigationdrawer.NavigationDrawerFragment;
import org.neidhardt.dynamicsoundboard.soundcontrol.SoundSheetFragment;

public abstract class BaseFragment extends Fragment
{
	public SoundActivity getBaseActivity()
	{
		return (SoundActivity)this.getActivity();
	}

	public NavigationDrawerFragment getNavigationDrawerFragment()
	{
		return (NavigationDrawerFragment)this.getFragmentManager().findFragmentById(R.id.navigation_drawer_fragment);
	}
}
