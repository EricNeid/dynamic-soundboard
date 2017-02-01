package org.neidhardt.dynamicsoundboard.persistance.model;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by eric.neidhardt@gmail.com on 19.12.2016.
 */

public class NewSoundSheet {

	public String fragmentTag;
	public String label;
	public boolean isSelected;
	public boolean isSelectedForDeletion;

	@Nullable
	public List<NewMediaPlayerData> mediaPlayers;
}