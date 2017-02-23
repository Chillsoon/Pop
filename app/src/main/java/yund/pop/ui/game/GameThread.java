package yund.pop.ui.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import yund.pop.entity.BonusEntity;
import yund.pop.entity.CoinEntity;
import yund.pop.entity.ExplosionEntity;
import yund.pop.entity.GameEntity;
import yund.pop.entity.GameEntityType;
import yund.pop.entity.PopEntity;
import yund.pop.ui.shop.ShopItem;

class GameThread extends Thread {

    private static final String LOG_TAG = GameThread.class.getSimpleName();

	private static final int SPAWN_PERIOD_SECONDS = 2;
	private static final int LEVEL_UP_PERIOD_SECONDS = 15;
	private static final int SPAWN_NUMBER_ON_MISS = 5;

	private boolean canRun = true;
	private boolean isWait = false;
	private boolean started = false;

	private SurfaceHolder mHolder;

	private List<GameEntity> entityList = new LinkedList<>();
    private List<GameEntity> deathNote = new LinkedList<>();
    private Random random = new Random();

	// time
	private long levelTimer;
	private long spawnTimer;

	// counter
	private int level;
	private long score;
	private int coin;
	private int missCount;
	private int popCount;
	private int gameOverLimit;

	// Need this to access android stuff
	private GameView gameView;

	GameThread(SurfaceHolder holder, GameView gameView) {
		mHolder = holder;
		reset();
		this.gameView = gameView;
	}

	private void reset(){
		level = 1;
		levelTimer = System.currentTimeMillis();
		score = 0;
		spawnTimer = System.currentTimeMillis();
		coin = 0;
		missCount = 0;
		popCount = 0;
		gameOverLimit = 50;
	}

	private void levelUp(){
		if((System.currentTimeMillis() - levelTimer)/1000 >= LEVEL_UP_PERIOD_SECONDS){
			level++;
			levelTimer = System.currentTimeMillis();
		}
	}

	private void spawnPopEntity() {
		if((System.currentTimeMillis() - spawnTimer)/1000 >= SPAWN_PERIOD_SECONDS){
			for (int i = 0; i < level * missCount + level; i++) {
				addRandomPopEntity();
			}
			spawnTimer = System.currentTimeMillis();
		}
	}

	private void addRandomPopEntity() {
		PopEntity newEntity = new PopEntity(gameView.getContext(), getPopType(), getRandomX(), getRandomY());
		newEntity.adjustInitialPosition(gameView.getWidth(), gameView.getHeight());
		newEntity.setSpeed(random.nextInt(5) - 2, random.nextInt(5) - 2);
		entityList.add(newEntity);
		popCount++;
	}

	private GameEntityType getPopType() {
		if (level > 5) {
            int type = random.nextInt(GameEntityType.BALL5.ordinal() - GameEntityType.BALL0.ordinal());
			return GameEntityType.values()[GameEntityType.BALL0.ordinal() + type];
		}
        int type = random.nextInt(GameEntityType.BALLOON5.ordinal() - GameEntityType.BALLOON0.ordinal());
		return  GameEntityType.values()[GameEntityType.BALLOON0.ordinal() + type];
	}

	private float getRandomX() {
		return random.nextInt(gameView.getWidth());
	}

	private float getRandomY() {
        return random.nextInt(gameView.getHeight());
	}

	void onTouchEvent(MotionEvent event) {
		if(!isWait){
			float fx = event.getX();
			float fy = event.getY();

			if(event.getAction() == MotionEvent.ACTION_DOWN){
				entityList.add(new ExplosionEntity(gameView.getContext(), 0, fx, fy));
                for (GameEntity entity : entityList) {
                    if (entity.isClicked(fx, fy) && !(entity instanceof ExplosionEntity)) {
                        entity.onClicked();
                        return;
                    }
                }

				onMissClicked();

			} else if(event.getAction() == MotionEvent.ACTION_UP){

			}
		}
	}

	private void onMissClicked() {
		for (int i = 0; i < SPAWN_NUMBER_ON_MISS; i++) {
			addRandomPopEntity();
		}
		missCount++;
		updatePopCount();
	}

	int getPopCount() {
		return popCount;
	}

