/**
 * 
 */
package com.simpragma.magicchef.utils;

import java.net.URLEncoder;
import java.util.List;

import com.instaops.android.Log;

/**
 * @author swagataacharyya
 * 
 */
public class UrlBuilder {
	private static String baseUrl = "";

	public static String getFinalUrl(List<String> ingredients,
			List<String> searchTerms, String[] allergies){
		baseUrl="http://www.recipepuppy.com/api/";
		try{
		if(ingredients!=null){
			baseUrl+="?i=";
			String attached = "";
			int ingredientCount = 0;
			for(String ingredient:ingredients){
				attached+=ingredient.trim();
				if(ingredientCount<ingredients.size()-1){
					attached+=",";
				}
				ingredientCount++;
			}
			baseUrl+=URLEncoder.encode(attached, "utf-8");
		}
		if(searchTerms!=null){
			if(ingredients==null){
				baseUrl+="?q=";
			}else{
			baseUrl+="&q=";
			}
			String attached = "";
			int searchCount = 0;
			for(String searchTerm:searchTerms){
				attached+=searchTerm.trim();
				if(searchCount<searchTerms.size()-1){
					attached+=" ";
				}
				searchCount++;
			}
			baseUrl+=URLEncoder.encode(attached, "utf-8");
		}
		}catch(Exception e){
			Log.d("App", e.getMessage());
			e.printStackTrace();
		}
		return baseUrl;
	}
}
