package org.sinluce.synonym;

import java.util.HashMap;


public class SynonymMapAlAtr implements SynonymEngine {
	  private static HashMap<String, String[]> map = new HashMap<String, String[]>();

	  static {
	    map.put("nome", new String[] {"al_nome"});
	    map.put("idade", new String[] {"al_idade"});
	    map.put("idades", new String[] {"al_idade"});
	    map.put("nivel", new String[] {"al_nivel"});
	    map.put("cpf", new String[] {"al_cpf"});
	  
	  }

	  public String[] getSynonyms(String s) {
	    return map.get(s);
	  }
	}