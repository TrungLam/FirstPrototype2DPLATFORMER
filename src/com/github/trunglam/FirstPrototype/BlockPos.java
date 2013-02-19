package com.github.trunglam.FirstPrototype;

public class BlockPos {
	public float x;
	public float y, width, jumpClearHeight;
	
	public BlockPos(float x, float width, float y, float JCH) {
		this.x = x;
		this.y = y;
		this.width = width + x;
		this.jumpClearHeight = JCH;
	}
	
	public float getJCH() {
		return this.jumpClearHeight;
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
	
	public float getWidth() {
		return this.width;
	}
	
	public boolean collisionX(float xSprite) {
		if (xSprite > x && xSprite < width)
			return true;
		return false;
	}
}
