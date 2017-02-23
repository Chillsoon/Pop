package yund.pop.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import yund.pop.R;
import yund.pop.util.GamePreferenceManager;

public class ScoreActivity extends AppCompatActivity {

	@BindView(R.id.score_layout) LinearLayout scoreLayout;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score);
		ButterKnife.bind(this);
		initializeScoreLayout();
	}

	private void initializeScoreLayout() {
		LayoutInflater inflater = LayoutInflater.from(this);
		List<Long> scores = GamePreferenceManager.getTopScores(this);

		if (scores.size() > 0) {
			scoreLayout.removeAllViews();
			for (int i = 0; i < scores.size(); i++) {
				TextView view = (TextView) inflater.inflate(R.layout.view_top_scores, scoreLayout, false);
				view.setText((i + 1) + ": " + scores.get(i));
				scoreLayout.addView(view);
			}
		} else {
			TextView view = (TextView) inflater.inflate(R.layout.view_top_scores, scoreLayout, false);
			view.setText(R.string.no_record);
			scoreLayout.addView(view);
		}
	}
}
