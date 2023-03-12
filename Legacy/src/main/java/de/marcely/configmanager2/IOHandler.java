package de.marcely.configmanager2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import de.marcely.configmanager2.objects.*;
import lombok.Getter;

@Deprecated
public class IOHandler {
	
	@Getter private final ConfigContainer container;
	
	public IOHandler(ConfigContainer container){
		this.container = container;
	}
	
	public int load(){
		// prepare
		container.getRootTree().clear();
		
		// start
		try{
			final BufferedReader reader = new BufferedReader(new InputStreamReader(this.container.openInputStream(), StandardCharsets.UTF_8));
			
			String line = null;
			
			Tree tree = container.getRootTree();
			int ixx=0;
			
			while((line = reader.readLine()) != null){
				line = replaceFirstSpaces(line);
				ixx++;

				if(line.length() == 0){
					tree.addChild(new EmptyLine(tree));
					continue;
				}
				
				final char firstChar = line.charAt(0);
				final char lastChar = line.charAt(line.length()-1);
				boolean newParent = false;
				
				if(firstChar == '#')
					tree.addChild(new Comment(tree, replaceFirstSpaces(line.substring(1))));
				else if(line.contains(":") && !replaceLastSpaces(line).endsWith("{")){
					final String[] strs = line.split(":");
					String value = "";
					
					for(int i=1; i<strs.length; i++){
						value += strs[i];
						
						if(i+1<strs.length)
							value += ":";
					}
					
					if(!line.startsWith("!"))
						tree.addChild(new Config(replaceLastSpaces(strs[0]), tree, replaceFirstSpaces(value)));
					else
						tree.addChild(new Description(tree, replaceLastSpaces(strs[0]).substring(1), replaceFirstSpaces(value)));
				}else if(lastChar == '{'){
					final String name = replaceLastSpaces(line.substring(0, line.length()-1));
					
					tree = new Tree(name, tree);
					tree.getParent().addChild(tree);
					newParent = true;
				}else if(replaceLastSpaces(line).equals("}")){
					tree = tree.getParent();
					newParent = true;

					if(tree == null){
						reader.close();
						return IOResult.RESULT_FAILED_LOAD_NOTVALID;
					}
					
				}else{
					if(line.isEmpty())
						tree.addChild(new EmptyLine(tree));
					else
						tree.addChild(new ListItem(line, tree));
				}
				
				if(!newParent){
					// add list item for every config
					final String value = replaceLastSpaces(line);
					
					tree.getRawChilds().add(value);
				}
			}
			
			reader.close();

			if(!tree.equals(container.getRootTree()))
				return IOResult.RESULT_FAILED_LOAD_NOTVALID;
			
		}catch(IOException e){
			e.printStackTrace();
			
			return IOResult.RESULT_FAILED_UNKOWN;
		}
		
		return IOResult.RESULT_SUCCESS;
	}
	
	public int save(){
		// start
		try{
			final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(this.container.openOutputStream(), StandardCharsets.UTF_8));
			final List<String> lines = getLines(container.getRootTree(), "");
			
			for(String line:lines){
				writer.write(line);
				writer.newLine();
			}
			
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
			
			return IOResult.RESULT_FAILED_UNKOWN;
		}
		
		
		return IOResult.RESULT_SUCCESS;
	}
	
	private List<String> getLines(Tree tree, String currentPrefix){
		return getLines(tree, currentPrefix, tree.equals(container.getRootTree()));
	}
	
	private List<String> getLines(Tree tree, String currentPrefix, boolean root){
		final List<String> lines = new ArrayList<String>();
		
		for(Config c:tree.getChilds()){
			if(c.getType() == Config.TYPE_TREE){
				
				lines.add(currentPrefix + c.getName() + " {");
				lines.addAll(getLines((Tree) c, currentPrefix + "	", false));
				lines.add(currentPrefix + "}");
				
			}else if(c.getType() == Config.TYPE_COMMENT)
				lines.add(currentPrefix + "# " + c.getValue());
			else if(c.getType() == Config.TYPE_EMPTYLINE)
				lines.add("");
			else if(c.getType() == Config.TYPE_DESCRIPTION)
				lines.add(currentPrefix + "!" + c.getName() + ": " + c.getValue());
			else if(c.getType() == Config.TYPE_LISTITEM)
				lines.add(currentPrefix + c.getValue());
			else
				lines.add(currentPrefix + c.getName() + ": " + c.getValue());
		}
		
		return lines;
	}
	
	private String replaceFirstSpaces(String str){
		while(str.startsWith(" ") || str.startsWith("	"))
			str = str.substring(1, str.length());
		
		return str;
	}
	
	private String replaceLastSpaces(String str){
		while(str.endsWith(" ") || str.endsWith("	"))
			str = str.substring(0, str.length()-1);
		
		return str;
	}
}