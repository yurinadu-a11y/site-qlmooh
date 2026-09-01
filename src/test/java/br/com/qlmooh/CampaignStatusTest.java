package br.com.qlmooh;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para CampaignStatus.
 */
class CampaignStatusTest {

    @Test
    void testAllStatusesHaveDescription() {
        for (CampaignStatus status : CampaignStatus.values()) {
            assertNotNull(status.getDescricao(), "Status " + status + " deve ter descrição");
            assertFalse(status.getDescricao().isEmpty(), "Status " + status + " deve ter descrição não vazia");
        }
    }

    @Test
    void testDraftStatus() {
        assertEquals("Rascunho", CampaignStatus.DRAFT.getDescricao());
        assertEquals("DRAFT", CampaignStatus.DRAFT.getStatusCode());
    }

    @Test
    void testPendingReviewStatus() {
        assertEquals("Aguardando Aprovação", CampaignStatus.PENDING_REVIEW.getDescricao());
    }

    @Test
    void testApprovedStatus() {
        assertEquals("Aprovada", CampaignStatus.APPROVED.getDescricao());
    }

    @Test
    void testRejectedStatus() {
        assertEquals("Rejeitada", CampaignStatus.REJECTED.getDescricao());
    }

    @Test
    void testPaidStatus() {
        assertEquals("Paga", CampaignStatus.PAID.getDescricao());
    }

    @Test
    void testScheduledStatus() {
        assertEquals("Agendada", CampaignStatus.SCHEDULED.getDescricao());
    }

    @Test
    void testActiveStatus() {
        assertEquals("Ativa", CampaignStatus.ACTIVE.getDescricao());
    }

    @Test
    void testEndedStatus() {
        assertEquals("Encerrada", CampaignStatus.ENDED.getDescricao());
    }

    @Test
    void testCancelledStatus() {
        assertEquals("Cancelada", CampaignStatus.CANCELLED.getDescricao());
    }

    @Test
    void testAllStatusesCount() {
        assertEquals(9, CampaignStatus.values().length, "Deve ter 9 status");
    }
}
