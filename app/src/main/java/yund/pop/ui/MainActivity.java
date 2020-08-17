package yund.pop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import yund.pop.R;
import yund.pop.ui.game.GameActivity;
import yund.pop.ui.shop.ShopActivity;

public class MainActivity extends AppCompatActivity {

	private static final int REQUEST_GAME = 10;
	private static final int REQUEST_SHOP = 11;
	private boolean backPressedOnce = false;

	@BindView(R.id.coin_text) CoinView coinView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

	@OnClick(R.id.start_button)
    public void onStartButtonClicked() {
	    Intent intent = new Intent(this, GameActivity.class);
		startActivityForResult(intent, REQUEST_GAME);
    }

    @OnClick(R.id.score_button)
    public void onScoreButtonClicked() {
	    Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.setting_button)
    public void onSettingButtonClicked() {
	    Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.shop_button)
    public void onShopButtonClicked() {
	    Intent intent = new Intent(this, ShopActivity.class);
        startActivityForResult(intent, REQUEST_SHOP);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		coinView.refresh();
		if (requestCode == REQUEST_GAME) {
			onGameResult(resultCode, data);
		} else if (requestCode == REQUEST_SHOP) {

		}
	}

	@Override
	public void onBackPressed() {
		if (backPressedOnce) {
			super.onBackPressed();
			return;
		}

		backPressedOnce = true;
		Toast.makeText(this, getString(R.string.back_to_exit), Toast.LENGTH_SHORT).show();

		// Toast.LENGTH_SHORT is 2 seconds long
		new Handler().postDelayed(() -> {
			backPressedOnce = false;
		}, 2000);
	}

	private void onGameResult(int resultCode, Intent data) {

	}
}
