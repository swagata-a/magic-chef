/**
 * 
 */
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
import android.widget.SearchView;

/**
 * @author swagataacharyya
 *
 */
public class SearchResultsActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
		Log.d("App", "SearchResultsAct");
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
//	      Toast.makeText(getApplicationContext(), query, Toast.LENGTH_LONG).show();
	      Intent passIntent = new Intent(this,RecipeFinder.class);
	      passIntent.putExtra("query", query);
	      startActivity(passIntent);
	    }
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.searchable, menu);
	    SearchManager searchManager =
	            (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	     SearchView searchView =
	             (SearchView) menu.findItem(R.id.search).getActionView();
	     searchView.setSearchableInfo(
	             searchManager.getSearchableInfo(getComponentName()));
	     
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.advancedSearch:
	            Intent intent = new Intent(this,AdvanceSettings.class);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
}
