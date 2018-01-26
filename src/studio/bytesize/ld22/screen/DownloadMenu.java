package studio.bytesize.ld22.screen;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import studio.bytesize.helper.MyOS;
import studio.bytesize.helper.MyXML;
import studio.bytesize.ld22.Game;
import studio.bytesize.ld22.gfx.Color;
import studio.bytesize.ld22.gfx.Font;
import studio.bytesize.ld22.gfx.Screen;
import studio.bytesize.ld22.sound.Sound;

public class DownloadMenu extends Menu
{
	private Game game;
	private int selected = 0;
	private ArrayList<DownloadablePlugin> plugins = new ArrayList<DownloadablePlugin>();

	public DownloadMenu(Game game)
	{
		Sound.load("pluginsSelected", this.getClass().getResource("/pluginsSelected.wav"));
		this.game = game;
		
		loadPluginsFromXML();
	}
	
	public void loadPluginsFromXML()
	{
		NodeList pluginsXML = MyXML.getPluginNodes();
		
		String name = "name";
		String url = "url";
		String save_name = "save_name";
		
		for(int i = 0; i < pluginsXML.getLength(); i++)
		{
			Element element = (Element)pluginsXML.item(i);
			
			Element pluginName = (Element)element.getElementsByTagName(name).item(0);
			Element pluginURL = (Element)element.getElementsByTagName(url).item(0);
			Element pluginSaveName = (Element)element.getElementsByTagName(save_name).item(0);

			plugins.add(new DownloadablePlugin(pluginName.getTextContent(), pluginURL.getTextContent(),
					pluginSaveName.getTextContent()));
		}
	}
	
	public void downloadPlugins()
	{
		File file = new File(MyOS.getAppdataLocation() + "/.minicraft/tmp/");
		if (!file.exists())
		{
			file.mkdir();
		}
		
		for(int i = 0; i < plugins.size(); i++)
		{
			System.out.println(plugins.get(i).getName() + " " + plugins.get(i).getFilePath());
			URL website = null;
			try
			{
				website = new URL(plugins.get(i).getFilePath());
			}
			catch (MalformedURLException e2)
			{
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			try (InputStream in = website.openStream())
			{
			    try
				{
					Files.copy(in, Paths.get(MyOS.getAppdataLocation() + "/.minicraft/tmp/" +
				plugins.get(i).getSimpleFileName()) , StandardCopyOption.REPLACE_EXISTING);
					
					in.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
	}

	public void tick()
	{
		if (input.up.clicked) selected--;
		if (input.down.clicked) selected++;

		int len = plugins.size() + 1;
		if (len == 0) selected = 0;
		if (selected < 0) selected += len;
		if (selected >= len) selected -= len;

		if (input.attack.clicked || input.menu.clicked)
		{
			if (selected == plugins.size())
			{
				for (int i = plugins.size()-1; i >= 0; i--)
				{
					if (!plugins.get(i).enabled)
					{
						plugins.remove(i);
					}
				}
				
				Sound.play("pluginsSelected");
				downloadPlugins();
				game.setMenu(new RestartMenu());
			}
			else
			{
				plugins.get(selected).enabled = !plugins.get(selected).enabled;
			}
		}
	}

	public void render(Screen screen)
	{
		screen.clear(0);

		Font.draw("deselect", screen, (8 * 8) + (4 * 8), 8, Color.get(0, 500, 500, 500));
		Font.draw("and", screen, (8 * 8), 8, Color.get(0, 444, 444, 444));
		Font.draw("select", screen, 8, 8, Color.get(0, 050, 050, 050));
		Font.draw("Minicraft plugins", screen, 12, 16, Color.get(0, 444, 444, 444));

		renderPluginList(screen, 1, 3, 12, 12, selected, plugins);

		String msg;
		int cnt = 0;
		
		for (int j = 0; j < plugins.size(); j++)
		{
			if (plugins.get(j).enabled) cnt++;
		}
		
		if(cnt > 99) cnt = 99;
		String s = (cnt == 0 || cnt > 1) ? "s" : "";
		msg = "Download " + cnt + "plugin" + s;
		
		int col = Color.get(0, 333, 333, 333);

		if (selected == plugins.size())
		{
			msg = "" + msg + "";
			col = Color.get(0, 555, 555, 555);
		}

		Font.draw(msg, screen, (screen.w - msg.length() * 8) / 2, screen.h - 16, col);

		if (selected == plugins.size())
		{
			Font.draw("" + cnt, screen, ((screen.w - msg.length() * 8) / 2) + 9*8, screen.h - 16, Color.get(0, 050, 050, 050));
		}
		else
		{
			Font.draw("" + cnt, screen, ((screen.w - msg.length() * 8) / 2) + 9*8, screen.h - 16, Color.get(0, 040, 040, 040));
		}
	}
}
