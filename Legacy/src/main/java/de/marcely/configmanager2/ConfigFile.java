package de.marcely.configmanager2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lombok.Getter;

@Deprecated
public class ConfigFile extends ConfigContainer {
	
	@Getter private final File file;
	
	public ConfigFile(File file){
		this(file, false);
	}
	
	public ConfigFile(File file, boolean getConfigNeverNull){
		super(getConfigNeverNull);
		
		this.file = file;
	}
	
	@Override
	public InputStream openInputStream() throws IOException {
		return new FileInputStream(file);
	}

	@Override
	public OutputStream openOutputStream() throws IOException {
		return new FileOutputStream(file);
	}
	
	/**
	 * 
	 * @return Returns the result of the class IOResult
	 */
	public int load(){
		if(!file.exists())
			return IOResult.RESULT_FAILED_LOAD_MISSINGFILE;
		else if(!file.canRead())
			return IOResult.RESULT_FAILED_LOAD_NOPERMS;
		
		return super.load();
	}
	
	/**
	 * 
	 * @return Returns the result of the class IOResult
	 */
	public int save(){
		// check if everything is ok
		if(file.exists() && !file.canWrite())
			return IOResult.RESULT_FAILED_SAVE_NOPERMS;
		
		// recreate
		try{
			if(file.exists())
				file.delete();
			file.createNewFile();
			
			// check if everything is ok again
			if(!file.canWrite())
				return IOResult.RESULT_FAILED_SAVE_NOPERMS;
		
		}catch(IOException e){
			e.printStackTrace();
			
			return IOResult.RESULT_FAILED_SAVE_NOPERMS;
		}
		
		return super.save();
	}
	
	public boolean exists(){
		return this.file.exists();
	}
}
