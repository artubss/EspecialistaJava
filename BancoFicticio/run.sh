#!/bin/bash
#!/bin/bash

# Compilar o projeto
echo "Compilando o projeto..."
mvn clean package -DskipTests

# Executar a aplicação
echo "Executando a aplicação..."
java -jar target/banco-ficticio-0.0.1-SNAPSHOT.jar
echo "======================================"
echo "Iniciando o Sistema Bancario Ficticio"
echo "======================================"

echo "Compilando o projeto..."
mvn clean package -DskipTests
#!/bin/bash

# Compilar o projeto
echo "Compilando o projeto..."
mvn clean package -DskipTests

# Executar a aplicação
echo "Executando a aplicação..."
java -jar target/banco-ficticio-0.0.1-SNAPSHOT.jar
if [ $? -ne 0 ]; then
    echo "Erro na compilação do projeto."
    exit 1
fi

echo
echo "Iniciando a aplicação..."
echo "Acesse http://localhost:8080 no seu navegador"
echo
echo "Pressione Ctrl+C para encerrar a aplicação"
echo

mvn spring-boot:run 