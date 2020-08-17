package yund.pop.ui.shop;

import android.content.Context;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import yund.pop.R;
import yund.pop.util.GamePreferenceManager;

public class ShopItemView extends RelativeLayout {

	@BindView(R.id.shop_item_left_image) View leftImage;
	@BindView(R.id.shop_item_description) TextView description;
	@BindView(R.id.shop_item_cost) TextView costView;
	@BindView(R.id.shop_item_count) TextView itemCount;
	@BindView(R.id.shop_item_right_layout) LinearLayout descriptionLayout;

	private ShopItem type;

	public ShopItemView(Context context) {
		super(context);
		init();
	}

	public ShopItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ShopItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public ShopItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	private void init() {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.view_shop_item, this, false);
		addView(view);
		ButterKnife.bind(this, view);
	}

	public void setType(ShopItem type) {
		this.type = type;
		leftImage.setBackgroundResource(type.getImageId());
		description.setText(type.getStringId());
		costView.setText("" + type.getCost());
		refreshItemCount();
	}

	public ShopItem getType() {
		return type;
	}

	public void refreshItemCount() {
		int count = GamePreferenceManager.getItemCount(getContext(), type);
		itemCount.setText("" + count);
		setSelected(count > 0);
	}

	public void removeDescription() {
		descriptionLayout.setVisibility(GONE);
	}
}
