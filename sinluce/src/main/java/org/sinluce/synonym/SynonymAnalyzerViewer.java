package org.sinluce.synonym;

import java.io.IOException;
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

public class SynonymAnalyzerViewer {
	private Analyzer cogroo;

	public static int and;

	public static int or;

	public static int between;
	
	public static boolean data = false;
	
	public static	String valor1 = "";
	public static String valor2 = "";
	public static String data1 = "";
	public static String data2 = "";
	//public static String[] tab_bet;
	public static String tab_betw;


	public SynonymAnalyzerViewer() {

		ComponentFactory factory = ComponentFactory.create(new Locale("pt", "BR"));

		cogroo = factory.createPipe();
	}

	public void analyzeAndPrintDocument(String documentText) {

		// Create a document and set the text.
		Document document = new DocumentImpl();
		document.setText(documentText);

		// analyze it
		cogroo.analyze(document);

		print(document);
	}

	@SuppressWarnings("null")
	private void print(Document document) {
		StringBuilder output = new StringBuilder();

		Aluno aluno = new Aluno();
		aluno.atributos.add("nome");
		aluno.atributos.add("idade");
		aluno.atributos.add("nível");
		aluno.atributos.add("cpf");

		Funcionario funcionario = new Funcionario();
		funcionario.atributos.add("nome");
		funcionario.atributos.add("idade");
		funcionario.atributos.add("cargo");
		funcionario.atributos.add("salário");
		funcionario.atributos.add("salario");
		funcionario.atributos.add("cpf");

		List<String> al = aluno.getAtributos();
		List<String> fun = funcionario.getAtributos();
		// System.out.println(aluno.getClass().getName().toLowerCase().contains("aluno")
		// + " e " + al.contains("nome"));

		// Collection<String> atr1 = null;
		List<String> atr = new ArrayList<>();
		atr.add("nome");
		atr.add("salário");
		atr.add("salario");
		atr.add("idade");
		atr.add("cpf");
		atr.add("função");
		atr.add("nota");
		atr.add("notas fiscais");
		atr.add("contratados");
		atr.add("nascimento");

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
		table.add("NFE");

		final String SELECT = "Select";
		String WHERE = "WHERE";
		final String FROM = "FROM";
		String tabelas = " ";
		String tabelaSin = " ";
		String atr_simp = " ";
		String atr_comp = "";
		String atr_rep = "";
		String[] sp = null;
		int posi = 0;
		int c_maior = 0;
		int c_menor = 0;
		int c_igual = 0;
		int qnt_tab = 0;
	
		List<String> repTab = new ArrayList<>();

		for (Sentence sentence : document.getSentences()) {

			for (Token token : sentence.getTokens()) {
				String lexeme = token.getLexeme();

				String lemmas = Arrays.toString(token.getLemmas());

				String pos = token.getPOSTag();
				// System.out.println(lexeme+ " "+ lemmas+ " "+pos);
				
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
				if (pos.equals("n") && (atr.contains(lexeme))) {
					atr_simp = atr_simp + lexeme + ", ";
					lexeme = "ATR." + lexeme;

				}
				if (pos.equals("n") && (table.contains(lexeme))) {
					atr_rep = lexeme;
					if (!repTab.contains(atr_rep)) {

						tabelas = tabelas + lexeme + ", ";
						tabelaSin = tabelaSin + lexeme + ", ";
						lexeme = "TABLE." + lexeme;
						qnt_tab++;
						repTab.add(atr_rep);
					}

				}
				if (pos.equals("prop")) {
					sp = atr_simp.split(",");
					posi = sp.length;

					if (posi >= 0) {
						atr_comp = atr_comp + sp[posi - 2] + "=" + "\'" + lexeme + "\'";

					} else {
						atr_comp = atr_comp + atr_simp + lexeme;

					}

					lexeme = "ATR.NOME" + lexeme;
				}
				if (pos.equals("num") && between == 0) {
					sp = atr_simp.split(",");
					posi = sp.length;
					if (posi >= 0) {
						System.out.println(atr_simp);
						atr_comp = atr_comp + sp[posi-1]
								+ (c_maior > 0 ? ">" : c_menor > 0 ? "<" : c_igual > 0 ? "=" : "") + lexeme;

					} else {
						atr_comp = atr_comp + atr_simp + (c_maior > 0 ? "=" : "") + lexeme;

					}
					lexeme = lexeme + ".NUM";
					c_maior = 0;
				}
				if (pos.equals("num") && between == 1) {
					sp = atr_simp.split(",");
					posi = sp.length;
					if (posi >= 0) {
						if(data && between==1){
							System.out.println("if");
						//	atr_comp = atr_comp + sp[posi-1] + "BETWEEN " + lexeme;
							atr_comp =atr_comp + tabelaSin.trim().replaceAll(",", "") + "." + tab_betw  +"##BETWEEN##"  +"\'"+ data1+"\'" +"##and##" + "\'"+data2+"\'";
							break;
						}else{
							if(valor1 == lexeme || valor2==lexeme) continue;
						
							atr_comp = atr_simp +"BETWEEN"  + valor1 + " " + valor2;
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

		////

		// System.out.println(aluno.getClass().getName().toLowerCase().contains("aluno")
		// + " e " +al.contains("nome") );

		String[] simples = atr_simp.replaceAll(",", "").split(" ");
		String[] tab = tabelas.replaceAll(",", "").split(" ");
		int pos = 0;
		int pesoAl = 0;
		int pesoFun = 0;

		for (String string : simples) {
			// System.out.print(string);
			if (al.contains(string)) {
				pesoAl++;
			}
			if (fun.contains(string)) {
				pesoFun++;
			}

			// System.out.println("peso aluno "+pesoAl+" - peso funcionário
			// "+pesoFun);
		}

		////

		int comp = atr_comp.length();
		System.out.println("atributo composto = " + comp + " qtd tabelas =" + qnt_tab);
		if (comp == 0) {
			WHERE = "";

			StringBuilder sb = new StringBuilder(atr_simp);
			int del = sb.lastIndexOf(",");
			if (del >= 0)
				sb.deleteCharAt(del);
			atr_simp = sb.toString();

			StringBuilder sb2 = new StringBuilder(tabelas);
			int del1 = sb2.lastIndexOf(",");
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

		if (and > 0 && data==false )
			atr_comp = atr_comp.replace(" ", " and ");
		if (or > 0)
			atr_comp = atr_comp.replace(" ", " or ");

		if (qnt_tab == 1 && !atr_simp.equals(" ")) {
			String[] single = atr_simp.split(", ");
			atr_simp = "";
			String[] compound = atr_comp.split(" ");
			atr_comp = "";

			for (String string : compound) {
				if (string.equals("and") || string.equals("or")) {
					atr_comp = atr_comp + string + ", ";
				} else if(between == 0) {
					atr_comp = atr_comp + tabelaSin.trim().replaceAll(",", "") + "." + string + " ";
				}else if(between == 1 && data == false){
					System.out.println("aqui");
					atr_comp =atr_comp + tabelaSin.trim().replaceAll(",", "") + "." + tab_betw + " " +"BETWEEN "  + valor1 + " and " + valor2;
					break;
				}
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
			System.out.println(SELECT + " " + atr_simp + " " + FROM + tabelas + WHERE + " " + atr_comp);
			System.out.println(output.toString());

		} else {
			// atr_comp = atr_comp.replaceAll("_", " ");
			if (atr_simp.equals(" ")) {
				if (table.contains(new String(tabelas.trim()))) {
					atr_simp = " " + tabelas.trim() + "." + "nome ";
				}
			}
			atr_comp = atr_comp.replaceAll("_", " ");
			atr_comp = atr_comp.replaceAll("and,", "and");
			atr_comp = atr_comp.replaceAll("##", " ");
			System.out.println(SELECT + atr_simp + FROM + tabelas + WHERE + " " + atr_comp);
			System.out.println(output.toString());
		}
	}

	public static void main(String[] args) throws IOException {

		SynonymEngine engine = new TestSynonymEngine();
		 //String query = "informe todo os funcionários com salário igual do que 1000 e com nome Marcos Vinícios";
		// String query = "liste o nome dos alunos e o cpf dos funcionários";
		// String query = "liste todos os alunos ";
		// String query = "liste todos os funcionários";
		 String query = "informe todo os funcionários com salário igual a 1000 e nome igual a João Silva e idade igual a 21";
		// String query = "informe todo os funcionários com salário igual a 1000";
		// e nome dos aluno com idade do aluno igual a 21";
	// String query = "liste o cpf dos alunos com nome João";
		//String query = "informe as notas fiscais geradas entre 20/10/2000 e 30/10/2000";
		//String query = "informe umas funcionários nascidos entre 20/10/2000 e 30/10/2000";
		//String query = "informe o nome dos alunos com salário entre  20  e 30";
	//	String query = "informe o nome dos funcionario com idade maior do que 20 e menor do que 30";
		query = AnalyzerUtils.displayTokensWithPositions(new SynonymAnalyzerS3(engine), query);
		System.out.println(query + "\n");
		String str = AnalyzerUtils.displayTokensWithPositions(new SynonymAnalyzerS2(engine), query);
		System.out.println(str);

		Pattern pattern = Pattern
				.compile("entre(\\s)+(\\d+|\\d{2}\\/\\d{2}\\/\\d{4})(\\s)+e\\s+(\\d{2}\\/\\d{2}\\/\\d{4}|\\d+)");
		Matcher matcher = pattern.matcher(query);
		Pattern pattern1 = Pattern.compile(
				"maior\\s+(do)?\\s+que\\s(\\d+|\\d{2}\\/\\d{2}\\/\\d{4})\\s+e\\s+menor\\s+(do)?\\s+que\\s+(\\d+|\\d{2}\\/\\d{2}\\/\\d{4})");
		Matcher matcher1 = pattern1.matcher(query);
		String[] bet = null;
		String query2="";
		query2 =  query.replaceAll("\\s+", " ");
		String[] tab_bet  = query2.toLowerCase().split(" ");
		for (int i=0; i<tab_bet.length; i++) {
			if(tab_bet[i].equals("entre") || tab_bet[i].equals("maior")){
				tab_betw = tab_bet[i-1];
				break;
			}
		}
		
		if (matcher.find()){
			between = 1;
				if(matcher.group(2).matches("\\d{2}\\/\\d{2}\\/\\d{4}")){
					System.out.println("É data");
					data = true;
					data1= matcher.group(2);
					data2=matcher.group(4);
				}else{
					System.out.println("É valor");
					valor1= matcher.group(2);
					valor2=matcher.group(4);
				}
			}else if(matcher1.find()){
				between = 1;
				if(matcher1.group(2).matches("\\d{2}\\/\\d{2}\\/\\d{4}")){
					System.out.println("É data");
					data = true;
					data1= matcher1.group(2);
					data2=matcher1.group(4);
				}else{
					System.out.println("É valor");
					valor1= matcher1.group(2);
					valor2=matcher1.group(4);
				}
			} else {
			between = 0;
		    }
		
		System.out.println(between);

		String[] vecStr = query.split(" ");

		for (String string : vecStr) {
			if (string.equals("e"))
				and++;
			if (string.equals("ou"))
				or++;

		}

		SynonymAnalyzerViewer ex = new SynonymAnalyzerViewer();

		Scanner kb = new Scanner(System.in);

		// input = input.toLowerCase();
		// input.replaceAll("com", "");
		str = str.replaceAll("me diga", "").replaceAll("infome", "").replaceAll("liste", "");
		// input.replaceAll("do", "");

		ex.analyzeAndPrintDocument(str);

	}}

/**
 * SELECT DEPARTMENTS.DEPARTMENT_NAME, EMPLOYEES.FIRST_NAME FROM DEPARTMENTS
 * join EMPLOYEES on (EMPLOYEES.DEPARTMENT_ID = DEPARTMENTS.DEPARTMENT_ID) WHERE
 * EMPLOYEES.FIRST_NAME='Neena';
 */