package studio.bytesize.ld22.screen;

import studio.bytesize.ld22.gfx.Color;
import studio.bytesize.ld22.gfx.Font;
import studio.bytesize.ld22.gfx.Screen;

public class RestartMenu extends Menu
{

	public RestartMenu()
	{
	}

	public void tick()
	{
		
	}

	public void render(Screen screen)
	{
		screen.clear(0);

		Font.draw("DOWNLOAD SUCCESSFUL", screen, 0 * 8 + 4, 4 * 8, Color.get(0, 555, 555, 555));
		Font.draw("Please close and", screen, 1 * 8 + 4, 7 * 8, Color.get(0, 333, 333, 333));
		Font.draw("reopen for changes", screen, 0 * 8 + 4, 8 * 8, Color.get(0, 333, 333, 333));
		Font.draw("to take effect", screen, 2 * 8 + 4, 9 * 8, Color.get(0, 333, 333, 333));
	}
}
