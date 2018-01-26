package studio.bytesize.ld22.screen;

import studio.bytesize.ld22.MinicraftPlugin;
import studio.bytesize.ld22.gfx.Screen;

public class PluginMenuItem implements ListItem
{
	public boolean enabled = true;
	public MinicraftPlugin plugin;

	public PluginMenuItem(MinicraftPlugin plugin)
	{
		this.plugin = plugin;
		enabled = plugin.autoEnabled();
	}

	public void renderInventory(Screen screen, int x, int y)
	{
		// Font.draw(plugin.getName(), screen, x, y, Color.get(0, 555, 555, 555));
	}

	public String getName()
	{
		return plugin.getName();
	}
}
