package studio.bytesize.ld22.item;

import studio.bytesize.ld22.entity.Entity;
import studio.bytesize.ld22.entity.ItemEntity;
import studio.bytesize.ld22.entity.Player;
import studio.bytesize.ld22.gfx.Screen;
import studio.bytesize.ld22.gfx.SpriteSheet;
import studio.bytesize.ld22.level.Level;
import studio.bytesize.ld22.level.tile.Tile;
import studio.bytesize.ld22.screen.ListItem;

public class Item implements ListItem
{
	public SpriteSheet sheet = null;
	public Player player;

	public int getColor()
	{
		return 0;
	}

	public int getSprite()
	{
		return 0;
	}

	public void onTake(ItemEntity itemEntity)
	{

	}

	public void renderInventory(Screen screen, int x, int y)
	{

	}

	public void renderActive(Screen screen, int x, int y)
	{

	}

	public boolean interact(Player player, Entity entity, int attackDir)
	{
		return false;
	}

	public void renderIcon(Screen screen, int x, int y)
	{

	}

	public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir)
	{
		return false;

	}

	public boolean isDepleted()
	{
		return false;
	}

	public boolean canAttack()
	{
		return false;
	}

	public int getAttackDamageBonus(Entity e)
	{
		return 0;
	}

	public String getName()
	{
		return "";
	}

	public boolean matches(Item item)
	{
		return item.getClass() == getClass();
	}
	
	public void setPlayer(Player player)
	{
		this.player = player;
	}
	
	public Player getPlayer()
	{
		return player;
	}
}
