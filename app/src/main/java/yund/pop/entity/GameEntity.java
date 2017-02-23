package yund.pop.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import yund.pop.util.GameImageManager;

public class GameEntity {

	private Context context;

	private float x;
	private float y;
	private int speedX;
	private int speedY;

	private int width;
	private int height;

	boolean clicked = false;

	GameEntityType type;
	int imageIndex = 0;

	GameEntity(Context context, GameEntityType type, float x, float y) {
		this.context = context;
		this.type = type;
		updateImageSize(GameImageManager.getInstance(context).getImage(type.getImageResourceIds()[imageIndex]));
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void move() {
		x += speedX;
		y += speedY;
	}

	public void draw(Canvas canvas) {
        Bitmap image = GameImageManager.getInstance(context).getImage(type.getImageResourceIds()[imageIndex]);
        canvas.drawBitmap(image, x, y, null);
        updateImageSize(image);
	}

	public void setSpeed(int speedX, int speedY) {
		this.speedX = speedX;
		this.speedY = speedY;
	}

    private float getRightX() {
        return x + width;
    }

    private float getBottomY() {
        return y + height;
    }

	public void changeDirection(int width, int height) {
		if (x + speedX < 0 || getRightX() + speedX > width) {
			speedX *= -1;
		}

		if (y + speedY < 0 || getBottomY() + speedY > height) {
			speedY *= -1;
		}
	}

	public boolean canMove(int width, int height) {
		return x + speedX >= 0 && getRightX() + speedX <= width && y + speedY >= 0 && getBottomY() + speedY <= height;
	}

	private void updateImageSize(Bitmap bitmap) {
		width = bitmap.getWidth();
		height = bitmap.getHeight();
	}

    public boolean isClicked(float x, float y){
        return (this.x <= x && getRightX() >= x && this.y <= y && getBottomY() >= y);
    }

    public void adjustInitialPosition(int width, int height) {
        if (getRightX() > width) {
            x = width - this.width;
        }

        if (getBottomY() > height) {
            y = height - this.height;
        }
    }

	public void onClicked() {
		clicked = true;
	}

	public boolean isClicked() {
		return clicked;
	}
}
