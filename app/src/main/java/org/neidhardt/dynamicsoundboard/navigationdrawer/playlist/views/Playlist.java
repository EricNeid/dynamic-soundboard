package org.neidhardt.dynamicsoundboard.navigationdrawer.playlist.views;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import org.neidhardt.dynamicsoundboard.R;
import org.neidhardt.dynamicsoundboard.mediaplayer.EnhancedMediaPlayer;
import org.neidhardt.dynamicsoundboard.navigationdrawer.NavigationDrawerList;
import org.neidhardt.dynamicsoundboard.navigationdrawer.NavigationDrawerListPresenter;
import org.neidhardt.dynamicsoundboard.views.DividerItemDecoration;

/**
 * Project created by eric.neidhardt on 27.08.2014.
 */
public class Playlist extends NavigationDrawerList implements PlaylistAdapter.OnItemClickListener
{
	public static final String TAG = Playlist.class.getName();

	private PlaylistAdapter adapter;
	private PlaylistPresenter presenter;

	@SuppressWarnings("unused")
	public Playlist(Context context)
	{
		super(context);
		this.init(context);
	}

	@SuppressWarnings("unused")
	public Playlist(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.init(context);
	}

	@SuppressWarnings("unused")
	public Playlist(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.init(context);
	}

	private void init(Context context)
	{
		this.presenter = new PlaylistPresenter();

		this.adapter = new PlaylistAdapter();
		this.adapter.setOnItemClickListener(this);

		LayoutInflater.from(context).inflate(R.layout.view_playlist, this, true);

		RecyclerView playlist = (RecyclerView) this.findViewById(R.id.rv_playlist);
		if (!this.isInEditMode())
		{
			playlist.addItemDecoration(new DividerItemDecoration());
			playlist.setLayoutManager(new LinearLayoutManager(context));
			playlist.setItemAnimator(new DefaultItemAnimator());
		}
		playlist.setAdapter(this.adapter);
	}

	@Override
	protected void onAttachedToWindow()
	{
		super.onAttachedToWindow();
		this.presenter.onAttachedToWindow();
		this.adapter.onAttachToWindow();
	}

	@Override
	protected void onDetachedFromWindow()
	{
		this.adapter.onDetachedFromWindow();
		this.presenter.onDetachedFromWindow();
		super.onDetachedFromWindow();
	}

	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();
		this.presenter.setView(this);
	}

	@Override
	public void onItemClick(View view, EnhancedMediaPlayer player, int position)
	{
		this.presenter.onItemClicked(view, player, position);
	}

	@Override
	protected int getActionModeTitle()
	{
		return R.string.cab_title_delete_play_list_sounds;
	}

	@Override
	protected void onDeleteSelected(SparseArray<View> selectedItems)
	{
		this.presenter.onDeleteSelected(selectedItems);
	}

	@Override
	protected int getItemCount()
	{
		return this.adapter.getItemCount();
	}

	@Override
	public NavigationDrawerListPresenter getPresenter()
	{
		return this.presenter;
	}

	public PlaylistAdapter getAdapter()
	{
		return adapter;
	}
}