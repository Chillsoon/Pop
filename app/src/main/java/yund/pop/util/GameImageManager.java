package yund.pop.util;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.collection.LruCache;

public class GameImageManager {

	// Singleton
	private static GameImageManager instance;

	private LruCache<Integer, Bitmap> imageCache;
	private Context context;

	private GameImageManager(final Context context) {
		this.context = context;
		imageCache = new LruCache<Integer, Bitmap>(getCacheSize(context)) {
			@Override
			protected Bitmap create(Integer key) {
				return BitmapFactory.decodeResource(context.getResources(), key);
			}
		};
	}

	public static GameImageManager getInstance(Context context) {
		if (instance == null) {
			instance = new GameImageManager(context);
		}
		return instance;
	}

	private static int getCacheSize(Context context) {
		int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();

		// Use 1/8th of the available memory for this memory cache.
		return 1024 * 1024 * memClass / 8;
	}

	public Bitmap getImage(int resId) {
		Bitmap image = imageCache.get(resId);
		if (image == null) {
			image = BitmapFactory.decodeResource(context.getResources(), resId);
			imageCache.put(resId, image);
		}
		return image;
	}
}
