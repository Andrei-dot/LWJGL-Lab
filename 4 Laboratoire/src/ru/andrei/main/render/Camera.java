package ru.andrei.main.render;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_TRANSFORM_BIT;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.concurrent.TimeUnit;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;

import ru.andrei.main.math.Vector3f;

public class Camera {
	
	private float fov;
	private float zNear;
	private float zFar;
	
	private Vector3f pos;
	private Vector3f rota;
	
	public static float mouseSpeed = 0.3f;
	public static float moveSpeed = 3f;
	
	public Camera(Vector3f position)
	{
		this.pos = position;
		rota = new Vector3f(0,0,0);
	}
	
	public Camera setPerspectiveProjection(float fov, float zNear, float zFar)
	{
		this.fov = fov;
		this.zNear = zNear;
		this.zFar = zFar;
		
		return this;
	}
	
	public Vector3f getForward()
	{
		Vector3f r = new Vector3f();

		Vector3f rot = new Vector3f(rota);
		
		float cosinusY = (float) Math.cos(Math.toRadians(rot.getY() - 90));
		float sinusY = (float) Math.sin(Math.toRadians(rot.getY() - 90));
		float cosinusP = (float) Math.cos(Math.toRadians(-rot.getX()));
		float sinusP = (float) Math.sin(Math.toRadians(-rot.getX()));
		
		// Euler Angles
		
		r.setX(cosinusY * cosinusP);
		r.setY(sinusP);
		r.setZ(sinusY * cosinusP);
		
		r.normalize();
		
		return new Vector3f(r);
	}

	public Vector3f getBack() { return new Vector3f(getForward().mul(-1)); }
	
	public Vector3f getRight() {
		Vector3f rot = new Vector3f(rota);
		
		Vector3f r = new Vector3f();
		r.setX((float) Math.cos(Math.toRadians(rot.getY())));
		r.setZ((float) Math.sin(Math.toRadians(rot.getY())));
		r.normalize();
		
		return new Vector3f(r);
	}
	
	public Vector3f getLeft() {
		return new Vector3f(getRight().mul(-1));
	}
	
	public void getPerspectiveProjection()
	{
		glEnable(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(fov, (float) Display.getWidth() / Display.getHeight(), zNear, zFar);
		glEnable(GL_MODELVIEW);
	}
	
	public void update()
	{
		glPushAttrib(GL_TRANSFORM_BIT);
		
		// Idem que pour le translatef(c'est indispensable) => ça se fait par axe par exemple rotate.getX(), est égal à x y z
		// Exemple ; rotate.getX(),1,0,0 va faire rotate sur l'axe X
		glRotatef(rota.getX(),1,0,0);
		glRotatef(rota.getY(),0,1,0);
		glRotatef(rota.getZ(),0,0,1);
		// glTranslatef = /!\ Important ne pas oublier, fonction permettant de nous faire bouger.
		glTranslatef(-pos.getX(),-pos.getY(),-pos.getZ());
		glPopMatrix();
	}
	
	/*
	 * Bouger avec le clavier
	 */
	
	float speed = 0.1f;
	
	public void keyboardInputListener()
	{
		rota.addX(-Mouse.getDY() * mouseSpeed);
		rota.addY(Mouse.getDX() * mouseSpeed);

		if(rota.getX() > 90) { rota.setX(90);}
		if(rota.getX() < -90) { rota.setX(-90);}

		
		if(Keyboard.isKeyDown(Keyboard.KEY_Z) || Keyboard.isKeyDown(Keyboard.KEY_UP))
		{
			pos.add(getForward().mul(moveSpeed));
		}	
		if(Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN))
		{
			pos.add(getBack().mul(moveSpeed));
		}	
		if(Keyboard.isKeyDown(Keyboard.KEY_Q) || Keyboard.isKeyDown(Keyboard.KEY_LEFT))
		{
			pos.add(getLeft().mul(moveSpeed));
		}	
		if(Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
		{
			pos.add(getRight().mul(moveSpeed));
		}	
		
		//TODO: temp
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
		{
			pos.add(new Vector3f(0,speed,0));
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
		{
			pos.add(new Vector3f(0,-speed,0));
		}	
		
	}
	
	/*
	 * Some useless stuff
	 */
	
	@SuppressWarnings("unused")
	private static void ws(int time) {
		try {
			TimeUnit.SECONDS.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Getters & Setters
	 */
	
	public Vector3f getPosition() { return pos; }

	public void setPosition(Vector3f position) { this.pos = pos; }

	public void setRotation(Vector3f rotation) { this.rota = rota; }
}
