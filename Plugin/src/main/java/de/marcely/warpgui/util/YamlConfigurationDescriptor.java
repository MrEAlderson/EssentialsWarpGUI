package de.marcely.warpgui.util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Bukkit's implementation of {@link YamlConfiguration} don't support saving of comments or descriptions.
 * This class is making it possible.
 */
public class YamlConfigurationDescriptor extends YamlConfiguration {
	
	private final List<String> comments = new ArrayList<>();
	private int emptyLineIndex = 0;
	
	/**
	 * Adds a comment to the configuration
	 * 
	 * @param comment What the comment shall say
	 */
	public void addComment(String comment){
		Objects.requireNonNull(comment, "comment");
		
		this.comments.add(comment);
		
		this.set("@@@TMP_DESCRIPTOR_COMMENT" + this.comments.size(), "tmp");
	}
	
	/**
	 * Adds an empty line to the configuration
	 */
	public void addEmptyLine(){
		this.set("@@@TMP_DESCRIPTOR_EMPTYLINE" + (++this.emptyLineIndex), "tmp");
	}

	/**
	 * Adds a new empty line at the height of the configuration section.
	 * <p>
	 *    Keep in mind that it's not possible to use {@link #addEmptyLine()} to put an empty line between sections due to technical reasons.
	 *    Simply use this method to put
	 * </p>
	 *
	 * @param section To which section the empty line shall be added to
	 */
	public void addEmptyLine(ConfigurationSection section){
		section.set("@@@TMP_DESCRIPTOR_EMPTYLINE" + (++this.emptyLineIndex), "tmp");
	}

	@Override
	public String saveToString(){
		String str = super.saveToString();
		
		// comments
		for(int i=0; i<this.comments.size(); i++){
			final String comment = this.comments.get(i);
			String replacement = "#";
			
			if(comment.length() >= 1 && comment.charAt(0) != '#')
				replacement += " " + comment;
			else
				replacement += comment;
			
			str = str.replace(
					"'@@@TMP_DESCRIPTOR_COMMENT" + (i+1) + "': tmp",
					replacement);
		}
		
		// empty lines
		for(int i=0; i<this.emptyLineIndex; i++){
			str = str.replace(
					"'@@@TMP_DESCRIPTOR_EMPTYLINE" + (i+1) + "': tmp",
					"");
		}
		
		return str;
	}
}
