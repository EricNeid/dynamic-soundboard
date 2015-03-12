package org.neidhardt.dynamicsoundboard.dialog.soundlayouts;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import org.neidhardt.dynamicsoundboard.R;
import org.neidhardt.dynamicsoundboard.customview.edittext.CustomEditText;

/**
 * Created by eric.neidhardt on 12.03.2015.
 */
public abstract class SoundLayoutDialog extends DialogFragment implements View.OnClickListener
{
	private CustomEditText soundLayoutName;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		@SuppressLint("InflateParams") View view = this.getActivity().getLayoutInflater().inflate(this.getLayoutId(), null);
		this.soundLayoutName = (CustomEditText)view.findViewById(R.id.et_name_sound_layout);
		this.soundLayoutName.setHint(this.getHintForName());

		view.findViewById(R.id.b_cancel).setOnClickListener(this);
		view.findViewById(R.id.b_ok).setOnClickListener(this);

		AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
		builder.setView(view);

		return builder.create();
	}

	protected abstract int getLayoutId();

	protected abstract String getHintForName();

	@Override
	public void onClick(View v)
	{
		int id = v.getId();
		if (id == R.id.b_cancel)
			this.dismiss();
		else if (id == R.id.b_ok)
			this.deliverResult();
	}

	protected abstract void deliverResult();
}
