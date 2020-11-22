package de.marcely.configmanager2;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import de.marcely.configmanager2.objects.Comment;
import de.marcely.configmanager2.objects.Config;
import de.marcely.configmanager2.objects.Description;
import de.marcely.configmanager2.objects.EmptyLine;
import de.marcely.configmanager2.objects.Tree;
import lombok.Getter;

public class ConfigPicker {
	
	@Getter private final ConfigContainer container;
	@Getter private final List<Config> allConfigs = new ArrayList<Config>();
	
	public ConfigPicker(ConfigContainer container){
		this.container = container;
	}
	
	
	public Config addConfig(String path, Object value){
		return addConfig(path, value.toString());
	}
	
	public Config addConfig(String path, String value){
		final String[] strs = path.split("\\.");
		
		final Tree tree = path.contains(".") ? getTree(path.substring(0, path.lastIndexOf('.')), true) : container.getRootTree();
		final Config config = new Config(strs[strs.length-1], tree, value);
		tree.addChild(config);
		allConfigs.add(config);
		
		return config;
	}
	
	public Comment addComment(String value){
		return addComment("", value);
	}
	
	public Comment addComment(String path, String value){
		final Tree tree = getTree(path, true);
		final Comment config = new Comment(tree, value);
		tree.addChild(config);
		
		return config;
	}
	
	public EmptyLine addEmptyLine(){
		return addEmptyLine("");
	}
	
	public EmptyLine addEmptyLine(String path){
		final Tree tree = getTree(path, true);
		final EmptyLine config = new EmptyLine(tree);
		tree.addChild(config);
		
		return config;
	}
	
	public @Nullable Config getConfig(String path){
		final Tree tree = getTree(path.contains("\\.") ? path.substring(0, path.lastIndexOf('.')) : "", false);
		
		if(tree != null){
			final String[] strs = path.split("\\.");
			
			return tree.getConfigChild(strs[strs.length-1]);
		}else
			return !container.getConfigNeverNull ? null : new Config(null, null, null) /* TODO */;
	}
	
	public List<Config> getConfigsWhichStartWith(String name){
		final List<Config> list = new ArrayList<Config>();
		
		for(Config c:allConfigs){
			if(c.getAbsolutePath().startsWith(name))
				list.add(c);
		}
		
		return list;
	}
	
	public List<Config> getConfigsWhichEndWith(String name){
		final List<Config> list = new ArrayList<Config>();
		
		for(Config c:allConfigs){
			if(c.getAbsolutePath().endsWith(name))
				list.add(c);
		}
		
		return list;
	}
	
	public List<Config> getConfigs(String name){
		final List<Config> list = new ArrayList<Config>();
		
		for(Config c:allConfigs){
			if(c.getAbsolutePath().equals(name))
				list.add(c);
		}
		
		return list;
	}
	
	public Description setDescription(String name, String value){
		if(!containsBase())
			container.getRootTree().getChilds().add(0, new EmptyLine(container.getRootTree()));
		
		Description config = getDescription(name);
		if(config == null)
			config = new Description(container.getRootTree(), name, value);
		else
			config.setValue(value);
			
		container.getRootTree().getChilds().add(0, config);
		
		return config;
	}
	
	public @Nullable Description getDescription(String name){
		for(Config c:container.getRootTree().getChilds()){
			if(c.getName() != null && c.getName().equals(name) && c.getType() == Config.TYPE_DESCRIPTION)
				return (Description) c;
		}
		
		return null;
	}
	
	public boolean containsBase(){
		for(Config c:container.getRootTree().getChilds()){
			if(c.getType() == Config.TYPE_DESCRIPTION && ((Description) c).isBase())
				return true;
		}
		
		return false;
	}
	
	public @Nullable Tree getTree(String path, boolean newInstance){
		if(path.equals(""))
			return container.getRootTree();
		
		final String[] strs = path.split("\\.");
		
		int cIndex = 0;
		String name = "";
		Tree cTree = container.getRootTree();
		
		while(cIndex < strs.length){
			// append name
			if(!name.equals(""))
				name += ".";
			name += strs[cIndex];
			
			// get tree
			Tree nTree = cTree.getTreeChild(strs[cIndex]);
			
			// new tree if chield is missing
			if(nTree == null){
				if(newInstance){
					nTree = new Tree(strs[cIndex], cTree);
					cTree.addChild(nTree);
				}else
					return null;
			}
			
			// next
			cTree = nTree;
			cIndex++;
		}
		
		return cTree;
	}
}
