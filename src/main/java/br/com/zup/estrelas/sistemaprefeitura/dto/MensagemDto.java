package br.com.zup.estrelas.sistemaprefeitura.dto;

public class MensagemDto {

	private String mensagem;
	
	public MensagemDto(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MensagemDto other = (MensagemDto) obj;
        if (mensagem == null) {
            if (other.mensagem != null)
                return false;
        } else if (!mensagem.equals(other.mensagem))
            return false;
        return true;
    }
}
