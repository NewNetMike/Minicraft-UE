package studio.bytesize.ld22.entity.particles;

import studio.bytesize.ld22.entity.Entity;
import studio.bytesize.ld22.gfx.Color;
import studio.bytesize.ld22.gfx.Font;
import studio.bytesize.ld22.gfx.Screen;

// A fun, bouncy piece of text
public class TextParticle extends Entity
{
	private String msg;
	private int col;
	private int time = 0;
	public double xa, ya, za;
	public double xx, yy, zz;

	public TextParticle(String msg, int x, int y, int col)
	{
		this.msg = msg;
		this.x = x;
		this.y = y;
		this.col = col;

		xx = x;
		yy = y;
		zz = 2;

		xa = random.nextGaussian() * 0.3;
		ya = random.nextGaussian() * 0.2;
		za = random.nextFloat() * 0.7 + 2;
	}

	public void tick()
	{
		time++;

		if (time > 60)
		{
			remove();
		}

		xx += xa;
		yy += ya;
		zz += za;

		if (zz < 0)
		{
			zz = 0;
			za *= -0.5;
			xa *= 0.6;
			ya *= 0.6;
		}

		za -= 0.15;

		x = (int)xx;
		y = (int)yy;
	}

	public void render(Screen screen)
	{
		Font.draw(msg, screen, x - msg.length() * 4 + 1, y - (int)(zz) + 1, Color.get(-1, 0, 0, 0));
		Font.draw(msg, screen, x - msg.length() * 4, y - (int)(zz), col);
	}
}
