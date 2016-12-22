package org.neidhardt.dynamicsoundboard.soundmanagement.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.CheckBox
import kotlinx.android.synthetic.main.dialog_sound_settings_layout.view.*
import org.greenrobot.eventbus.EventBus
import org.neidhardt.android_utils.views.CustomEditText
import org.neidhardt.android_utils.views.SimpleSpinner
import org.neidhardt.dynamicsoundboard.R
import org.neidhardt.dynamicsoundboard.manager.NewSoundSheetManager
import org.neidhardt.dynamicsoundboard.mediaplayer.MediaPlayerController
import org.neidhardt.dynamicsoundboard.mediaplayer.MediaPlayerFactory
import org.neidhardt.dynamicsoundboard.persistance.model.NewMediaPlayerData
import org.neidhardt.dynamicsoundboard.persistance.model.NewSoundSheet
import org.neidhardt.dynamicsoundboard.soundmanagement.events.SoundChangedEvent
import org.neidhardt.utils.letThis
import java.util.*
import kotlin.properties.Delegates

/**
 * File created by eric.neidhardt on 23.02.2015.
 */
class SoundSettingsDialog : SoundSettingsBaseDialog() {

	override var fragmentTag: String by Delegates.notNull<String>()
	override var player: MediaPlayerController by Delegates.notNull<MediaPlayerController>()
	override var soundSheet: NewSoundSheet? = null

	private var soundName: CustomEditText? = null
	private var soundSheetName: CustomEditText? = null
	private var soundSheetSpinner: SimpleSpinner? = null
	private var addNewSoundSheet: CheckBox? = null

	private var indexOfCurrentFragment = -1

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		@SuppressLint("InflateParams") val view = this.activity.layoutInflater.inflate(R.layout.dialog_sound_settings_layout, null)

		this.soundName = view.et_name_file.letThis {
			it.text = this.player.mediaPlayerData.label
		}
		this.soundSheetName = view.et_name_new_sound_sheet.letThis {
			it.text = this.soundSheetManager.suggestedName
			it.visibility = View.GONE
		}
		this.soundSheetSpinner = view.s_sound_sheets?.letThis {
			this.setAvailableSoundSheets(it)
		}
		this.addNewSoundSheet = view.cb_add_new_sound_sheet.letThis {
			it.setOnCheckedChangeListener { view,isChecked ->
				this.soundSheetSpinner?.visibility = if (isChecked) View.GONE else View.VISIBLE
				this.soundSheetName?.visibility = if (isChecked) View.VISIBLE else View.GONE
			}
		}

		return AlertDialog.Builder(context).apply {
			this.setTitle(R.string.dialog_sound_settings_title)
			this.setView(view)
			this.setPositiveButton(R.string.dialog_save, { dialogInterface, i ->
				val hasLabelChanged = player.mediaPlayerData.label != soundName!!.displayedText
				deliverResult()
				dismiss()
				if (hasLabelChanged)
					RenameSoundFileDialog.show(fragmentManager, player.mediaPlayerData)
			})
			this.setNegativeButton(R.string.dialog_cancel, { dialogInterface, i -> dismiss()})
		}.create()
	}

	private fun setAvailableSoundSheets(spinner: SimpleSpinner) {
		val soundSheets = this.soundSheetManager.soundSheets
		val labels = ArrayList<String>()
		for (i in soundSheets.indices) {
			if (soundSheets[i].fragmentTag == this.fragmentTag)
				this.indexOfCurrentFragment = i
			labels.add(soundSheets[i].label)
		}
		if (this.indexOfCurrentFragment == -1)
			throw IllegalStateException(TAG + " Current fragment of sound " + this.player.mediaPlayerData + " is not found in list of sound sheets " + soundSheets)

		spinner.items = labels
		spinner.setSelection(this.indexOfCurrentFragment)
	}

	private fun deliverResult() {
		val soundLabel = this.soundName!!.displayedText
		val indexOfSelectedSoundSheet = this.soundSheetSpinner!!.selectedItemPosition

		val addNewSoundSheet = this.addNewSoundSheet!!.isChecked
		val hasSoundSheetChanged = addNewSoundSheet || indexOfSelectedSoundSheet != this.indexOfCurrentFragment

		if (!hasSoundSheetChanged) {
			this.player.mediaPlayerData.label = soundLabel
			EventBus.getDefault().post(SoundChangedEvent(this.player))
		} else {
			if (soundSheet != null)
				this.soundManager.remove(soundSheet!!, listOf(this.player))
			else
				this.playlistManager.remove(listOf(this.player))


			val uri = Uri.parse(this.player.mediaPlayerData.uri)

			val mediaPlayerData: NewMediaPlayerData
			if (addNewSoundSheet) {
				val soundSheetName = this.soundSheetName!!.displayedText
				val fragmentTag = NewSoundSheetManager.getNewFragmentTagForLabel(soundSheetName)
				val soundSheet = NewSoundSheet().apply {
					this.fragmentTag = fragmentTag
					this.label = soundSheetName
				}
				this.soundSheetManager.add(soundSheet)

				mediaPlayerData = MediaPlayerFactory.getNewMediaPlayerData(fragmentTag, uri, soundLabel)
			} else {
				val fragmentTag = this.soundSheetManager.soundSheets[indexOfSelectedSoundSheet].fragmentTag
				mediaPlayerData = MediaPlayerFactory.getNewMediaPlayerData(fragmentTag, uri, soundLabel)
			}

			if (this.soundSheet != null)
				this.soundManager.add(soundSheet!!, mediaPlayerData)
			else
				this.playlistManager.add(mediaPlayerData)
		}
	}

	companion object {
		private val TAG = SoundSettingsDialog::class.java.name

		fun showInstance(manager: FragmentManager, playerData: NewMediaPlayerData) {
			val dialog = SoundSettingsDialog()
			addArguments(dialog, playerData.playerId, playerData.fragmentTag)
			dialog.show(manager, TAG)
		}
	}

}
