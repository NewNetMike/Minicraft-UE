package studio.bytesize.ld22.item.resource;

import java.util.HashMap;

import studio.bytesize.ld22.entity.Player;
import studio.bytesize.ld22.gfx.SpriteSheet;
import studio.bytesize.ld22.level.Level;
import studio.bytesize.ld22.level.tile.Tile;

public class Resource
{
	private static HashMap<String, Resource> resources = new HashMap<String, Resource>();

	public static void load(Resource resource)
	{
		resources.put(resource.name.toUpperCase(), resource);
	}

	public static Resource get(String name)
	{
		return resources.get(name.toUpperCase());
	}

	public final String name;
	public final int sprite;
	public final int color;
	public SpriteSheet sheet;

	public Resource(String name, int sprite, int color, SpriteSheet sheet)
	{
		this.sheet = sheet;
		if (name.length() > 10) throw new RuntimeException("Resource name cannot be longer than six characters!");
		this.name = name;
		this.sprite = sprite;
		this.color = color;
	}

	public Resource(String name, int sprite, int color)
	{
		this.sheet = null;
		if (name.length() > 7) throw new RuntimeException("Resource name cannot be longer than 7 characters!");
		this.name = name;
		this.sprite = sprite;
		this.color = color;
	}

	public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir)
	{

		return false;
	}
}
