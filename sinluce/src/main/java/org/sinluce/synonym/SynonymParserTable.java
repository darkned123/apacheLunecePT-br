package org.sinluce.synonym;

/**
 * Copyright Manning Publications Co.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific lan      
*/

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sinluce.AnalyzerUtils;


public class SynonymParserTable {

	public static String parse(String str, Boolean bool, String table) throws IOException {
	
		String[] s1;
		
		Boolean boo = bool;
		String c = str;
		String[] con = str.split(" ");
		int contt = con.length*2;
		if(table.equals("funcionario")){
			SynonymEngine engine = new SynonymMapFunAtr();
			AnalyzerUtils.displayTokens(new SynonymAnalyzerS2(engine), c);
			String parser =  AnalyzerUtils.getString();
			String[] p = parser.split(" ");
			parser = "";
			for (int i = 0; i < p.length; i++) {
				if(i>p.length-contt){
					parser = parser +p[i]+ " ";
				}
			}
			return parser;
		} else if(table.equals("Aluno")){
			SynonymEngine engine = new SynonymMapAlAtr();
			AnalyzerUtils.displayTokens(new SynonymAnalyzerS2(engine), c);
			String parser =  AnalyzerUtils.getString();
			String[] p = parser.split(" ");
			parser = "";
			for (int i = 0; i < p.length; i++) {
				if(i>p.length-contt){
					parser = parser +p[i]+ " ";
				}
			}
			return parser;
		}else{
		
		
		SynonymEngine engine = new SynonymMap();
	
		
		String[] ccc = c.split("\\.");
		String rec = "";
		for (String string : ccc) {
			rec = rec + string + " ";
		}
		
		AnalyzerUtils.displayTokens(new SynonymAnalyzerS4(engine), rec);
		String cc =  AnalyzerUtils.getString();
		AnalyzerUtils.displayTokens(new SynonymAnalyzerS3(engine),rec);
		String s = AnalyzerUtils.getString();
	

		s1 = s.split(" ");
		
	
		String[] resultado = new String[s1.length];
		for (int i = 0; i < s1.length; i++) {

			if (cc.contains(s1[i])) {
				
				resultado[i] = s1[i];
			}else{
				resultado[i-1] = s1[i];
			}

		}
		String strMap = "";
		for (String string : resultado) {
			if(string == null){
				continue;
			}else{
				strMap = strMap + string + " ";
			}
				
		}
		StringBuilder strBMap = new StringBuilder(strMap);
		strBMap.delete(0, cc.length());
		strMap = strBMap.toString();
	
		String[] cont = strMap.split(" ");
		if(cont.length ==2 && boo == true){
			return cont[1];
		}
		if(cont.length > 2) {
			strMap = strMap.replaceAll(cont[0], "");
			return strMap;
		}else{
			return strMap;
		}
		
		
		
		
	}}

	
}
