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

public class DirtTile extends Tile
{
	public DirtTile()
	{
		super();
	}

	public void render(Screen screen, Level level, int x, int y)
	{
		int col = Color.get(level.dirtColor, level.dirtColor, level.dirtColor - 111, level.dirtColor - 111);
		// level.setData(x, y, 1000);

		screen.render(x * 16 + 0, y * 16 + 0, 0, col, 0);
		screen.render(x * 16 + 8, y * 16 + 0, 1, col, 0);
		screen.render(x * 16 + 0, y * 16 + 8, 2, col, 0);
		screen.render(x * 16 + 8, y * 16 + 8, 3, col, 0);
	}

	// If a shovel is used on dirt, that means the player is digging a hole!!! - DUH!!!
	public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir)
	{
		if (item instanceof ToolItem)
		{
			ToolItem tool = (ToolItem)item;
			if (tool.type == ToolType.shovel)
			{
				if (player.payStamina(4 - tool.level))
				{
					level.setTile(xt, yt, Tile.get("hole"), 0);
					level.add(new ItemEntity(new ResourceItem(Resource.get("dirt")), xt * 16 + random.nextInt(10) + 3, yt * 16 + random.nextInt(10) + 3));
					Sound.play("monsterHurt");
					return true;
				}
			}
			if (tool.type == ToolType.hoe)
			{
				if (player.payStamina(4 - tool.level))
				{
					level.setTile(xt, yt, Tile.get("farmland"), 0);
					Sound.play("monsterHurt");
					return true;
				}
			}
		}
		return false;
	}
}
