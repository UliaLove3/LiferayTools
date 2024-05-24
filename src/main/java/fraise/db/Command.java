package fraise.db;

import javax.portlet.ActionRequest;

public interface Command {
    String getCommand();
    void handleCommand(ActionRequest actionRequest);
}
