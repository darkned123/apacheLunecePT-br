package org.sinluce.synonym;

import java.util.HashMap;


public class SynonymMapFunAtr implements SynonymEngine {
	  private static HashMap<String, String[]> map = new HashMap<String, String[]>();

	  static {
	    map.put("nome", new String[] {"fnome"});
	    map.put("salario", new String[] {"fsal"});
	    map.put("salarios", new String[] {"fsal"});
	    map.put("cargo", new String[] {"fcargo"});
	    map.put("idade", new String[] {"fidade"});
	    map.put("nascidos", new String[] {"fdnasci"});
	    map.put("cpf", new String[] {"fcpf"});
	    map.put("departamento", new String[] {"fdep"});
	  }

	  public String[] getSynonyms(String s) {
	    return map.get(s);
	  }
	}