package com.ericneidhardt.dynamicsoundboard.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ericneidhardt.dynamicsoundboard.R;

/**
 * Created by eric.neidhardt on 29.08.2014.
 */
public class CustomEditText extends LinearLayout
{
	protected EditText input;

	public CustomEditText(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText, 0, 0);
		int layoutToInflate = array.getResourceId(R.styleable.CustomEditText_layout, R.layout.view_edittext);

		this.inflateLayout(context, layoutToInflate);
	}

	protected void inflateLayout(Context context, int layoutToInflate)
	{
		LayoutInflater.from(context).inflate(layoutToInflate, this, true);
		this.input = (EditText) this.findViewById(R.id.edittext);
	}

	public void setText(String text)
	{
		if (this.input != null)
			this.input.setText(text);
	}

	public Editable getText()
	{
		if (this.input != null)
			return this.input.getText();
		return null;
	}

	public String getDisplayedText()
	{
		String userInput = this.getText().toString();
		if (userInput != null && !userInput.isEmpty())
			return userInput;
		return this.getHint().toString();
	}

	public void setHint(String hint)
	{
		if (this.input != null)
			this.input.setHint(hint);
	}

	public CharSequence getHint()
	{
		if (this.input != null)
			return this.input.getHint();
		return null;
	}

}
