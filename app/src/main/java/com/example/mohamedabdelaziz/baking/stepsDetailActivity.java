package com.example.mohamedabdelaziz.baking;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * An activity representing a single steps detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link stepsListActivity}.
 */
public class stepsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_detail);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putString(stepsDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(stepsDetailFragment.ARG_ITEM_ID));

            arguments.putString(stepsDetailFragment.ARG_ITEM_RECIPE,
                    getIntent().getStringExtra(stepsDetailFragment.ARG_ITEM_RECIPE));

            stepsDetailFragment fragment = new stepsDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.steps_detail_container,fragment).commit();

        }
    }
}
