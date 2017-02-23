package yund.pop.entity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;

import yund.pop.util.GameImageManager;

public class BonusEntity extends GameEntity {

	private Context context;
	private float rotation;

	public BonusEntity(Context context, GameEntityType type, float x, float y) {
		super(context, type, x, y);
		this.context = context;
		rotation = 0;
	}

	@Override
	public void draw(Canvas canvas) {
		Matrix rotator = new Matrix();
		rotator.postRotate(rotation, getWidth()/2, getHeight()/2);
		rotation = (rotation + 22.5f) % 360;
		rotator.postTranslate(getX(), getY());

		canvas.drawBitmap(GameImageManager.getInstance(context).getImage(type.getImageResourceIds()[imageIndex]), rotator, null);
	}

	public GameEntityType getGameEntityType() {
		return type;
	}
}
