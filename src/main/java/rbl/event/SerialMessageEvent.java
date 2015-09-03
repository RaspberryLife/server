package rbl.event;

import rbl.util.Log;
import rbl.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter Mösenthin.
 */
public class SerialMessageEvent
{

	public static final String DEBUG_TAG = SerialMessageEvent.class.getSimpleName();

	public enum Type
	{
		SEND,
		RECEIVE
	}

	private Type messageType;

	private static int messageSequenceNumber = 0;

	private String rawContent = "";

	private int moduleType;
	private int moduleId;
	private int messageSeq;
	private int instructionId;
	private List<String> parameters = new ArrayList<String>();

	public SerialMessageEvent(String content)
	{
		this.rawContent = content;
	}

	public SerialMessageEvent()
	{

	}

	public void buildModel()
	{
		try
		{
			String[] split = rawContent.split(":");
			moduleType = Integer.parseInt(split[0]);
			moduleId = Integer.parseInt(split[1]);
			messageSeq = Integer.parseInt(split[2]);
			instructionId = Integer.parseInt(split[3]);

			for (int i = 4; i < split.length; i++)
			{
				parameters.add(split[i]);
			}
		}
		catch (Exception e)
		{
			Log.add(DEBUG_TAG, "Unable to populate serial message", e);
			e.printStackTrace();
		}
	}

	public String buildSerial()
	{
		String serialMessage = "";
		serialMessage += StringUtil.getZeroPadded(String.valueOf(moduleType), 3, true);

		//Target module id
		serialMessage += ":";
		serialMessage += StringUtil.getZeroPadded(String.valueOf(moduleId), 2, true);

		// Message number
		serialMessage += ":";
		serialMessage += StringUtil.getZeroPadded(String.valueOf(getMessageSequenceNumber()),
				2, true);

		// InstructionEvent id
		serialMessage += ":";
		serialMessage += StringUtil.getZeroPadded(String.valueOf(instructionId), 2, true);

		// Parameters
		if (parameters != null && parameters.size() > 0)
		{
			for (String p : parameters)
			{
				serialMessage += ":";
				//padding usually set to 4 (now to 2 since reed only ready two chars)
				serialMessage += StringUtil.getZeroPadded(p, 2, true);
			}
		}
		serialMessage = StringUtil.getZeroPadded(serialMessage, 32, false);

		rawContent = serialMessage;
		return rawContent;
	}

	private int getMessageSequenceNumber()
	{
		messageSequenceNumber++;
		if (messageSequenceNumber > 99)
		{
			messageSequenceNumber = 0;
		}
		return messageSequenceNumber;
	}

	@Override
	public String toString()
	{
		buildModel();
		return "MT:" + moduleType + " MID:" + moduleId + " SN:" + messageSeq + " IID:" + instructionId + " PARAM:"
				+ parameters.toString();
	}

	public Type getMessageType()
	{
		return messageType;
	}

	public void setMessageType(Type messageType)
	{
		this.messageType = messageType;
	}

	public static void setMessageSequenceNumber(int messageSequenceNumber)
	{
		SerialMessageEvent.messageSequenceNumber = messageSequenceNumber;
	}

	public String getRawContent()
	{
		return rawContent;
	}

	public void setRawContent(String rawContent)
	{
		this.rawContent = rawContent;
	}

	public int getModuleType()
	{
		return moduleType;
	}

	public void setModuleType(int moduleType)
	{
		this.moduleType = moduleType;
	}

	public int getModuleId()
	{
		return moduleId;
	}

	public void setModuleId(int moduleId)
	{
		this.moduleId = moduleId;
	}

	public int getMessageSeq()
	{
		return messageSeq;
	}

	public void setMessageSeq(int messageSeq)
	{
		this.messageSeq = messageSeq;
	}

	public int getInstructionId()
	{
		return instructionId;
	}

	public void setInstructionId(int instructionId)
	{
		this.instructionId = instructionId;
	}

	public List<String> getParameters()
	{
		return parameters;
	}

	public void setParameters(List<String> parameters)
	{
		this.parameters = parameters;
	}
}
