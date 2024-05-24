package fraise.util;

public class TextTemplate{
	private String template;
	
	public TextTemplate(String template){
		this.template=template;
	}
	
	public void fillTemplate(String placeholder, String value){
		template = template.replace(placeholder, value);
	}
	
	@Override
	public String toString(){
		return template;
	}
}
