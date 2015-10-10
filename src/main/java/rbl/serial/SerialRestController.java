package rbl.serial;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import rbl.event.SerialMessageEvent;
import rbl.event.EventBusService;

import java.util.List;

public class SerialRestController
{
	@RequestMapping(value = "/rbl/serial/send", method = RequestMethod.POST)
	public void sendSerialMessage(
			@RequestParam(value = "instructionId") int instructionId,
			@RequestParam(value = "moduleId") int moduleId,
			@RequestParam(value = "moduleType") int moduleType,
			@RequestParam(value = "parameters") List<String> parameters)
	{
		SerialMessageEvent sme = new SerialMessageEvent();
		sme.setMessageType(SerialMessageEvent.Type.SEND);
		sme.setInstructionId(instructionId);
		sme.setModuleId(moduleId);
		sme.setModuleType(moduleType);
		sme.setParameters(parameters);
		EventBusService.post(sme);
	}
}
