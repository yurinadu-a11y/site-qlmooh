package br.com.qlmooh;

import java.text.NumberFormat;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Clock;
import java.util.Locale;

/**
 * Utilitários para preços, formatação e validação,
 * espelhando a lógica do backend Python (utils.py).
 */
public class PricingUtils {

    public static final double PRICE_MIN = 59.00;
    public static final double PRICE_MAX = 99.00;
    public static final double PRICE_SEPTEMBER_SPECIAL = 1200.00;
    public static final int MIN_CAMPAIGN_DAYS = 1;
    public static final int MAX_CAMPAIGN_DAYS = 365;
    public static final int INSERTIONS_PER_DAY = 4_320;
    private static final int PROMOTION_MAX_DAYS = 30;
    private static final int PROMOTION_YEAR = 2026;
    private static final int PROMOTION_MONTH = 9;

    /**
     * Calcula o preço por dia com interpolação linear:
     * 1 dia → R$99, 365 dias → R$59.
     */
    public static double calculateDailyPrice(int days) {
        validateDuration(days);
        if (days == MIN_CAMPAIGN_DAYS) return PRICE_MAX;
        double ratio = (double) (days - MIN_CAMPAIGN_DAYS)
                / (MAX_CAMPAIGN_DAYS - MIN_CAMPAIGN_DAYS);
        double price = PRICE_MAX - (PRICE_MAX - PRICE_MIN) * ratio;
        return roundCurrency(price);
    }

    /** Calcula o valor total da campanha. */
    public static double calculateCampaignTotal(int days) {
        double daily = calculateDailyPrice(days);
        double total = daily * days;
        return roundCurrency(total);
    }

    /**
     * Retorna o preço promocional de setembro/2026 (R$1.200,00)
     * para campanhas de 1 a 30 dias cujo início é em setembro de 2026.
     */
    public static Double getSeptemberSpecial(String startDate, int days) {
        return getSeptemberSpecial(startDate, days, Clock.systemDefaultZone());
    }

    static Double getSeptemberSpecial(String startDate, int days, Clock clock) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate today = LocalDate.now(clock);
        if (today.getMonthValue() == PROMOTION_MONTH && today.getYear() == PROMOTION_YEAR
                && start.getMonthValue() == PROMOTION_MONTH && start.getYear() == PROMOTION_YEAR
                && days >= MIN_CAMPAIGN_DAYS && days <= PROMOTION_MAX_DAYS) {
            return PRICE_SEPTEMBER_SPECIAL;
        }
        return null;
    }

    /** Formata moeda brasileira: R$ 1.200,00 */
    public static String formatCurrency(double value) {
        NumberFormat fmt = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));
        String result = fmt.format(value);
        // Java usa non-breaking space (U+00A0) entre R$ e o valor;
        // normalizamos para espaço comum para compatibilidade com o backend.
        return result.replace('\u00A0', ' ');
    }

    /**
     * Calcula datas de início/fim e inserções previstas.
     * 18h/dia × 3600s/h ÷ 15s = 4.320 inserções/dia.
     */
    public static CampaignDates calculateCampaignDates(String startDate, int days) {
        validateDuration(days);
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = start.plusDays(days);
        int totalInsertions = INSERTIONS_PER_DAY * days;
        return new CampaignDates(
            start.toString(),
            end.toString(),
            days,
            INSERTIONS_PER_DAY,
            totalInsertions
        );
    }

    private static void validateDuration(int days) {
        if (days < MIN_CAMPAIGN_DAYS || days > MAX_CAMPAIGN_DAYS) {
            throw new IllegalArgumentException(
                    "A duração deve estar entre " + MIN_CAMPAIGN_DAYS + " e "
                            + MAX_CAMPAIGN_DAYS + " dias.");
        }
    }

    private static double roundCurrency(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /** DTO simples para datas e inserções. */
    public static class CampaignDates {
        public final String startDate;
        public final String endDate;
        public final int days;
        public final int insertionsPerDay;
        public final int totalInsertions;

        public CampaignDates(String startDate, String endDate, int days,
                             int insertionsPerDay, int totalInsertions) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.days = days;
            this.insertionsPerDay = insertionsPerDay;
            this.totalInsertions = totalInsertions;
        }

        @Override
        public String toString() {
            return String.format(
                "CampaignDates{início=%s, fim=%s, dias=%d, inserções/dia=%d, total=%d}",
                startDate, endDate, days, insertionsPerDay, totalInsertions
            );
        }
    }
}
