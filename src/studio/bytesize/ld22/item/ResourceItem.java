package studio.bytesize.ld22.item;

import studio.bytesize.ld22.entity.ItemEntity;
import studio.bytesize.ld22.entity.Player;
import studio.bytesize.ld22.gfx.Color;
import studio.bytesize.ld22.gfx.Font;
import studio.bytesize.ld22.gfx.Screen;
import studio.bytesize.ld22.item.resource.Resource;
import studio.bytesize.ld22.level.Level;
import studio.bytesize.ld22.level.tile.Tile;

public class ResourceItem extends Item
{
	public Resource resource;
	public int count = 1;

	// An item that is a resource (wood, stone, etc...)
	public ResourceItem(Resource resource)
	{
		this.resource = resource;
		this.sheet = resource.sheet;
	}

	public ResourceItem(Resource resource, int count)
	{
		this.resource = resource;
		this.count = count;
		this.sheet = resource.sheet;
	}

	public int getColor()
	{
		return resource.color;
	}

	public int getSprite()
	{
		return resource.sprite;
	}

	// For displaying items in the world when the player uses them
	public void renderIcon(Screen screen, int x, int y)
	{
		if (resource.sheet != null) screen.render(x, y, resource.sprite, resource.color, 0, resource.sheet);
		else screen.render(x, y, resource.sprite, resource.color, 0);
	}

	// Renders the item details for invetory menu, crafting menu, etc...
	public void renderInventory(Screen screen, int x, int y)
	{
		if (resource.sheet != null) screen.render(x, y, resource.sprite, resource.color, 0, resource.sheet);
		else screen.render(x, y, resource.sprite, resource.color, 0);
		Font.draw(resource.name, screen, x + 24, y, Color.get(-1, 555, 555, 555));
		int cc = count;
		if (cc > 99) cc = 99;
		Font.draw("" + cc, screen, x + 8, y, Color.get(-1, 444, 444, 444));
	}

	public String getName()
	{
		return resource.name;
	}

	public void onTake(ItemEntity itemEntity)
	{

	}

	public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir)
	{
		if (resource.interactOn(tile, level, xt, yt, player, attackDir))
		{
			count--;
			return true;
		}

		return false;
	}

	public boolean isDepleted()
	{
		return count <= 0;
	}
}
