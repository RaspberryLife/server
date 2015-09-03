package rbl.util;

import rbl.data.Config;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Peter Mösenthin.
 * <p>
 * The log handles any console or logging output and will later be used to write log files.
 */
public class Log
{

	private static final String DEBUG_TAG = Log.class.getSimpleName();
	public static final String LOG_FILE = "rbl_server.log";

	private static PrintWriter printWriter;
	private static FileWriter fileWriter;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private static final Date logDate = new Date();

	public static void init()
	{
		try
		{
			fileWriter = new FileWriter(LOG_FILE, true);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		Log.add(DEBUG_TAG, "Writing log to " + LOG_FILE);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		printWriter = new PrintWriter(bufferedWriter);
	}

	public static void add(String tag, String message)
	{
		switch (Config.DEBUG_LEVEL)
		{
			case 0:
				break;
			case 1:
				print(getTime() + " [" + tag + "]: " + message);
				break;
			default:
				print(getTime() + " [" + tag + "]: " + message);
		}
	}

	private static String getTime(){
		return sdf.format(logDate);
	}

	public static void add(String tag, String message, Exception e)
	{
		message += " Exception: "
				+ e.getClass().getSimpleName() + " - "
				+ e.getMessage();
		add(tag, message);
	}

	private static void print(String text)
	{
		System.out.println(text);
		if (printWriter != null)
		{
			printWriter.println(text);
			printWriter.flush();
		}
	}
}
