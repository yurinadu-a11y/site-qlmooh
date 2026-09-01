package br.com.qlmooh;

/**
 * Enum de status de campanha alinhado ao backend Python.
 */
public enum CampaignStatus {
    DRAFT("Rascunho"),
    PENDING_REVIEW("Aguardando Aprovação"),
    APPROVED("Aprovada"),
    REJECTED("Rejeitada"),
    PAID("Paga"),
    SCHEDULED("Agendada"),
    ACTIVE("Ativa"),
    ENDED("Encerrada"),
    CANCELLED("Cancelada");

    private final String descricao;

    CampaignStatus(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
