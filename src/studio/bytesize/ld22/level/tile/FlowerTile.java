package studio.bytesize.ld22.level.tile;

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

public class FlowerTile extends GrassTile
{
	public FlowerTile()
	{
		super();
		connectsToGrass = true;
	}

	public void render(Screen screen, Level level, int x, int y)
	{
		super.render(screen, level, x, y); // Render the grass
		int data = level.getData(x, y);
		int shape = (data / 16) % 2;

		int flowerCol = Color.get(10, level.grassColor, 555, 440);

		// Unique flower shapes (TL, TR, BL, BR)
		if (shape == 0) screen.render(x * 16 + 0, y * 16 + 0, 1 + 1 * 32, flowerCol, 0);
		if (shape == 1) screen.render(x * 16 + 8, y * 16 + 0, 1 + 1 * 32, flowerCol, 0);
		if (shape == 2) screen.render(x * 16 + 0, y * 16 + 8, 1 + 1 * 32, flowerCol, 0);
		if (shape == 3) screen.render(x * 16 + 8, y * 16 + 8, 1 + 1 * 32, flowerCol, 0);
	}

	// If we hurt a flower with a bare hand, 2 flowers pop out???
	public void hurt(Level level, int x, int y, Mob source, int dmg, int attackDir)
	{
		int count = random.nextInt(2) + 1;
		for (int i = 0; i < count; i++)
		{
			level.add(new ItemEntity(new ResourceItem(Resource.get("flower")), x * 16 + random.nextInt(10) + 3, y * 16 + random.nextInt(10) + 3));
		}
		level.setTile(x, y, get("grass"), 0);
	}

	// If we hurt a flower with a shovel, 2 flowers pop out??? + the grass is turned to dirt!
	public boolean interact(Level level, int x, int y, Player player, Item item, int attackDir)
	{
		if (item instanceof ToolItem)
		{
			ToolItem tool = (ToolItem)item;
			if (tool.type == ToolType.shovel)
			{
				if (player.payStamina(4 - tool.level))
				{
					level.setTile(x, y, get("dirt"), 0);
					level.add(new ItemEntity(new ResourceItem(Resource.get("Flower")), x * 16 + random.nextInt(10) + 3, y * 16 + random.nextInt(10) + 3));
					return true;
				}
			}
		}
		return false;
	}
}
