package com.simpragma.magicchef;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.simpragma.magicchef.bo.Recipe;
import com.simpragma.magicchef.db.RecipeDao;

public class RecipeScreen extends Activity {

	private WebView webView;
	private Button addFavButton;
	private RecipeDao recipeDao;
	String id;
	String url = "";
	String title = "";
	String thumbNail = "";
	String ingredients = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_screen);

		addFavButton = (Button) findViewById(R.id.addFavButton);

		WebViewClient myWebClient = new WebViewClient() {
			ProgressDialog progressDialog = new ProgressDialog(
					RecipeScreen.this);

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				progressDialog.setMessage("Loading Page");
				progressDialog.setCancelable(false);
				progressDialog.setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						if (keyCode == KeyEvent.KEYCODE_SEARCH) {
							return true;
						} else
							return false;
					}
				});
				progressDialog.show();
			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				progressDialog.setMessage(getString(R.string.loading_page));
				progressDialog.setCancelable(false);
				progressDialog.setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						if (keyCode == KeyEvent.KEYCODE_SEARCH) {
							return true;
						} else
							return false;
					}
				});
				view.loadUrl(url);
				return true;
			}

			public void onPageFinished(WebView view, String url) {
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			}
		};
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(myWebClient);
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		// Get data via the key
		url = extras.getString("url");
		title = extras.getString("title");
		thumbNail = extras.getString("thumbNail");
		ingredients = extras.getString("ingredients");
		id = extras.getString("id");
		if(id != null){
			addFavButton.setText("Remove Fav");
			addFavButton.refreshDrawableState();
		}
		if (url != null) {
			webView.loadUrl(url);
		}
		addFavButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if(id==null){
				Recipe recipe = new Recipe(title, url, ingredients, thumbNail);
				if (recipeDao.getRecipeByUrl(url) == null) {
					recipeDao.createRecipe(recipe);
					Toast.makeText(getApplicationContext(), "Created",
							Toast.LENGTH_LONG).show();
				}
				}else{
					recipeDao.deleteRecipe(url);
					Toast.makeText(getApplicationContext(), "Deleted",
							Toast.LENGTH_LONG).show();
					id=null;
					addFavButton.setText("Add Fav");
					addFavButton.refreshDrawableState();
				}
			}
				
		});
		recipeDao = new RecipeDao(this);
		recipeDao.open();
	}

	/**
	 * This is a pretty useful method. If one Webview contains links, and we
	 * keep visiting the links, then pressing hardware back button will go to
	 * previously visited link. Normal behavior is, it goes to the previous
	 * activity where this Webview was accessed from.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Check if the key event was the Back button and if there's history
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
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
}
