package nader.eww.db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class Converters {
	@TypeConverter
	public static String fromReactionMap(Map<String, Integer> reactions) {
		if (reactions == null) {
			return null;
		}
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, Integer>>() {}.getType();
		return gson.toJson(reactions, type);
	}
	
	@TypeConverter
	public static Map<String, Integer> toReactionMap(String reactionsString) {
		if (reactionsString == null) {
			return null;
		}
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, Integer>>() {}.getType();
		return gson.fromJson(reactionsString, type);
	}
}