package br.com.qlmooh;

import java.util.Objects;

/**
 * Representa uma campanha de outdoors digitais.
 * <p>
 * Campos alinhados com o backend QLM OOH (Python Flask).
 */
public class Campaign {
    private String code;
    private int userId;
    private String name;
    private int durationDays;
    private double dailyPrice;
    private double totalPrice;
    private String startDate;
    private String endDate;
    private String videoFilename;
    private int insertionsTotal;
    private int insertionsPerDay;
    private CampaignStatus status;
    private String termoVersion;

    public Campaign() {
        this.status = CampaignStatus.DRAFT;
        this.insertionsPerDay = 4320; // 18h * 3600s / 15s
    }

    public Campaign(String code, int userId, String name, int durationDays,
                    double dailyPrice, double totalPrice, String startDate,
                    String endDate, String videoFilename) {
        this();
        this.code = code;
        this.userId = userId;
        this.name = name;
        validateDuration(durationDays);
        this.durationDays = durationDays;
        this.insertionsTotal = this.insertionsPerDay * durationDays;
        this.dailyPrice = dailyPrice;
        this.totalPrice = totalPrice;
        this.startDate = startDate;
        this.endDate = endDate;
        this.videoFilename = videoFilename;
    }

    // ── Getters & Setters ──────────────────────────────────

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getDurationDays() { return durationDays; }
    public void setDurationDays(int durationDays) {
        validateDuration(durationDays);
        this.durationDays = durationDays;
        this.insertionsTotal = this.insertionsPerDay * durationDays;
    }

    private static void validateDuration(int durationDays) {
        if (durationDays < PricingUtils.MIN_CAMPAIGN_DAYS
                || durationDays > PricingUtils.MAX_CAMPAIGN_DAYS) {
            throw new IllegalArgumentException("Duração de campanha inválida: " + durationDays);
        }
    }

    public double getDailyPrice() { return dailyPrice; }
    public void setDailyPrice(double dailyPrice) { this.dailyPrice = dailyPrice; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getVideoFilename() { return videoFilename; }
    public void setVideoFilename(String videoFilename) { this.videoFilename = videoFilename; }

    public int getInsertionsTotal() { return insertionsTotal; }
    public void setInsertionsTotal(int insertionsTotal) { this.insertionsTotal = insertionsTotal; }

    public int getInsertionsPerDay() { return insertionsPerDay; }
    public void setInsertionsPerDay(int insertionsPerDay) {
        if (insertionsPerDay <= 0) {
            throw new IllegalArgumentException("Inserções por dia devem ser maiores que zero.");
        }
        this.insertionsPerDay = insertionsPerDay;
        this.insertionsTotal = this.insertionsPerDay * this.durationDays;
    }

    public CampaignStatus getStatus() { return status; }
    public void setStatus(CampaignStatus status) {
        this.status = Objects.requireNonNull(status, "Status é obrigatório.");
    }

    public String getTermoVersion() { return termoVersion; }
    public void setTermoVersion(String termoVersion) { this.termoVersion = termoVersion; }

    @Override
    public String toString() {
        return String.format(
            "Campaign{code='%s', name='%s', dias=%d, total=%s, status=%s, inserções=%d}",
            code, name, durationDays, PricingUtils.formatCurrency(totalPrice),
            status.getDescricao(), insertionsTotal
        );
    }
}
