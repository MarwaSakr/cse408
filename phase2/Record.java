
public class Record implements Comparable{
	
	private String value;
	private int frequency;
	public Record left, right;
	public String coding;
	
	public Record() {
		this.value = "";
		this.frequency = 0;		
		this.left= null;
		this.right = null;
		this.coding = "";
	}
	
	public Record(String str) {
		this.value = str;
		this.frequency = 1;
		this.left= null;
		this.right = null;
		this.coding = "";
	}
	public Record(String str, int f) {
		this.value = str;
		this.frequency = f;
		this.left= null;
		this.right = null;
		this.coding = "";
	}
	
	public Record(String str, int f, Record left, Record right) {
		this.value = str;
		this.frequency = f;
		this.left = left;
		this.right = right;
		this.coding = "";
	}
	
	public String getValue() {
		return this.value;
	}
	
	public int getFrequency() {
		return this.frequency;
	}
	
	public void incrementFreq() {
		this.frequency++;
	}
	
	public String toString() {
		return ("Value: " + this.value + " Frequency: " + this.frequency);
		
	}

	@Override
	public int compareTo(Object another) throws ClassCastException {
		// TODO Auto-generated method stub
		if(!(another instanceof Record))
			throw new ClassCastException("A Record object expected.");
		int anotherFrequency = ((Record)another).getFrequency();
		if(this.getFrequency() == anotherFrequency)
			return 0;
		else if(this.getFrequency() > anotherFrequency)
			return -1;
		else
			return 1;
	}
	
	  // is the node a leaf node?
    public boolean isLeaf() {
        assert (left == null && right == null) || (left != null && right != null);
        return (left == null && right == null);
    }

}
