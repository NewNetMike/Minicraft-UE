package studio.bytesize.ld22.screen;

import studio.bytesize.ld22.entity.Player;
import studio.bytesize.ld22.gfx.Font;
import studio.bytesize.ld22.gfx.Screen;
import studio.bytesize.ld22.item.Item;

public class InventoryMenu extends Menu
{
	private Player player;
	private int selected = 0;

	public InventoryMenu(Player player)
	{
		this.player = player;

		// When the menu is pulled up, put the
		// player's active item back into the inventory
		if (player.activeItem != null)
		{
			player.inventory.items.add(0, player.activeItem);
			player.activeItem = null;
		}
	}

	public void tick()
	{
		if (input.menu.clicked) game.setMenu(null);

		if (input.up.clicked) selected--;
		if (input.down.clicked) selected++;

		int len = player.inventory.items.size();
		if (len == 0) selected = 0;
		if (selected < 0) selected += len;
		if (selected >= len) selected -= len;

		// Put item into player's hand, remove from inventory, and close menu
		if (input.attack.clicked && len > 0)
		{
			Item item = player.inventory.items.remove(selected);
			player.activeItem = item;
			player.activeItem.setPlayer(player);
			game.setMenu(null);
		}
	}

	public void render(Screen screen)
	{
		Font.renderFrame(screen, "inventory", 1, 1, 12, 11);
		renderItemList(screen, 1, 1, 12, 11, player.inventory.items, selected);
	}
}
