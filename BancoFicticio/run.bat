@echo off
echo ======================================
echo Iniciando o Sistema Bancario Ficticio
echo ======================================
@echo off
echo Compilando o projeto...
call mvn clean package -DskipTests

echo Executando a aplicacao...
java -jar target\banco-ficticio-0.0.1-SNAPSHOT.jar
echo Compilando o projeto...
call mvn clean package -DskipTests

if %ERRORLEVEL% NEQ 0 (
    echo Erro na compilacao do projeto.
    pause
    exit /b %ERRORLEVEL%
)
@echo off
echo Compilando o projeto...
call mvn clean package -DskipTests

echo Executando a aplicacao...
java -jar target\banco-ficticio-0.0.1-SNAPSHOT.jar
echo.
echo Iniciando a aplicacao...
echo Acesse http://localhost:8080 no seu navegador
echo.
echo Pressione Ctrl+C para encerrar a aplicacao
echo.

call mvn spring-boot:run

pause 