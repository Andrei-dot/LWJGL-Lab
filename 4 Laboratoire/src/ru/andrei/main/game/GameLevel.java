package ru.andrei.main.game;

public class GameLevel {
	
	Level lvl;
	
	public GameLevel()
	{
		lvl = new Level("map");
	}
	
	public void update()
	{
		lvl.update();
	}
	
	public void render()
	{
		lvl.render();
	}
	
}