	//-------------------------------------
	//  Move All
	//-------------------------------------
	private void MoveAll() {
		levelUp();
		spawnPopEntity();

		for (GameEntity entity : entityList) {
            if (entity instanceof PopEntity && ((PopEntity) entity).isDead()) {
                deathNote.add(entity);
                score += ((PopEntity) entity).getScore();
            } else if (entity.isClicked()) {
				deathNote.add(entity);
	            if (entity instanceof CoinEntity) {
		            coin += ((CoinEntity) entity).getValue();
	            } else if (entity instanceof BonusEntity) {
		            applyBonus(((BonusEntity) entity).getGameEntityType());
	            }
            } else if (entity instanceof ExplosionEntity && ((ExplosionEntity) entity).isFinished()) {
	            deathNote.add(entity);
            } else {
				if (!entity.canMove(gameView.getWidth(), gameView.getHeight())) {
                    entity.changeDirection(gameView.getWidth(), gameView.getHeight());
                }
                entity.move();
            }
		}

        for (GameEntity entity : deathNote) {
	        if (!(entity instanceof ExplosionEntity)) {
		        entityList.add(new ExplosionEntity(gameView.getContext(), 1, entity.getX(), entity.getY()));
	        }
            entityList.remove(entity);
	        if (entity instanceof PopEntity) {
		        popCount--;
		        GameEntity toAdd;
		        if (random.nextInt(100) >= 50) {
			        toAdd = new CoinEntity(gameView.getContext(), entity.getX(), entity.getY());
		        } else {
			        toAdd = new BonusEntity(gameView.getContext(), GameEntityType.BONUS0, entity.getX(), entity.getY());
		        }
		        toAdd.setSpeed(random.nextInt(5) - 2, random.nextInt(5) - 2);
		        entityList.add(toAdd);
	        }
        }

        deathNote.clear();
	}

	private void applyBonus(GameEntityType type) {
		switch (type) {
			case BONUS0:
				score += entityList.size() * 10;
				entityList.clear();
				deathNote.clear();
				popCount = 0;
				break;
			case BONUS1:
			case BONUS2:
			case BONUS3:
			case BONUS4:
			case BONUS5:
				break;
			default:
				break;
		}
	}


	//-------------------------------------
	//  updateScore
	//-------------------------------------
	private void updateScore(){
		gameView.onScoreChanged(score);
	}


	//-------------------------------------
	//  updateCoin
	//-------------------------------------
	private void updateCoin(){
		gameView.onCoinEarned(coin);
	}

	//-------------------------------------
	//  updatePopCount
	//-------------------------------------
	private void updatePopCount(){
		gameView.onCountChanged(popCount, gameOverLimit);
	}


	//-------------------------------------
	//  DrawAll
	//-------------------------------------
	private void DrawAll(Canvas canvas) {
		// drawing black background first
		canvas.drawColor(Color.WHITE);

		// Game is playing
		for (GameEntity entity : entityList) {
			entity.draw(canvas);
		}

		updateScore();
		updateCoin();
		updatePopCount();
	}

	public void run() {
		started = true;
		Canvas canvas = null;
		while (canRun) {

			synchronized (this) {
				if (isWait) {
					try {
						wait();
					} catch (Exception e) {
						// nothing
					}
				}
			}

			if(!isWait){
				canvas = mHolder.lockCanvas();
				try {
					synchronized (this) {
						MoveAll();
						DrawAll(canvas);
					}
				} catch (Exception e) {
                    Log.e(LOG_TAG, "", e);
                } finally {
					if (canvas != null) {
                        mHolder.unlockCanvasAndPost(canvas);
                    }
				}
			}
		}
	}

	void stopThread() {
		canRun = false;
		synchronized (this) {
			this.notify();
		}
	}

	void pauseAndResume(boolean value) {
		isWait = value;
		synchronized (this) {
			this.notify();
		}
	}

	boolean isRunning() {
		return started;
	}

	void onShopItemUsed(ShopItem type) {
		switch (type) {
			case ITEM0:
				gameOverLimit += 10;
				break;
			case ITEM1:
				missCount = 0;
				break;
			default:
				break;
		}
	}
}
