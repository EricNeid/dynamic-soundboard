package org.neidhardt.dynamicsoundboard.navigationdrawer.header.views;

import org.neidhardt.dynamicsoundboard.navigationdrawer.header.events.OpenSoundLayoutsEvent;
import org.neidhardt.dynamicsoundboard.navigationdrawer.soundlayouts.events.SoundLayoutRemovedEvent;
import org.neidhardt.dynamicsoundboard.navigationdrawer.soundlayouts.events.SoundLayoutRenamedEvent;
import org.neidhardt.dynamicsoundboard.navigationdrawer.soundlayouts.events.SoundLayoutSelectedEvent;
import org.neidhardt.dynamicsoundboard.navigationdrawer.soundlayouts.model.SoundLayoutModel;
import org.neidhardt.dynamicsoundboard.navigationdrawer.soundlayouts.views.SoundLayoutSettingsDialog;
import org.neidhardt.dynamicsoundboard.navigationdrawer.soundlayouts.views.SoundLayoutsPresenter;
import org.neidhardt.dynamicsoundboard.presenter.BaseViewPresenter;

/**
 * Created by eric.neidhardt on 27.05.2015.
 */
public class NavigationDrawerHeaderPresenter
		extends
			BaseViewPresenter<NavigationDrawerHeader>
		implements
			SoundLayoutSettingsDialog.OnSoundLayoutRenamedEventListener,
			SoundLayoutsPresenter.OnSoundLayoutRemovedEventListener,
			SoundLayoutsPresenter.OnSoundLayoutSelectedEventListener
{
	private static final String TAG = NavigationDrawerHeaderPresenter.class.getName();

	private SoundLayoutModel soundLayoutModel;

	public void setSoundLayoutModel(SoundLayoutModel soundLayoutModel)
	{
		this.soundLayoutModel = soundLayoutModel;
	}

	@Override
	protected boolean isEventBusSubscriber()
	{
		return true;
	}

	@Override
	public void onAttachedToWindow()
	{
		super.onAttachedToWindow();
		if (this.getView() == null)
			throw new NullPointerException(TAG +": supplied view is null");

		if (this.soundLayoutModel == null)
			throw new NullPointerException(TAG +": supplied model is null");

		this.getView().showCurrentLayoutName(this.soundLayoutModel.getActiveSoundLayout().getLabel());
	}

	@Override
	@SuppressWarnings("unused")
	public void onEvent(SoundLayoutRenamedEvent event)
	{
		if (this.getView() == null || this.soundLayoutModel == null)
			return;

		this.getView().showCurrentLayoutName(this.soundLayoutModel.getActiveSoundLayout().getLabel());
	}

	@Override
	@SuppressWarnings("unused")
	public void onEvent(SoundLayoutRemovedEvent event)
	{
		if (this.getView() == null || this.soundLayoutModel == null)
			return;

		this.getView().showCurrentLayoutName(this.soundLayoutModel.getActiveSoundLayout().getLabel());
	}

	@Override
	@SuppressWarnings("unused")
	public void onEvent(SoundLayoutSelectedEvent event)
	{
		if (this.getView() == null || this.soundLayoutModel == null)
			return;

		this.getView().animateLayoutChanges();
		this.getView().showCurrentLayoutName(this.soundLayoutModel.getActiveSoundLayout().getLabel());
	}

	public void onChangeLayoutClicked()
	{
		this.getView().animateLayoutChanges();
		this.getEventBus().post(new OpenSoundLayoutsEvent());
	}

	public interface OnOpenSoundLayoutsEvent
	{
		/**
		 * This is called by greenRobot EventBus when the user clicks on the open SoundLayouts button in navigation drawer header.
		 * @param event delivered OpenSoundLayoutsEvent
		 */
		void onEvent(OpenSoundLayoutsEvent event);
	}
}
