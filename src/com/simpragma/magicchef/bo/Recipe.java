/**
 * 
 */
package com.simpragma.magicchef.bo;

/**
 * @author swagataacharyya
 * 
 */
public class Recipe {
	private String title;
	private String href;
	private String ingredients;
	private String thumbnail;

	public Recipe() {
		// TODO Auto-generated constructor stub
	}
	public Recipe(String title,String href,String ingredients,String thumbnail) {
		this.title = title;
		this.thumbnail = thumbnail;
		this.ingredients=ingredients;
		this.href = href;
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

}
