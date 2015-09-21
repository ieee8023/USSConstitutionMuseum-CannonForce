package com.ussc.serialreader;

import java.io.File;
import java.util.HashMap;

public class SWFLauncher extends Thread {
	
	private String bitValue;
	private int distanceValue;
	
	private HashMap<String, int[]> hash;
	
	public SWFLauncher()
	{
		this.bitValue 		= null;
		this.distanceValue 	= -1;
		
		this.hash 			= buildHash();
	}
	
	public void set_input(byte[] bytes)
	{
		
		// Get bit value in binary.
		StringBuilder binary = new StringBuilder();
		int val = bytes[0];
        for (int i = 0; i < 8; i++)
        {
           binary.append((val & 128) == 0 ? 0 : 1);
           val <<= 1;
        }
        
        // Set values.
        this.bitValue = binary.toString();
        this.distanceValue = bytes[1];
        System.out.println("mag: " + this.bitValue + " | " + "dis:" + this.distanceValue);
        // Check to see if this called to fire.
		int fire = bytes[2];
		if (fire == 13){
			// make sure that the lookup values are set.
			if (this.bitValue != null && this.distanceValue != -1)
			{
				int[] files_array = (int[]) hash.get(this.bitValue);
				if (files_array != null)
				{
					System.out.println("Size of array: " + files_array.length);

					// 0 uses index 0 as well. 
					int lookup_dis = (this.distanceValue > 0) ? this.distanceValue - 1 : 0;
					System.out.println("Look for index: " + lookup_dis);
					
					String filename = int_to_filename( files_array[lookup_dis] );
					fire(filename);
				}
			}
		}
		
	}
	
	public void reset()
	{
		this.bitValue 		= null;
		this.distanceValue 	= -1;
	}
	
	@SuppressWarnings("unused")
	public void fire(String animation)
	{
		String filepath = new File("animations/" + animation).getAbsolutePath();
	    System.out.println("Fired: " + animation);
		try
    	{
    		Runtime rt = Runtime.getRuntime();
    	 	Process p = rt.exec(filepath);
    	}
    	catch(Exception e)
    	{
    		System.out.println(e);
    	}
		
		// Reset storage.
		reset();
    }
	 
	 private String int_to_filename(int in_int)
	 {
		 String s;
		 if ( in_int < 10 )
			 s = "0" + in_int;
		 else
			 s = "" + in_int;
		 
		 return "~" + s + ".exe";
	 }
	 
	 
	 // Binary number representation.
	 // --------------------------------------------------
	 // | Bit7 | Bit6 | Bit5 | Bit4 | Bit3 | Bit2 | Bit1 |
	 // --------------------------------------------------
			 
	 private HashMap<String, int[]> buildHash() {
		 
		 // The entire hash map to be returned.
		 HashMap<String, int[]> return_hash = new HashMap<String, int[]>();
		 
		 int[] any_frame_wo_planking = { 45, 45, 46, 46, 47, 48, 49, 49, 50, 50, 51, 52, 53, 53, 54, 55, 56, 56, 57, 58, 58, 59, 60, 60, 78, 79, 80, 81 };
		 return_hash.put("00000100", any_frame_wo_planking);
		 return_hash.put("00001000", any_frame_wo_planking);
		 return_hash.put("00010000", any_frame_wo_planking);

		 int[] live_oak_fame_both_sides_planked = { 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30 };
		 return_hash.put("00010011", live_oak_fame_both_sides_planked);
		 
		 int[] live_oak_fame_w_inner_planking = { 69, 69, 69, 70, 70, 70, 71, 71, 71, 72, 72, 72, 73, 73, 73, 74, 74, 74, 75, 75, 75, 76, 76, 76, 84, 84, 85, 85 };
		 return_hash.put("00010010", live_oak_fame_w_inner_planking);
		 
		 int[] live_oak_fame_w_outer_planking = { 23, 23, 23, 23, 23, 23, 24, 24, 24, 24, 24, 24, 25, 25, 25, 25, 25, 25, 26, 26, 26, 26, 26, 26, 15, 15, 15, 15 };
		 return_hash.put("00010001", live_oak_fame_w_outer_planking);
		 
		 int[] narrow_spaced_fame_both_sides_planked = { 28, 28, 28, 28, 28, 28, 29, 29, 29, 29, 29, 29, 17, 17, 17, 17, 17, 17, 18, 18, 18, 18, 18, 18, 11, 11, 11, 11 };
		 return_hash.put("00001011", narrow_spaced_fame_both_sides_planked);
		 
		 int[] narrow_spaced_fame_w_inner_planking = { 65, 65, 65, 65, 65, 65, 66, 66, 66, 66, 66, 66, 67, 67, 67, 67, 67, 67, 68, 68, 68, 68, 68, 68, 83, 83, 83, 83 };
		 return_hash.put("00001010", narrow_spaced_fame_w_inner_planking);
		 
		 int[] narrow_spaced_fame_w_outer_planking = { 21, 21, 21, 21, 21, 21, 22, 22, 22, 22, 22, 22, 13, 13, 13, 13, 13, 13, 14, 14, 14, 14, 14, 14, 6, 6, 6, 6 };
		 return_hash.put("00001001", narrow_spaced_fame_w_outer_planking);
		 
		 int[] no_hull = { 41, 41, 41, 41, 41, 41, 42, 42, 42, 42, 42, 42, 43, 43, 43, 43, 43, 43, 44, 44, 44, 44, 44, 44, 77, 77, 77, 77 };
		 return_hash.put("00000000", no_hull);
		 
		 int[] wide_spaced_frame_both_sides_planked = { 27, 27, 27, 27, 27, 27, 16, 16, 16, 16, 16, 16, 9, 9, 9, 9, 9, 9, 10, 10, 10, 10, 10, 10, 7, 7, 7, 7 };
		 return_hash.put("00000111", wide_spaced_frame_both_sides_planked);
		 
		 int[] wide_spaced_frame_w_inner_planking = { 61, 61, 61, 61, 61, 61, 62, 62, 62, 62, 62, 62, 63, 63, 63, 63, 63, 63, 64, 64, 64, 64, 64, 64, 82, 82, 82, 82 };
		 return_hash.put("00000110", wide_spaced_frame_w_inner_planking);
		 
		 int[] wide_spaced_frame_w_outer_planking = { 20, 20, 20, 20, 20, 20, 12, 12, 12, 12, 12, 12, 8, 8, 8, 8, 8, 8, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5 };
		 return_hash.put("00000101", wide_spaced_frame_w_outer_planking);
		 
		 return return_hash;
	 }
}
