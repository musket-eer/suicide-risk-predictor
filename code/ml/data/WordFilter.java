/**
 * 
 */
package ml.data;

import java.io.IOException;

import ml.utils.HashMapCounter;

/**
 * @author _musket.eer
 *
 */
public class WordFilter extends TextDataReader{
	private int minimumWordLength;
	public WordFilter(String textFile) {
		super(textFile);
		// TODO Auto-generated constructor stub
		
	}
	
	public void setMinimumWordLength(int minimumLength) {
		minimumWordLength = minimumLength;
	}
	
	@Override
	public Example next() {
		Example data = null;
		
		if( hasNext() ){
			data = new Example();
			
			// parse the line
			String[] parts = nextLine.split("\\s+");
			
			data.setLabel(Double.parseDouble(parts[0]));
	
			// do a little bit of preprocessing and count how
			// many times each word occurs
			HashMapCounter<String> counter = new HashMapCounter<String>();
			
			for( int i = 1; i < parts.length; i++ ){
				String w = parts[i].toLowerCase();
				
				// check if it has at least one alphabet character
				if( !w.matches("[^a-z]+")){
					counter.increment(w);
				}
			}
			
			for( String word: counter.keySet() ){
				if (word.length() >= minimumWordLength) {
				if( !wordToFeature.containsKey(word) ){
					wordToFeature.put(word, currentFeature);
					currentFeature++;
				}
				
				data.addFeature(wordToFeature.get(word), counter.get(word));
			}
				}
			
			try {
				nextLine = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return data;
	}

}
