import java.util.ArrayList;


public class ShannonFanoEncoding {
	
	public int findSplit(ArrayList<Record> arraylist) {
		int length = arraylist.size();
		
		int temp = 0;
		
		for(int i = 0; i < length; i++) {
			temp += arraylist.get(i).getFrequency();
		}
		temp = temp/length;
		
		int split = -1;
		
		for(int i =0; i < length; i++) {
			if(arraylist.get(i).getFrequency() > temp) {
				split = i;
				break;
			}
		}
		
		if(split == -1) {
			split = length/2;
		}
		//System.out.println("Temp: " + temp);
		//System.out.println("Split: " + split);
		
		return temp;
		
	}
	
	public void buildTree(ArrayList<Record> yuv, int start, int end, int totalCounts) {
		
		int center = 0;
		if(start < end - 1) {
			int sp = findSplit(yuv);
			for(int i = start; i < end; i++) {
				if(yuv.get(i).getFrequency() > sp) {
					center = i;
					yuv.get(i).coding += "0";
				} else {
					yuv.get(i).coding += "1";
				}
			}
			center++;
			System.out.println("Center : " + center);
			buildTree(yuv, start, center, 0);
			buildTree(yuv, center, end, 0);
			System.out.println("code: " + yuv.get(3).coding);
		}
		
		
		
		/*int semiTotal = 0;
		int center = 0;
		if(start < end - 1) {
			//int split = findSplit(yuv);
			for(int i = start; i < end; i++) {
				if(semiTotal < totalCounts /2) {
					center = i;
					semiTotal += yuv.get(i).getFrequency();
					yuv.get(i).coding += "0";
				} else {
					yuv.get(i).coding += "1";
				}
			}
			center++;
			System.out.println(yuv.get(0).coding);
			buildTree(yuv, start, center, semiTotal);
			buildTree(yuv, center, end, totalCounts - semiTotal);
		}*/		
	}
	
	public void buildCode(ArrayList<Record> rec, Record x, String s) {
		if(!x.isLeaf()) {
			buildCode(rec, x.left, s + '0');
			buildCode(rec, x.right, s + '1');
		} else {
			if(rec.contains(x)) {
				x.coding = s;
			}
		}
	}
}
