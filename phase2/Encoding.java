import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;


public class Encoding {
	
	private ArrayList<Record> records = new ArrayList<Record>();
	
	private String[] Array_Y;
	
	private Hashtable freq_Hash = new Hashtable();
	
	public ArrayList<Record> findFrequency(String[] array) {
		
		Array_Y = Arrays.copyOf(array, array.length);
		
		
		// Create a frequency table of Y, U, or V (Stores to hashtable first)
		for(int i = 0; i < Array_Y.length; i++) {
			
			if(freq_Hash.containsKey(Array_Y[i])) {
				int frequency = (Integer) freq_Hash.get(Array_Y[i]);
				frequency ++ ;
				freq_Hash.put(Array_Y[i], frequency);
			} else {
				freq_Hash.put(Array_Y[i], 1);
			}
			
		}
			
		// Copy frequency hastable to an array list
		for(Enumeration e = freq_Hash.keys(); e.hasMoreElements();) {
			String value = (String) e.nextElement();
			int freq = (Integer) freq_Hash.get(value);
			
			Record temp = new Record(value, freq);
			records.add(temp);
		}
		
		// Print out frequency hash table 
		
		/*Enumeration e = freq_Hash.keys();
		while( e. hasMoreElements() ){
			String value = (String) e.nextElement();
			Integer frequency = (Integer) freq_Hash.get(value);
			System.out.println("Value: " + value + " Frequency: " + frequency);
		}*/		
		
		return records;
	}
}
