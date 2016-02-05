package org.neidhardt.dynamicsoundboard.soundcontrol

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.emtronics.dragsortrecycler.DragSortRecycler
import de.greenrobot.event.EventBus
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import org.neidhardt.dynamicsoundboard.R
import org.neidhardt.dynamicsoundboard.SoundboardApplication
import org.neidhardt.dynamicsoundboard.dao.MediaPlayerData
import org.neidhardt.dynamicsoundboard.dao.SoundSheet
import org.neidhardt.dynamicsoundboard.fileexplorer.AddNewSoundFromDirectoryDialog
import org.neidhardt.dynamicsoundboard.mediaplayer.MediaPlayerController
import org.neidhardt.dynamicsoundboard.mediaplayer.PlayerAction
import org.neidhardt.dynamicsoundboard.mediaplayer.events.MediaPlayerFailedEvent
import org.neidhardt.dynamicsoundboard.mediaplayer.events.MediaPlayerFailedEventListener
import org.neidhardt.dynamicsoundboard.misc.FileUtils
import org.neidhardt.dynamicsoundboard.misc.IntentRequest
import org.neidhardt.dynamicsoundboard.misc.Logger
import org.neidhardt.dynamicsoundboard.soundactivity.BaseFragment
import org.neidhardt.dynamicsoundboard.soundcontrol.events.OnOpenSoundDialogEventListener
import org.neidhardt.dynamicsoundboard.soundcontrol.events.OpenSoundRenameEvent
import org.neidhardt.dynamicsoundboard.soundcontrol.events.OpenSoundSettingsEvent
import org.neidhardt.dynamicsoundboard.soundmanagement.dialog.AddNewSoundDialog
import org.neidhardt.dynamicsoundboard.soundmanagement.dialog.RenameSoundFileDialog
import org.neidhardt.dynamicsoundboard.soundmanagement.events.*
import org.neidhardt.dynamicsoundboard.soundmanagement.model.SoundsDataAccess
import org.neidhardt.dynamicsoundboard.soundmanagement.model.SoundsDataStorage
import org.neidhardt.dynamicsoundboard.soundmanagement.views.ConfirmDeleteSoundsDialog
import org.neidhardt.dynamicsoundboard.soundmanagement.views.SoundSettingsDialog
import org.neidhardt.dynamicsoundboard.soundsheetmanagement.views.ConfirmDeleteSoundSheetDialog
import org.neidhardt.dynamicsoundboard.views.floatingactionbutton.AddPauseFloatingActionButton
import org.neidhardt.dynamicsoundboard.views.recyclerviewhelpers.DividerItemDecoration

/**
 * File created by eric.neidhardt on 02.07.2015.
 */
private val KEY_FRAGMENT_TAG = "org.neidhardt.dynamicsoundboard.soundcontrol.SoundSheetFragment.fragmentTag"

fun getNewInstance(soundSheet: SoundSheet): SoundSheetFragment
{
	val fragment = SoundSheetFragment()
	val args = Bundle()
	args.putString(KEY_FRAGMENT_TAG, soundSheet.fragmentTag)
	fragment.arguments = args
	return fragment
}

