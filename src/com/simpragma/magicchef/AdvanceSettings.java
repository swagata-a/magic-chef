package com.simpragma.magicchef;

import com.simpragma.magicchef.R;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

public class AdvanceSettings extends Activity {

	EditText ingredients;
	AutoCompleteTextView cuisine;
	Button submitAdvanced;
	// EditText exclude;
	EditText preference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.advance_settings);
		ingredients = (EditText) findViewById(R.id.ingredients);
		cuisine = (AutoCompleteTextView) findViewById(R.id.cuisine);
		submitAdvanced = (Button) findViewById(R.id.submitAdvanced);
		// exclude = (EditText) findViewById(R.id.ingredients_exclude);
		// preference = (EditText) findViewById(R.id.preference);
		submitAdvanced.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String ingredientText = ingredients.getText().toString();
				String cuisineText = cuisine.getText().toString();
				// String excludeText = exclude.getText().toString();
				// String preferenceText = preference.getText().toString();
				Intent intent = new Intent(AdvanceSettings.this,
						RecipeFinder.class);
				intent.putExtra("query", ingredientText);
				intent.putExtra("cuisine", cuisineText);
				// intent.putExtra("exclude", excludeText);
				// intent.putExtra("preference", preferenceText);
				startActivity(intent);
			}
		});

		ArrayAdapter<String> cuisineAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, CUISINES);
		cuisine.setAdapter(cuisineAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.searchable, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d("adv", item.getItemId() + " and " + R.id.advancedSearch);
		switch (item.getItemId()) {
		case R.id.advancedSearch:
			Intent intent = new Intent(this, AdvanceSettings.class);
			startActivity(intent);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private static final String[] CUISINES = new String[] { "African", "Asian",
			"European", "Indian", "German", "Oceania", "Greek" };

}
