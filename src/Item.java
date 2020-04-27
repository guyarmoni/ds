/**
 * Name: Guy Armoni
 * Username: guya
 * ID: 205988595
 * 
 * Name: Omer Militscher
 * Username: militscher
 * ID: 313585085
 */

public class Item{
	
	private int key;
	private String info;
	
	public Item (int key, String info){
		this.key = key;
		this.info = info;
	}
	public int getKey(){
		return key;
	}
	public String getInfo(){
		return info;
	}
}