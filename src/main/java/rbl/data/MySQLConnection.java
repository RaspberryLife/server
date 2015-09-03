package rbl.data;

import rbl.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by Peter Mösenthin.
 * <p>
 * Class to handle the actual MySQL Database.
 */
public class MySQLConnection
{

	private Connection mConnection;

	public static final String DEBUG_TAG = MySQLConnection.class.getSimpleName();

	public boolean open()
	{
		Log.add(DEBUG_TAG, "Opening database connection");
		Properties connectionProps = new Properties();
		connectionProps.put("user", Config.getConf().getString("database.user"));
		connectionProps.put("password", Config.getConf().getString("database.password"));
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			mConnection = DriverManager.getConnection(
					"jdbc:mysql://" +
							Config.getConf().getString("database.host")
							+ ":"
							+ Config.getConf().getString("database.port")
							+ "/",
					connectionProps);

		}
		catch (Exception e)
		{
			Log.add(DEBUG_TAG, "Could not open database");
			return false;
		}
		return true;
	}

	public boolean databaseExists()
	{
		ResultSet resultSet = null;
		String dbname = Config.getConf().getString("database.db");
		try
		{
			resultSet = mConnection.getMetaData().getCatalogs();

			while (resultSet.next())
			{
				// Get the database name, which is at position 1
				String databaseName = resultSet.getString(1);
				if (databaseName.equals(dbname))
				{
					return true;
				}
			}
			resultSet.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return false;
	}

	public void createDatabase()
	{
		Log.add(DEBUG_TAG, "Creating database");
		Statement statement = null;
		try
		{
			statement = mConnection.createStatement();
			statement.execute("CREATE DATABASE IF NOT EXISTS rbl_data COLLATE utf8_general_ci");
		}
		catch (Exception e)
		{
			Log.add(DEBUG_TAG, "Could not create database: ", e);
		}
	}

	public void close()
	{
		Log.add(DEBUG_TAG, "Closing connection");
		try
		{
			mConnection.close();
		}
		catch (Exception e)
		{
			Log.add(DEBUG_TAG, "Could not close connection: ", e);
		}
	}

}
