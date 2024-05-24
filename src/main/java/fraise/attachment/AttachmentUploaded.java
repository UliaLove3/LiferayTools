package fraise.attachment;

import com.liferay.portal.kernel.repository.model.FileEntry;

public class AttachmentUploaded extends Attachment{
	private String fileURL;
	
	private FileEntry fileEntry;
	
	
	public AttachmentUploaded(String title, String filename, byte[] bytes, String fileURL, FileEntry fileEntry){
		super(title, filename, bytes);
		this.fileURL=fileURL;
		this.fileEntry=fileEntry;
	}
	
	public String getFileURL(){
		return fileURL;
	}
	
	public FileEntry getFileEntry(){
		return fileEntry;
	}
}
