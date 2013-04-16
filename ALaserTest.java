package com.mfp.cakegdx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Vector2;

public class ALaserTest implements ApplicationListener {
	Mesh mesh;
	Texture texture;

	float pi = 3.14159f;
	List<Float> vertices;
	
	float shipX;
	float shipY;
	short pieces = 100;
	float time;
	short minPiece = 20;
	float arcStep = 0.002f;
	double arc;
	float radius = 0.5f;
	
	float laserV = 0.6f; //move speed
	float newLaserSpeed = 0.05f; // gen new speed
	float laserNumPerGen = 50; // laser num each gen
	float lastGenTime;
	private float cf;
	short maxLaserNum = 500;
	boolean toWide = true;
	
	Laser lastGenLaser;
	List<Laser> lasers;
	List<Short> indices;
	
	float middleX;
	float middleY;
	float minArc = pi / 6;
	
	private void update() {
		float delta = (float) Math.min(0.06, Gdx.graphics.getDeltaTime());
		
		Iterator<Laser> ite = lasers.iterator();
		while (ite.hasNext()) {
			Laser l = ite.next();
			if (l.leftLowPos.y > 1) {
				ite.remove();
				continue;
			}
			l.move(delta);
		}
		
		time += delta;
		lastGenTime += delta;
//		System.out.println("lastGenTime " + lastGenTime);
		if (lastGenTime > newLaserSpeed) {
			newLaser(delta);
			lastGenTime = 0;
		}
		
		this.genMeshData();
	}
	
