package studio.bytesize.ld22.level.tile;

import studio.bytesize.ld22.entity.Entity;
import studio.bytesize.ld22.entity.ItemEntity;
import studio.bytesize.ld22.entity.Mob;
import studio.bytesize.ld22.entity.Player;
import studio.bytesize.ld22.gfx.Color;
import studio.bytesize.ld22.gfx.Screen;
import studio.bytesize.ld22.item.Item;
import studio.bytesize.ld22.item.ResourceItem;
import studio.bytesize.ld22.item.ToolItem;
import studio.bytesize.ld22.item.ToolType;
import studio.bytesize.ld22.item.resource.Resource;
import studio.bytesize.ld22.level.Level;

public class WheatTile extends Tile
{
	public WheatTile()
	{
		super();
	}

	public void render(Screen screen, Level level, int x, int y)
	{
		int col = Color.get(level.dirtColor - 121, level.dirtColor - 11, level.dirtColor, 50);

		int age = level.getData(x, y);
		int icon = age / 10;
		if (icon >= 3)
		{
			col = Color.get(level.dirtColor - 121, level.dirtColor - 11, 50 + (icon) * 100, 40 + (icon - 3) * 2 * 100);
			if (age == 50)
			{
				col = Color.get(0, 0, 50 + (icon) * 100, 40 + (icon - 3) * 2 * 100);
			}
			icon = 3;
		}

		screen.render(x * 16 + 0, y * 16 + 0, 4 + 3 * 32 + icon, col, 0);
		screen.render(x * 16 + 8, y * 16 + 0, 4 + 3 * 32 + icon, col, 0);
		screen.render(x * 16 + 0, y * 16 + 8, 4 + 3 * 32 + icon, col, 1);
		screen.render(x * 16 + 8, y * 16 + 8, 4 + 3 * 32 + icon, col, 1);
	}

	public void tick(Level level, int xt, int yt)
	{
		// Increase the wheat's age until we reach 50
		if (random.nextInt(2) == 0) return;
		int age = level.getData(xt, yt);
		if (age < 50) level.setData(xt, yt, age + 1);
	}

	public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir)
	{
		if (item instanceof ToolItem)
		{
			ToolItem tool = (ToolItem)item;
			if (tool.type == ToolType.shovel)
			{
				if (player.payStamina(4 - tool.level))
				{
					level.setTile(xt, yt, Tile.get("dirt"), 0);
					return true;
				}
			}
		}
		return false;
	}

	// Walking on the crops ruin them!!!
	public void steppedOn(Level level, int xt, int yt, Entity entity)
	{
		if (random.nextInt(60) != 0) return;
		if (level.getData(xt, yt) < 2) return;
		harvest(level, xt, yt);
	}

	public void hurt(Level level, int x, int y, Mob source, int dmg, int attackDir)
	{

		harvest(level, x, y);
	}

	private void harvest(Level level, int x, int y)
	{
		int age = level.getData(x, y);
		int count = random.nextInt(2);

		// Drop seeds
		for (int i = 0; i < count; i++)
		{
			level.add(new ItemEntity(new ResourceItem(Resource.get("seeds")), x * 16 + random.nextInt(10) + 3, y * 16 + random.nextInt(10) + 3));
		}

		// Drop Wheat if its age is between 40 (less wheat drop) and 50 (most wheat drop)
		count = 0;
		if (age == 50)
		{
			count = random.nextInt(3) + 2;

		}
		else if (age >= 40)
		{
			count = random.nextInt(2) + 1;
		}
		for (int i = 0; i < count; i++)
		{
			level.add(new ItemEntity(new ResourceItem(Resource.get("Wheat")), x * 16 + random.nextInt(10) + 3, y * 16 + random.nextInt(10) + 3));
		}

		level.setTile(x, y, Tile.get("dirt"), 0);
	}
}
