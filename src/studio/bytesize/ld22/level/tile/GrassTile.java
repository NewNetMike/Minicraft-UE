package studio.bytesize.ld22.level.tile;

import studio.bytesize.ld22.entity.ItemEntity;
import studio.bytesize.ld22.entity.Player;
import studio.bytesize.ld22.gfx.Color;
import studio.bytesize.ld22.gfx.Screen;
import studio.bytesize.ld22.item.Item;
import studio.bytesize.ld22.item.ResourceItem;
import studio.bytesize.ld22.item.ToolItem;
import studio.bytesize.ld22.item.ToolType;
import studio.bytesize.ld22.item.resource.Resource;
import studio.bytesize.ld22.level.Level;
import studio.bytesize.ld22.sound.Sound;

public class GrassTile extends Tile
{
	public GrassTile()
	{
		super();
		connectsToGrass = true;
	}

	public void render(Screen screen, Level level, int x, int y)
	{
		// This render creates smooth corners and shapes, so the world isn't obviously blocky
		int col = Color.get(level.grassColor, level.grassColor, level.grassColor + 111, level.grassColor + 111);
		int transitionColor = Color.get(level.grassColor - 111, level.grassColor, level.grassColor + 111, level.dirtColor);
		// level.setData(x, y, 1000);

		boolean u = !level.getTile(x, y - 1).connectsToGrass;
		boolean d = !level.getTile(x, y + 1).connectsToGrass;
		boolean l = !level.getTile(x - 1, y).connectsToGrass;
		boolean r = !level.getTile(x + 1, y).connectsToGrass;

		if (!u && !l)
		{
			screen.render(x * 16 + 0, y * 16 + 0, 0, col, 0);
		}
		else
		{
			screen.render(x * 16 + 0, y * 16 + 0, (l ? 11 : 12) + (u ? 0 : 1) * 32, transitionColor, 0);
		}

		if (!u && !r)
		{
			screen.render(x * 16 + 8, y * 16 + 0, 1, col, 0);
		}
		else
		{
			screen.render(x * 16 + 8, y * 16 + 0, (r ? 13 : 12) + (u ? 0 : 1) * 32, transitionColor, 0);
		}

		if (!d && !l)
		{
			screen.render(x * 16 + 0, y * 16 + 8, 2, col, 0);
		}
		else
		{
			screen.render(x * 16 + 0, y * 16 + 8, (l ? 11 : 12) + (d ? 2 : 1) * 32, transitionColor, 0);
		}

		if (!d && !r)
		{
			screen.render(x * 16 + 8, y * 16 + 8, 3, col, 0);
		}
		else
		{
			screen.render(x * 16 + 8, y * 16 + 8, (r ? 13 : 12) + (d ? 2 : 1) * 32, transitionColor, 0);
		}
	}

	// Spread grass onto adjacent dirt tiles
	public void tick(Level level, int xt, int yt)
	{
		if (random.nextInt(40) != 0) return; // Growing offset delay thing-a-ma-bob

		int xn = xt;
		int yn = yt;

		if (random.nextBoolean()) xn += random.nextInt(2) * 2 - 1;
		else yn += random.nextInt(2) * 2 - 1;

		if (level.getTile(xn, yn) == Tile.get("dirt"))
		{
			level.setTile(xn, yn, this, 0);
		}
	}

	// Turn grass to dirt when a shovel is used - AKA the player is digging
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
					Sound.play("monsterHurt");
					return true;
				}
			}
			if (tool.type == ToolType.hoe)
			{
				if (player.payStamina(4 - tool.level))
				{
					Sound.play("monsterHurt");
					if (random.nextInt(5) == 0)
					{
						level.add(new ItemEntity(new ResourceItem(Resource.get("seeds")), xt * 16 + random.nextInt(10) + 3, yt * 16 + random.nextInt(10) + 3));

					}
					level.setTile(xt, yt, Tile.get("farmLand"), 0);
					return true;
				}
			}
		}
		return false;
	}
}
