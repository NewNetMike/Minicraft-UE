package studio.bytesize.ld22.crafting;

import studio.bytesize.ld22.entity.Furniture;
import studio.bytesize.ld22.entity.Player;
import studio.bytesize.ld22.item.FurnitureItem;

public class FurnitureRecipe extends Recipe
{
	private Class<? extends Furniture> clazz;

	public FurnitureRecipe(Class<? extends Furniture> clazz) throws InstantiationException, IllegalAccessException
	{
		super(new FurnitureItem(clazz.newInstance()));
		this.clazz = clazz;
	}

	public void craft(Player player)
	{
		try
		{
			player.inventory.add(0, new FurnitureItem(clazz.newInstance()));
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

}
