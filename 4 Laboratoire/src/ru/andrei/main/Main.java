package ru.andrei.main;

import static org.lwjgl.opengl.GL11.glViewport;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import ru.andrei.main.game.GameLevel;
import ru.andrei.main.math.Vector3f;
import ru.andrei.main.render.Camera;
import ru.andrei.main.render.DisplayManager;

public class Main {

	public static final float FPS_MAX = 60;
	boolean running = false;
	
	Camera c;
	GameLevel g;
		
	public Main() {
		DisplayManager.createFrame(720, 480, "Java 3D LWJGL - GitHub Project");
		c = new Camera(new Vector3f(2, 1, 2));
		c.setPerspectiveProjection(70.0f, 0.1f, 1000.0f);
		
		g = new GameLevel();
	}

	public void start() {
		running = true;
		loop();
	}

	public void stop() {
		running = false;
	}

	public void quit() {
		DisplayManager.dispose();
		System.exit(0);
	}

	public void loop() {
		
		long lastTickTime = System.nanoTime();
		long lastRenderTime = System.nanoTime();
		
		double tickTime = 1000000000.0 / 60.0;
		double renderTime = 1000000000.0 / FPS_MAX;
		
		int ticks = 0;
		int FPS = 0;
		
		long timer = System.currentTimeMillis();
		
		/*
		 * Tick'n'Timer
		 */
		
		while (running) {
			if (DisplayManager.isClosed()) { stop(); }
			boolean rendered = false;
			if (System.nanoTime() - lastTickTime > tickTime) { tick(); ticks++; lastTickTime += tickTime; }
			else if (System.nanoTime() - lastRenderTime > renderTime) { 
				render(); 
				DisplayManager.update(); 
				FPS++;
				rendered = true; 
				lastRenderTime += renderTime; 
				}
			if(System.currentTimeMillis() - timer > 1000)
			{
				timer += 1000;
				System.out.println(ticks + " ticks, " + FPS + " fps");
				ticks = 0;
				FPS = 0;
			} else 
			{
				try
				{
					Thread.sleep(1);
				} catch (InterruptedException ioe)
				{
					ioe.printStackTrace();
				}
			}
		}
		quit();
	}

	public void tick() {
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) Mouse.setGrabbed(false);
		if(Mouse.isButtonDown(0)) Mouse.setGrabbed(true);
		if(!Mouse.isGrabbed()) return;
		c.keyboardInputListener();
		g.update();
	}

	public void render() {
		// Resize -> OK
		if(Display.wasResized()) { glViewport(0,0, Display.getWidth(), Display.getHeight()); }
		DisplayManager.clearBuffers();
		c.getPerspectiveProjection();
		c.update();
		
		g.render();
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.start();
	}
}
