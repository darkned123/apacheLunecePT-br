package org.sinluce.synonym;

import java.util.HashMap;

public class SynonymMap implements SynonymEngine {
	  private static HashMap<String, String[]> map = new HashMap<String, String[]>();

	  static {
	    map.put("Funcionários", new String[] {"funcionario"});
	    map.put("Funcionário", new String[] {"funcionario"});
	    map.put("funcionarios", new String[] {"funcionario"});
	    map.put("funcionários", new String[] {"funcionario"});
	    map.put("Funcionário", new String[] {"funcionario"});
	    map.put("empregados", new String[] {"funcionario"});
	    map.put("Empregados", new String[] {"funcionario"});
	    map.put("Alunos", new String[] {"Aluno"});
	    map.put("alunos", new String[] {"Aluno"});
	    map.put("aluno", new String[] {"Aluno"});
	    map.put("alunos", new String[] {"Aluno"});
	    map.put("aluno", new String[] {"Aluno"});
	  }

	  public String[] getSynonyms(String s) {
	    return map.get(s);
	  }
	}