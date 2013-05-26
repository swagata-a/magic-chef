package com.simpragma.magicchef;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.instaops.android.Log;
import com.simpragma.magicchef.adapter.RecipeAdapter;
import com.simpragma.magicchef.bo.Recipe;
import com.simpragma.magicchef.json.JsonParser;
import com.simpragma.magicchef.utils.RecipeUtil;
import com.simpragma.magicchef.utils.UrlBuilder;

public class RecipeFinder extends Activity {

	ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
	public static final String TAG_RESULTS = "results";
	public static final String TAG_TITLE = "title";
	public static final String TAG_HREF = "href";
	public static final String TAG_INGREDIENTS = "ingredients";
	public static final String TAG_THUMBNAIL = "thumbnail";
	public static final String CREDITS_URL = "http://www.recipepuppy.com/about/api/";
	JSONArray results = null;
	RecipeAdapter adapter = null;
	ListView list;
	ProgressDialog dialog;
	TextView noRecipe;
	TextView credits;
	List<String> ingredientsList = new ArrayList<String>();
	List<String> searchTermsList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_finder);
		noRecipe = (TextView) findViewById(R.id.no_recipe);
		if (RecipeUtil.isNetworkConnected(getApplicationContext())) {
			Log.d("App", "Network Connected");
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				String query = (String) extras.getString("query");
				String cuisine = (String) extras.getString("cuisine");
				if (query != null) {
					String[] queryArray = query.split(",");

					for (String data : queryArray) {
						String[] dataArray = data.split(" ");
						for (String str : dataArray) {
							ingredientsList.add(str);
						}
					}
				}
				if (cuisine != null) {
					String[] cuisineArray = cuisine.split(",");
					for (String data : cuisineArray) {
						String[] dataArray = data.split(" ");
						for (String str : dataArray) {
							searchTermsList.add(str);
						}
					}
				}
			}
			String url = UrlBuilder.getFinalUrl(ingredientsList,
					searchTermsList, null);
			Log.d("App", url);
			dialog = ProgressDialog.show(RecipeFinder.this,
					getString(R.string.please_wait),
					getString(R.string.searching_recipes), true);
			dialog.setCancelable(true);
			new RecipeFetcher().execute(url);
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							finish();
						}
					});
			builder.setTitle(getString(R.string.internet_needed));
			builder.setMessage(getString(R.string.no_internet_message));
			builder.setCancelable(true);
			AlertDialog dlg = builder.create();
			dlg.show();
		}
	}

	private class RecipeFetcher extends
			AsyncTask<String, Integer, ArrayList<Recipe>> {

		@Override
		protected ArrayList<Recipe> doInBackground(String... params) {
			Log.d("App", "do In Background " + params[0]);
			JsonParser parser = new JsonParser();
			JSONObject json = parser.getJSONFromUrl(params[0]);
			JSONArray contacts = null;
			try {
				contacts = json.getJSONArray(TAG_RESULTS);
				for (int i = 0; i < contacts.length(); i++) {
					JSONObject c = contacts.getJSONObject(i);
					Recipe recipe = new Recipe(c.getString(TAG_TITLE),
							c.getString(TAG_HREF),
							c.getString(TAG_INGREDIENTS),
							c.getString(TAG_THUMBNAIL));
					recipeList.add(recipe);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Log.d("App", "Exiting doInBackground");
			return recipeList;
		}

		@Override
		protected void onPostExecute(final ArrayList<Recipe> result) {
			super.onPostExecute(result);
			list = (ListView) findViewById(R.id.list);
			credits = (TextView) findViewById(R.id.credits);
			if (result.size() == 0) {
				noRecipe.setVisibility(View.VISIBLE);
			}
			adapter = new RecipeAdapter(getApplicationContext(), result);
			list.setAdapter(adapter);
			dialog.dismiss();
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					Recipe recipe = result.get(position);
					Intent intent = new Intent(getApplicationContext(),
							RecipeScreen.class);
					intent.putExtra("url", recipe.getHref());
					intent.putExtra("title", recipe.getTitle());
					intent.putExtra("thumbNail", recipe.getThumbnail());
					intent.putExtra("ingredients", recipe.getIngredients());
					startActivity(intent);
				}
			});

			credits.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(CREDITS_URL));
					startActivity(browserIntent);
				}
			});
		}
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
