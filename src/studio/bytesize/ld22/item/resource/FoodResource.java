package studio.bytesize.ld22.item.resource;

import studio.bytesize.ld22.entity.Player;
import studio.bytesize.ld22.gfx.SpriteSheet;
import studio.bytesize.ld22.level.Level;
import studio.bytesize.ld22.level.tile.Tile;

public class FoodResource extends Resource
{
	private int heal;
	private int staminaCost;

	public FoodResource(String name, int sprite, int color, int heal, int staminaCost)
	{
		super(name, sprite, color, null);
		this.heal = heal;
		this.staminaCost = staminaCost;
	}

	public FoodResource(String name, int sprite, int color, SpriteSheet sheet, int heal, int staminaCost)
	{
		super(name, sprite, color, sheet);
		this.heal = heal;
		this.staminaCost = staminaCost;
	}

	public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir)
	{
		// Heal the player (aka eat the food)
		if (player.health < player.maxHealth && player.payStamina(staminaCost))
		{
			player.heal(heal);
			return true;
		}
		return false;
	}

}
