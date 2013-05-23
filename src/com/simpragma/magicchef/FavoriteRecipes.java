package com.simpragma.magicchef;

import java.util.ArrayList;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.simpragma.magicchef.adapter.RecipeAdapter;
import com.simpragma.magicchef.bo.Recipe;
import com.simpragma.magicchef.db.RecipeDao;

public class FavoriteRecipes extends Activity {

	private RecipeDao recipeDao;
	private RecipeAdapter adapter = null;
	private ListView list;
	private TextView noFav;
	ArrayList<Recipe> recipeList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorite_recipes);
		list = (ListView) findViewById(R.id.favlist);
		noFav = (TextView) findViewById(R.id.no_fav);
		recipeDao = new RecipeDao(this);
		recipeDao.open();

		recipeList = recipeDao.getAllRecipes();
		adapter = new RecipeAdapter(getApplicationContext(), recipeList);
		list.setAdapter(adapter);
		if (recipeList.size() == 0) {
			noFav.setVisibility(View.VISIBLE);
		}
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Recipe recipe = recipeList.get(position);
				Intent intent = new Intent(getApplicationContext(),
						RecipeScreen.class);
				intent.putExtra("id", recipe.getId()+"");
				intent.putExtra("url", recipe.getHref());
				intent.putExtra("title", recipe.getTitle());
				intent.putExtra("thumbNail", recipe.getThumbnail());
				intent.putExtra("ingredients", recipe.getIngredients());
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		recipeDao.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		recipeDao.close();
		super.onPause();
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
		switch (item.getItemId()) {
		case R.id.advancedSearch:
			Intent intent = new Intent(this, AdvanceSettings.class);
			startActivity(intent);
			return true;
		case R.id.favorites:
			Intent favIntent = new Intent(this, FavoriteRecipes.class);
			startActivity(favIntent);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

}
