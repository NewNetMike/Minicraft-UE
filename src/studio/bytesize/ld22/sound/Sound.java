package studio.bytesize.ld22.sound;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
import java.util.HashMap;

public class Sound
{
	private static HashMap<String, Sound> sounds = new HashMap<String, Sound>();

	public static void load(String name, String filepath)
	{
		load(name, Sound.class.getResource(filepath));
	}

	public static void load(String name, URL url)
	{
		sounds.put(name, new Sound(url));
	}

	public static void play(String name)
	{
		sounds.get(name).play();
	}

	private AudioClip clip;

	private Sound(URL url)
	{
		try
		{
			clip = Applet.newAudioClip(url);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}

	public void play()
	{
		try
		{
			new Thread()
			{
				public void run()
				{
					clip.play();
				}
			}.start();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}
}
