package com.simpragma.magicchef;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import com.simpragma.magicchef.adapter.RecipeAdapter;
import com.simpragma.magicchef.bo.Recipe;
import com.simpragma.magicchef.json.JsonParser;
import com.simpragma.magicchef.utils.RecipeUtil;

public class RecipeFinder extends Activity {

	private static String url = "http://www.recipepuppy.com/api/";
	// ArrayList<HashMap<String, String>> recipeList = new
	// ArrayList<HashMap<String, String>>();
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

	TextView credits;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_finder);
		if (RecipeUtil.isNetworkConnected(getApplicationContext())) {
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				url = "http://www.recipepuppy.com/api/";
				String query = (String) extras.getString("query");
				String cuisine = (String) extras.getString("cuisine");
				String finalQuery = "";
				String finalCuisine = "";
				if (query != null) {
					String[] queryTokens = query.split(",");
					List<String> spaceTokens = new ArrayList<String>();
					for (String str : queryTokens) {
						String[] spcToken = str.split(" ");
						for (String str1 : spcToken) {
							spaceTokens.add(str1);
						}
					}
					for (String queryStr : spaceTokens) {
						finalQuery += queryStr.trim() + ",";
					}
					Log.d("query", "To Send " + finalQuery);
				}
				if (cuisine != null) {
					String[] cuisineTokens = cuisine.split(",");
					List<String> spaceTokens = new ArrayList<String>();
					for (String str : cuisineTokens) {
						String[] spcToken = str.split(" ");
						for (String str1 : spcToken) {
							spaceTokens.add(str1);
						}
					}
					for (String cuisineStr : spaceTokens) {
						finalCuisine += cuisineStr.trim() + " ";
					}
				}
				// if (exclude != null) {
				// String[] excludeTokens = exclude.split(",");
				// List<String> spaceTokens = new ArrayList<String>();
				// for (String str : excludeTokens) {
				// String[] spcToken = str.split(" ");
				// for (String str1 : spcToken) {
				// spaceTokens.add(str1);
				// }
				// }
				// for (String excludeStr : spaceTokens) {
				// finalExclude += excludeStr.trim() + " ";
				// }
				// }
				String encodedQuery = finalCuisine;
				try {
					if (finalQuery.split(",").length > 0) {
						url += "?i=" + finalQuery;
					}
					// if(finalQuery.split(",").length > 0 &&
					// finalExclude.trim().length()>0){
					// url+="-"+URLEncoder.encode(finalExclude.trim(), "utf-8");
					// }else{
					// url+="?i=-"+URLEncoder.encode(finalExclude.trim(),
					// "utf-8");
					// }

					encodedQuery = URLEncoder.encode(finalCuisine, "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (finalQuery.split(",").length > 0
						&& finalCuisine.split(",").length > 0) {

					url += "&q=" + encodedQuery;
				} else if (finalQuery.split(",").length == 0
						&& finalCuisine.split(",").length > 0) {

					url += "?q=" + encodedQuery;
				}
			}
			Log.d("url", url);
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
			builder.setCancelable(false);
			AlertDialog dlg = builder.create();
			dlg.show();
		}
	}

	private class RecipeFetcher extends
			AsyncTask<String, Integer, ArrayList<Recipe>> {

		@Override
		protected ArrayList<Recipe> doInBackground(String... params) {
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
			return recipeList;
		}

		@Override
		protected void onPostExecute(final ArrayList<Recipe> result) {
			super.onPostExecute(result);
			list = (ListView) findViewById(R.id.list);
			credits = (TextView) findViewById(R.id.credits);
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
