/**
* Adds an GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @version 1.4
* @website http://marcely.de/ 
*/

package de.marcely.warpgui.config;

import java.util.ArrayList;
import java.util.List;

public class MultiKeyMap<obj1, obj2>{
	List<obj1> l1 = new ArrayList<obj1>();
	List<obj2> l2 = new ArrayList<obj2>();
	
	public MultiKeyMap(){ }
	
	public int size(){
		return l1.size();
	}
	
	public void put(obj1 o1, obj2 o2){
		l1.add(o1);
		l2.add(o2);
	}
	
	public obj2 getFirst(obj1 o1){
		if(get(o1).size() >= 1)
			return get(o1).get(0);
		else
			return null;
	}
	
	public List<obj2> get(obj1 o1){
		List<obj2> list = new ArrayList<obj2>();
		int i=0;
		for(obj1 o:l1){
			if(o1.equals(o))
				list.add(l2.get(i));
			i++;
		}
		return list;
	}
	
	public ArrayList<MultiKeyEntry<obj1, obj2>> entrySet(){
		ArrayList<MultiKeyEntry<obj1, obj2>> list = new ArrayList<MultiKeyEntry<obj1, obj2>>();
		int i=0;
		for(obj1 o:this.l1){
			list.add(new MultiKeyEntry<obj1, obj2>(o, l2.get(i)));
			i++;
		}
		return list;
	}
	
	public void remove(obj1 o1){
		int i=0;
		for(obj1 o:l1){
			if(o.equals(o1)){
				l1.remove(i);
				l2.remove(i);
			}
			i++;
		}
	}
	
	public void remove(obj1 o1, obj2 o2){
		int i=0;
		for(obj1 o:l1){
			if(o.equals(o1) && l2.get(i).equals(o2)){
				l1.remove(i);
				l2.remove(i);
			}
			i++;
		}
	}
	
	public boolean containsKey(obj1 o1){
		return l1.contains(o1);
	}
	
	public boolean containsValue(obj2 o2){
		return l2.contains(o2);
	}
	
	public void replace(obj1 o1, obj2 o2){
		remove(o1);
		put(o1, o2);
	}
	
	public List<obj1> keySet(){
		return new ArrayList<obj1>(l1);
	}
}
