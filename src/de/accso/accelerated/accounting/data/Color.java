package de.accso.accelerated.accounting.data;

public class Color {
	
	private String name;
	private String code;
	
	public Color(String name, String code){
		this.name = name;
		this.code = code;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getCode(){
		return this.code;
	}
}
