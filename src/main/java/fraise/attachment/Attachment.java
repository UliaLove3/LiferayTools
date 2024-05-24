package fraise.attachment;

public class Attachment{
	private String title;
	private String filename;
	private byte[] bytes;
	
	public Attachment(String filename, byte[] bytes){
		this.filename=filename;
		this.title = filename;
		this.bytes=bytes;
	}
	
	public Attachment(String title, String filename, byte[] bytes){
		this.title = title;
		this.filename=filename;
		this.bytes=bytes;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getFilename(){
		return filename;
	}
	
	public byte[] getBytes(){
		return bytes;
	}
	
	public void setTitle(String title){
		this.title=title;
	}
}
