package ru.andrei.main.render;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.BufferUtils;

public class Texturing {
	
	public static Texturing env = new Texturing("wall.png", GL_NEAREST);

	private int id;
	private int width, height;
	
	public Texturing(String path, int filter)
	{
		int[] pixels = null;
		
		try {
			BufferedImage image = ImageIO.read(Texturing.class.getResource("/textures/" + path));
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		int[] data = new int[width * height];
		for(int i = 0; i < data.length; i++)
		{
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);
			
			data[i] = a << 24 | b << 16 | g << 8 | r;
		}
		
		int id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);
		
		/*
		 * Pixelisé ou flouté (filtre)
		 */
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
		
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		
		this.id = id;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void bind()
	{
		glBindTexture(GL_TEXTURE_2D, id);
	}
}
