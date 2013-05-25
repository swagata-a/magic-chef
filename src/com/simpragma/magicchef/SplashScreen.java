package com.simpragma.magicchef;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.simpragma.magicchef.bo.Recipe;
import com.simpragma.magicchef.db.RecipeDao;
import com.simpragma.magicchef.utils.RecipeUtil;

public class SplashScreen extends Activity {
	private Handler splashHandler = new Handler();
	private RecipeDao recipeDao;
	ArrayList<Recipe> recipeList;
	Intent newIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		recipeDao = new RecipeDao(this);
		recipeDao.open();
		recipeList = recipeDao.getAllRecipes();
		
		Runnable r = new Runnable() {
			public void run() {
				if(recipeList.size()==0){
				 newIntent = new Intent(SplashScreen.this, RecipeFinder.class);
				}else{
				 newIntent = new Intent(SplashScreen.this, FavoriteRecipes.class);	
//				 newIntent.putExtra("data", recipeList);
				}
				startActivity(newIntent);
				finish();
			}
		};
		if (RecipeUtil.isNetworkConnected(getApplicationContext()))
			splashHandler.postDelayed(r, 2000);
		else {
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

	public void onResume(Bundle savedInstanceState) {
		super.onResume();
	}
}
