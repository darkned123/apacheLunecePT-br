package org.sinluce.synonym;



import java.util.HashMap;


public class TestSynonymEngine implements SynonymEngine {
  private static HashMap<String, String[]> map = new HashMap<String, String[]>();

  public static  void addSynonym(String word, String[] syn){
	    map.put(word,syn);
	  }

  public String[] getSynonyms(String s) {
    return map.get(s);
  }
}
