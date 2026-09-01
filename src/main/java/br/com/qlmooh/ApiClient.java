package br.com.qlmooh;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cliente HTTP simples para interagir com a API backend do QLM OOH.
 * <p>
 * Permite login, criação de campanhas e envio de dados ao backend Flask.
 */
public class ApiClient {
    private static final Logger log = LoggerFactory.getLogger(ApiClient.class);

    private final String baseUrl;
    private final ObjectMapper mapper;
    private String accessToken;

    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.mapper = new ObjectMapper();
    }

    /**
     * Realiza login no backend e armazena o token de acesso.
     *
     * @param email    email do usuário
     * @param password senha do usuário
     * @return token de acesso armazenado
     * @throws Exception se login falhar
     */
    public String login(String email, String password) throws Exception {
        log.info("Fazendo login como {}...", email);
        var json = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password);

        String response = Request.post(baseUrl + "/api/auth/login")
                .bodyString(json, ContentType.APPLICATION_JSON)
                .connectTimeout(Timeout.ofSeconds(5))
                .execute()
                .returnContent()
                .asString();

        var node = mapper.readTree(response);
        this.accessToken = node.get("access_token").asText();
        log.info("Login OK — token recebido ({} chars)", accessToken.length());
        return accessToken;
    }

    /** Cria uma nova campanha via API. */
    public String createCampaign(Campaign campaign) throws Exception {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalStateException("Faça login antes de criar campanhas.");
        }

        String termo = "2026-01"; // versão atual
        String json = mapper.valueToTree(campaign).toString();
        var sb = new StringBuilder(json);
        int idx = sb.lastIndexOf("}");
        sb.insert(idx, String.format(",\"termo_version\":\"%s\",\"termo_accepted\":true}", termo));

        String response = Request.post(baseUrl + "/api/campaigns")
                .addHeader("Authorization", "Bearer " + accessToken)
                .bodyString(sb.toString(), ContentType.APPLICATION_JSON)
                .connectTimeout(Timeout.ofSeconds(10))
                .execute()
                .returnContent()
                .asString();

        var node = mapper.readTree(response);
        if (node.has("error")) {
            throw new RuntimeException("Erro ao criar campanha: " + node.get("error").asText());
        }
        String code = node.get("code").asText();
        log.info("Campanha criada: {}", code);
        return code;
    }

    /** Verifica saúde do backend. */
    public boolean healthCheck() throws Exception {
        String response = Request.get(baseUrl + "/api/health")
                        .connectTimeout(Timeout.ofSeconds(3))
                        .execute()
                        .returnContent()
                        .asString();
        var node = mapper.readTree(response);
        return "ok".equals(node.get("status").asText());
    }
}
