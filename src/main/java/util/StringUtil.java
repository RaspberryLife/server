package util;

/**
 * Created by Peter Mösenthin.
 * <p>
 * Class to handle the Authentication. Currently there really isn't any real
 * authentication
 */
public class StringUtil
{

	public static String getZeroPadded(String content, int maxLength, boolean padLeft)
	{
		int maxPaddingPosition = maxLength - content.length();
		String padded = "";
		for (int i = 0; i < maxPaddingPosition; i++)
		{
			padded += "0";
		}
		if (padLeft)
		{
			padded += content;
		}
		else
		{
			padded = content + padded;
		}
		return padded;
	}
}
