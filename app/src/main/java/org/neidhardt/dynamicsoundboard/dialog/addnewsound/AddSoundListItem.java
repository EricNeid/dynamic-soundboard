package org.neidhardt.dynamicsoundboard.dialog.addnewsound;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.neidhardt.dynamicsoundboard.R;
import org.neidhardt.dynamicsoundboard.customview.edittext.CustomEditText;

public class AddSoundListItem extends LinearLayout implements CustomEditText.OnTextEditedListener
{
	private TextView soundPath;
	private CustomEditText soundName;
	private boolean wasSoundNameAltered = false;

	public AddSoundListItem(Context context)
	{
		super(context);
		LayoutInflater.from(context).inflate(R.layout.view_add_sound_list_item, this, true);
		this.soundPath = (TextView)this.findViewById(R.id.tv_path);
		this.soundName = (CustomEditText)this.findViewById(R.id.et_name_file);
		this.soundName.setOnTextEditedListener(this);
	}

	@Override
	public void onTextEdited(String text)
	{
		this.wasSoundNameAltered = true;
	}

	public void setPath(String path)
	{
		this.soundPath.setText(path);
	}

	public void setSoundName(String name)
	{
		this.soundName.setText(name);
		this.wasSoundNameAltered = false;
	}

	public String getSoundName()
	{
		return this.soundName.getText().toString();
	}

	public boolean wasSoundNameAltered()
	{
		return this.wasSoundNameAltered;
	}

}
