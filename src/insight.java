import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

public class insight {

	// store all paths of files
	List<Path> myPaths;
	// store # words in each line
	List<Integer> numOfWords;
	// store # occurrences of each word
	SortedMap<String, Integer> wordCount;

	public insight() {
		myPaths = new ArrayList<Path>();
		numOfWords = new ArrayList<Integer>();
		wordCount = new TreeMap<String, Integer>();
		importPath();
	}

	private void importPath() {
		Path dir = FileSystems.getDefault().getPath("..//wc_input");
		DirectoryStream<Path> stream;

		// import paths
		try {
			stream = Files.newDirectoryStream(dir);
			for (Path path : stream) {
				myPaths.add(path);
			}
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// sort filename without extensions
		Collections.sort(myPaths, new Comparator<Path>() {
			public int compare(Path o1, Path o2) {
				String s1 = o1.getFileName().toString();
				String s2 = o2.getFileName().toString();
				int pos1 = s1.lastIndexOf(".");
				int pos2 = s2.lastIndexOf(".");
				if (pos1 > 0)
					s1 = s1.substring(0, pos1);
				if (pos2 > 0)
					s2 = s2.substring(0, pos2);
				return s1.compareTo(s2);
			}
		});

		// show filenames
//		for (Path p : myPaths) {
//			System.out.println(p.getFileName());
//		}
	}

	public void count() throws FileNotFoundException {
		for (Path p : myPaths) {
			Scanner sc;
			sc = new Scanner(new FileReader(p.toFile()));
			while (sc.hasNextLine()) {
				// remove everything except a-z and 0-9 and store each word in array
				String str = sc.nextLine().trim();
				String[] words = str.replaceAll("[^a-zA-Z0-9 ]", "")
						.toLowerCase().split("\\s+");
				numOfWords.add(words.length);
				// save the words and frequency in map
				for (String s : words) {
					if (wordCount.containsKey(s)) {
						wordCount.put(s, wordCount.get(s) + 1);
					}
					else {
						wordCount.put(s, 1);
					}
				}
			}
			sc.close();
		}
	}
	
	public void show() throws FileNotFoundException, UnsupportedEncodingException {
		
		PrintWriter writer = new PrintWriter("..//wc_output//wc_result.txt", "UTF-8");
		for (String s : wordCount.keySet()) {
			writer.println(s + "\t" + wordCount.get(s));
		}
		writer.close();
		// calculate the moving median
		PrintWriter writer2 = new PrintWriter("..//wc_output//med_result.txt", "UTF-8");
		List<Integer> sorted = new ArrayList<Integer>();
		for (int i = 0; i < numOfWords.size(); i++) {
			sorted.add(numOfWords.get(i));
			Collections.sort(sorted);
			int mid = sorted.size()/2;
			if (sorted.size() % 2 == 1) {
				writer2.println(sorted.get(mid));
			}
			else {
				writer2.println(((float)sorted.get(mid-1) + (float)sorted.get(mid))/(float)2);
			}
		}
		writer2.close();
	}

	public static void main(String[] args) {
		insight t = new insight();
		try {
			t.count();
			t.show();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
