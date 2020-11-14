package br.com.zup.estrelas.sistemaprefeitura.enums;

public enum Area {

	ADMINISTRACAO("administracao"), FAZENDA("fazenda"), COMUNICACAO("comunicacao"), EDUCACAO("educacao"),
	SAUDE("saude"), TRANSPORTE("transporte"),  LAZER("lazer"), MEIO_AMBIENTE("meio ambiente");
	
	private String value;
	
	 Area(String value) {
	        this.value = value;
	    }

	    public String getValue() {
	        return value;
	    }

	    @Override
	    public String toString() {
	        return this.value;
	    }

}