class SoundSheetFragment :
		BaseFragment(),
		DragSortRecycler.OnDragStateChangedListener,
		DragSortRecycler.OnItemMovedListener,
		OnOpenSoundDialogEventListener,
		OnSoundsChangedEventListener,
		MediaPlayerFailedEventListener
{
	private val LOG_TAG = javaClass.name

	var fragmentTag: String = javaClass.name

	private val eventBus: EventBus = EventBus.getDefault()
	private val soundsDataStorage: SoundsDataStorage = SoundboardApplication.getSoundsDataStorage()
	private val soundsDataAccess: SoundsDataAccess = SoundboardApplication.getSoundsDataAccess()

	private val dragSortRecycler: SoundDragSortRecycler = SoundDragSortRecycler(R.id.b_reorder).apply {
		this.setOnItemMovedListener(this@SoundSheetFragment)
		this.setOnDragStateChangedListener(this@SoundSheetFragment)
	}
	private val scrollListener: SoundSheetScrollListener = SoundSheetScrollListener(this.dragSortRecycler)

	private var floatingActionButton: AddPauseFloatingActionButton? = null

	private var soundPresenter: SoundPresenter? = null
	private var soundAdapter: SoundAdapter? = null
	private var soundLayout: RecyclerView? = null
	private val soundLayoutAnimator = SlideInLeftAnimator()

	private var coordinatorLayout: CoordinatorLayout? = null

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		this.retainInstance = true
		this.setHasOptionsMenu(true)

		val args = this.arguments
		var fragmentTag: String? = args.getString(KEY_FRAGMENT_TAG)
				?: throw NullPointerException(LOG_TAG + ": cannot create fragment, given fragmentTag is null")
		this.fragmentTag = fragmentTag as String

		this.soundPresenter = SoundPresenter(this.fragmentTag, this.eventBus, this.soundsDataAccess)
		this.soundAdapter = SoundAdapter(this.soundPresenter as SoundPresenter, this.soundsDataStorage, this.eventBus)
		this.soundPresenter?.adapter = this.soundAdapter
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
	{
		if (container == null)
			return null

		val fragmentView = inflater.inflate(R.layout.fragment_soundsheet, container, false)

		this.floatingActionButton = fragmentView.findViewById(R.id.fab) as AddPauseFloatingActionButton?
		this.coordinatorLayout = fragmentView.findViewById(R.id.coordinator_layout) as CoordinatorLayout

		this.soundLayout = (fragmentView.findViewById(R.id.rv_sounds) as RecyclerView).apply {
			adapter = soundAdapter
			layoutManager = LinearLayoutManager(activity)
			itemAnimator = soundLayoutAnimator
			addItemDecoration(DividerItemDecoration())
			addItemDecoration(dragSortRecycler)
			addOnItemTouchListener(dragSortRecycler)
			addOnScrollListener(scrollListener)
			addOnScrollListener(dragSortRecycler.scrollListener)
		}
		this.soundAdapter?.recyclerView = this.soundLayout

		return fragmentView
	}

	override fun onStart()
	{
		super.onStart()
		if (!this.eventBus.isRegistered(this))
			this.eventBus.register(this)
	}

	override fun onResume()
	{
		super.onResume()

		val activity = this.baseActivity
		activity.setSoundSheetActionsEnable(true)
		activity.findViewById(R.id.action_add_sound).setOnClickListener({ view
			-> AddNewSoundDialog(this.fragmentManager, this.fragmentTag) })
		activity.findViewById(R.id.action_add_sound_dir).setOnClickListener({ view
			-> AddNewSoundFromDirectoryDialog.showInstance(this.fragmentManager, this.fragmentTag) })

		this.soundPresenter!!.onAttachedToWindow()
		this.attachScrollViewToFab()

		this.soundAdapter!!.startProgressUpdateTimer()
	}

	override fun onPause()
	{
		super.onPause()
		this.soundPresenter!!.onDetachedFromWindow()
		this.soundAdapter!!.stopProgressUpdateTimer()
	}

	override fun onStop()
	{
		super.onStop()
		this.eventBus.unregister(this)
	}

	private fun attachScrollViewToFab()
	{
		this.floatingActionButton?.show(true)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
	{
		if (resultCode == Activity.RESULT_OK)
		{
			if (requestCode == IntentRequest.GET_AUDIO_FILE)
			{
				val soundUri = data!!.data
				val soundLabel = FileUtils.stripFileTypeFromName(FileUtils.getFileNameFromUri(this.activity, soundUri))
				val playerData = MediaPlayerData.getNewMediaPlayerData(this.fragmentTag, soundUri, soundLabel)

				this.soundsDataStorage.createSoundAndAddToManager(playerData)
				return
			}
		}
		super.onActivityResult(requestCode, resultCode, data)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean
	{
		super.onOptionsItemSelected(item)
		when (item.itemId)
		{
			R.id.action_clear_sounds_in_sheet ->
			{
				ConfirmDeleteSoundsDialog.showInstance(this.fragmentManager, this.fragmentTag)
				return true
			}
			R.id.action_delete_sheet ->
			{
				ConfirmDeleteSoundSheetDialog.showInstance(this.fragmentManager, this.fragmentTag)
				return true
			}
			else -> return false
		}
	}

	override fun onDragStart()
	{
		Logger.d(LOG_TAG, "onDragStart")
		this.soundLayout?.itemAnimator = null // drag does not work with default animator
		this.soundAdapter?.stopProgressUpdateTimer()
	}

	override fun onDragStop()
	{
		Logger.d(LOG_TAG, "onDragStop")
		this.soundLayout?.invalidateItemDecorations()
		this.soundAdapter?.notifyDataSetChanged()
		this.soundLayout?.itemAnimator = this.soundLayoutAnimator // add animator for delete animation
		this.soundAdapter?.startProgressUpdateTimer()
	}

	override fun onItemMoved(from: Int, to: Int)
	{
		this.soundsDataStorage.moveSoundInFragment(fragmentTag, from, to)
	}

	override fun onEvent(event: OpenSoundRenameEvent)
	{
		RenameSoundFileDialog(this.fragmentManager, event.data)
	}

	override fun onEvent(event: OpenSoundSettingsEvent)
	{
		SoundSettingsDialog.showInstance(this.fragmentManager, event.data)
	}

	override fun onEventMainThread(event: SoundsRemovedEvent)
	{
		if (this.soundAdapter?.getValues()?.size == 0)
			this.floatingActionButton?.show(true)
	}

	override fun onEvent(event: MediaPlayerFailedEvent)
	{
		val snackbar = this.getSnackbarForError(event.player, event.failingAction)
		snackbar.show()
	}

	private fun getSnackbarForError(failingPlayer: MediaPlayerController, failingAction: PlayerAction): Snackbar
	{
		val messageId = if (failingAction == PlayerAction.PLAY)
			R.string.sound_control_play_failed
		else
			R.string.sound_control_failed

		val message = this.resources.getString(messageId).replace("{%s0}", failingPlayer.mediaPlayerData.label)
		val snackbar = Snackbar.make(this.coordinatorLayout as CoordinatorLayout, message, Snackbar.LENGTH_LONG)

		snackbar.setAction(R.string.sound_control_reset_player, {
			val soundUri = failingPlayer.mediaPlayerData.uri
			failingPlayer.setSoundUri(soundUri)
		})

		return snackbar
	}

	override fun onEventMainThread(event: SoundMovedEvent) {}

	override fun onEventMainThread(event: SoundAddedEvent) {}

	override fun onEventMainThread(event: SoundChangedEvent) {}
}

