package studio.bytesize.ld22.screen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import studio.bytesize.ld22.crafting.Recipe;
import studio.bytesize.ld22.entity.Player;
import studio.bytesize.ld22.gfx.Color;
import studio.bytesize.ld22.gfx.Font;
import studio.bytesize.ld22.gfx.Screen;
import studio.bytesize.ld22.item.FurnitureItem;
import studio.bytesize.ld22.item.Item;
import studio.bytesize.ld22.item.ResourceItem;
import studio.bytesize.ld22.item.resource.Resource;
import studio.bytesize.ld22.sound.Sound;

// List of craftable items - If we have the necessary resources
// we can create the item that is selected upon click

public class CraftingMenu extends Menu
{
	private Player player;
	private int selected = 0;
	private List<Recipe> recipes;

	public CraftingMenu(List<Recipe> recipes, Player player)
	{
		this.recipes = new ArrayList<Recipe>(recipes);
		this.player = player;

		for (int i = 0; i < recipes.size(); i++)
		{
			this.recipes.get(i).checkCanCraft(player);
		}

		Collections.sort(this.recipes, new Comparator<Recipe>()
		{
			public int compare(Recipe r1, Recipe r2)
			{
				if (r1.canCraft && !r2.canCraft) return -1;
				if (!r1.canCraft && r2.canCraft) return 1;
				return 0;
			}
		});
	}

	public void tick()
	{
		if (input.menu.clicked) game.setMenu(null);

		if (input.up.clicked) selected--;
		if (input.down.clicked) selected++;

		int len = recipes.size();
		if (len == 0) selected = 0;
		if (selected < 0) selected += len;
		if (selected >= len) selected -= len;

		if (input.attack.clicked && len > 0)
		{
			Recipe r = recipes.get(selected);
			r.checkCanCraft(player);

			if (r.canCraft)
			{
				r.deductCost(player);
				r.craft(player);
				Sound.play("craft");
			}

			for (int i = 0; i < recipes.size(); i++)
			{
				recipes.get(i).checkCanCraft(player);
			}
		}
	}

	public void render(Screen screen)
	{
		Font.renderFrame(screen, "have", 12, 1, 19, 3);
		Font.renderFrame(screen, "cost", 12, 4, 19, 11);
		Font.renderFrame(screen, "crafting", 0, 1, 11, 11);

		renderItemList(screen, 0, 1, 11, 11, recipes, selected);

		if (recipes.size() > 0)
		{
			int xo = 13 * 8;
			Recipe recipe = recipes.get(selected);
			int hasResultItems = player.inventory.count(recipe.resultTemplate);
			recipe.resultTemplate.renderIcon(screen, xo, 2 * 8);
			// screen.render(xo, 2*8, recipe.resultTemplate.getSprite(), recipe.resultTemplate.getColor(), 0);
			Font.draw("" + hasResultItems, screen, xo + 8, 2 * 8, Color.get(-1, 555, 555, 555));

			List<Item> iCosts = new ArrayList<Item>();
			for (int j = 0; j < recipe.costs.size(); j++)
			{
				if(!recipe.costs.get(j).isFurniture)iCosts.add(new ResourceItem(Resource.get(recipe.costs.get(j).name), recipe.costs.get(j).count));
				else try
				{
					iCosts.add(new FurnitureItem(recipe.costs.get(j).clazz.newInstance()));
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}

			List<Item> costs = iCosts;
			for (int i = 0; i < costs.size(); i++)
			{
				Item item = costs.get(i);
				int yo = (5 + i) * 8;

				item.renderIcon(screen, xo, yo);
				int requiredAmt = 1;
				if (item instanceof ResourceItem)
				{
					requiredAmt = ((ResourceItem)item).count;
				}
				int has = player.inventory.count(item);
				int color = Color.get(-1, 555, 555, 555);
				if (has < requiredAmt)
				{
					color = Color.get(-1, 222, 222, 222);
				}
				if (has > 99) has = 99;
				Font.draw("" + requiredAmt + "/" + has, screen, xo + 8, yo, color);
			}
		}

		// renderItemList(screen, 0, 12, 4, 11, recipes.get(selected).costs, -1);
	}
}
