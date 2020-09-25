package ru.andrei.main.render;

import ru.andrei.main.math.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class RenderManager {
	
	public static void setFloor(float x, float z, Vector3f color)
	{
		glColor3f(color.getX(),color.getY(),color.getZ());
		
		/*
		 * Permet de draw le carré au sol
		 */
		glVertex3f(x + 1, 0, z);
		glVertex3f(x, 0, z);
		glVertex3f(x, 0, z + 1);
		glVertex3f(x + 1, 0, z + 1);
	}
	
	public static void setCeiling(float x, float z, Vector3f color)
	{
		glColor3f(color.getX(),color.getY(),color.getZ());
		
		/*
		 * Permet de draw le carré au plafond 
		 */
		glVertex3f(x, 3, z);
		glVertex3f(x + 1, 3, z);
		glVertex3f(x + 1, 3, z + 1);
		glVertex3f(x, 3, z + 1);
	}
	
	public static void setWall(float x0, float z0, float x1, float z1, Vector3f color)
	{
		glColor3f(color.getX(), color.getY(), color.getZ());
		
		glTexCoord2f(0,0); glVertex3f(x0,0,z0);
		glTexCoord2f(1,0); glVertex3f(x1,0,z1);
		glTexCoord2f(1,1); glVertex3f(x1,3,z1);
		glTexCoord2f(0,1); glVertex3f(x0,3,z0);
		/*
		 *  3 dans les deux derniers vertex en 2nd argument est la hauteur
		 */
	}
}
