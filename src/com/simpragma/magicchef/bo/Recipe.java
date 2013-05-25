/**
 * 
 */
package com.simpragma.magicchef.bo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @author swagataacharyya
 * 
 */
public class Recipe implements Parcelable{
	public String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String title;
	private String href;
	private String ingredients;
	private String thumbnail;

	public Recipe() {
		// TODO Auto-generated constructor stub
	}

	public Recipe(String title, String href, String ingredients,
			String thumbnail) {
		this.title = title;
		this.thumbnail = thumbnail;
		this.ingredients = ingredients;
		this.href = href;
	}

	/**
	 * Parcelable requirements
	 * 
	 * @param in
	 */
	public Recipe(Parcel in) {
		String[] data = new String[5];

		in.readStringArray(data);
		this.id = data[0];
		this.title = data[1];
		this.href = data[2];
		this.ingredients = data[3];
		this.thumbnail = data[4];
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	@Override
	public String toString() {
		return "Recipe [title=" + title + ", href=" + href + ", ingredients="
				+ ingredients + ", thumbnail=" + thumbnail + "]";
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] { this.id, this.title, this.href,
				this.ingredients, this.thumbnail });
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in); 
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
		}
	};
}
