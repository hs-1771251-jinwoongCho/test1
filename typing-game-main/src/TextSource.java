import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.Vector;

public class TextSource {
	private Vector<String> v = new Vector<String>();
	private String lang = "en";
	
	public TextSource() {
		
	}
	
	public TextSource(String lang) {
		this.lang = lang;
		readFile(lang);
	}
	
	public void readFile(String lang) {
		String fileName;
		String word;
		
		if ("ko".equals(lang)) {
			fileName = "ko.txt";
			try {
				String line;
				BufferedReader br = new BufferedReader(
						new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
				while((line = br.readLine()) != null) {
					word = line.trim();
					v.add(word);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}	        
		}
		else {
			fileName = "en.txt";
			try {
				System.out.println("영타");
				Scanner fscanner = new Scanner(new FileReader(fileName));
				while(fscanner.hasNext()) {
					word = fscanner.nextLine();
					v.add(word);
				}
				fscanner.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public String get(String lang) {
		int index = (int)(Math.random()*v.size());
		return v.get(index);
	}
}