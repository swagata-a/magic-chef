/**
 * 
 */
package com.simpragma.magicchef.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.simpragma.magicchef.bo.Recipe;

/**
 * 
 * @author Swagata
 * 
 */
public class RecipeDao {

	// Database fields
	private SQLiteDatabase database;
	private DbHelper dbHelper;
	private String[] allColumns = { DbHelper.COLUMN_ID, DbHelper.COLUMN_HREF,
			DbHelper.COLUMN_INGREDIENTS, DbHelper.COLUMN_THUMBNAIL,
			DbHelper.COLUMN_TITLE };

	public RecipeDao(Context context) {
		dbHelper = new DbHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Recipe createRecipe(Recipe recipe) {
		ContentValues values = new ContentValues();
		values.put(DbHelper.COLUMN_HREF, recipe.getHref());
		values.put(DbHelper.COLUMN_INGREDIENTS, recipe.getIngredients());
		values.put(DbHelper.COLUMN_THUMBNAIL, recipe.getThumbnail());
		values.put(DbHelper.COLUMN_TITLE, recipe.getTitle());
		long insertId = database.insert(DbHelper.TABLE_RECIPES, null, values);
		Cursor cursor = database.query(DbHelper.TABLE_RECIPES, allColumns,
				DbHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		Recipe newRecipe = getRecipe(cursor);
		cursor.close();
		return newRecipe;
	}

	public void deleteRecipe(Recipe recipe) {
		long id = recipe.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(DbHelper.TABLE_RECIPES,
				DbHelper.COLUMN_ID + " = " + id, null);
	}

	public void deleteRecipe(String url) {
		database.delete(DbHelper.TABLE_RECIPES, DbHelper.COLUMN_HREF + " =?", new String[] { url.trim() });
	}

	public ArrayList<Recipe> getAllRecipes() {
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		Cursor cursor = database.query(DbHelper.TABLE_RECIPES, allColumns,
				null, null, null, null, DbHelper.COLUMN_TITLE);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				while (!cursor.isAfterLast()) {
					Recipe recipe = getRecipe(cursor);
					recipes.add(recipe);
					cursor.moveToNext();
				}
				// Make sure to close the cursor
				cursor.close();
			}
		} 
		return recipes;
	}

	public Recipe getRecipeByUrl(String url) {
		Cursor cursor = database.query(DbHelper.TABLE_RECIPES, allColumns,
				DbHelper.COLUMN_HREF + "=?", new String[] { url.trim() }, null,
				null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				Recipe recipe = getRecipe(cursor);
				cursor.close();
				return recipe;
			}
		}
		return null;
	}

	private Recipe getRecipe(Cursor cursor) {
		Recipe recipe = new Recipe();
		recipe.setId(cursor.getLong(0));
		recipe.setHref(cursor.getString(1));
		recipe.setIngredients(cursor.getString(2));
		recipe.setThumbnail(cursor.getString(3));
		recipe.setTitle(cursor.getString(4));
		return recipe;
	}
}