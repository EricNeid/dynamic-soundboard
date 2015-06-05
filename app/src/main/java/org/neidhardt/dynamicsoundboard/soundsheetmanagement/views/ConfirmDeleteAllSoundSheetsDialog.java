package org.neidhardt.dynamicsoundboard.soundsheetmanagement.views;

import android.app.FragmentManager;
import org.neidhardt.dynamicsoundboard.R;
import org.neidhardt.dynamicsoundboard.dao.SoundSheet;
import org.neidhardt.dynamicsoundboard.soundactivity.SoundActivity;
import org.neidhardt.dynamicsoundboard.views.BaseConfirmDeleteDialog;

import java.util.List;

/**
 * File created by eric.neidhardt on 16.02.2015.
 */
public class ConfirmDeleteAllSoundSheetsDialog extends BaseConfirmDeleteDialog
{
	private static final String TAG = ConfirmDeleteAllSoundSheetsDialog.class.getName();

	public static void showInstance(FragmentManager manager)
	{
		ConfirmDeleteAllSoundSheetsDialog dialog = new ConfirmDeleteAllSoundSheetsDialog();
		dialog.show(manager, TAG);
	}

	@Override
	protected int getInfoTextResource()
	{
		return R.string.dialog_confirm_delete_all_soundsheets_message;
	}

	@Override
	protected void delete()
	{
		List<SoundSheet> soundSheets = SoundActivity.getSoundSheetsDataAccess().getSoundSheets();
		this.getSoundActivity().removeSoundFragments(soundSheets);

		SoundActivity.getSoundSheetsDataStorage().removeAllSoundSheets();
	}
}
