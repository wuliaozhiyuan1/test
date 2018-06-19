package test;

public class TestFor {
	
	public void Test(){
		
	}
	
	public static void main(String[] args) {
		String s = "[join]aa";
		System.out.println(s);
		System.out.println(s.replaceFirst("[join]", ""));
		System.out.println(s.replaceFirst("\\[join\\]", ""));
	}
	
}
