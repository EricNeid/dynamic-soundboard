package org.neidhardt.dynamicsoundboard.navigationdrawer.viewmodel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.databinding.BindingAdapter;
import android.view.View;

/**
 * @author Eric.Neidhardt@GMail.com on 20.06.2016.
 */
public class NavigationDrawerButtonBarAdapter {

	@BindingAdapter("animateVisibleSlide")
	public static void slideView(final View view, Boolean visible) {
		view.animate().cancel();
		if (visible) {
			view.setTranslationX(-1f * view.getWidth());
			view.animate().withLayer().translationX(0f).setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					view.setTranslationX(0f);
				}
			});
		}
		else {
			view.setTranslationX(0f);
			view.animate().withLayer().translationX(-1f * view.getWidth()).setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					view.setTranslationX(-1f * view.getWidth());
				}
			});
		}
	}

	@BindingAdapter("animateVisibleFade")
	public static void fadeView(final View view, Boolean visible) {
		view.animate().cancel();
		if (visible) {
			view.setAlpha(0f);
			view.animate().withLayer().alpha(1f).setDuration(400).setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					view.setAlpha(1f);
				}
			});
		}
		else {
			view.setAlpha(1f);
			view.animate().withLayer().alpha(0f).setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					view.setAlpha(0f);
				}
			});
		}
	}
}
