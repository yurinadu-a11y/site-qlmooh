# QLM OOH — Java Platform

Plataforma de Outdoor Digital em Java. Cliente integrado com o backend Python (Flask)
do QLM OOH, com cálculo de preços progressivos, validação de campanhas, cliente HTTP API
e testes automatizados.

## 📋 Índice

- [Requisitos](#requisitos)
- [Como Rodar](#como-rodar)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Preços](#preços)
- [Integrações](#integrações)
- [Testes](#testes)
- [Docker](#docker)
- [CI/CD](#cicd)

## Requisitos

- **JDK 21+** (testado no JDK 26)
- **Maven 3.9+** (ou use `./build.sh` que usa javac como fallback)

## Como Rodar

```bash
# Compilar, testar e executar (via Maven)
./build.sh

# Apenas testes (falhas interrompem o build)
./build.sh test

# Build via Maven diretamente
mvn compile      # compila
mvn test         # roda 27 testes JUnit 5
mvn package      # cria uber-jar em target/
java -jar target/site-qlmooh-1.0.0.jar
```

## Estrutura do Projeto

```
src/
├── main/java/br/com/qlmooh/
│   ├── Main.java              # Ponto de entrada — demo completa
│   ├── Campaign.java          # Modelo de campanha
│   ├── CampaignStatus.java    # Enum de status (9 valores)
│   ├── PricingUtils.java      # Cálculo de preços, inserções, formatação
│   └── ApiClient.java         # Cliente HTTP para API backend (HttpClient5)
├── test/java/br/com/qlmooh/
│   ├── PricingUtilsTest.java  # 10 testes
│   ├── CampaignTest.java      # 6 testes
│   └── CampaignStatusTest.java # 11 testes
└── lib/                      # JARs de dependências (fallback javac)
```

## Preços

| Duração   | Preço/dia  | Total       |
|-----------|-----------|-------------|
| 1 dia     | R$ 99,00  | R$ 99,00    |
| 30 dias   | R$ 95,81  | R$ 2.874,30 |
| 90 dias   | R$ 89,22  | R$ 8.029,80 |
| 365 dias  | R$ 59,00  | R$ 21.535,00|

**Fórmula:** `daily = 99 - (99 - 59) * (days - 1) / 364`

**Promoção Setembro/2026:** R$ 1.200,00 para campanhas de 1-30 dias iniciando em setembro/2026.

**Inserções:** 18h/dia × 3600s/h ÷ 15s = **4.320 inserções/dia**.

## Integrações

### API Backend (Python Flask)
O cliente `ApiClient.java` se conecta ao backend em `http://localhost:5000`:
- `POST /api/auth/login` — login com email/senha, retorna JWT
- `POST /api/campaigns` — cria campanha (requer Bearer token)
- `GET /api/health` — verifica saúde do backend

### Variáveis de Ambiente
```
API_BASE_URL=http://localhost:5000  # URL do backend
```

Configure copiando `.env.example` para `.env`.

## Testes

```
Tests run: 27, Failures: 0, Errors: 0, Skipped: 0
```

- **PricingUtilsTest** (10): preços progressivos, formatação, inserções, validações
- **CampaignTest** (6): construtor, getters/setters, toString, status progression
- **CampaignStatusTest** (11): todos os 9 enums e descrições

O modelo valida duração e inserções, e o cliente HTTP valida parâmetros,
status HTTP e campos obrigatórios das respostas.

## Docker

```bash
docker build -t site-qlmooh .
docker run -it --rm -e API_BASE_URL=http://host.docker.internal:5000 site-qlmooh
```

## CI/CD

GitHub Actions (`.github/workflows/ci.yml`) faz build e testes em cada push/PR.

## Dependências

- Jackson (JSON)
- Apache HttpClient5 (HTTP)
- SLF4J (logging)
- JUnit 5 (testes)
