package studio.bytesize.ld22.crafting;

import java.util.ArrayList;
import java.util.List;

import studio.bytesize.ld22.entity.Furniture;
import studio.bytesize.ld22.entity.Player;
import studio.bytesize.ld22.gfx.Color;
import studio.bytesize.ld22.gfx.Font;
import studio.bytesize.ld22.gfx.Screen;
import studio.bytesize.ld22.item.FurnitureItem;
import studio.bytesize.ld22.item.Item;
import studio.bytesize.ld22.item.ResourceItem;
import studio.bytesize.ld22.item.resource.Resource;
import studio.bytesize.ld22.screen.ListItem;

public abstract class Recipe implements ListItem
{
	public class ItemTemplate
	{
		public String name;
		public int count;
		public boolean isFurniture = false;
		public Class<? extends Furniture> clazz;

		public ItemTemplate(String name, int count)
		{
			this.name = name;
			this.count = count;
		}

		public ItemTemplate(Class<? extends Furniture> clazz, int count)
		{
			this.clazz = clazz;
			this.count = count;
			isFurniture = true;
		}
	}

	public List<ItemTemplate> costs = new ArrayList<ItemTemplate>();
	public boolean canCraft = false;
	public Item resultTemplate;

	public Recipe(Item resultTemplate)
	{
		this.resultTemplate = resultTemplate;
	}

	public Recipe addCost(String resource, int count)
	{
		costs.add(new ItemTemplate(resource, count));
		return this;
	}

	public Recipe addCost(Class<? extends Furniture> clazz, int count)
	{
		costs.add(new ItemTemplate(clazz, count));
		return this;
	}

	// Makes sure the player has the proper resources in the proper amounts
	public void checkCanCraft(Player player)
	{
		for (int i = 0; i < costs.size(); i++)
		{
			if (!costs.get(i).isFurniture)
			{
				Item item = new ResourceItem(Resource.get(costs.get(i).name), costs.get(i).count);
				if (item instanceof ResourceItem)
				{
					ResourceItem ri = ((ResourceItem)item);
					if (!player.inventory.hasResources(ri.resource, ri.count))
					{
						canCraft = false;
						return;
					}
				}
			}
			else
			{
				Item item = null;
				try
				{
					item = new FurnitureItem(costs.get(i).clazz.newInstance());
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (item instanceof FurnitureItem)
				{
					FurnitureItem ri = ((FurnitureItem)item);
					if (!player.inventory.hasFurniture(ri, costs.get(i).count))
					{
						canCraft = false;
						return;
					}
				}
			}
		}

		canCraft = true;
		return;
	}

	public void renderInventory(Screen screen, int x, int y)
	{
		screen.render(x, y, this.resultTemplate.getSprite(), this.resultTemplate.getColor(), 0, this.resultTemplate.sheet);
		Font.draw(this.resultTemplate.getName(), screen, x + 8, y, canCraft ? Color.get(-1, 555, 555, 555) : Color.get(-1, 222, 222, 222));
	}

	public abstract void craft(Player player);

	public void deductCost(Player player)
	{
		for (int i = 0; i < costs.size(); i++)
		{
			Item item = null;
			if (!costs.get(i).isFurniture) item = new ResourceItem(Resource.get(costs.get(i).name), costs.get(i).count);
			else try
			{
				item = new FurnitureItem(costs.get(i).clazz.newInstance());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			if (item instanceof ResourceItem)
			{
				ResourceItem ri = ((ResourceItem)item);
				player.inventory.removeResource(ri.resource, ri.count);
			}
			else if (item instanceof FurnitureItem)
			{
				FurnitureItem fi = ((FurnitureItem)item);
				player.inventory.removeItem(fi);
			}
		}
	}
}
