package com.khrismuc.crushthrees;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public class Three extends Actor {

	public int value;
	public int special = 0;
	public float destX, destY;
	
	public boolean connected = false;
	public boolean removeOnDestReached = false;
	public boolean dead = false;
	public Field field;
	SequenceAction action;

	public Three(int v, int sp, Field f) {
		value = v;
		special = sp;
		field = f;
	}
	
	public TextureRegion getTexture() {
		
		int ti = (int) MathUtils.log(3.0f, value);
		if (ti > 7) ti = 7;
		
		return new TextureRegion(field.threes, ti * 128, special * 128, 128, 128);
	}

	public void threeUp() {
		value *= 3;
		action = new SequenceAction(Actions.scaleTo(1.2f, 1.2f, 0.2f, Interpolation.circle), Actions.scaleTo(1.0f,
				1.0f, 0.2f, Interpolation.circle));
		this.addAction(action);
	}

	@Override
	public void act(float delta) {

		if (action != null)
			action.act(delta);

		float dx = destX - getX();
		float dy = destY - getY();

		float threshold = 0.5f;
		float lazyness = 0.2f;

		boolean destReached = true;

		if (dx > -threshold && dx < threshold)
			setX(destX);
		else {
			setX(getX() + dx * lazyness);
			destReached = false;
		}
		if (dy > -threshold && dy < threshold)
			setY(destY);
		else {
			setY(getY() + dy * lazyness);
			destReached = false;
		}

		if (removeOnDestReached && destReached)
			dead = true;
	}

	public void mergeTo(int dx, int dy) {
		destX += dx * Sys.game.cellSize;
		destY += dy * Sys.game.cellSize;
		removeOnDestReached = true;
		toBack();
	}
	
	public void mergeTo(Three t) {
		Point dest = field.getCoords(t);
		Point self = field.getCoords(this);
		mergeTo(dest.x - self.x, dest.y - self.y);
	}

	public void moveTo(int x, int y) {
		Point gc = field.getGraphicCoords(x, y);
		destX = gc.fx;
		destY = gc.fy;
	}

	public void draw(Batch batch, float parentAlpha) {

		float x = getX() - (getScaleX() - 1f) * getWidth() / 2f;
		float y = getY() - (getScaleY() - 1f) * getHeight() / 2f;

		batch.draw(getTexture(), x, y, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(),
				getRotation());
		if (this == field.selected) {
			batch.draw(new TextureRegion(field.threes, 0, 256, 128, 128), getX(), getY(), getWidth(), getHeight());
		}
	}

}
