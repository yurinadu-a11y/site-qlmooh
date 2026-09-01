package br.com.qlmooh;

import org.junit.jupiter.api.Test;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para PricingUtils.
 */
class PricingUtilsTest {

    @Test
    void testDailyPrice1Day() {
        assertEquals(99.00, PricingUtils.calculateDailyPrice(1), 0.001);
    }

    @Test
    void testDailyPrice365Days() {
        assertEquals(59.00, PricingUtils.calculateDailyPrice(365), 0.001);
    }

    @Test
    void testDailyPrice30Days() {
        assertEquals(95.81, PricingUtils.calculateDailyPrice(30), 0.001);
    }

    @Test
    void testDailyPriceDecreasing() {
        double prev = PricingUtils.calculateDailyPrice(1);
        for (int d = 2; d <= 365; d++) {
            double curr = PricingUtils.calculateDailyPrice(d);
            assertTrue(curr <= prev, "Preço deveria ser não-crescente: " + d);
            prev = curr;
        }
    }

    @Test
    void testTotalPrice1Day() {
        assertEquals(99.00, PricingUtils.calculateCampaignTotal(1), 0.001);
    }

    @Test
    void testTotalPrice365Days() {
        assertEquals(21535.00, PricingUtils.calculateCampaignTotal(365), 0.01);
    }

    @Test
    void testTotalPrice30Days() {
        assertEquals(2874.30, PricingUtils.calculateCampaignTotal(30), 0.001);
    }

    @Test
    void testFormatCurrency() {
        assertEquals("R$ 1.200,00", PricingUtils.formatCurrency(1200.00));
        assertEquals("R$ 99,00", PricingUtils.formatCurrency(99.00));
    }

    @Test
    void testInsertions30Days() {
        var dates = PricingUtils.calculateCampaignDates("2026-09-01", 30);
        assertEquals(4320, dates.insertionsPerDay);
        assertEquals(129600, dates.totalInsertions);
    }

    @Test
    void testInvalidDaysThrows() {
        assertThrows(IllegalArgumentException.class, () -> PricingUtils.calculateDailyPrice(0));
        assertThrows(IllegalArgumentException.class, () -> PricingUtils.calculateDailyPrice(366));
        assertThrows(IllegalArgumentException.class,
                () -> PricingUtils.calculateCampaignDates("2026-09-01", 0));
    }

    @Test
    void testSeptemberSpecialUsesCampaignAndCurrentDate() {
        Clock september = Clock.fixed(
                Instant.parse("2026-09-10T12:00:00Z"), ZoneOffset.UTC);
        assertEquals(1200.00,
                PricingUtils.getSeptemberSpecial("2026-09-15", 30, september));
        assertNull(PricingUtils.getSeptemberSpecial("2026-10-01", 30, september));
        assertNull(PricingUtils.getSeptemberSpecial("2026-09-15", 31, september));
    }
}
