package studio.bytesize.helper;

public class MyOS
{
	public static String getAppdataLocation()
	{
		String appDataRoaming = "...";
		
		String OS = (System.getProperty("os.name")).toUpperCase();

		// to determine what the workingDirectory is.

		// if it is some version of Windows
		if (OS.contains("WIN"))
		{
			// it is simply the location of the "AppData" folder
			appDataRoaming = System.getenv("APPDATA");
		}
		else // Otherwise, we assume Linux or Mac
		{
			// in either case, we would start in the user's home directory
			appDataRoaming = System.getProperty("user.home");

			if (!OS.contains("LINUX"))
			{
				// if we are on a Mac, we are not done, we look for "Application Support"
				appDataRoaming += "/Library/Application Support";
			}
		}
		
		return appDataRoaming;
	}
}
