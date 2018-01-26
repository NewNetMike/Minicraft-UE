package studio.bytesize.ld22.screen;

import studio.bytesize.ld22.entity.Inventory;
import studio.bytesize.ld22.entity.Player;
import studio.bytesize.ld22.gfx.Font;
import studio.bytesize.ld22.gfx.Screen;

// Pulls up player inventory and chest inventory and
// allows us to switch between the two flawlessly
// adding and subtracting items

public class ContainerMenu extends Menu
{
	private Player player;
	private Inventory container;

	private String title;
	private int selected = 0;
	private int oSelected;
	private int window = 0;

	public ContainerMenu(Player player, String title, Inventory container)
	{
		this.player = player;
		this.container = container;
		this.title = title;
	}

	public void tick()
	{
		if (input.menu.clicked) game.setMenu(null);

		if (input.left.clicked && window == 1)
		{
			window = 0;
			int tmp = selected;
			selected = oSelected;
			oSelected = tmp;
		}

		if (input.right.clicked && window == 0)
		{
			window = 1;
			int tmp = selected;
			selected = oSelected;
			oSelected = tmp;
		}

		Inventory i = window == 1 ? player.inventory : container;
		Inventory i2 = window == 0 ? player.inventory : container;

		int len = i.items.size();
		if (selected < 0) selected = 0;
		if (selected >= len) selected = len - 1;

		if (input.up.clicked) selected--;
		if (input.down.clicked) selected++;

		if (len == 0) selected = 0;
		if (selected < 0) selected += len;
		if (selected >= len) selected -= len;

		if (input.attack.clicked && len > 0)
		{
			i2.add(oSelected, i.items.remove(selected));
			if (selected >= i.items.size()) selected = i.items.size() - 1;
		}
	}

	public void render(Screen screen)
	{
		if (window == 1) screen.setOffset(6 * 8, 0);
		Font.renderFrame(screen, title, 1, 1, 12, 11);
		renderItemList(screen, 1, 1, 12, 11, container.items, window == 0 ? selected : -oSelected);

		Font.renderFrame(screen, "inventory", 13, 1, 13 + 11, 11);
		renderItemList(screen, 13, 1, 13 + 11, 11, player.inventory.items, window == 1 ? selected : oSelected);
		screen.setOffset(0, 0);
	}
}
