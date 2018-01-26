package studio.bytesize.ld22.screen;

import java.util.List;

import studio.bytesize.ld22.Game;
import studio.bytesize.ld22.InputHandler;
import studio.bytesize.ld22.gfx.Color;
import studio.bytesize.ld22.gfx.Font;
import studio.bytesize.ld22.gfx.Screen;

public class Menu
{
	protected Game game;
	protected InputHandler input;

	public void init(Game game, InputHandler input)
	{
		this.game = game;
		this.input = input;
	}

	public void tick()
	{

	}

	public void render(Screen screen)
	{

	}

	// Renders an inventory in scrollable list form (for inventory menu, crafting menu, etc...)
	public void renderItemList(Screen screen, int xo, int yo, int x1, int y1, List<? extends ListItem> listItems, int selected)
	{
		boolean renderCursor = true;

		if (selected < 0)
		{
			selected = -selected - 1;
			renderCursor = false;
		}

		int w = x1 - xo;
		int h = y1 - yo - 1;
		int i0 = 0;
		int i1 = listItems.size();
		if (i1 > h) i1 = h;
		int io = selected - h / 2;
		if (io > listItems.size() - h) io = listItems.size() - h;
		if (io < 0) io = 0;

		for (int i = i0; i < i1; i++)
		{
			listItems.get(i + io).renderInventory(screen, (1 + xo) * 8, (i + 1 + yo) * 8);
		}

		if (renderCursor)
		{
			int yy = selected + 1 - io + yo;
			Font.draw(">", screen, (xo + 0) * 8, yy * 8, Color.get(5, 555, 555, 555));
			Font.draw("<", screen, (xo + w) * 8, yy * 8, Color.get(5, 555, 555, 555));
		}
	}

	// Renders an inventory in scrollable list form (for inventory menu, crafting menu, etc...)
	public void renderPluginList(Screen screen, int xo, int yo, int x1, int y1, List<PluginMenuItem> listItems, int selected)
	{

		if (selected < 0)
		{
			selected = -selected - 1;
		}

		int h = y1 - yo - 1;
		int i0 = 0;
		int i1 = listItems.size();
		if (i1 > h) i1 = h;
		int io = selected - h / 2;
		if (io > listItems.size() - h) io = listItems.size() - h;
		if (io < 0) io = 0;

		for (int i = i0; i < i1; i++)
		{
			int col = 0;
			String msg = listItems.get(i + io).getName();

			if (listItems.get(i + io).enabled && listItems.get(i + io).getName() != "GETMORE PLUGINS") col = Color.get(0, 040, 040, 040);
			else if(listItems.get(i + io).getName() == "GETMORE PLUGINS")col = Color.get(0, 302, 302, 302);
			else col = Color.get(0, 300, 300, 300);

			if (i + io == selected)
			{
				msg = "> " + listItems.get(i + io).getName() + " <";
				if (listItems.get(i + io).enabled && listItems.get(i + io).getName() != "GETMORE PLUGINS")
					{
					col = Color.get(0, 050, 050, 050);
					}
				else if(listItems.get(i + io).getName() == "GETMORE PLUGINS")
					{
					col = Color.get(0, 503, 503, 503);
					}
				else col = Color.get(0, 500, 500, 500);
			}
			Font.draw(msg, screen, (screen.w - msg.length() * 8) / 2, (i + 1 + yo) * 8, col);
		}
	}
	
	// Renders an inventory in scrollable list form (for inventory menu, crafting menu, etc...)
		public void renderPluginList(Screen screen, int xo, int yo, int x1, int y1, int selected, List<DownloadablePlugin> listItems)
		{

			if (selected < 0)
			{
				selected = -selected - 1;
			}

			int h = y1 - yo - 1;
			int i0 = 0;
			int i1 = listItems.size();
			if (i1 > h) i1 = h;
			int io = selected - h / 2;
			if (io > listItems.size() - h) io = listItems.size() - h;
			if (io < 0) io = 0;

			for (int i = i0; i < i1; i++)
			{
				int col = 0;
				String msg = listItems.get(i + io).getName();

				if (listItems.get(i + io).enabled && listItems.get(i + io).getName() != "GETMORE PLUGINS") col = Color.get(0, 040, 040, 040);
				else if(listItems.get(i + io).getName() == "GETMORE PLUGINS")col = Color.get(0, 303, 303, 303);
				else col = Color.get(0, 300, 300, 300);

				if (i + io == selected)
				{
					msg = "> " + listItems.get(i + io).getName() + " <";
					if (listItems.get(i + io).enabled && listItems.get(i + io).getName() != "GETMORE PLUGINS")
						{
						col = Color.get(0, 050, 050, 050);
						}
					else if(listItems.get(i + io).getName() == "GETMORE PLUGINS")
						{
						col = Color.get(0, 505, 505, 505);
						}
					else col = Color.get(0, 500, 500, 500);
				}
				Font.draw(msg, screen, (screen.w - msg.length() * 8) / 2, (i + 1 + yo) * 8, col);
			}
		}
}
