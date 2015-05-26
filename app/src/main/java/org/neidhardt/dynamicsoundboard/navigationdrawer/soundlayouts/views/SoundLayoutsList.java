package org.neidhardt.dynamicsoundboard.navigationdrawer.soundlayouts.views;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import org.neidhardt.dynamicsoundboard.R;
import org.neidhardt.dynamicsoundboard.dao.SoundLayout;
import org.neidhardt.dynamicsoundboard.navigationdrawer.NavigationDrawerList;
import org.neidhardt.dynamicsoundboard.navigationdrawer.NavigationDrawerListPresenter;
import org.neidhardt.dynamicsoundboard.views.DividerItemDecoration;

/**
 * Created by eric.neidhardt on 08.03.2015.
 */
public class SoundLayoutsList extends NavigationDrawerList implements SoundLayoutsListAdapter.OnItemClickListener
{
	private Interpolator animationInterpolator = new AccelerateDecelerateInterpolator();

	private RecyclerView soundLayouts;
	private SoundLayoutsListAdapter adapter;
	private SoundLayoutsPresenter presenter;

	@SuppressWarnings("unused")
	public SoundLayoutsList(Context context)
	{
		super(context);
		this.init(context);
	}

	@SuppressWarnings("unused")
	public SoundLayoutsList(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.init(context);
	}

	@SuppressWarnings("unused")
	public SoundLayoutsList(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.init(context);
	}

	private void init(Context context)
	{
		this.presenter = new SoundLayoutsPresenter();

		this.adapter = new SoundLayoutsListAdapter();
		this.adapter.setOnItemClickListener(this);

		LayoutInflater.from(context).inflate(R.layout.view_sound_layout_list, this, true);

		this.soundLayouts = (RecyclerView) this.findViewById(R.id.rv_sound_layouts_list);
		if (!this.isInEditMode())
		{
			this.soundLayouts.addItemDecoration(new DividerItemDecoration());
			this.soundLayouts.setLayoutManager(new LinearLayoutManager(context));
			this.soundLayouts.setItemAnimator(new DefaultItemAnimator());
		}
		this.soundLayouts.setAdapter(this.adapter);
	}

	@Override
	protected void onAttachedToWindow()
	{
		super.onAttachedToWindow();
		this.presenter.onAttachedToWindow();
		this.adapter.onAttachedToWindow();
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

	public void setAdapter(SoundLayoutsListAdapter adapter)
	{
		this.adapter = adapter;
		this.adapter.setOnItemClickListener(this);
		this.soundLayouts.setAdapter(adapter);
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
	protected int getActionModeTitle()
	{
		return R.string.cab_title_delete_sound_layouts;
	}

	@Override
	public void onItemClick(View view, SoundLayout data, int position)
	{
		this.presenter.onItemClick(view, data, position);
	}

	public boolean isActive()
	{
		return this.getVisibility() == View.VISIBLE;
	}

	public void toggleVisibility()
	{
		if (this.getVisibility() == View.VISIBLE)
			this.hideSelectSoundLayout();
		else
			this.showSelectSoundLayoutOverlay();
	}

	private void showSelectSoundLayoutOverlay()
	{
		this.setVisibility(VISIBLE);
	}

	private void hideSelectSoundLayout()
	{
		this.setVisibility(INVISIBLE);
	}

	public SoundLayoutsListAdapter getAdapter()
	{
		return this.adapter;
	}

	@Override
	public NavigationDrawerListPresenter getPresenter()
	{
		return this.presenter;
	}
}