package fraise.db;

import com.liferay.portal.kernel.util.ParamUtil;

import javax.portlet.ActionRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class Commands {
    HashMap<String, Command> commands = new HashMap<>();

    public void addCommand(String nameOfTemplate, Command command){
        commands.put(nameOfTemplate, command);
    }

    public void addCommandAttribute(HttpServletRequest httpServletRequest){
        for(Map.Entry<String, Command> i:commands.entrySet()){
            httpServletRequest.setAttribute(i.getKey(), i.getValue());
        }
    }

    public void handleCommands(ActionRequest actionRequest){
        String command = ParamUtil.getString(actionRequest, "command");
        for(Command i: commands.values()){
            if(i.getCommand().equalsIgnoreCase(command)){
                i.handleCommand(actionRequest);
                break;
            }
        }
    }
}
