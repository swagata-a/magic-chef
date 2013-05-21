package com.simpragma.magicchef;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;

import com.simpragma.magicchef.adapter.RecipeAdapter;
import com.simpragma.magicchef.json.JsonParser;

public class RecipeFinder extends Activity {

	private static String url = "http://www.recipepuppy.com/api/";
	ArrayList<HashMap<String, String>> recipeList = new ArrayList<HashMap<String, String>>();
	public static final String TAG_RESULTS = "results";
	public static final String TAG_TITLE = "title";
	public static final String TAG_HREF = "href";
	public static final String TAG_INGREDIENTS = "ingredients";
	public static final String TAG_THUMBNAIL = "thumbnail";
	JSONArray results = null;
	ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
	RecipeAdapter adapter = null;
	ListView list;
	ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_finder);
		if (isNetworkConnected()) {
			Log.d("connection", "Internet Available");
			Log.d("connection", "Outside");
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				url = "http://www.recipepuppy.com/api/";
				String query = (String) extras.getString("query");
				String cuisine = (String) extras.getString("cuisine");
				// String exclude = (String) extras.getString("exclude");

				String finalQuery = "";
				String finalCuisine = "";
				// String finalExclude = "";
				if (query != null) {
					String[] queryTokens = query.split(",");
					Log.d("query", "ActualQuery" + query);
					List<String> spaceTokens = new ArrayList<String>();
					for (String str : queryTokens) {
						Log.d("query", "tokens" + str);
						String[] spcToken = str.split(" ");
						for (String str1 : spcToken) {
							Log.d("query", "Adding " + str1);
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
					Log.d("query", "ActualQuery" + cuisine);
					List<String> spaceTokens = new ArrayList<String>();
					for (String str : cuisineTokens) {
						Log.d("query", "tokens" + str);
						String[] spcToken = str.split(" ");
						for (String str1 : spcToken) {
							Log.d("query", "Adding " + str1);
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
			dialog = ProgressDialog.show(RecipeFinder.this, "Please wait",
					"Serching Recipes", true);
			dialog.setCancelable(true);
			new RecipeFetcher().execute(url);
		} else {
			Log.d("connection", "No Internet Available");
			AlertDialog.Builder builder = new AlertDialog.Builder(
					this);
			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							dialog.dismiss();
							finish();
						}
					});
			builder.setTitle("Internet Needed");
			builder.setMessage("This application needs internet. Please enable your connection");
			builder.setCancelable(false);
			AlertDialog dlg = builder.create();
			dlg.show();
		}
	}

	private class RecipeFetcher extends
			AsyncTask<String, Integer, ArrayList<HashMap<String, String>>> {

		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(
				String... params) {
			JsonParser parser = new JsonParser();
			Log.d("url", params[0]);
			JSONObject json = parser.getJSONFromUrl(params[0]);
			JSONArray contacts = null;
			try {
				// Getting Array of Recipes
				contacts = json.getJSONArray(TAG_RESULTS);

				// looping through All Recipes
				for (int i = 0; i < contacts.length(); i++) {
					JSONObject c = contacts.getJSONObject(i);

					HashMap<String, String> map = new HashMap<String, String>();
					// adding each child node to HashMap key => value
					map.put(TAG_TITLE, c.getString(TAG_TITLE));
					map.put(TAG_HREF, c.getString(TAG_HREF));
					map.put(TAG_INGREDIENTS, c.getString(TAG_INGREDIENTS));
					map.put(TAG_THUMBNAIL, c.getString(TAG_THUMBNAIL));
					map.put(TAG_HREF, c.getString(TAG_HREF));
					recipeList.add(map);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return recipeList;
		}

		@Override
		protected void onPostExecute(
				final ArrayList<HashMap<String, String>> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			list = (ListView) findViewById(R.id.list);
			adapter = new RecipeAdapter(getApplicationContext(), result);
			list.setAdapter(adapter);
			dialog.dismiss();
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					HashMap<String, String> recipe = result.get(position);
					Intent intent = new Intent(getApplicationContext(),
							RecipeScreen.class);
					intent.putExtra("url", recipe.get(TAG_HREF));
					startActivity(intent);
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

	private boolean isNetworkConnected() {
		Log.d("connection", "came in");
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		Log.d("connection",
				(activeNetworkInfo != null && activeNetworkInfo.isConnected())
						+ "");
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
