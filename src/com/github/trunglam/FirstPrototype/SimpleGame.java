package com.github.trunglam.FirstPrototype;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class SimpleGame extends BasicGame{
	
	Image fullMap = null;
	Image land = null;
	Image standing = null;
	Image jumpingRight = null, jumpingLeft = null;
	Animation walkingRight = null, walkingLeft = null;
	float x = 32;
	float y = 35;
	float scale = 1.0f;
	int position = 0;
	int v = 70;
	boolean jump = false;
	boolean firstMap = true;
	boolean max = false;
	boolean min= false;
	float verticalSpeed = 0.0f;
	BlockPos firstBlock = null;
	BlockPos secondBlock = null;
	
	enum State { STAND, RIGHT, LEFT, JUMP }
	State currentState = State.STAND;
	
	public SimpleGame() {
		super("First Game!");
	}

	@Override
	public void render(GameContainer arg0, Graphics g) throws SlickException {
		land.draw(0,0);
		String pos = "x = " + x + ", y =" + y;
		String feetPos = "feet y =" + (y + 92);
		g.drawString(pos, 200, 0);
		g.drawString(feetPos, 200, 50);
		
		switch (currentState) {
		case STAND:
			standing.draw(x, y);
			break;
		case RIGHT:
			walkingRight.draw(x, y);
			break;
		case LEFT:
			walkingLeft.draw(x, y);
			break;
		case JUMP:
			if (position == 0)
				jumpingRight.draw(x, y);
			else
				jumpingLeft.draw(x, y);
				
		}
	}

	@Override
	public void init(GameContainer arg0) throws SlickException {
		fullMap = new Image("data/level_2.png");
		land = fullMap.getSubImage(0, 0, 480, 160);
		firstBlock = new BlockPos(-2, 25, 160-64, 4);
		secondBlock = new BlockPos(382, 32, 160-64, 4);
		
		standing = new Image("data/front.png");
		jumpingRight = new Image("data/jump.png");
		jumpingLeft = jumpingRight.getFlippedCopy(true, false);
		
		
		Image[] walkingArrayRight = {new Image("data/walk0001.png"), new Image("data/walk0002.png"), new Image("data/walk0003.png"),
				new Image("data/walk0004.png"), new Image("data/walk0005.png"), new Image("data/walk0006.png"), new Image("data/walk0007.png"),
				new Image("data/walk0008.png"), new Image("data/walk0009.png"), new Image("data/walk0010.png")
		};
		Image[] walkingArrayLeft = {new Image("data/walk0001.png").getFlippedCopy(true, false), 
				new Image("data/walk0002.png").getFlippedCopy(true, false), 
				new Image("data/walk0003.png").getFlippedCopy(true, false),
				new Image("data/walk0004.png").getFlippedCopy(true, false), 
				new Image("data/walk0005.png").getFlippedCopy(true, false), 
				new Image("data/walk0006.png").getFlippedCopy(true, false),
				new Image("data/walk0007.png").getFlippedCopy(true, false),
				new Image("data/walk0008.png").getFlippedCopy(true, false),
				new Image("data/walk0009.png").getFlippedCopy(true, false),
				new Image("data/walk0010.png").getFlippedCopy(true, false)
		};
		int[] duration = { v, v, v, v, v, v, v, v, v, v};
		
		walkingRight = new Animation(walkingArrayRight, duration, false);
		walkingLeft = new Animation(walkingArrayLeft, duration, false);

	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		Input input = gc.getInput();
		
		if (input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_W)) {
			if (input.isKeyDown(Input.KEY_A)) {
				if (x > 0) {
					x -= .2*delta;
					walkingLeft.update(delta);
				}
				else
					min = true;
				position = 1;
				if (currentState == State.STAND)
					currentState = State.LEFT;
			}
			if (input.isKeyDown(Input.KEY_D)) {
				if (x < 480 - 75) {
					x += .2*delta;
					walkingRight.update(delta);
				}
				else
					max = true;
				position = 0;
				if (currentState == State.STAND)
					currentState = State.RIGHT;
			}
			if (input.isKeyDown(Input.KEY_W) && !jump) {
				verticalSpeed = -.2f * delta;
				jump = true;
				currentState = State.JUMP;
			}
		}
		else {
			currentState = State.STAND;
		}
		
		if (y < -15 && jump){					//ONCE JUMP IS AT PEAK
			verticalSpeed = .2f * delta;
		}
		if (y <= 35)
			y += verticalSpeed;
		else {
			currentState = State.STAND;
			verticalSpeed = 0;
			jump = false;
			y = 35;
		}
		
		if (firstMap && firstBlock.collisionX(x)) {
			if (y <= firstBlock.jumpClearHeight) {
				y = firstBlock.jumpClearHeight;
			}
			else
				x = 32;
		}
		
		if (!firstMap && secondBlock.collisionX(x)) {
			if (y <= secondBlock.jumpClearHeight) {
				y = secondBlock.jumpClearHeight;
			}
			else
				x = secondBlock.getX();
		}
		
		

		
		
		if (firstMap && max) {
			land = fullMap.getSubImage(481, 0, 480, 160);

			x = 0;
			firstMap = false;
			max = false;
		}
		
		if (!(firstMap) && min) {
			land = fullMap.getSubImage(0, 0, 480, 160);
			
			x = 480 - 75;
			firstMap = true;
			min = false;
		}
		
		
	}
	
	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new SimpleGame());
		
		app.setDisplayMode(480, 160, false);
		app.setResizable(true);
		app.start();
	}
}
