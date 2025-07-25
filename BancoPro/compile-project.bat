@echo off
rem Script para compilar o projeto BancoPro com configuracoes de encoding

echo Compilando o projeto BancoPro...

rem Configurar a codificacao UTF-8 para o console
chcp 65001

rem Configurar variavel de ambiente para encoding
set JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8

rem Limpar e instalar o projeto
call mvnw.cmd clean package -DskipTests -Dproject.build.sourceEncoding=UTF-8 -Dproject.reporting.outputEncoding=UTF-8

rem Verificar se a compilacao teve sucesso
if %ERRORLEVEL% == 0 (
    echo.
    echo Compilacao concluida com sucesso!
    echo Para executar o projeto, use: mvnw.cmd spring-boot:run
) else (
    echo.
    echo Erro na compilacao do projeto.
)

pause
