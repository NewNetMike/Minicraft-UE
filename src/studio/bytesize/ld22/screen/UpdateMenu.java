package studio.bytesize.ld22.screen;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import studio.bytesize.ld22.gfx.Color;
import studio.bytesize.ld22.gfx.Font;
import studio.bytesize.ld22.gfx.Screen;

public class UpdateMenu extends Menu
{

	public UpdateMenu()
	{
	}

	public void tick()
	{
		if (input.attack.clicked || input.menu.clicked)
		{
			if(Desktop.isDesktopSupported())
			{
			  try
			{
				Desktop.getDesktop().browse(new URI("http://gamejolt.com/games/minicraft-ultimate-edition/129004"));
				System.exit(0);
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (URISyntaxException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
	}

	public void render(Screen screen)
	{
		screen.clear(0);

		Font.draw("UPDATE REQUIRED!", screen, 2 * 8 + 4, 2 * 8, Color.get(0, 555, 555, 555));
		Font.draw("There is a new ver-", screen, 0 * 8 + 4, 4 * 8, Color.get(0, 333, 333, 333));
		Font.draw("-sion of minicraft:", screen, 0 * 8 + 4, 5 * 8, Color.get(0, 333, 333, 333));
		Font.draw("ultimate edition", screen, 1 * 8 + 4, 6 * 8, Color.get(0, 333, 333, 333));
		
		Font.draw("Press any button to", screen, 0 * 8 + 4, 8 * 8, Color.get(0, 333, 333, 333));
		Font.draw("open your browser", screen, 1 * 8 + 4, 9 * 8, Color.get(0, 333, 333, 333));
		Font.draw("to download page", screen, 1 * 8 + 4, 10 * 8, Color.get(0, 333, 333, 333));
	}
}
