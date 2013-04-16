package com.mfp.cakegdx;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class ATiltTest implements ApplicationListener, InputProcessor
{

	public static void main(String[] args) {
		new LwjglApplication(new ATiltTest(), "ATiltTest", 500, 200, false);
	}
	
	Vector2 pos;
	boolean touching = false;
	
	float tgtX;
	float tgtY;
	
	float vx;
	float vy;
	
	int lvl;
	Texture t;
	SpriteBatch sb;
	
	BitmapFont font;
	
	@Override
	public void create()
	{
		Gdx.input.setInputProcessor(this);
		Pixmap pm = new Pixmap(8, 8, Pixmap.Format.RGBA8888);
		pm.setColor(Color.RED);
		pm.drawCircle(0, 0, 5);
		t = new Texture(pm);
		pm.dispose();
		sb = new SpriteBatch();
		
		font = new BitmapFont(Gdx.files.internal("data/default.fnt"), false);
	}

	private void update() {
		if (pos == null) {
			return;
		}
		float dt = (float) Math.min(0.06, Gdx.graphics.getDeltaTime());
		float dis = pos.dst(tgtX, tgtY);
		vx = (tgtX - pos.x) * 30f;
		vy = (tgtY - pos.y) * 30f;
		if (dis > 5) {
			lvl = 3;
		} else if (dis > 3) {
			lvl = 2;
		} else if (dis > 0) {
			lvl = 1;
		} else {
			lvl = 0;
		}
		pos.add(vx * dt, vy * dt);
		System.out.println("lv " + lvl);
	}
	
	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render()
	{
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		update();
		sb.begin();
		if (pos != null) {
			sb.draw(t, this.pos.x, this.pos.y);	
			System.out.println("drawing");
		}
		font.draw(sb, lvl + "", 5, 150);
		sb.end();
	}

	@Override
	public void resize(int arg0, int arg1)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean keyDown(int arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int arg0, int arg1)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3)
	{
		if (pos == null) {
			pos = new Vector2(arg0, arg1);
		}
		touching = true;
		return true;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2)
	{
		tgtX = arg0;
		tgtY = arg1;
		return true;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3)
	{
		touching = false;
		return true;
	}

}
