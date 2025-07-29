@echo off
echo ========================================
echo Construyendo Sistema de Recibos...
echo ========================================

echo Limpiando proyecto anterior...
call mvn clean

echo Compilando y empaquetando...
call mvn package -DskipTests

if %errorlevel% neq 0 (
    echo Error en la construcción
    pause
    exit /b 1
)

echo ========================================
echo Construcción completada exitosamente!
echo ========================================
echo Archivo JAR generado: target/real-estate-receipts-1.0.0.jar
echo.
echo Para ejecutar:
echo java -jar target/real-estate-receipts-1.0.0.jar
echo.
pause
