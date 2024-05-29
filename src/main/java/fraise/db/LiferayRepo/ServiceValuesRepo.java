package fraise.db.LiferayRepo;

import fraise.db.Repo;
import fraise.db.Values;

import javax.portlet.PortletContext;

public class ServiceValuesRepo extends LiferayRepo<String>{
    private final PortletContext portletContext;

    public ServiceValuesRepo(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

    @Override
    public String read(Values key) {
        return (String) portletContext.getAttribute(key.prefix()+key.getKey());
    }

    @Override
    public void save(Values key, String value) {
        portletContext.setAttribute(key.prefix()+key.getKey(), value);
    }
}
