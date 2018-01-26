package studio.bytesize.ld22.entity.particles;

import studio.bytesize.ld22.entity.Entity;
import studio.bytesize.ld22.gfx.Color;
import studio.bytesize.ld22.gfx.Screen;
import studio.bytesize.ld22.level.Level;
import studio.bytesize.ld22.sound.Sound;

// Renders basically a curvy white X to indicate hitting a tile
public class SmashParticle extends Entity
{
	private int time = 0;

	public SmashParticle(Level level, int x, int y)
	{
		this.x = x;
		this.y = y;

		int xd = level.player.x - x;
		int yd = level.player.y - y;
		if (xd * xd + yd * yd < 85 * 85)
		{
			Sound.play("monsterHurt");
		}
	}

	public void tick()
	{
		time++;
		if (time > 10)
		{
			remove();
		}
	}

	public void render(Screen screen)
	{
		int col = Color.get(-1, 555, 555, 555);

		screen.render(x - 8, y - 8, 5 + 12 * 32, col, 2);
		screen.render(x - 0, y - 8, 5 + 12 * 32, col, 3);
		screen.render(x - 8, y - 0, 5 + 12 * 32, col, 0);
		screen.render(x - 0, y - 0, 5 + 12 * 32, col, 1);
	}
}
