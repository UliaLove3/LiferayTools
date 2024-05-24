package fraise.util;

import com.liferay.portal.kernel.exception.NoSuchUserException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;

import java.util.List;

public class UtilUserByEmail{
	private static final Log log = LogFactoryUtil.getLog(UtilUserByEmail.class);
	
	public static User getOrNullUserByEmail(String email){
		User user = null;
		try{
			List<Company> companies = CompanyLocalServiceUtil.getCompanies();
			for(Company i:companies){
				try{
					user=UserLocalServiceUtil.getUserByEmailAddress(i.getCompanyId(), email);
					break;
				}catch(NoSuchUserException e){
				
				}
			}
		}catch(Exception ex){
			log.error(ex);
		}
		return user;
	}
	
}
