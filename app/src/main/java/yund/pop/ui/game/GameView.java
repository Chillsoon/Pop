package yund.pop.ui.game;

import android.content.Context;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import yund.pop.ui.shop.ShopItem;
import yund.pop.util.GamePreferenceManager;

public class GameView extends SurfaceView implements Callback{

	private GameThread mThread;
	SurfaceHolder mHolder;

	private OnScoreChangeListener onScoreChangeListener;
	private OnCoinEarnedListener onCoinEarnedListener;
	private OnCountChangedListener onCountChangedListener;

	// options
	private Vibrator vibrator;
	private boolean isVibrationOn;
	private boolean isSoundOn;

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);

		mHolder = holder;
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

		initGame();
		setFocusable(true);
	}

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mThread != null) {
            mThread.stopThread();
            mThread = null;
        }
    }

    private void initGame() {
		isVibrationOn = GamePreferenceManager.isVibrationOn(getContext());
		isSoundOn = GamePreferenceManager.isSoundOn(getContext());
	}

	public void surfaceCreated(SurfaceHolder holder) {
		if (mThread == null) {
			mThread = new GameThread(holder, this);
		}
	}

	public void surfaceChanged(SurfaceHolder arg0, int format, int width, int height) {
		resumeGame();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		pauseGame();
	}

	public void stopGame() {
        if (mThread != null) {
            mThread.stopThread();
        }
	}

	public void pauseGame() {
        if (mThread != null) {
            mThread.pauseAndResume(true);
        }
	}

	public void resumeGame() {
        if (mThread != null) {
	        if (mThread.isRunning()) {
		        mThread.pauseAndResume(false);
	        } else {
		        mThread.start();
	        }
        }
	}

	public void restartGame() {
        if (mThread != null) {
            mThread.stopThread();
            mThread = null;
        }
		mThread = new GameThread(mHolder, this);
		mThread.start(); 
	}

	public void vibrate(long l){
		if(isVibrationOn){
			vibrator.vibrate(l);
		}
	}


	//-------------------------------------
	//  onTouch Event
	//-------------------------------------
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mThread.onTouchEvent(event);
		return true;
	}

	//-------------------------------------
	//  Listeners
	//-------------------------------------
	public interface OnScoreChangeListener {
		void onScoreChanged(long score);
	}

	public interface OnCoinEarnedListener {
		void onCoinEarned(int value);
	}

	public interface OnCountChangedListener {
		void onCountChanged(int current, int max);
	}

	void onScoreChanged(long score) {
		if (onScoreChangeListener != null) {
			onScoreChangeListener.onScoreChanged(score);
		}
	}

	void onCoinEarned(int value) {
		if (onCoinEarnedListener != null) {
			onCoinEarnedListener.onCoinEarned(value);
		}
	}

	void onCountChanged(int current, int max) {
		if (onCountChangedListener != null) {
			onCountChangedListener.onCountChanged(current, max);
		}
	}

	public void setOnScoreChangeListener(OnScoreChangeListener listener) {
		onScoreChangeListener = listener;
	}

	public void setOnCoinEarnedListener(OnCoinEarnedListener listener) {
		onCoinEarnedListener = listener;
	}

	public void setOnCountChangedListener(OnCountChangedListener onCountChangedListener) {
		this.onCountChangedListener = onCountChangedListener;
	}

	public void onItemClicked(ShopItem type) {
		mThread.onShopItemUsed(type);
	}
}


