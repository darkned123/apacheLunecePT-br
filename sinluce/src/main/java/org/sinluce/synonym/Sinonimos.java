package org.sinluce.synonym;

public class Sinonimos implements Sinonimo{

	private String palavra;
	private String[] mapeamento;

	public Sinonimos() {
		// TODO Auto-generated constructor stub
	}
	public String getPalavra() {
		return palavra;
	}

	public void setPalavra(String palavra) {
		this.palavra = palavra;
	}

	public String[] getMapeamento() {
		return mapeamento;
	}

	public void setMapeamento(String[] mapeamento) {
		this.mapeamento = mapeamento;
	}

}
