package yund.pop.entity;

import android.content.Context;

public class ExplosionEntity extends GameEntity {

	private boolean finished = false;

	public ExplosionEntity(Context context, int type, float x, float y) {
		super(context, type == 0 ? GameEntityType.EXPLOSION0 : GameEntityType.EXPLOSION1, x, y);
	}

	@Override
	public void move() {
		if (imageIndex < type.getImageResourceIds().length - 1) {
			imageIndex++;
		} else {
			finished = true;
		}
	}

	public boolean isFinished() {
		return finished;
	}
}
