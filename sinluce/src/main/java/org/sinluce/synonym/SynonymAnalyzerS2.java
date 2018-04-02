package org.sinluce.synonym;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CachingTokenFilter;
import org.apache.lucene.analysis.ISOLatin1AccentFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;

public class SynonymAnalyzerS2 extends Analyzer {
	  private SynonymEngine engine;

	    public SynonymAnalyzerS2(SynonymEngine engine) {
	        this.engine = engine;
	    }

	    public TokenStream tokenStream(String fieldName, Reader reader) {
	        TokenStream result = new SynonymFilter(
	                new StopFilter(true, //CachingTokenFilter
	                        new CachingTokenFilter(
	                                new StandardFilter(
	                                        new StandardTokenizer(
	                                                Version.LUCENE_30, reader))),
	                        BrazilianAnalyzer.getDefaultStopSet()),
	                engine
	        );
	        return result;
	    }
}
