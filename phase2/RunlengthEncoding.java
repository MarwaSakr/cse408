import java.util.ArrayList;
import java.util.Collections;


public class RunlengthEncoding {
	
	private ArrayList<Integer> arraylist_YUV= new ArrayList<Integer>();
	
	
	public ArrayList encode(int[] array) {	
		
		for(int i = 0; i < array.length; i++) {
			int runlength = 1;
			while(i+1 < array.length && array[i] == array[i+1]) {
				runlength++;
				i++;
			}
			
			arraylist_YUV.add(runlength);
			arraylist_YUV.add(array[i]);
			
		}
		
		return arraylist_YUV;
		
	}
	
}
