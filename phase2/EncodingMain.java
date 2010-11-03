import java.util.ArrayList;
import java.util.Collections;

public class EncodingMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Encoding encode = new Encoding();
		
		ShannonFanoEncoding shannon = new ShannonFanoEncoding();
		
		RunlengthEncoding runlength = new RunlengthEncoding();
		
		String[] test = {"H", "E", "L", "L", "O"};
		int[] test2 = {3, 3, 3, 4, 5, 5, 10, 1, 5, 4};
		
		ArrayList<Record> records_Y = new ArrayList<Record>();
		
		records_Y = encode.findFrequency(test);
		
		//Collections.sort(records_Y);
		Object[] elements = records_Y.toArray();		
		
		for(int i = 0; i < elements.length; i++) {
			System.out.println(elements[i]);
		}
		
		Collections.sort(records_Y);
		elements = records_Y.toArray();
		
		System.out.println();		
		
		for(int i = 0; i < elements.length; i++) {
			System.out.println(elements[i]);
		}
		
		ArrayList<Integer> temp  = runlength.encode(test2);
		
		elements = temp.toArray();
		for(int i = 0; i < elements.length; i++) {
			System.out.println(elements[i]);
		}
		
		shannon.buildTree(records_Y, 0, records_Y.size(), 5);
	}
}
