package br.com.qlmooh;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
 * Utilitários para preços, formatação e validação,
 * espelhando a lógica do backend Python (utils.py).
 */
public class PricingUtils {

    public static final double PRICE_MIN = 59.00;
    public static final double PRICE_MAX = 99.00;
    public static final double PRICE_SEPTEMBER_SPECIAL = 1200.00;

    /**
     * Calcula o preço por dia com interpolação linear:
     * 1 dia → R$99, 365 dias → R$59.
     */
    public static double calculateDailyPrice(int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("Número de dias deve ser maior que zero.");
        }
        if (days == 1) return PRICE_MAX;
        double ratio = (double) (days - 1) / (365 - 1);
        double price = PRICE_MAX - (PRICE_MAX - PRICE_MIN) * ratio;
        return Math.round(price * 100.0) / 100.0;
    }

    /** Calcula o valor total da campanha. */
    public static double calculateCampaignTotal(int days) {
        double daily = calculateDailyPrice(days);
        double total = daily * days;
        return Math.round(total * 100.0) / 100.0;
    }

    /**
     * Retorna o preço promocional de setembro/2026 (R$1.200,00)
     * para campanhas de 1 a 30 dias cujo início é em setembro de 2026.
     */
    public static Double getSeptemberSpecial(String startDate, int days) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate now = LocalDate.now();
        if (now.getMonthValue() == 9 && now.getYear() == 2026
                && start.getMonthValue() == 9 && start.getYear() == 2026
                && days >= 1 && days <= 30) {
            return PRICE_SEPTEMBER_SPECIAL;
        }
        return null;
    }

    /** Formata moeda brasileira: R$ 1.200,00 */
    public static String formatCurrency(double value) {
        NumberFormat fmt = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
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
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = start.plusDays(days);
        int insertionsPerDay = 18 * 3600 / 15; // 4320
        int totalInsertions = insertionsPerDay * days;
        return new CampaignDates(
            start.toString(),
            end.toString(),
            days,
            insertionsPerDay,
            totalInsertions
        );
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
