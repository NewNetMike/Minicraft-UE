package studio.bytesize.ld22.level.tile;

import studio.bytesize.ld22.entity.AirWizard;
import studio.bytesize.ld22.entity.Entity;
import studio.bytesize.ld22.gfx.Screen;
import studio.bytesize.ld22.level.Level;

public class InfiniteFallTile extends Tile
{
	public InfiniteFallTile()
	{
		super();
	}

	public void render(Screen screen, Level level, int x, int y)
	{

	}

	public void tick(Level level, int xt, int yt)
	{

	}

	public boolean mayPass(Level level, int x, int y, Entity e)
	{
		if (e instanceof AirWizard) return true;
		return false;
	}
}
