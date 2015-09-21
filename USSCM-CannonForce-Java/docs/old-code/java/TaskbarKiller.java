package com.ussc.serialreader;

import java.io.File;

class TaskbarKiller {
   
	@SuppressWarnings("unused")
	public TaskbarKiller()
    {
    	String filepath = new File("inc/taskbar_killer.exe").getAbsolutePath();
    	try
    	{
    		Runtime rt = Runtime.getRuntime() ;
    	 	Process p = rt.exec(filepath) ;
    	}
    	catch(Exception e)
    	{
    		System.out.println(e);
    	}
    }
	
}



