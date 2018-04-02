package org.sinluce.synonym;

/**
 * Created by Matheus on 23/03/2017.
 */
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;

import java.io.Reader;


public class SynonymAnalyzerS extends Analyzer {
    private SynonymEngine engine;

    public SynonymAnalyzerS(SynonymEngine engine) {
        this.engine = engine;
    }

    public TokenStream tokenStream(String fieldName, Reader reader) {
        TokenStream result = new SynonymFilter(
                new StopFilter(true,
                        new LowerCaseFilter(
                                new StandardFilter(
                                        new StandardTokenizer(
                                                Version.LUCENE_30, reader))),
                        BrazilianAnalyzer.getDefaultStopSet()),
                engine
        );
        return result;
    }
}