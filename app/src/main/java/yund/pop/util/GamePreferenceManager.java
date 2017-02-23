package yund.pop.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import yund.pop.ui.shop.ShopItem;

public class GamePreferenceManager {

	private static final String PREFERENCE_NAME_OPTIONS = "options";
	private static final String KEY_VIBRATION = "vib";
	private static final String KEY_SOUND = "sound";

	private static final String PREFERNCE_NAME_VALUES = "values";
	private static final String KEY_SCORE = "key_score";
	private static final String KEY_COIN = "key_coin";

	private static final int TOP_SCORE_LIMIT = 9;

	public static boolean isVibrationOn(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME_OPTIONS, Context.MODE_PRIVATE);
		return preferences.getBoolean(KEY_VIBRATION, false);
	}

	public static boolean isSoundOn(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME_OPTIONS, Context.MODE_PRIVATE);
		return preferences.getBoolean(KEY_SOUND, false);
	}

	public static void setVibrationOption(Context context, boolean isVibrationOn) {
		SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME_OPTIONS, Context.MODE_PRIVATE);
		preferences.edit().putBoolean(KEY_VIBRATION, isVibrationOn).apply();
	}

	public static void setSoundOption(Context context, boolean isSoundOn) {
		SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME_OPTIONS, Context.MODE_PRIVATE);
		preferences.edit().putBoolean(KEY_SOUND, isSoundOn).apply();
	}

	public static List<Long> getTopScores(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(PREFERNCE_NAME_VALUES, Context.MODE_PRIVATE);
		String topScores = preferences.getString(KEY_SCORE, "");
		return parseScoreString(topScores);
	}

	public static void saveNewScore(Context context, long score) {
		SharedPreferences preferences = context.getSharedPreferences(PREFERNCE_NAME_VALUES, Context.MODE_PRIVATE);
		String topScores = preferences.getString(KEY_SCORE, "");
		preferences.edit().putString(KEY_SCORE, generateTopTenScoreString(parseScoreString(topScores), score)).apply();
	}

	private static List<Long> parseScoreString(String scores) {
		List<Long> scoreList = new LinkedList<>();
		if (scores.length() > 0) {
			String[] array = scores.split(",");
			for (String s : array) {
				scoreList.add(Long.parseLong(s));
			}
		}

		Collections.sort(scoreList);
		Collections.reverse(scoreList);
		return scoreList;
	}

	private static String generateTopTenScoreString(List<Long> scoreList, long newScore) {
		scoreList.add(newScore);
		Collections.sort(scoreList);
		Collections.reverse(scoreList);
		while (scoreList.size() > TOP_SCORE_LIMIT) {
			scoreList.remove(scoreList.size() - 1);
		}
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < scoreList.size(); i++) {
			builder.append(scoreList.get(i));
			if (i < scoreList.size() - 1) {
				builder.append(",");
			}
		}

		return builder.toString();
	}

	public static void setCoin(Context context, int coins) {
		SharedPreferences preferences = context.getSharedPreferences(PREFERNCE_NAME_VALUES, Context.MODE_PRIVATE);
		preferences.edit().putInt(KEY_COIN, coins).apply();
	}

	public static int getCoin(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(PREFERNCE_NAME_VALUES, Context.MODE_PRIVATE);
		return preferences.getInt(KEY_COIN, 0);
	}

	public static void buyShopItem(Context context, ShopItem item) {
		SharedPreferences preferences = context.getSharedPreferences(PREFERNCE_NAME_VALUES, Context.MODE_PRIVATE);
		int currentCoin = preferences.getInt(KEY_COIN, 0);
		if (currentCoin >= item.getCost()) {
			preferences.edit().putInt(item.name(), preferences.getInt(item.name(), 0) + 1).putInt(KEY_COIN, currentCoin - item.getCost()).apply();
		}
	}

	public static boolean useShopItem(Context context, ShopItem item) {
		SharedPreferences preferences = context.getSharedPreferences(PREFERNCE_NAME_VALUES, Context.MODE_PRIVATE);
		int currentCount = preferences.getInt(item.name(), 0);
		if (currentCount > 0) {
			preferences.edit().putInt(item.name(), currentCount - 1).apply();
			return true;
		}
		return false;
	}

	public static int getItemCount(Context context, ShopItem item) {
		SharedPreferences preferences = context.getSharedPreferences(PREFERNCE_NAME_VALUES, Context.MODE_PRIVATE);
		return preferences.getInt(item.name(), 0);
	}
}
