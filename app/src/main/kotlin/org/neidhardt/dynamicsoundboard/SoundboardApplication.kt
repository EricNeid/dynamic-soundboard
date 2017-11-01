package org.neidhardt.dynamicsoundboard

import android.content.Context
import android.support.multidex.MultiDexApplication
import org.greenrobot.eventbus.EventBus
import org.neidhardt.dynamicsoundboard.manager.PlaylistManager
import org.neidhardt.dynamicsoundboard.manager.SoundLayoutManager
import org.neidhardt.dynamicsoundboard.manager.SoundManager
import org.neidhardt.dynamicsoundboard.manager.SoundSheetManager
import org.neidhardt.dynamicsoundboard.misc.registerIfRequired
import org.neidhardt.dynamicsoundboard.repositories.AppDataStorage
import org.neidhardt.dynamicsoundboard.repositories.PreferenceRepository
import org.neidhardt.utils.ValueHolder
import java.util.*

open class SoundboardApplication : MultiDexApplication() {

	companion object {

		private var staticContext: Context? = null
		val context: Context get() = this.staticContext as Context

		private val random = Random()

		val storage by lazy { AppDataStorage(this.context) }
		val preferenceRepository by lazy { PreferenceRepository(this.context) }

		val soundSheetManager by lazy { SoundSheetManager(this.context) }
		val soundManager by lazy { SoundManager(this.context) }
		val playlistManager by lazy { PlaylistManager(this.context) }
		val soundLayoutManager by lazy {
			SoundLayoutManager(this.context,
					this.soundSheetManager,
					this.playlistManager,
					this.soundManager) }

		val randomNumber: Int get() = this.random.nextInt(Integer.MAX_VALUE)

		val taskCounter: ValueHolder<Int> by lazy { ValueHolder(0) }
	}

	override fun onCreate() {
		super.onCreate()
		staticContext = this.applicationContext

		soundLayoutManager.initIfRequired(storage.get())

		val eventBus = EventBus.getDefault()
		eventBus.registerIfRequired(playlistManager)
	}
}
