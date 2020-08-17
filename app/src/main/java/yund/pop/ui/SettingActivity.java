package yund.pop.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import yund.pop.R;
import yund.pop.util.GamePreferenceManager;

public class SettingActivity extends AppCompatActivity {

	@BindView(R.id.setting_vibration_button) TextView vibrationButton;
	@BindView(R.id.setting_sound_button) TextView soundButton;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		ButterKnife.bind(this);
		initializeSettingValues();
	}

	private void initializeSettingValues() {
		if (GamePreferenceManager.isVibrationOn(this)) {
			setVibrationOption(true);
		}

		if (GamePreferenceManager.isSoundOn(this)) {
			setSoundOption(true);
		}
	}

	@OnClick(R.id.setting_vibration_button)
	public void onVibrationButtonClicked() {
		setVibrationOption(!vibrationButton.isSelected());
	}

	@OnClick(R.id.setting_sound_button)
	public void onSoundButtonClicked() {
		setSoundOption(!soundButton.isSelected());
	}

	private void setVibrationOption(boolean turnOn) {
		if (turnOn) {
			vibrationButton.setSelected(true);
			vibrationButton.setText(R.string.vibration_on);
		} else {
			vibrationButton.setSelected(false);
			vibrationButton.setText(R.string.vibration_off);
		}
		GamePreferenceManager.setVibrationOption(this, turnOn);
	}

	private void setSoundOption(boolean turnOn) {
		if (turnOn) {
			soundButton.setSelected(true);
			soundButton.setText(R.string.sound_on);
		} else {
			soundButton.setSelected(false);
			soundButton.setText(R.string.sound_off);
		}
		GamePreferenceManager.setSoundOption(this, turnOn);
	}
}
