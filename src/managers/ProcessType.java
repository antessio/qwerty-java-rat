package managers;

import java.io.Serializable;

public class ProcessType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2284940150530016117L;
	public static final int CONNECTION=1;
	public static final int COMMANDS=2;
	public static final int FILES=3;
	public static final int SCREEN=4;
	public static final int WRONG=-1;
	public static final String SEPARATOR="-";
	private int type;
	private String name;
	public ProcessType(){
		
	}
	public ProcessType(int type){
		this(type,typeToName(type));
	}
	public ProcessType(int type, String name) {
		this.type = type;
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public static String typeToName(int type){
		switch(type){
			case CONNECTION: 
				return "CONNECTION";
			case COMMANDS:
				return "COMMANDS";
			case FILES:
				return "FILES";
			case SCREEN:
				return "SCREEN";
			default:
				return "WRONG";
		}
		
	}
	public static int nameToType(String name){

		if(name.equals("CONNECTION"))
			return CONNECTION;
		
		if(name.equals("COMMANDS"))
			return COMMANDS;
		
		if(name.equals("FILES"))
			return FILES;
		
		if(name.equals("SCREEN"))
			return SCREEN;
		
		return WRONG;
	}
	

	@Override
	public boolean equals(Object obj) {
		ProcessType toCompare=(ProcessType)obj;
		if(toCompare.getName().equals(name) && toCompare.getType()==type)
			return true;
		else return false;
		
	}
	@Override
	public String toString() {
		return name+SEPARATOR+type;
	}
	
}
