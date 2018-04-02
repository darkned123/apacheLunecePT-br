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
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.util.Version;

public class SynonymAnalyzerS3 extends Analyzer {
	  private SynonymEngine engine;

	    public SynonymAnalyzerS3(SynonymEngine engine) {
	        this.engine = engine;
	    }

	    public TokenStream tokenStream(String fieldName, Reader reader) {
	        TokenStream result = new SynonymFilter(
	                new StopFilter(true, //CachingTokenFilter
	                        new ISOLatin1AccentFilter(
	                                new StandardFilter(
	                                        new StandardTokenizer(
	                                                Version.LUCENE_30, reader))),
	                        StopAnalyzer.ENGLISH_STOP_WORDS_SET),
	                engine
	        );
	        return result;
	    }
}
