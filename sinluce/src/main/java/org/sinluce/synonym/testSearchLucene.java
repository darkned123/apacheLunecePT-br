package org.sinluce.synonym;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.lucene.queryParser.ParseException;

public class testSearchLucene {

	public static void main(String[] args) throws IOException, ParseException{
		SearchLucene sr = new SearchLucene();
		
		
		
		Sinonimos sin = new Sinonimos();
		
		sin.setPalavra("nfe");
		sin.setMapeamento(new String[] { "nota fiscal eletronica", "nota fiscal" });
		
		Sinonimos sin2 = new Sinonimos();
		
		sin2.setPalavra("ufma");
		sin2.setMapeamento(new String[] { "universidade federal do maranhão" });
		
		
		List<Sinonimos> sn = new ArrayList<>();
		sn.add(sin);
		sn.add(sin2);
		sr.iniciarSinonimos(sn);
		
		Documento doc1 = new Documento();
				doc1.setId(1);
				doc1.setDescricao("teste");
				doc1.setValor(	"Lorem Ipsum é nf e nota fiscal  simplesmente uma simulação de texto da indústria tipográfica e de Universidade Federal do Maranhão impressos Estados Unidos da America,"
						+ " e vem sendo utilizado desde o século XVI, quando um impressor desconhecido pegou uma bandeja de tipos e os embaralhou para fazer"
						+ " um livro de modelos de tipos. Lorem Ipsum sobreviveu não só a cinco séculos, como também ao salto para a editoração eletrônica, "
						+ "permanecendo essencialmente inalterado. Se popularizou na década de 60, quando a Letraset lançou decalques contendo passagens de Lorem Ipsum,"
						+ " e mais recentemente quando passou a ser integrado a softwares de editoração eletrônica como Aldus PageMaker Lava Jato.");
			doc1.setTipo(21);
			
		Documento doc2 = new Documento();
		doc2.setId(2);
		doc2.setDescricao("teste2");
		doc2.setValor("Esta segunda-feira (20) foi o início da ainda nota fiscal eletronica realização deste sonho, que começou com uma aula inaugural de boas vindas aos mais novos estudantes da Universidade Federal do Maranhão,"
				+ " realizada no auditório principal do Centro Pedagógico Paulo Freire. A recepção aos calouros, "
				+ "que marca o começo do adoramos primeiro semestre letivo de 2017, teve como tema “O campus também é seu! Vamos conversar?”, e foi conduzida pelo professor do curso de Turismo, Davi Andrade.");
		doc2.setTipo(2121);
			
		List<Documento> list = new ArrayList<>();
		list.add(doc1);
		list.add(doc2);
		
		sr.iniciarDocumentos(list);
		sr.readindex();
		List<Documento> documentos = sr.buscar("esta segunda-feira");
		
		for (Documento documento : documentos) {
			System.out.println(documento.getValor());
			//System.out.println(documento.getTipo());
			//System.out.println(documento.getId());
		}
		
		sr.Close();
		
	}
}
