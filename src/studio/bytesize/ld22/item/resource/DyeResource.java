package studio.bytesize.ld22.item.resource;

import studio.bytesize.ld22.entity.Player;
import studio.bytesize.ld22.gfx.Color;
import studio.bytesize.ld22.gfx.SpriteSheet;
import studio.bytesize.ld22.level.Level;
import studio.bytesize.ld22.level.tile.Tile;

public class DyeResource extends Resource
{
	public int mainColor;

	public DyeResource(String name, int sprite, int mainC)
	{
		super(name, sprite, Color.get(-1, mainC, mainC, mainC), null);
		mainColor = mainC;
	}

	public DyeResource(String name, int sprite, int mainC, SpriteSheet sheet)
	{
		super(name, sprite, Color.get(-1, mainC, mainC, mainC), sheet);
		mainColor = mainC;
	}

	public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir)
	{
		// Change the color of the players clothes
		if (player.shirtColor != mainColor)
		{
			player.shirtColor = mainColor;
			return true;
		}
		return false;
	}

}
