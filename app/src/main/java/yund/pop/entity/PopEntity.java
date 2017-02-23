package yund.pop.entity;

import android.content.Context;

public class PopEntity extends GameEntity {

	private int damageTaken;

	public PopEntity(Context context, GameEntityType type, float x, float y){
		super(context, type, x, y);
		damageTaken = 0;
	}

	@Override
	public void onClicked() {
		damageTaken++;
		if (type.ordinal() >= GameEntityType.BALL0.ordinal() && type.ordinal() <= GameEntityType.BALL5.ordinal()) {
			imageIndex = (imageIndex + 1) % type.getImageResourceIds().length;
		}
	}

	public boolean isDead() {
		return type.getHp() - damageTaken <= 0;
	}

	public int getScore() {
		return type.getHp() * 150;
	}
}
