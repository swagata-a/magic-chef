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

import com.simpragma.magicchef.RecipeFinder;
import com.simpragma.magicchef.R;

public class RecipeAdapter extends BaseAdapter {

	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;

	public RecipeAdapter(Context a, ArrayList<HashMap<String, String>> d) {
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

		HashMap<String, String> recipe = new HashMap<String, String>();
		recipe = data.get(position);

		title.setText(recipe.get(RecipeFinder.TAG_TITLE).trim());
		ingredients.setText(recipe.get(RecipeFinder.TAG_INGREDIENTS).trim());
		// Interesting part. I didn't know we needed to download the image from
		// web first before showing in an ImageView
		if(recipe.get(RecipeFinder.TAG_THUMBNAIL).length()>0){
			URL newurl;
			try {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
				newurl = new URL(recipe.get(RecipeFinder.TAG_THUMBNAIL));
				Bitmap bm = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream()); 
				thumbnail.setImageBitmap(bm);
			} catch (Exception e) {
				// How do I handle this exception?
				e.printStackTrace();
			} 
		}
		return vi;
	}
}