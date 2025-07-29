# Sistema de Recibos para Inmobiliaria

## Descripción
Aplicación de escritorio Java para gestión de recibos de inmobiliaria con interfaz gráfica JavaFX y base de datos embebida H2.

## Características
- ✅ Login seguro con autenticación
- ✅ Crear nuevos recibos
- ✅ Buscar recibos por arrendatario o número
- ✅ Cambiar contraseña de usuario
- ✅ Base de datos embebida (sin instalación externa)
- ✅ Interfaz gráfica moderna y fácil de usar

## Credenciales por defecto
- **Usuario:** admin
- **Contraseña:** admin123

## Requisitos del sistema
- Java 17 o superior
- 512MB RAM mínimo
- 100MB espacio en disco

## Instalación y uso

### Opción 1: Ejecutar con Maven
```bash
mvn clean javafx:run
```

### Opción 2: Crear archivo ejecutable JAR
```bash
mvn clean package
java -jar target/real-estate-receipts-1.0.0.jar
```

### Opción 3: Crear instalador (Windows)
```bash
mvn clean package
# Usar Launch4j para crear .exe
```

## Estructura de la base de datos
- **Tabla users:** Usuarios del sistema
- **Tabla receipts:** Recibos generados

## Funcionalidades detalladas

### 1. Login
- Autenticación segura
- Usuario único por sistema

### 2. Crear recibos
- Número automático único
- Campos: arrendatario, monto, concepto, período
- Fecha automática

### 3. Buscar recibos
- Por nombre de arrendatario
- Por número de recibo
- Vista ordenada por fecha

### 4. Gestión de usuario
- Cambio de contraseña
- Cierre de sesión seguro

## Tecnologías utilizadas
- Java 17
- JavaFX 19 (Interfaz gráfica)
- Spring Boot 3.2 (Backend)
- H2 Database (Base de datos embebida)
- Maven (Gestión de dependencias)

## Soporte
Para problemas o consultas, contactar al desarrollador.
