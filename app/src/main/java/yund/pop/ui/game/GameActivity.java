package yund.pop.ui.game;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import yund.pop.R;
import yund.pop.ui.shop.ShopItem;
import yund.pop.ui.shop.ShopItemView;
import yund.pop.util.GamePreferenceManager;

public class GameActivity extends AppCompatActivity {

	@BindView(R.id.game_surface_view) GameView gameView;
	@BindView(R.id.score_text) TextView scoreTextView;
	@BindView(R.id.count_down_text) TextView countDownText;
	@BindView(R.id.item_layout) LinearLayout itemLayout;
	@BindView(R.id.coin_text) TextView coinText;
	@BindView(R.id.status_text) TextView statusText;
	@BindView(R.id.result_layout) LinearLayout resultLayout;

	private long score = 0;
	private int coinEarned = 0;
	private int initialCoin;

	private Handler countDownHandler = new Handler();
	private int countDown;
	private long countDownStartTime;

	private GameView.OnScoreChangeListener onScoreChangeListener = score -> {
		this.score = score;
		runOnUiThread(() -> scoreTextView.setText(getString(R.string.score) + ": " + score));
	};

	private GameView.OnCoinEarnedListener onCoinEarnedListener = value -> {
		coinEarned = value;
		runOnUiThread(() -> coinText.setText("" + (initialCoin + coinEarned)));
	};

	private GameView.OnCountChangedListener onCountChangedListener = (current, max) -> {
		runOnUiThread(() -> {
			statusText.setText(getString(R.string.count) + ": " + current + " / " + max);
			if (current > max) {
				gameView.pauseGame();
				showGameResult();
			}
		});
	};

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		ButterKnife.bind(this);

		initializeGameView();
		initializeItemLayout();
		initializeScoreText();

		countDown = 3;
		initialCoin = GamePreferenceManager.getCoin(this);
	}

    @Override
    protected void onResume() {
        super.onResume();
	    if (countDown <= 0) {
		    gameView.resumeGame();
	    } else {
		    startCountDown();
	    }
    }

    @Override
    protected void onPause() {
        super.onPause();
	    countDown = 3;
        gameView.pauseGame();
	    GamePreferenceManager.setCoin(this, coinEarned + initialCoin);
    }

	@Override
    protected void onDestroy() {
        super.onDestroy();
        gameView.stopGame();
	    GamePreferenceManager.saveNewScore(this, score);
    }


	private void startCountDown() {
		countDownStartTime = System.currentTimeMillis();
		countDownText.setText("" + countDown);
		countDownText.setVisibility(View.VISIBLE);
		postCountDownRunnable();
	}

	private void postCountDownRunnable() {
		gameView.pauseGame();
		countDownHandler.post(() -> {
			if ((System.currentTimeMillis() - countDownStartTime) / 1000 >= 1) {
				countDown--;
				if (countDown <= 0) {
					gameView.resumeGame();
					countDownText.setVisibility(View.GONE);
					return;
				}
				countDownText.setText("" + countDown);
				countDownStartTime = System.currentTimeMillis();
			}
			postCountDownRunnable();
		});
	}

    private void initializeGameView() {
		gameView.setOnScoreChangeListener(onScoreChangeListener);
	    gameView.setOnCoinEarnedListener(onCoinEarnedListener);
		gameView.setOnCountChangedListener(onCountChangedListener);
	}

	private void initializeItemLayout() {
		itemLayout.removeAllViews();
		for(ShopItem item : ShopItem.values()) {
			ShopItemView view = new ShopItemView(this);
			view.setType(item);
			view.removeDescription();
			view.setOnClickListener(onItemClickListener);
			itemLayout.addView(view);
		}
	}

	private View.OnClickListener onItemClickListener = v -> {
		// Can't use when the game is over or before the game is not started yet
		if (resultLayout.getVisibility() == View.VISIBLE ||
				countDownText.getVisibility() == View.VISIBLE) {
			return;
		}


		ShopItemView item = (ShopItemView) v;
		if (GamePreferenceManager.useShopItem(this, item.getType())) {
			gameView.onItemClicked(item.getType());
		}
		item.refreshItemCount();
	};

	private void initializeScoreText() {
		scoreTextView.setText(getString(R.string.score) + ": 0");
	}

	private void showGameResult() {
		resultLayout.setVisibility(View.VISIBLE);
	}

	@OnClick(R.id.retry_button)
	public void onClickRetry() {
		gameView.restartGame();
		resultLayout.setVisibility(View.GONE);
	}

	@OnClick(R.id.exit_button)
	public void onExitClicked() {
		onBackPressed();
	}
}
