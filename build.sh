#!/bin/bash
# Script de build rápido — compila, testa e executa o projeto QLM OOH
set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$PROJECT_DIR"

# Tenta usar Maven primeiro (funciona com internet ou com artifacts pré-cacheados).
if mvn -v >/dev/null 2>&1; then
    echo "🔧 Usando Maven..."
    case "${1:-run}" in
        test)
            mvn test -B
            exit 0
            ;;
        package)
            mvn package -B
            exit 0
            ;;
    esac
    if mvn package -B -DskipTests; then
        if [ -f "target/site-qlmooh-1.0.0.jar" ]; then
            echo "✅ Build via Maven concluído"
            java -jar target/site-qlmooh-1.0.0.jar
        fi
    else
        echo "Maven falhou; usando javac..."
        CLASSPATH=$(find lib -name "*.jar" -print | tr '\n' ':')
        mkdir -p out out-test
        javac -d out -cp "$CLASSPATH" src/main/java/br/com/qlmooh/*.java
        javac -d out-test -cp "$CLASSPATH:out" src/test/java/br/com/qlmooh/*Test.java
        java -cp "out:$CLASSPATH" br.com.qlmooh.Main
    fi
else
    # Fallback: use javac directly
    echo "🔧 Usando javac (Maven não disponível)..."
    CLASSPATH=$(find lib -name "*.jar" | tr '\n' ':')
    rm -rf out out-test && mkdir -p out out-test
    javac -d out -cp "$CLASSPATH" src/main/java/br/com/qlmooh/*.java
    javac -d out-test -cp "$CLASSPATH:out" src/test/java/br/com/qlmooh/*Test.java

    if [ "$1" == "test" ]; then
        java -cp "lib/junit-platform-console-standalone-1.11.4.jar:out:out-test:$CLASSPATH" \
            org.junit.platform.console.ConsoleLauncher \
            --select-class br.com.qlmooh.PricingUtilsTest \
            --select-class br.com.qlmooh.CampaignTest \
            --select-class br.com.qlmooh.CampaignStatusTest
    else
        if [ "$1" != "test" ]; then
            java -cp "out:$CLASSPATH" br.com.qlmooh.Main
        fi
    fi
fi
