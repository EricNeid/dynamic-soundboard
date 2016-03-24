package org.neidhardt.dynamicsoundboard.navigationdrawer.soundsheets

import android.view.LayoutInflater
import android.view.ViewGroup
import org.neidhardt.dynamicsoundboard.R
import org.neidhardt.dynamicsoundboard.dao.SoundSheet
import org.neidhardt.dynamicsoundboard.navigationdrawer.NavigationDrawerItemClickListener
import org.neidhardt.dynamicsoundboard.views.recyclerviewhelpers.BaseAdapter

open class SoundSheetsAdapter
(
		private val presenter: SoundSheetsPresenter
) :
		BaseAdapter<SoundSheet, SoundSheetViewHolder>(),
		NavigationDrawerItemClickListener<SoundSheet>
{
	init { this.setHasStableIds(true) }

	override fun getItemId(position: Int): Long = this.values[position].fragmentTag.hashCode().toLong()

	override val values: List<SoundSheet>
		get() = this.presenter.values

	override fun getItemViewType(position: Int): Int = R.layout.view_sound_sheet_item

	override fun getItemCount(): Int = this.values.size

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundSheetViewHolder
	{
		val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
		return SoundSheetViewHolder(view, this)
	}

	override fun onBindViewHolder(holder: SoundSheetViewHolder, position: Int)
	{
		val data = this.values[position]

		val sounds = this.presenter.getSoundsInFragment(data.fragmentTag)
		val soundCount = sounds.size

		holder.bindData(data, soundCount, position == this.itemCount - 1)
	}

	override fun onItemClick(data: SoundSheet)
	{
		this.presenter.onItemClick(data)
	}
}
