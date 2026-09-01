package br.com.qlmooh;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;
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
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalArgumentException("A URL base da API é obrigatória.");
        }
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
        if (email == null || email.isBlank() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Email e senha são obrigatórios.");
        }
        ObjectNode payload = mapper.createObjectNode()
                .put("email", email)
                .put("password", password);

        String response = execute(Request.post(baseUrl + "/api/auth/login")
                .bodyString(payload.toString(), ContentType.APPLICATION_JSON), "login");

        var node = mapper.readTree(response);
        this.accessToken = requiredText(node, "access_token", "Resposta de login sem token.");
        log.info("Login OK — token recebido ({} chars)", accessToken.length());
        return accessToken;
    }

    /** Cria uma nova campanha via API. */
    public String createCampaign(Campaign campaign) throws Exception {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalStateException("Faça login antes de criar campanhas.");
        }
        if (campaign == null) {
            throw new IllegalArgumentException("A campanha é obrigatória.");
        }

        ObjectNode payload = mapper.valueToTree(campaign);
        payload.put("termo_version", "2026-01");
        payload.put("termo_accepted", true);

        String response = execute(Request.post(baseUrl + "/api/campaigns")
                .addHeader("Authorization", "Bearer " + accessToken)
                .bodyString(payload.toString(), ContentType.APPLICATION_JSON), "criação de campanha");

        var node = mapper.readTree(response);
        String code = requiredText(node, "code", "Resposta sem código da campanha.");
        log.info("Campanha criada: {}", code);
        return code;
    }

    /** Verifica saúde do backend. */
    public boolean healthCheck() throws Exception {
        String response = execute(Request.get(baseUrl + "/api/health"), "health check");
        var node = mapper.readTree(response);
        return node.has("status") && "ok".equals(node.get("status").asText());
    }

    private String execute(Request request, String operation) throws Exception {
        ClassicHttpResponse response = (ClassicHttpResponse) request
                .connectTimeout(Timeout.ofSeconds(5))
                .responseTimeout(Timeout.ofSeconds(10))
                .execute()
                .returnResponse();
        String body = response.getEntity() == null ? "" : EntityUtils.toString(response.getEntity());
        int status = response.getCode();
        if (status < 200 || status >= 300) {
            throw new IllegalStateException("Falha na " + operation + " (HTTP " + status + "): " + body);
        }
        return body;
    }

    private String requiredText(JsonNode node,
                                String field, String message) {
        if (!node.hasNonNull(field) || node.get(field).asText().isBlank()) {
            throw new IllegalStateException(message);
        }
        return node.get(field).asText();
    }
}
