package studio.bytesize.ld22.level.tile;

import studio.bytesize.ld22.entity.Entity;
import studio.bytesize.ld22.entity.ItemEntity;
import studio.bytesize.ld22.entity.Mob;
import studio.bytesize.ld22.entity.Player;
import studio.bytesize.ld22.entity.particles.SmashParticle;
import studio.bytesize.ld22.entity.particles.TextParticle;
import studio.bytesize.ld22.gfx.Color;
import studio.bytesize.ld22.gfx.Screen;
import studio.bytesize.ld22.item.Item;
import studio.bytesize.ld22.item.ResourceItem;
import studio.bytesize.ld22.item.ToolItem;
import studio.bytesize.ld22.item.ToolType;
import studio.bytesize.ld22.item.resource.Resource;
import studio.bytesize.ld22.level.Level;

public class OreTile extends Tile
{
	private String toDrop;
	private int color;

	public OreTile(String toDrop)
	{
		super();
		this.toDrop = toDrop;
	}

	public void render(Screen screen, Level level, int x, int y)
	{
		color = (Resource.get(toDrop).color & 0xffffff00) + Color.get(level.dirtColor);
		screen.render(x * 16 + 0, y * 16 + 0, 17 + 1 * 32, color, 0);
		screen.render(x * 16 + 8, y * 16 + 0, 18 + 1 * 32, color, 0);
		screen.render(x * 16 + 0, y * 16 + 8, 17 + 2 * 32, color, 0);
		screen.render(x * 16 + 8, y * 16 + 8, 18 + 2 * 32, color, 0);
	}

	public boolean mayPass(Level level, int x, int y, Entity e)
	{
		return false;
	}

	public void hurt(Level level, int x, int y, int dmg)
	{
		// We use the level data array to set rock's damage/life
		int damage = level.getData(x, y) + 1;
		level.add(new SmashParticle(level, x * 16 + 8, y * 16 + 8));
		level.add(new TextParticle("" + dmg, x * 16 + 8, y * 16 + 8, Color.get(-1, 500, 500, 500)));

		if (dmg > 0)
		{
			int count = random.nextInt(2);

			if (damage >= random.nextInt(10) + 3)
			{
				level.setTile(x, y, Tile.get("dirt"), 0);
				count += 2;
			}
			else
			{
				level.setData(x, y, damage);
			}

			for (int i = 0; i < count; i++)
				level.add(new ItemEntity(new ResourceItem(Resource.get(toDrop)), x * 16 + random.nextInt(10) + 3, y * 16 + random.nextInt(10) + 3));
		}
	}

	public void hurt(Level level, int x, int y, Mob source, int dmg, int attackDir)
	{
		hurt(level, x, y, 0);
	}

	public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir)
	{
		if (item instanceof ToolItem)
		{
			ToolItem tool = (ToolItem)item;
			if (tool.type == ToolType.pickaxe)
			{
				if (player.payStamina(6 - tool.level))
				{
					hurt(level, xt, yt, 1);
					return true;
				}
			}
		}
		return false;
	}

	// If something bumps into a cactus, hurt that something!!! - DUH!!!
	public void bumpedInto(Level level, int x, int y, Entity entity)
	{
		entity.hurt(this, x, y, 3);
	}
}
