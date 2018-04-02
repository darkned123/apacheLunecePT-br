package org.sinluce.synonym;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.queryParser.ParseException;

public interface BuscadorDocumentos {
	public void iniciarSinonimos(List<Sinonimos> lista) throws IOException;
	public void iniciarDocumentos(List<Documento> lista) throws IOException;
	public List<Documento> buscar(String texto) throws IOException, ParseException;
}
