package yund.pop.entity;

import android.content.Context;

public class CoinEntity extends GameEntity {

	private static final int COIN_VALUE = 5;

	public CoinEntity(Context context, float x, float y) {
		super(context, GameEntityType.COIN, x, y);
	}

	public int getValue() {
		return COIN_VALUE;
	}
}
