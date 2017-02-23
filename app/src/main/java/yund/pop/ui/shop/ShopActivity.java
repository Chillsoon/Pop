package yund.pop.ui.shop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import yund.pop.R;
import yund.pop.ui.CoinView;
import yund.pop.util.GamePreferenceManager;

public class ShopActivity extends AppCompatActivity {

	@BindView(R.id.shop_item_list) LinearLayout itemList;
	@BindView(R.id.coin_text) CoinView coin;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop);
		ButterKnife.bind(this);
		initializeItemList();
	}

	private void initializeItemList() {
		for (ShopItem item : ShopItem.values()) {
			ShopItemView view = new ShopItemView(this);
			view.setType(item);
			view.setOnClickListener(onItemClickListener);
			itemList.addView(view);
		}
	}

	private View.OnClickListener onItemClickListener = v -> {
		ShopItemView item = (ShopItemView) v;
		GamePreferenceManager.buyShopItem(this, item.getType());
		coin.refresh();
		item.refreshItemCount();
	};
}
