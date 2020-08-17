package yund.pop.ui;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import yund.pop.R;
import yund.pop.util.GamePreferenceManager;

public class CoinView extends TextView {
	public CoinView(Context context) {
		super(context);
		init();
	}

	public CoinView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CoinView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public CoinView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	private void init() {
		setCompoundDrawablesWithIntrinsicBounds(R.drawable.coin_bitmap, 0, 0, 0);
		setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.coin_drawable_margin));
		setText("" + GamePreferenceManager.getCoin(getContext()));
		setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.coin_text_size));
		setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
		setGravity(Gravity.CENTER);
	}

	public void refresh() {
		setText("" + GamePreferenceManager.getCoin(getContext()));
	}
}
