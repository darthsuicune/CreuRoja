package net.creuroja.android.controller;

import android.view.View;
import android.widget.TextView;

import net.creuroja.android.view.fragments.NavigationDrawerFragment;

/**
 * Created by lapuente on 15.07.14.
 */
public class NavigationDrawerController {
	public NavigationDrawerFragment fragment;

	public NavigationDrawerController(NavigationDrawerFragment fragment) {
		this.fragment = fragment;
	}

	public void prepareLegendObject(TextView v, final int legendObject) {

	}

	public void prepareMapType(TextView v, final int mapType) {
		if (v != null) {
			v.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View view) {
					fragment.mapTypeChanged(mapType);
				}
			});
		}
	}
}
