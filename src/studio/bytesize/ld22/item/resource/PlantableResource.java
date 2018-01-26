package studio.bytesize.ld22.item.resource;

import java.util.Arrays;
import java.util.List;

import studio.bytesize.ld22.entity.Player;
import studio.bytesize.ld22.gfx.SpriteSheet;
import studio.bytesize.ld22.level.Level;
import studio.bytesize.ld22.level.tile.Tile;

public class PlantableResource extends Resource
{
	private List<String> sourceTiles;
	private String targetTile;

	public PlantableResource(String name, int sprite, int color, String targetTile, String... sourceTiles1)
	{
		this(name, sprite, color, targetTile, Arrays.asList(sourceTiles1));
	}
	
	public PlantableResource(String name, int sprite, int color, SpriteSheet sheet, String targetTile, String... sourceTiles1)
	{
		this(name, sprite, color, targetTile, Arrays.asList(sourceTiles1));
		this.sheet = sheet;
	}

	public PlantableResource(String name, int sprite, int color, String targetTile, List<String> sourceTiles)
	{
		super(name, sprite, color);
		this.sourceTiles = sourceTiles;
		this.targetTile = targetTile;
	}

	// Place the item when we click the button
	public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir)
	{
		for (int i = 0; i < sourceTiles.size(); i++)
		{
			if (Tile.get(sourceTiles.get(i)) == tile)
			{
				level.setTile(xt, yt, Tile.get(targetTile), 0);
				return true;
			}
		}

		return false;
	}
}
