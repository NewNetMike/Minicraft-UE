package studio.bytesize.ld22.item;

import studio.bytesize.ld22.entity.Furniture;
import studio.bytesize.ld22.entity.ItemEntity;
import studio.bytesize.ld22.entity.Player;
import studio.bytesize.ld22.gfx.Color;
import studio.bytesize.ld22.gfx.Font;
import studio.bytesize.ld22.gfx.Screen;
import studio.bytesize.ld22.level.Level;
import studio.bytesize.ld22.level.tile.Tile;

public class FurnitureItem extends Item
{
	public Furniture furniture;
	public boolean placed = false;

	public FurnitureItem(Furniture furniture)
	{
		this.furniture = furniture;
		sheet = furniture.sheet;
	}

	public int getColor()
	{
		return furniture.col;
	}

	public int getSprite()
	{
		return furniture.sprite + 10 * 32;
	}

	public void renderIcon(Screen screen, int x, int y)
	{
		if (furniture.sheet == null) screen.render(x, y, getSprite(), getColor(), 0);
		else screen.render(x, y, getSprite(), getColor(), 0, furniture.sheet);
	}

	public void renderInventory(Screen screen, int x, int y)
	{
		if (furniture.sheet == null) screen.render(x, y, getSprite(), getColor(), 0);
		else screen.render(x, y, getSprite(), getColor(), 0, furniture.sheet);
		Font.draw(furniture.name, screen, x + 8, y, Color.get(-1, 555, 555, 555));
	}

	public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir)
	{
		// Place the furniture
		if (tile.mayPass(level, xt, yt, furniture))
		{
			furniture.x = xt * 16 + 8;
			furniture.y = yt * 16 + 8;
			level.add(furniture);
			placed = true;
			return true;
		}
		return false;

	}

	public boolean isDepleted()
	{
		return placed;
	}

	public void onTake(ItemEntity itemEntity)
	{

	}

	public boolean canAttack()
	{
		return false;
	}

	public String getName()
	{
		return furniture.name;
	}
}
