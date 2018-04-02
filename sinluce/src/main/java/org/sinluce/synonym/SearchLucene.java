package org.sinluce.synonym;

import org.sinluce.AnalyzerUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexDeletionPolicy;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.KeepOnlyLastCommitDeletionPolicy;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.document.NumericField;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SearchLucene implements BuscadorDocumentos{

	private  IndexSearcher searcher ;
	private  SynonymAnalyzer synonymAnalyzer = new SynonymAnalyzer(new TestSynonymEngine());
	private  RAMDirectory directory;
	private  IndexWriter writer;

	
	public List<Documento>  buscar(String pesquisa) throws IOException, ParseException {
		searcher = new IndexSearcher(directory, true);
		List<Documento> docReturn = new ArrayList<>();
		Documento dc;
		TestSynonymEngine engine = new TestSynonymEngine();

		String busca = pesquisa;
		String str = AnalyzerUtils.displayTokensWithPositions(new SynonymAnalyzerS(engine), busca);
		System.out.println(
				"--------------------------------------------------------------------------------------------");
		System.out.println("Pesquisa: " + pesquisa);
		System.out.println("Pesquisa real: " + str);
	
		QueryParser queryParser = new QueryParser(Version.LUCENE_30, "valor", synonymAnalyzer);
		Query query = queryParser.parse((str.isEmpty())?"z2>?=-||23":str); //alt

		TopDocs topDocs1 = searcher.search(query, 20); //nº re resultados

		System.out.println("Total de resultados: " + topDocs1.totalHits);
		ScoreDoc[] scoreDosArray1 = topDocs1.scoreDocs;
	
		for (ScoreDoc scoredoc : scoreDosArray1) {
			dc = new Documento();
		
			Document document = searcher.doc(scoredoc.doc);
			dc.setId(Integer.parseInt(document.get("id")));
			dc.setValor(document.get("valor"));
			dc.setDescricao(document.get("descricao"));
			dc.setTipo(Integer.parseInt(document.get("tipo")));
			docReturn.add(dc);
		}
		
		return docReturn;
	}

	public void iniciarDocumentos(List<Documento> doc1)
			throws IOException { 
		/**
		 * FSDirectory— An implementation of Directory that stores indexes in the actual file system. This is useful for large indices.
     	 * RAMDirectory— An implementation that stores all the indices in the memory. This is suitable for smaller indices that can be fully loaded in
     	 * memory and destroyed when the application terminates. As the index is held in memory, it is comparatively faster.
		 */
		// Directory directory = FSDirectory.open(new File("diretório/para/armazenar/indice"));
		
		directory = new RAMDirectory();

		/**
		 * IndexDeletionPolicy An interface used to implement a policy to
		 * customize deletion of stale commits from the index directory. The
		 * default deletion policy is KeepOnlyLastCommitDeletionPolicy, which
		 * keeps only the most recent commits and immediately removes all prior
		 * commits after a new commit is done.
		 */
		IndexDeletionPolicy deletionPolicy = new KeepOnlyLastCommitDeletionPolicy();
		writer = new IndexWriter(directory, synonymAnalyzer, true, deletionPolicy, // #1
				IndexWriter.MaxFieldLength.UNLIMITED);
		
		for (Documento documento : doc1) {
			
			System.out.println("Indexing doc...");
			Document doc = new Document();
			doc.add(new NumericField("id", Field.Store.YES, true).setIntValue(documento.getId()));
			doc.add(new Field("valor", documento.getValor(), Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field("descricao", documento.getDescricao(), Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new NumericField("tipo", Field.Store.YES, true).setIntValue(documento.getTipo()));
			writer.addDocument(doc);
			
		}
		
		optimize();
		closeWriter();

		//readindex();
	}

	public  void closeWriter() throws IOException {
		writer.close();
	}

	public  void Close() throws IOException {
		searcher.close();
		directory.close();
	}


	public  void optimize() throws IOException {
		writer.optimize();
	}

	public  void readindex() throws IOException {
		IndexReader indexReader = IndexReader.open(directory);

		System.out.println();
		System.out.println("Is optimized:" + indexReader.isOptimized());
		System.out.println("maxDoc:" + indexReader.maxDoc());
		System.out.println("numDocs:" + indexReader.numDocs());
		System.out.println();
		indexReader.close();
	}

	

	public void iniciarSinonimos(List<Sinonimos> sinonimos) throws IOException {
		for (Sinonimos sinonimos2 : sinonimos) {
		
			TestSynonymEngine.addSynonym(sinonimos2.getPalavra(), sinonimos2.getMapeamento());
		}
		
	}

}
