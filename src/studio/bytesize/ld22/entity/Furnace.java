package studio.bytesize.ld22.entity;

import studio.bytesize.ld22.crafting.Crafting;
import studio.bytesize.ld22.gfx.Color;
import studio.bytesize.ld22.screen.CraftingMenu;

public class Furnace extends Furniture
{
	public Furnace()
	{
		super("Furnace");
		col = Color.get(-1, 000, 222, 444);
		sprite = 3;
		xr = 3;
		yr = 2;
	}

	public boolean use(Player player, int attackDir)
	{
		player.game.setMenu(new CraftingMenu(Crafting.furnaceRecipes, player));
		return true;
	}
}
