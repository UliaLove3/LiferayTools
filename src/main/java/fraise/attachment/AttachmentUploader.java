package fraise.attachment;

import com.liferay.document.library.kernel.exception.DuplicateFileEntryException;
import com.liferay.document.library.kernel.exception.NoSuchFolderException;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.service.ServiceContext;

import java.net.URLConnection;

public class AttachmentUploader{
	private static final Log log =LogFactoryUtil.getLog(AttachmentUploader.class);
	private final ServiceContext serviceContext;
	private final long userid;
	public AttachmentUploader(ServiceContext serviceContext){
		this.serviceContext=serviceContext;
		this.userid = serviceContext.getUserId();
	}
	
	public AttachmentUploader(ServiceContext serviceContext, User fileOwner){
		this.serviceContext = serviceContext;
		this.userid =fileOwner.getUserId();
	}
	
	
	public AttachmentUploaded uploadAttachment(Attachment attachment, String folderName, String...tags) throws PortalException{
		Folder folder = getOrCreateFolder(folderName);
		String mimeType = URLConnection.guessContentTypeFromName(attachment.getFilename());
		
		FileEntry fileEntry;
		//Trying to upload, but if it already exists, we create a new one with a new title.
		try{
			fileEntry = DLAppLocalServiceUtil.addFileEntry(userid, folder.getRepositoryId(),folder.getFolderId(), attachment.getFilename(), mimeType, attachment.getTitle(), "", "", attachment.getBytes(), serviceContext);
		}catch(DuplicateFileEntryException ex){
			String title = "";
			if(attachment.getTitle().contains(".")){
				String[] tokens = attachment.getTitle().split("\\.");
				for(int j=0; j<tokens.length-1; j++){
					title += tokens[j];
				}
				title += System.currentTimeMillis() + "." + tokens[tokens.length-1];
			}else{
				title += attachment.getTitle() + System.currentTimeMillis();
			}
			attachment.setTitle(title);
			System.out.println(attachment.getTitle());
			fileEntry = DLAppLocalServiceUtil.addFileEntry(userid, folder.getRepositoryId(),folder.getFolderId(), attachment.getFilename(), mimeType, attachment.getTitle(), "", "", attachment.getBytes(), serviceContext);
		}
		DLAppLocalServiceUtil.updateAsset(fileEntry.getUserId(), fileEntry, fileEntry.getFileVersion(), new long[0], tags,new long[0]);
		
		String fileURL;
		if(serviceContext.getThemeDisplay()!=null){
			fileURL = serviceContext.getPortalURL() + serviceContext.getThemeDisplay().getPathContext() + "/documents/" + serviceContext.getThemeDisplay().getScopeGroupId() + "/" +
					fileEntry.getFolderId() +  "/" +fileEntry.getTitle() ;
		}else{
			fileURL = serviceContext.getPortalURL() +  "/c/document_library/get_file?uuid=" + fileEntry.getUuid() + "&amp;groupId=" + fileEntry.getGroupId()+"&amp;";
		}
		
		
		log.info("attachment \""+ attachment.getTitle() +"\" was uploaded on server");
		return new AttachmentUploaded(attachment.getTitle(), attachment.getFilename(), attachment.getBytes(), fileURL, fileEntry);
	}
	
	public Folder getOrCreateFolder(String folderName) throws PortalException, SystemException{
		long userId = this.userid;
		long parentFolderId = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;
		long repositoryId = serviceContext.getScopeGroupId();
		try {
			return DLAppLocalServiceUtil.getFolder(
					repositoryId, parentFolderId, folderName);
		} catch (final NoSuchFolderException e) {
			return DLAppLocalServiceUtil.addFolder(userId,
					repositoryId, parentFolderId, folderName,
					"", serviceContext);
		}
	}
}
