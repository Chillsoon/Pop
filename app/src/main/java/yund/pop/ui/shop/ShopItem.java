package yund.pop.ui.shop;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import yund.pop.R;

public enum ShopItem {

	ITEM0(R.drawable.shop_item_0, R.string.shop_item_0, 3),
	ITEM1(R.drawable.shop_item_1, R.string.shop_item_1, 5);

	private int imageId;
	private int stringId;
	private int cost;

	ShopItem(@DrawableRes int imageId, @StringRes int stringId, int cost) {
		this.imageId = imageId;
		this.stringId = stringId;
		this.cost = cost;
	}

	public int getImageId() {
		return imageId;
	}

	public int getStringId() {
		return stringId;
	}

	public int getCost() {
		return cost;
	}
}
