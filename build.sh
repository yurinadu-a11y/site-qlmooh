#!/bin/bash
# Script de build rápido — compila, testa e executa o projeto QLM OOH
set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$PROJECT_DIR"

# Classpath com todas as dependências baixadas manualmente
CLASSPATH="$(find lib -name "*.jar" ! -name "junit-platform-console-standalone-*.jar" | tr '\n' ':')"

echo "🔧 Compilando..."
rm -rf out out-test
mkdir -p out out-test
javac -d out -cp "$CLASSPATH" src/main/java/br/com/qlmooh/*.java
javac -d out-test -cp "$CLASSPATH:out" src/test/java/br/com/qlmooh/*Test.java
echo "✅ Compilação OK"

if [ "$1" == "test" ]; then
    echo "🧪 Executando testes..."
    java -cp "lib/junit-platform-console-standalone-1.11.4.jar:out:out-test:$CLASSPATH" \
        org.junit.platform.console.ConsoleLauncher \
        --select-class br.com.qlmooh.PricingUtilsTest 2>&1 | grep -E "tests successful|tests failed|FAILED|PASSED"
fi

if [ "$1" == "run" ] || [ -z "$1" ]; then
    echo "🚀 Executando..."
    java -cp "out:$CLASSPATH" br.com.qlmooh.Main
fi
