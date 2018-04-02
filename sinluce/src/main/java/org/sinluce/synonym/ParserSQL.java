package org.sinluce.synonym;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cogroo.analyzer.Analyzer;
import org.cogroo.analyzer.ComponentFactory;
import org.cogroo.text.Document;
import org.cogroo.text.Sentence;
import org.cogroo.text.Token;
import org.cogroo.text.impl.DocumentImpl;
import org.sinluce.AnalyzerUtils;

public class ParserSQL {
	private Analyzer cogroo;

	public static int and;

	public static int or;

	public static int between;
	
	public static int count;
	
	public static int avg;

	public static boolean data = false;

	public static String valor1 = "";
	public static String valor2 = "";
	public static String data1 = "";
	public static String data2 = "";
	// public static String[] tab_bet;
	public static String tab_betw;

	public ParserSQL() {

		ComponentFactory factory = ComponentFactory.create(new Locale("pt", "BR"));

		cogroo = factory.createPipe();
	}

	public void analyzeAndPrintDocument(String documentText) throws IOException {

		// Create a document and set the text.
		Document document = new DocumentImpl();
		document.setText(documentText);

		// analyze it
		cogroo.analyze(document);

		print(document);
	}

	@SuppressWarnings("null")
	private void print(Document document) throws IOException {
		StringBuilder output = new StringBuilder();

		Aluno aluno = new Aluno();
		aluno.atributos.add("nome");
		aluno.atributos.add("idade");
		aluno.atributos.add("idades");
		aluno.atributos.add("nível");
		aluno.atributos.add("cpf");

		Funcionario funcionario = new Funcionario();
		funcionario.atributos.add("nome");
		funcionario.atributos.add("idade");
		funcionario.atributos.add("idades");
		funcionario.atributos.add("cargo");
		funcionario.atributos.add("salário");
		funcionario.atributos.add("salários");
		funcionario.atributos.add("salario");
		funcionario.atributos.add("salarios");
		funcionario.atributos.add("departamento");
		funcionario.atributos.add("cpf");
		funcionario.atributos.add("nascidos");
	


		List<String> al = aluno.getAtributos();
		List<String> fun = funcionario.getAtributos();

		List<String> atr = new ArrayList<>();
		atr.add("nome");
		atr.add("salario");
		atr.add("salário");
		atr.add("salários");
		atr.add("idade");
		atr.add("idades");
		atr.add("cpf");
		atr.add("função");
		atr.add("cargo");
		atr.add("nota");
		atr.add("nascimento");
		atr.add("departamento");
		atr.add("nascidos");

		List<String> maior = new ArrayList<>();
		maior.add("maior");
		maior.add("maior do que");
		maior.add("maior que");
		maior.add("superior");
		maior.add("mais alto");
		maior.add("mais elevado");

		List<String> menor = new ArrayList<>();
		menor.add("menor");
		menor.add("menor do que");
		menor.add("menor que");
		menor.add("inferior");

		List<String> igual = new ArrayList<>();
		igual.add("igual a");
		igual.add("igual");
		igual.add("semelhante a");

		List<String> all = new ArrayList<>();
		all.add("todos");

		List<String> table = new ArrayList<>();
		table.add("funcionario");
		table.add("aluno");
		table.add("alunos");
		table.add("funcionário");
		table.add("funcionarios");
		table.add("funcionários");
		table.add("empregados");

		final String SELECT = "SELECT";
		String WHERE = "WHERE";
		final String FROM = "FROM";
		String tabelas = " ";
		String tabelaSin = " ";
		String tabelaSinComp = " ";
		String atr_simp = " ";
		String atr_comp = "";
		String atr_rep = "";
		String[] sp = null;
		int posi = 0;
		int c_maior = 0;
		int c_menor = 0;
		int c_igual = 0;
		int qnt_tab = 0;
		
		List<String> listTabela = new ArrayList<>();
		List<String> listAtr = new ArrayList<>();

		List<String> repTab = new ArrayList<>();

		for (Sentence sentence : document.getSentences()) {

			for (Token token : sentence.getTokens()) {
				String lexeme = token.getLexeme();

				String lemmas = Arrays.toString(token.getLemmas());
				
				String pos = token.getPOSTag();
				String feat = token.getFeatures();
				System.out.println(lexeme+ "--->"+lemmas+"---->"+pos+"--->"+feat);
				lemmas = lemmas.replace("[", "");
				lemmas = lemmas.replace("]", "");
				if (maior.contains(lexeme)) {
					lexeme = ">";
					c_maior++;
				}
				if (menor.contains(lexeme)) {
					lexeme = "<";
					c_menor++;
				}
				if (igual.contains(lexeme)) {
					lexeme = "=";
					c_igual++;
				}
				
				if(lexeme.equals("NFE") || lexeme.equals("nfe")){//...
					pos = "n";
				}
				if(lexeme.equals("nascidos") || lexeme.equals("nascimento")){
					pos = "n";
				}	
					
				if (pos.equals("n") && (atr.contains(lexeme))) {
					atr_simp = atr_simp + lexeme + ", ";
					lexeme = "ATR." + lexeme;
			

				} else if (pos.equals("n") && (table.contains(lexeme))) {
					atr_rep = lexeme;
					if (!repTab.contains(atr_rep)) {

						tabelas = tabelas + lexeme + ", ";
						tabelaSin = tabelaSin + lexeme + ", ";
						tabelaSinComp = tabelaSinComp + SynonymParserTable.parse(tabelaSin, true,"");
						lexeme = "TABLE." + lexeme;
					
						qnt_tab++;
						repTab.add(atr_rep);
					}
					

				}else{
					
				}
				if (pos.equals("prop")) {
					sp = atr_simp.split(",");
					posi = sp.length;

					if (posi >= 0) {
						try{
						atr_comp = atr_comp + sp[posi - 2] + "=" + "\'" + lexeme + "\'";
						}catch (Exception e) {
						}
					} else {
						atr_comp = atr_comp + atr_simp + lexeme;

					}

					lexeme = "ATR.NOME" + lexeme;
				}
				if (pos.equals("num") && between == 0) {
					sp = atr_simp.split(",");
					posi = sp.length;
					if (posi >= 0) {
						try{
						atr_comp = atr_comp + Normalizer.normalize(sp[posi - 2], Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")
								+ (c_maior > 0 ? ">" : c_menor > 0 ? "<" : c_igual > 0 ? "=" : "") + lexeme;
						}catch (Exception e) {
							
						}
					} else {
						atr_comp = atr_comp +  Normalizer.normalize(atr_simp, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "") + (c_maior > 0 ? "=" : "") + lexeme;

					}
					lexeme = lexeme + ".NUM";
					c_maior = 0;
				}
				if (pos.equals("num") && between == 1) {
					sp = atr_simp.split(",");
					posi = sp.length;
					if (posi >= 0) {
						if (data && between == 1) {
							
							atr_comp = atr_comp + tabelaSinComp.trim().replaceAll(",", "") + "." + tab_betw
									+ "##BETWEEN##" +"to_date(" +"\'" + data1 + "\')" + "##and##" +"to_date("+ "\'" + data2 + "\')";

							break;
						} else {
							if (valor1 == lexeme || valor2 == lexeme)
								continue;
							
							atr_comp = atr_simp + "BETWEEN" + valor1 + " " + valor2;
						}
					} else {
						
						atr_comp = atr_comp + atr_simp + (c_maior > 0 ? "=" : "") + lexeme;

					}
					lexeme = lexeme + ".NUM";
					c_maior = 0;
				}

				// if(pos.equals("v-fin") || pos.equals("art") ||
				// pos.equals("pron-pers") || pos.equals("pron-indp") ||
				// pos.equals("prp") || pos.equals("pron-det")){ lexeme =
				// "";lemmas="";}

				output.append(String.format("%s ", lexeme));
			}

		}

		

		int comp = atr_comp.length();
		System.out.println("atributo composto = " + comp + " qtd tabelas =" + qnt_tab);
		
		if(comp == 0 && qnt_tab == 0){
			System.out.println("Não foi possível realizar a consulta..");
			System.exit(0);
		}
	
		if (comp == 0) {
			WHERE = "";

			StringBuilder sb = new StringBuilder(atr_simp);
			int del = sb.lastIndexOf(",");
			if (del >= 0)
				sb.deleteCharAt(del);
			atr_simp = sb.toString();

			StringBuilder sb2 = new StringBuilder(tabelas);
			int del1 = sb2.lastIndexOf(",");
			if (del1 >= 0)
				sb2.deleteCharAt(del1);
			tabelas = sb2.toString();
		} else {

			StringBuilder sb = new StringBuilder(atr_simp);
			int del = sb.lastIndexOf(",");
			if (del >= 0)
				sb.deleteCharAt(del);
			atr_simp = sb.toString();

			StringBuilder sb2 = new StringBuilder(tabelas);
			int del1 = sb2.lastIndexOf(",");
			if (del1 >= 0)
				sb2.deleteCharAt(del1);
			tabelas = sb2.toString();

			StringBuilder sb3 = new StringBuilder(atr_comp);
			int del2 = sb3.indexOf(" ");
			if (del2 >= 0)
				sb3.deleteCharAt(del2);
			atr_comp = sb3.toString();
		}

		if (and > 0 && data == false)
			atr_comp = atr_comp.replace(" ", " and ");
		if (or > 0)
			atr_comp = atr_comp.replace(" ", " or ");

		String composto = atr_comp;
		if (qnt_tab == 1 && !atr_simp.equals(" ")) {

			String[] single = atr_simp.split(", ");
			atr_simp = "";
			String[] compound = atr_comp.split(" ");
			atr_comp = "";

			for (String string : compound) {
				if (string.equals("and") || string.equals("or")) {
					atr_comp = atr_comp + string + ", ";
				} else if (between == 0) {
					atr_comp = atr_comp + tabelaSinComp.trim().replaceAll(",", "") + "." + string + " ";
				} else if (between == 1 && data == false) {

					atr_comp = atr_comp + tabelaSinComp.trim().replaceAll(",", "") + "." +  Normalizer.normalize(tab_betw, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "") + " " + "BETWEEN "
							+ valor1 + " and " + valor2;
					break;
				}
			}
			if (between == 1 && data == true) {
				atr_comp = composto;
			}

			StringBuilder sb3 = new StringBuilder(atr_comp);
			int del2 = sb3.indexOf(",");
			if (del2 >= 0)
				sb3.deleteCharAt(del2);
			atr_comp = sb3.toString();

			for (String string1 : single) {
				atr_simp = atr_simp + tabelaSin.trim().replaceAll(",", "").replaceAll(", ", "") + "." + string1.trim()
						+ ", ";
			}

			StringBuilder sb = new StringBuilder(atr_simp);
			int del = sb.lastIndexOf(",");
			sb.deleteCharAt(del);
			atr_simp = sb.toString();

			atr_comp = atr_comp.replaceAll("_", " ");
			atr_comp = atr_comp.replaceAll("and,", "and");
			atr_comp = atr_comp.replaceAll("##", " ");
			atr_comp = atr_comp.replaceAll("to date", "to_date");
			
			if(comp ==0)
				atr_comp = "";
			System.out.println();
			
///////////////////////////////////////////////////////////////////////////////////////////////////////
			String map = SynonymParserTable.parse(atr_simp, false,"");
			String verifica = map;
			map = map.trim();
			
			String[] atributo = map.split(" ");
			String parser =  map.replaceAll(atributo[0]+" ", "");
			parser =  parser.replaceAll("  ", " ");
			System.out.println(parser + "--->"+atributo[0]);
			String[] arStr = parser.split(" ");
			
			parser=	SynonymParserTable.parse(parser, true,atributo[0]);
			String[] contr = parser.split(" ");
			List<String> rep = new ArrayList<>();
		
			for (int i = 0; i < contr.length; i++) {
				rep.add(contr[i]);
			}
			
			
			for (int i = 0; i < arStr.length; i++) {
				if(rep.contains(arStr[i])){
					rep.remove(arStr[i]);
				}
				
			}
			
			String pas1 = rep.toString();
			pas1 = pas1.replace("[", "");
			pas1 = pas1.replaceAll(",", "");
			pas1 = pas1.replace("]", "");
			System.out.println(pas1);
			String[] arStrr = pas1.split(" ");
			map = map.replaceAll(atributo[0]+" ", atributo[0]+"\\.");
			map = map.replaceAll("  ", ", ");
			
			atr_simp = map;
////////////////////////////////////////////////////////////////////////////////////////////////////			
	
			map = SynonymParserTable.parse(tabelas, true,"");
			tabelas = " " + map + " ";
			verifica = verifica.replaceAll(map, "");
			verifica = verifica.replaceAll("\\.", "");
			
			String[] atributos = verifica.trim().split("  ");
			for (String string : atributos) {
				listAtr.add(string.trim());
			}
///////////////////////////////////////////////////////////////////////////////////////////////////////
			if(aluno.getClass().getName().toLowerCase().contains(new String(atributo[0]).toLowerCase())){ //para cada tabela...
				int cont1 = listAtr.size();
				int cont2 = 0;
				for (String string : listAtr) {
					if(aluno.getAtributos().contains(new String(string))){
						cont2++;
					}	
				}
				if(cont2==cont1){
					for (int i = 0; i < arStr.length; i++) {
						atr_simp = atr_simp.replaceAll(arStr[i], arStrr[i]);
						atr_comp = atr_comp.replaceAll(arStr[i], arStrr[i]);
					}
					if(avg==1){
						atr_simp = atr_simp.trim();
						String complemento = "";
						complemento = "COUNT("+atr_simp+")";	
						System.out.println("\n\n"+SELECT +" "+ complemento +" "+ FROM + tabelas + WHERE + " " + atr_comp+"\n\n");
					}else{
					System.out.println("\n\n"+SELECT + " " + atr_simp + " " + FROM + tabelas + WHERE + " " + atr_comp+"\n\n");
					System.out.println(output.toString());
					}
				}else{
					System.out.println("Não foi possível realizar a consulta...");
					System.exit(0);
				}		
			}
			
			if(funcionario.getClass().getName().toLowerCase().contains(new String(atributo[0]).toLowerCase())){ //para cada tabela...
				int cont1 = listAtr.size();
				int cont2 = 0;
				for (String string : listAtr) {
					
					if(funcionario.getAtributos().contains(new String(string))){
						cont2++;
					}	
				}
				if(cont2==cont1){
					for (int i = 0; i < arStr.length; i++) {
						atr_simp = atr_simp.replaceAll(arStr[i], arStrr[i]);
						atr_comp = atr_comp.replaceAll(arStr[i], arStrr[i]);
					}
					if(avg==1){
						atr_simp = atr_simp.trim();
						String complemento = "";
						complemento = "\"AVG\"("+atr_simp+")";	
						System.out.println("\n\n"+SELECT +" "+ complemento +" "+ FROM + tabelas + WHERE + " " + atr_comp+"\n\n");
						System.out.println(output.toString());
					}else{
					System.out.println("\n\n"+SELECT + " " + atr_simp + " " + FROM + tabelas + WHERE + " " + atr_comp+"\n\n");
					System.out.println(output.toString());
					}
				}else{
					System.out.println(output.toString());
					System.out.println("Não foi possível realizar a consulta...");
					System.exit(0);
				}	
			}
				
//////////////////////////////////////////////////////////////////////////////////////////////////////
			
			

		} else {
			
			if (atr_simp.equals(" ")) { // liste todos os alunos, liste todas as
										// NFE -- 1º nome apenas
				if (table.contains(new String(tabelas.trim()))) {
					atr_simp = " " + tabelas.trim() + "." + "nome ";
				}
			}
			atr_comp = atr_comp.replaceAll("_", " ");    // remover 
			atr_comp = atr_comp.replaceAll("and,", "and");
			atr_comp = atr_comp.replaceAll("##", " ");
			
			System.out.println();

			String map = SynonymParserTable.parse(atr_simp, true,"");
			String verifica = map;
			map = map.trim();	
			System.out.println(map );
			String[] atributo = map.split(" ");
			
			map = map.replaceAll(" ", ", ");
			String[] cont = map.split(" ");
			if (cont.length > 1)
				atr_simp = map;		
///////////////////////////////////////////////////////////////////////////////
			map = SynonymParserTable.parse(tabelas, false,"");
		//	System.out.println(map);
			String[] tabs = map.split(" ");
			if(tabs.length>=1)
				tabelas = tabs[1];
			else
				tabelas = tabs[0];
			tabelas = " " + tabelas + " ";
			
			verifica = verifica.replaceAll(tabs[1]+" ", tabs[1]+"\\.");
			atr_simp = verifica;
			
			String pertence = atr_simp;
			pertence = pertence.replaceAll(tabs[1]+"\\.", "");
			pertence = pertence.trim();
			String[] atributos = pertence.split("  ");
			String atr1 ="";
			for (String string : atributos) {
				//System.out.println(string);
				atr1 = string;
				listAtr.add(string);
			}
			
			System.out.println(atr1);
			
			
			String parser =  atr1;
			String[] arStr = parser.split(" ");
			
			parser=	SynonymParserTable.parse(parser, true,atributo[0]);
			String[] contr = parser.split(" ");
			List<String> rep = new ArrayList<>();
		
			for (int i = 0; i < contr.length; i++) {
				rep.add(contr[i]);
			}
			
			
			for (int i = 0; i < arStr.length; i++) {
				if(rep.contains(arStr[i])){
					rep.remove(arStr[i]);
				}
				
			}
			
			String pas1 = rep.toString();
			pas1 = pas1.replace("[", "");
			pas1 = pas1.replaceAll(",", "");
			pas1 = pas1.replace("]", "");
			System.out.println(pas1);
			String[] arStrr = pas1.split(" ");
			
			if(aluno.getClass().getName().toLowerCase().contains(new String(tabs[1]).toLowerCase())){ //para cada tabela...
				int cont1 = listAtr.size();
				int cont2 = 0;
				for (String string : listAtr) {
					if(aluno.getAtributos().contains(new String(string))){
						cont2++;
					}	
				}
				if(cont2==cont1){
					for (int i = 0; i < arStr.length; i++) {
						atr_simp = atr_simp.replaceAll(arStr[i], arStrr[i]);
						atr_comp = atr_comp.replaceAll(arStr[i], arStrr[i]);
					}
					if(count==1){
						atr_simp = atr_simp.trim();
						//atr_simp = atr_simp.replaceAll(",", "");
						String[] quantidade = atr_simp.split(", ");
						String complemento = "";
						if(quantidade.length>0){
							complemento = "\"COUNT\"("+quantidade[0]+")";
						}else{
							complemento = "\"COUNT\"("+atr_simp+")";
						}
						System.out.println("\n\n"+SELECT +" "+ complemento +" "+ FROM + tabelas + WHERE + " " + atr_comp+"\n\n");
					}else{
					System.out.println("\n\n"+SELECT + atr_simp + FROM + tabelas + WHERE + " " + atr_comp+"\n\n");
					System.out.println(output.toString());
					}
				}else{
					System.out.println("Não foi possível realizar a consulta...");
					System.exit(0);
				}		
			}
			
			if(funcionario.getClass().getName().toLowerCase().contains(new String(tabs[1]).toLowerCase())){ //para cada tabela...
				int cont1 = listAtr.size();
				int cont2 = 0;
				for (String string : listAtr) {
					if(funcionario.getAtributos().contains(new String(string))){
						cont2++;
					}	
				}
				if(cont2==cont1){
					for (int i = 0; i < arStr.length; i++) {
						atr_simp = atr_simp.replaceAll(arStr[i], arStrr[i]);
						atr_comp = atr_comp.replaceAll(arStr[i], arStrr[i]);
					}
					if(count==1){
						atr_simp = atr_simp.trim();
						//atr_simp = atr_simp.replaceAll(",", "");
						String[] quantidade = atr_simp.split(", ");
						String complemento = "";
						if(quantidade.length>0){
							complemento = "\"COUNT\"("+quantidade[0]+")";
						}else{
							complemento = "\"COUNT\"("+atr_simp+")";
						}
						System.out.println("\n\n"+SELECT +" "+ complemento +" "+ FROM + tabelas + WHERE + " " + atr_comp+"\n\n");
					}else{
					System.out.println("\n\n"+SELECT + atr_simp + FROM + tabelas + WHERE + " " + atr_comp+"\n\n");
					System.out.println(output.toString());
					}
				}else{
					System.out.println("Não foi possível realizar a consulta...");
					System.exit(0);
				}	
			}
			
			
			
		}
	}

	public static void main(String[] args) throws IOException {
                                                                     //ainda não funciona procurar por data, apenas com between
		SynonymEngine engine = new TestSynonymEngine();
	// String query = "informe todo os funcionários com salário igual a 1000 e com nome Marcos Vinícios"; //1
		 String query = "liste o nome dos alunos e o cpf dos funcionários"; //no
		// String query = "liste todos os alunos ";
		//String query = "retorne o total de alunos";
		//String query = "nome dos alunos e idade dos alunos";
		//String query = "retone nome e idade dos alunos";
		//String query = "retone a média das idades dos alunos";
	//	String query = "retorne a media dos salários dos funcionários";
		//String query = "informe o número de alunos";
		//String query = "informe a quantidade total de funcionários";
		// String query = "liste todos os empregados";
		// String query = "liste o nome e o cpf dos alunos"; //1
		 //String query = "liste todos os funcionários"; //2-err
		 //String query = "liste todas as matrículas"; 
		// String query = "informe todo os funcionários com salário igual a 1000 e nome igual a João Silva e idade igual a 21"; //1
		//String query = "informe o nome de todo os funcionários com salário igual a 1000" e nome dos aluno com idade do aluno igual a 21"; //no
		//String query = "liste o cpf dos alunos com nome João"; //1
		// String query = "informe as notas fiscais geradas entre 20/10/2000 e
		// 30/10/2000";
	//	String query = "Retorne os funcionários com nome igual a Matheus e  do departamento de São Luis";
		//String query = "informe o nome dos funcionários nascidos entre 20/10/2000 e 30/10/2000"; //1
		 //String query = "informe o nome dos alunos com idade entre 20 e 30"; //1
		// String query = "retorne o nome dos funcionários com salário maior do que 20 e menor do que 30"; //1
		// query = AnalyzerUtils.displayTokensWithPositions(new
		// SynonymAnalyzerS3(engine), query)
		System.out.println(query + "\n");
		String str = AnalyzerUtils.displayTokensWithPositions(new SynonymAnalyzerS2(engine), query);
		System.out.println( str);
		

		Pattern pattern = Pattern
				.compile("entre(\\s)+(\\d+|\\d{2}\\/\\d{2}\\/\\d{4})(\\s)+e\\s+(\\d{2}\\/\\d{2}\\/\\d{4}|\\d+)");
		Matcher matcher = pattern.matcher(query);
		Pattern pattern1 = Pattern.compile(
				"maior\\s+(do)?\\s+que\\s(\\d+|\\d{2}\\/\\d{2}\\/\\d{4})\\s+e\\s+menor\\s+(do)?\\s+que\\s+(\\d+|\\d{2}\\/\\d{2}\\/\\d{4})");
		Matcher matcher1 = pattern1.matcher(query);

		String query2 = "";
		query2 = query.replaceAll("\\s+", " ");
		String[] tab_bet = query2.toLowerCase().split(" ");
		for (int i = 0; i < tab_bet.length; i++) {
			if (tab_bet[i].equals("entre") || tab_bet[i].equals("maior")) {
				tab_betw = tab_bet[i - 1];
				break;
			}
		}

		if (matcher.find()) {
			between = 1;
			if (matcher.group(2).matches("\\d{2}\\/\\d{2}\\/\\d{4}")) {
				data = true;
				data1 = matcher.group(2);
				data2 = matcher.group(4);
			} else {
				valor1 = matcher.group(2);
				valor2 = matcher.group(4);
			}
		} else if (matcher1.find()) {
			between = 1;
			if (matcher1.group(2).matches("\\d{2}\\/\\d{2}\\/\\d{4}")) {
				data = true;
				data1 = matcher1.group(2);
				data2 = matcher1.group(4);
			} else {
				valor1 = matcher1.group(2);
				valor2 = matcher1.group(4);
			}
		} else {
			between = 0;
		}


		String[] vecStr = query.split(" ");

		for (String string : vecStr) {
			if (string.equals("e"))
				and++;
			if (string.equals("ou"))
				or++;

		}

		Pattern pattern3 = Pattern.compile("quantidade total de|número de|total");
		Matcher matcher3 = pattern3.matcher(query);
		if(matcher3.find()) count = 1;
		
		Pattern pattern4 = Pattern.compile("média|AVG|avg|media");
		Matcher matcher4 = pattern4.matcher(query);
		if(matcher4.find()) avg = 1;
		
		ParserSQL ex = new ParserSQL();

		
		//str = str.replaceAll("me diga", "").replaceAll("infome", "").replaceAll("liste", "");
       
		ex.analyzeAndPrintDocument(str);

	}
}