	public void create() {
		cf = Color.toFloatBits(255, 255, 255, 0);
		lasers = new ArrayList<Laser>();
		mesh = new Mesh(true, 4 * (maxLaserNum), 6 * (maxLaserNum), new VertexAttribute(Usage.Position, 3, "a_position"), new VertexAttribute(Usage.ColorPacked, 4,
		"a_color"), new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoords"));
//		texture = new Texture(Gdx.files.internal("data/20894_64.png"), true);
		texture = new Texture(Gdx.files.internal("data/MEGAlaser.png"), true);
		texture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
	}
	
	public static void main(String[] args) {
		new LwjglApplication(new ALaserTest(), "mesh test", 600, 500, false);
	}
	
	public void render () {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		update();
		
		if (!this.lasers.isEmpty()) {
			Gdx.gl.glEnable(GL10.GL_TEXTURE_2D);
			texture.bind();
			mesh.render(GL10.GL_TRIANGLES, 0, 6 * lasers.size());
		}
		
		
//		System.out.println("renderring " + lasers.size());
	}
	
	
	class Laser {
		Vector2 leftHighPos ;
		Vector2 rightHighPos;
		Vector2 leftLowPos;
		Vector2 rightLowPos;
		
		
		Vector2 leftLowUV;
		Vector2 rightLowUV;
		Vector2 rightHighUV;
		Vector2 leftHighUV ;
		
		public void move(float delta) {
			float moveLength = (delta * laserV);
			leftHighPos.add(0, moveLength);
			rightHighPos.add(0, moveLength);
			leftLowPos.add(0, moveLength);
			rightLowPos.add(0, moveLength);
		}
		
		public void addToVertice(List<Float> vertices) {
			//left low put
			add(vertices, leftLowPos.x,leftLowPos.y,0f,cf, leftLowUV.x, leftLowUV.y);
//			System.out.println( leftLowPos.x + " "+leftLowPos.y);
			//right low put
			add(vertices, rightLowPos.x,rightLowPos.y,0f,cf, rightLowUV.x,rightLowUV.y);
			//right high put
			add(vertices, rightHighPos.x,rightHighPos.y,0f,cf, rightHighUV.x,rightHighUV.y);
			//left high put
			add(vertices, leftHighPos.x,leftHighPos.y,0f,cf, leftHighUV.x,leftHighUV.y);
//			System.out.println( (1f -  1f * (i  * 1.0f / pieces)) + " e  "+(1f -  1f * ( (i + 1)  * 1.0f / pieces)));
		}

		public void genUV(int idx, int size)
		{
			//left low put
			leftLowUV = new Vector2(0f,1f -  1f * ( (idx + 1)  * 1.0f / size));
//			System.out.println( leftLowPos.x + " "+leftLowPos.y);
			//right low put
			rightLowUV = new Vector2(1f,1f -  1f * ( (idx + 1)  * 1.0f / size));
			//right high put
			rightHighUV = new Vector2(1f,1f -  1f * ( idx   * 1.0f / size));
			//left high put
			leftHighUV = new Vector2(0f,1f -  1f * (idx  * 1.0f / size));
//			System.out.println( (1f -  1f * (i  * 1.0f / pieces)) + " e  "+(1f -  1f * ( (i + 1)  * 1.0f / pieces)));
		}
	}
	
	
	private void genMeshData() {
		vertices = new ArrayList<Float>();
		int idx = 0;
		for (Laser l : lasers) {
			l.genUV(idx, lasers.size());
			l.addToVertice(vertices);
			idx++;
		}
		
		indices = new ArrayList<Short>();
		int laserCount = this.lasers.size();
		for (short j = 0;j < laserCount ;j++) { //6 indice each laser
			short i = (short) (j * 4);
			this.add(indices, i, (short)(i+1),(short)(i+ 2), (short)(i+2),(short)(i+ 3),(short)(i));
		}
		
		

		float[] verticesFs = new float[vertices.size()]; //24 floats each laser
		int vCount = 0;
		for (Float f : vertices) {
			verticesFs[vCount] = f;
			vCount++;
		}
		
		short[] indicesSs = new short[indices.size()];
		int iCount = 0;
		for (Short s: indices) {
			indicesSs[iCount] = s;
			iCount++;
		}
		
		mesh.setVertices(verticesFs);
		mesh.setIndices(indicesSs);
	
		
	}
	
	public void newLaser(float delta) {
		for (int genCount = 0; genCount < this.laserNumPerGen; genCount++) {
			
			if (this.lasers.size() >= this.maxLaserNum) {
				return;
			}
			
//			System.out.println("gen new laser");
			
			
			Laser laser = new Laser();
			lasers.add(laser);
			
		
			laser.leftHighPos = this.calculatePos(true, true, genCount);
			laser.rightHighPos = this.calculatePos(false, true, genCount);
			laser.leftLowPos = this.calculatePos(true, false, genCount);
			laser.rightLowPos = this.calculatePos(false, false, genCount);
			if (arc < minArc || arc > (pi - minArc)) {
				arc = minArc;
			}
			arc += arcStep;
			arc %= pi;
			
			if (lasers.size() > maxLaserNum - 20) {
				System.out.println(laser.leftHighPos + " " + laser.rightHighPos + " " + laser.leftLowPos + " " + laser.rightLowPos );
			}
			lastGenLaser = laser;
		
		}
	}
	
	private void checkLastGenLaser() {
		if (this.lastGenLaser != null && lastGenLaser.leftLowPos != null) {
			return;
		}
		lastGenLaser = new Laser();
		lastGenLaser.leftLowPos = new Vector2(0, 0);
		lastGenLaser.rightLowPos = new Vector2(0, 0);
	}
	
	private Vector2 calculatePos(boolean left, boolean high, int genCount) {
		this.checkLastGenLaser();
		if (high && left) {
			return lastGenLaser.leftLowPos; //reuse
		}
		if (high && !left) {
			return lastGenLaser.rightLowPos; //reuse
		}
		
		float curY = shipY + (this.laserNumPerGen - genCount) / laserNumPerGen * (lastGenLaser.rightLowPos.y - shipY);
		if (left) {
			return new Vector2( (float) (shipX - Math.sin(arc) * radius /2), curY);
		}
		if (!left) {
			return new Vector2( (float) (shipX + Math.sin(arc) * radius /2), curY);
		}
		return null;
	}
	
	private void add(List<Short> indices, Short... shorts) {
		for (Short f : shorts) {
			indices.add(f);
		}
	}

	private void add(List<Float> vertices, Float... floats) {
		for (Float f : floats) {
			vertices.add(f);
		}
	}
	
	public boolean needsGL20 () {
		return false;
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
	public void resize(int arg0, int arg1)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume()
	{
		// TODO Auto-generated method stub
		
	}
}
