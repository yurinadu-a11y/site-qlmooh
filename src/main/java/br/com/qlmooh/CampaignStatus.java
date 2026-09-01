package br.com.qlmooh;

/**
 * Enum de status de campanha alinhado ao backend Python.
 */
public enum CampaignStatus {
    DRAFT("Rascunho", "DRAFT"),
    PENDING_REVIEW("Aguardando Aprovação", "PENDING_REVIEW"),
    APPROVED("Aprovada", "APPROVED"),
    REJECTED("Rejeitada", "REJECTED"),
    PAID("Paga", "PAID"),
    SCHEDULED("Agendada", "SCHEDULED"),
    ACTIVE("Ativa", "ACTIVE"),
    ENDED("Encerrada", "ENDED"),
    CANCELLED("Cancelada", "CANCELLED");

    private final String descricao;
    private final String statusCode;

    CampaignStatus(String descricao, String statusCode) {
        this.descricao = descricao;
        this.statusCode = statusCode;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getStatusCode() {
        return statusCode;
    }
}
