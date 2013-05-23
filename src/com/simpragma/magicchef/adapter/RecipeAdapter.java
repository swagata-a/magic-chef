package com.simpragma.magicchef.adapter;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.simpragma.magicchef.R;
import com.simpragma.magicchef.RecipeFinder;
import com.simpragma.magicchef.bo.Recipe;

public class RecipeAdapter extends BaseAdapter {

//	private ArrayList<HashMap<String, String>> data;
	private ArrayList<Recipe> data;
	private static LayoutInflater inflater = null;

	public RecipeAdapter(Context a, ArrayList<Recipe> d) {
		data = d;
		inflater = (LayoutInflater) a
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		Log.d("ConvertView", convertView+"");
		if (convertView == null)
			vi = inflater.inflate(R.layout.recipe_row, null);
		TextView title = (TextView) vi.findViewById(R.id.title);
		TextView ingredients = (TextView) vi.findViewById(R.id.ingredients);
		ImageView thumbnail = (ImageView) vi.findViewById(R.id.list_image);
		Recipe recipe = data.get(position);

		title.setText(recipe.getTitle().trim());
		ingredients.setText(recipe.getIngredients().trim());
		if(recipe.getThumbnail().trim().length()>0){
			URL newurl;
			try {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
				newurl = new URL(recipe.getThumbnail());
				Bitmap bm = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream()); 
				thumbnail.setImageBitmap(bm);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return vi;
	}
}