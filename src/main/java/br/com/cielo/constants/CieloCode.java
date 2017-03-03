package br.com.cielo.constants;


public enum CieloCode {

	RETURN00("00","Transação autorizada"),
	RETURN01("01","Transação referida pelo banco emissor"),
	RETURN04("04","Transação não autorizad"),
	RETURN05("05","Transação não autorizada"),
	RETURN06("06","Tente novamente"),
	RETURN07("07","Cartão com restrição"),
	RETURN08("08","Código de segurança inválido"),
	RETURN11("11","Transação autorizada"),
	RETURN13("13","Valor inválido"),
	RETURN14("14","Cartão inválido"),
	RETURN15("15","Banco emissor indisponível"),
	RETURN21("21","Cancelamento não efetuado"),
	RETURN41("41","Cartão com restrição"),
	RETURN51("51","Saldo insuficiente"),
	RETURN54("54","Cartão vencido"),
	RETURN57("57","Transação não permitida"),
	RETURN60("60","Transação não autorizada"),
	RETURN62("62","Transação não autorizada"),
	RETURN78("78","Cartão não foi desbloqueado pelo portador"),
	RETURN82("82","Erro no cartão"),
	RETURN91("91","Banco fora do ar"),
	RETURN96("96","Tente novamente"),
	RETURNAA("AA","Tempo excedido"),
	RETURNAC("AC","Use função débito"),
	RETURNGA("GA","Transação referida pela Cielo"),
	RETURN001("001","Mensagem inválida"),
	RETURN002("002","Credenciais inválidas"),
	RETURN003("003","Transação inexistente"),
	RETURN008("008","Código de Segurança Inválido"),
	RETURN010("010","Inconsistência no envio do cartão"),
	RETURN011("011","Modalidade não habilitad"),
	RETURN012("012","Número de parcelas inválido"),
	RETURN013("013","Flag de autorização automática inválida"),
	RETURN014("014","Autorização Direta inválida"),
	RETURN015("015","Autorização Direta sem Cartão"),
	RETURN016("016","Identificador, TID, inválido"),
	RETURN017("017","Código de segurança ausente"),
	RETURN018("018","Indicador de código de segurança inconsistente"),
	RETURN019("019","URL de Retorno não fornecida"),
	RETURN020("020","Status não permite autorização"),
	RETURN021("021","Prazo de autorização vencido"),
	RETURN025("025","Encaminhamento a autorização não permitida"),
	RETURN030("030","Status inválido para captura"),
	RETURN031("031","Prazo de captura vencido"),
	RETURN032("032","Valor de captura inválido"),
	RETURN033("033","Falha ao capturar"),
	RETURN034("034","Valor da taxa de embarque obrigatório"),
	RETURN035("035","Bandeira inválida para utilização da Taxa de Embarque"),
	RETURN036("036","Produto inválido para utilização da Taxa de Embarque"),
	RETURN040("040","Prazo de cancelamento vencido"),
	RETURN041("041","Status não permite cancelamento"),
	RETURN042("042","Falha ao cancelar"),
	RETURN043("043","Valor de cancelamento é maior que o valor autorizado"),
	RETURN051("051","Recorrência Inválida"),
	RETURN052("052","Token Inválido"),
	RETURN053("053","Recorrência não habilitada"),
	RETURN054("054","Transação com Token inválida"),
	RETURN055("055","Número do cartão não fornecido"),
	RETURN056("056","Validade do cartão não fornecido"),
	RETURN057("057","Erro inesperado gerando Token"),
	RETURN061("061","Transação Recorrente Inválida"),
	RETURN097("097","Sistema indisponível"),
	RETURN098("098","Timeout"),
	RETURN099("099","Erro inesperado"),
	RETURN0100("0","Transação Criada"),
	RETURN0101("1","Transação em Andamento"),
	RETURN0102("2","Transação Autenticada"),
	RETURN0103("3","Transação não Autenticada"),
	RETURN0104("4","Transação Autorizada"),
	RETURN0105("5","Transação não Autorizada"),
	RETURN0106("6","Transação Capturada"),
	RETURN0107("9","Transação Cancelada"),
	RETURN0108("10","Transação em Autenticação"),
	RETURN0109("12","Transação em Cancelamento");

	CieloCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	private String code;
	private String message;

	public String getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.message;
	}

	public static String valueOfCode(String code){

		for (CieloCode cieloCode : CieloCode.class.getEnumConstants()){
			if(cieloCode.getCode().equals(code)){
				return cieloCode.getMessage();
			}
		}
		throw new IllegalArgumentException("No enum const " + CieloCode.class.getName() + " for code \'" + code
				+ "\'");

	}


}
