# QLM OOH - Plataforma de Outdoor Digital

Java 21 + Maven backend para integração com a plataforma QLM OOH.

## Estrutura

```
src/main/java/br/com/qlmooh/
├── Main.java           # Ponto de entrada — demo de preços e campanhas
├── Campaign.java       # Modelo de campanha
├── CampaignStatus.java # Enum de status
├── PricingUtils.java   # Cálculo de preços, inserções, formatação
└── ApiClient.java      # Cliente HTTP para API backend (Flask)

src/test/java/br/com/qlmooh/
└── PricingUtilsTest.java
```

## Como rodar

```bash
# Compilar + testar + executar
./build.sh

# Apenas testes
./build.sh test
```

## Dependências

- Jackson (JSON)
- Apache HttpClient5 (HTTP)
- SLF4J (logging)
- JUnit 5 (testes)

