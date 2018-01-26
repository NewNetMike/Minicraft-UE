package studio.bytesize.ld22.level.tile;

import java.util.HashMap;
import java.util.Random;

import studio.bytesize.ld22.entity.Entity;
import studio.bytesize.ld22.entity.Mob;
import studio.bytesize.ld22.entity.Player;
import studio.bytesize.ld22.gfx.Screen;
import studio.bytesize.ld22.item.Item;
import studio.bytesize.ld22.level.Level;

public class Tile
{
	public static int tickCount = 0;
	protected final Random random = new Random();

	private static HashMap<String, Tile> tileCollection = new HashMap<String, Tile>();
	public static Tile[] tiles = new Tile[256];
	public static Tile blank = new Tile();
	public static int idCounter = 0;

	public final byte id;
	public boolean connectsToGrass = false;
	public boolean connectsToSand = false;
	public boolean connectsToWater = false;
	public boolean connectsToLava = false;

	public static void load(String name, Tile tile)
	{
		tileCollection.put(name.toUpperCase(), tile);
	}

	public static Tile get(String name)
	{
		return tileCollection.get(name.toUpperCase());
	}

	public Tile()
	{
		this.id = (byte)idCounter++;
		tiles[id] = this;
	}

	public void render(Screen screen, Level level, int x, int y)
	{

	}

	public boolean mayPass(Level level, int x, int y, Entity e)
	{
		return true;
	}

	public void hurt(Level level, int x, int y, Mob source, int dmg, int attackDir)
	{

	}

	public void bumpedInto(Level level, int x, int y, Entity entity)
	{

	}

	public void tick(Level level, int xt, int yt)
	{

	}

	public void steppedOn(Level level, int xt, int yt, Entity entity)
	{

	}

	public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir)
	{
		return false;
	}

	public boolean use(Level level, int xt, int yt, Player player, int attackDir)
	{
		return false;
	}

	public boolean connectsToLiquid()
	{
		return connectsToWater || connectsToLava;
	}

	public int getLightRadius(Level level, int x, int y)
	{
		return 0;
	}
}
