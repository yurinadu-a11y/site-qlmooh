package br.com.qlmooh;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe Campaign.
 */
class CampaignTest {

    @Test
    void testDefaultConstructor() {
        Campaign c = new Campaign();
        assertEquals(CampaignStatus.DRAFT, c.getStatus(), "Status padrão deve ser DRAFT");
        assertEquals(4320, c.getInsertionsPerDay(), "Inserções/dia padrão deve ser 4320");
        assertNull(c.getCode(), "Code deve ser null por padrão");
    }

    @Test
    void testFullConstructor() {
        Campaign c = new Campaign(
            "QLM-TEST-001", 1, "Campanha Teste", 30,
            95.81, 2874.30, "2026-09-01", "2026-10-01", "video.mp4"
        );
        assertEquals("QLM-TEST-001", c.getCode());
        assertEquals(1, c.getUserId());
        assertEquals("Campanha Teste", c.getName());
        assertEquals(30, c.getDurationDays());
        assertEquals(95.81, c.getDailyPrice(), 0.001);
        assertEquals(2874.30, c.getTotalPrice(), 0.001);
        assertEquals("2026-09-01", c.getStartDate());
        assertEquals("2026-10-01", c.getEndDate());
        assertEquals("video.mp4", c.getVideoFilename());
        assertEquals(129600, c.getInsertionsTotal());
        assertEquals(CampaignStatus.DRAFT, c.getStatus());
    }

    @Test
    void testInsertionsCalculation() {
        Campaign c = new Campaign();
        c.setDurationDays(365);
        c.setInsertionsTotal(c.getInsertionsPerDay() * c.getDurationDays());
        assertEquals(4320 * 365, c.getInsertionsTotal());
    }

    @Test
    void testCampaignInvariants() {
        Campaign c = new Campaign();
        assertThrows(IllegalArgumentException.class, () -> c.setDurationDays(366));
        assertThrows(IllegalArgumentException.class, () -> c.setInsertionsPerDay(0));
        assertThrows(NullPointerException.class, () -> c.setStatus(null));
        c.setDurationDays(10);
        assertEquals(43_200, c.getInsertionsTotal());
    }

    @Test
    void testGettersAndSetters() {
        Campaign c = new Campaign();
        c.setCode("ABC-123");
        c.setUserId(42);
        c.setName("Test");
        c.setDurationDays(60);
        c.setDailyPrice(75.00);
        c.setTotalPrice(4500.00);
        c.setStartDate("2026-01-01");
        c.setEndDate("2026-03-01");
        c.setVideoFilename("test.mp4");
        c.setInsertionsTotal(259200);
        c.setInsertionsPerDay(4320);
        c.setStatus(CampaignStatus.APPROVED);
        c.setTermoVersion("2026-01");

        assertEquals("ABC-123", c.getCode());
        assertEquals(42, c.getUserId());
        assertEquals("Test", c.getName());
        assertEquals(60, c.getDurationDays());
        assertEquals(75.00, c.getDailyPrice(), 0.001);
        assertEquals(4500.00, c.getTotalPrice(), 0.001);
        assertEquals("2026-01-01", c.getStartDate());
        assertEquals("2026-03-01", c.getEndDate());
        assertEquals("test.mp4", c.getVideoFilename());
        assertEquals(259200, c.getInsertionsTotal());
        assertEquals(4320, c.getInsertionsPerDay());
        assertEquals(CampaignStatus.APPROVED, c.getStatus());
        assertEquals("2026-01", c.getTermoVersion());
    }

    @Test
    void testToStringContainsFields() {
        Campaign c = new Campaign("QLM-001", 1, "Demo", 30,
                95.81, 2874.30, "2026-09-01", "2026-10-01", "demo.mp4");
        String s = c.toString();
        assertTrue(s.contains("QLM-001"), "toString deve conter o code");
        assertTrue(s.contains("Demo"), "toString deve conter o nome");
        assertTrue(s.contains("R$ 2.874,30"), "toString deve conter o total formatado");
        assertTrue(s.contains("129600"), "toString deve conter inserções");
    }

    @Test
    void testStatusProgression() {
        Campaign c = new Campaign();
        c.setStatus(CampaignStatus.DRAFT);
        c.setStatus(CampaignStatus.PENDING_REVIEW);
        assertEquals(CampaignStatus.PENDING_REVIEW, c.getStatus());
        c.setStatus(CampaignStatus.APPROVED);
        assertEquals(CampaignStatus.APPROVED, c.getStatus());
        c.setStatus(CampaignStatus.PAID);
        assertEquals(CampaignStatus.PAID, c.getStatus());
        c.setStatus(CampaignStatus.ACTIVE);
        assertEquals(CampaignStatus.ACTIVE, c.getStatus());
        c.setStatus(CampaignStatus.ENDED);
        assertEquals(CampaignStatus.ENDED, c.getStatus());
    }
}
