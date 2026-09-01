package br.com.qlmooh;

/**
 * QLM OOH — Plataforma de Outdoor Digital
 * <p>
 * Ponto de entrada principal. Executa demonstrações de:
 * - Cálculo de preços progressivos (R$99 → R$59/dia)
 * - Cálculo de inserções (4.320/dia × N dias)
 * - Formatação de moeda brasileira
 * - Simulação de criação de campanha
 * - Cliente HTTP para API backend
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== QLM OOH ===");
        System.out.println("Plataforma de Outdoor Digital\n");

        demoPricing();
        System.out.println();

        demoCampaign();
        System.out.println();

        demoApiClient();
        System.out.println("\n✅ Demonstração concluída.");
    }

    /** Mostra a tabela de preços progressivos. */
    private static void demoPricing() {
        System.out.println("--- Tabela de Preços Progressivos ---");
        System.out.printf("1 dia:    %s/dia → %s total%n",
                PricingUtils.formatCurrency(PricingUtils.calculateDailyPrice(1)),
                PricingUtils.formatCurrency(PricingUtils.calculateCampaignTotal(1)));
        System.out.printf("30 dias:  %s/dia → %s total%n",
                PricingUtils.formatCurrency(PricingUtils.calculateDailyPrice(30)),
                PricingUtils.formatCurrency(PricingUtils.calculateCampaignTotal(30)));
        System.out.printf("90 dias:  %s/dia → %s total%n",
                PricingUtils.formatCurrency(PricingUtils.calculateDailyPrice(90)),
                PricingUtils.formatCurrency(PricingUtils.calculateCampaignTotal(90)));
        System.out.printf("365 dias: %s/dia → %s total%n",
                PricingUtils.formatCurrency(PricingUtils.calculateDailyPrice(365)),
                PricingUtils.formatCurrency(PricingUtils.calculateCampaignTotal(365)));
    }

    /** Cria e exibe uma campanha de exemplo. */
    private static void demoCampaign() {
        System.out.println("--- Campanha de Exemplo ---");
        var dates = PricingUtils.calculateCampaignDates("2026-09-01", 30);
        System.out.println(dates);

        var total = PricingUtils.calculateCampaignTotal(30);
        var sep = PricingUtils.getSeptemberSpecial("2026-09-01", 30);
        if (sep != null) {
            total = sep;
            System.out.println("Promoção de setembro aplicada: " + PricingUtils.formatCurrency(sep));
        }

        var campaign = new Campaign(
                "QLM-DEMO-001", 1, "Campanha Demo", 30,
                PricingUtils.calculateDailyPrice(30), total,
                dates.startDate, dates.endDate, "demo_video.mp4"
        );
        campaign.setTermoVersion("2026-01");
        campaign.setStatus(CampaignStatus.APPROVED);
        System.out.println(campaign);
    }

    /** Testa conexão com o backend (se disponível). */
    private static void demoApiClient() {
        System.out.println("--- Cliente API (backend) ---");
        String backendUrl = System.getenv().getOrDefault("API_BASE_URL", "http://localhost:5000");
        var client = new ApiClient(backendUrl);

        try {
            if (client.healthCheck()) {
                System.out.println("✅ Backend online em " + backendUrl);
            } else {
                System.out.println("⚠️  Backend respondeu, mas status não é 'ok'");
            }
        } catch (Exception e) {
            System.out.println("ℹ️  Backend offline (esperado se não estiver rodando): " + e.getMessage());
            System.out.println("   Inicie o backend com: cd backend && python3 app.py");
        }
    }
}
