package yund.pop.entity;

import yund.pop.R;

public enum GameEntityType {
	BALLOON0(1, new int[] { R.drawable.balloon0 }),
	BALLOON1(1, new int[] { R.drawable.balloon1 }),
	BALLOON2(1, new int[] { R.drawable.balloon2 }),
	BALLOON3(1, new int[] { R.drawable.balloon3 }),
	BALLOON4(1, new int[] { R.drawable.balloon4 }),
	BALLOON5(1, new int[] { R.drawable.balloon5 }),
	BALL0(2, new int[] { R.drawable.ball0_0, R.drawable.ball0_1 }),
	BALL1(2, new int[] { R.drawable.ball0_0, R.drawable.ball0_2 }),
	BALL2(2, new int[] { R.drawable.ball0_0, R.drawable.ball0_3 }),
	BALL3(2, new int[] { R.drawable.ball1_0, R.drawable.ball1_1 }),
	BALL4(2, new int[] { R.drawable.ball1_0, R.drawable.ball1_2 }),
	BALL5(2, new int[] { R.drawable.ball1_0, R.drawable.ball1_3 }),
	COIN(1, new int[] { R.drawable.coin1 }),
	BONUS0(1, new int[] { R.drawable.bonus0 }),
	BONUS1(1, new int[] { R.drawable.bonus1 }),
	BONUS2(1, new int[] { R.drawable.bonus2 }),
	BONUS3(1, new int[] { R.drawable.bonus3 }),
	BONUS4(1, new int[] { R.drawable.bonus4 }),
	BONUS5(1, new int[] { R.drawable.bonus5 }),
	EXPLOSION0(1, new int[] {R.drawable.exp00, R.drawable.exp01, R.drawable.exp02, R.drawable.exp03, R.drawable.exp04, R.drawable.exp05 }),
	EXPLOSION1(1, new int[] {R.drawable.exp10, R.drawable.exp11, R.drawable.exp12, R.drawable.exp13 });


	private int hp;
	private int[] imageIds;

	GameEntityType(int hp, int[] defaultImageId) {
		this.hp = hp;
		imageIds = defaultImageId;
	}

	public int[] getImageResourceIds() {
		return imageIds;
	}

	public int getHp() {
		return hp;
	}
}
