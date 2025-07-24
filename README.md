# Desafío Técnico: Gestión de Tareas con Spring Boot y Java

La empresa NUEVO SPA desea desarrollar una plataforma de gestión de tareas para mejorar la productividad de sus equipos. El sistema debe permitir a los usuarios crear, actualizar, eliminar y listar tareas. Además, se requiere autenticación mediante JWT y documentación de la API utilizando OpenAPI y Swagger.

## Objetivo:
Crear una API RESTful utilizando Spring Boot que gestione usuarios y tareas, aplicando buenas prácticas, principios SOLID y utilizando las tecnologías especificadas.

## Requisitos Técnicos:
### Java:
- Utiliza Java 17 para la implementación.
- Utiliza las características de Java 17, como lambdas y streams, cuando sea apropiado.
- Utilizar Maven como gestor de dependencias

### Spring Boot 3.4.x:
- Construye la aplicación utilizando Spring Boot 3.4.x (última versión disponible).

### Base de Datos:

- Utiliza una base de datos H2.
- Crea tres tablas: usuarios, tareas y estados_tarea.
- La tabla usuarios debe contener datos pre cargados.
- La tabla estados_tarea debe contener estados pre cargados.

### JPA:
- Implementa una capa de persistencia utilizando JPA para almacenar y recuperar las tareas.

### JWT (JSON Web Token):

- Implementa la autenticación utilizando JWT para validar usuarios.

### OpenAPI y Swagger:

- Documenta la API utilizando OpenAPI y Swagger.

## Funcionalidades:
### Autenticación:
- Implementa un endpoint para la autenticación de usuarios utilizando JWT. 

### CRUD de Tareas:
- Implementa operaciones CRUD (Crear, Leer, Actualizar, Eliminar) para las tareas.

## Consideraciones:
### Seguridad:
- Asegúrate de que las operaciones CRUD de tareas solo sean accesibles para usuarios autenticados.

### Documentación:
- Utiliza OpenAPI y Swagger para documentar claramente la API.
- Puntos adicionales si se genera el API mediante metodologia API First. Generar el archivo openapi.yml Nota: Ejemplo Plugin Maven groupId org.openapitools, artifactId openapi-generator-maven-plugin

### Código Limpio:
- Escribe código ordenado, aplicando buenas prácticas y principios SOLID.

### Creatividad
- Se espera dada la descripción del problema se creen las entidades y metodos en consecuencia a lo solicitado.

## Entregables:
### Repositorio de GitHub:
- Realiza un Pull request a este repositorio indicando tu nombre, correo y cargo al que postulas.
- Todos los PR serán rechazados, no es un indicador de la prueba.

### Documentación:
- Incluye instrucciones claras sobre cómo ejecutar y probar la aplicación.
- **Incluir Json de prueba en un archivo texto o mediante un proyecto postman** Nota: Si no va se restaran puntos de la evaluación

## Evaluación:
Se evaluará la solución en función de los siguientes criterios:

- Correcta implementación de las funcionalidades solicitadas.
- Aplicación de buenas prácticas de desarrollo, patrones de diseño y principios SOLID.
- Uso adecuado de Java 17, Spring Boot 3.4.x, H2, JWT, OpenAPI y Swagger.
- Claridad y completitud de la documentación.
- **Puntos extras si la generación de la API se realizo mediante API First**

## ----------------------
## Informacion

Para acceder a los recursos se debe crear un usuario con el API de **POST /app/auth/register** posterior se debe 
generar el Token correspondiente con el API de **POST /app/auth/login** y enviarlo en el Header de las API's 
de **/app/user** y **/app/task** en el caso de no hacerlo se generara un error.

## Implementación

Para ejecutar este proyecto, ejecuta el método principal de la clase main.

## Unit tests

Para ejecutar las pruebas unitarias se debe ejecutar la opcion "mvn clean verify" de Maven.

## Api Reference

#### Registrar un usuario para generar Token

```http
POST /app/auth/register
```
```json
{
  "userName": "string",
  "password": "string",
  "role": "string"
}
```

#### Generar Token

```http
POST /app/auth/login
```
```json
{
  "userName": "string",
  "password": "string"
}
```

#### Validar que un Token esta funcionando Correctamente

```http
GET /app/auth/check-auth
```

#### Obtener una Task por Id

```http
GET /app/task/{id}
```

| Parámetro | Tipo     | Descripción                  |
|:----------| :------- |:-----------------------------|
| `id`      | `string` | **Requerido**. Id de la Task |

#### Obtener todas las Task

```http
GET /app/task
```

#### Crear una Task

```http
POST /app/task
```
```json
{
  "title": "string",
  "description": "string",
  "limitDate": "string",
  "idUser": "integer",
  "idTaskStatus": "integer"
}
```

| Parámetro | Tipo     | Descripción                                       |
|:----------| :------- |:--------------------------------------------------|
| `title`      | `string` | **Requerido**. Titulo de la Task                  |
| `description`      | `string` | **Requerido**. Descripcion de la Task             |
| `limitDate`      | `string` | **Requerido**. Fecha Limite de la Task            |
| `idUser`      | `string` | **Requerido**. Id del Usuario relacionado a la Task |
| `idTaskStatus`      | `string` | **Requerido**. Id del Estado de la Tarea          |

#### Modificar una tarea por Id

```http
PUT /app/task/{id}
```

```json
{
  "title": "string",
  "description": "string",
  "limitDate": "string",
  "idUser": "integer",
  "idTaskStatus": "integer"
}
```

| Parámetro | Tipo     | Descripción                  |
|:----------| :------- |:-----------------------------|
| `id`      | `string` | **Requerido**. Id de la Task |

| Parámetro | Tipo     | Descripción                                         |
|:----------| :------- |:----------------------------------------------------|
| `title`      | `string` | **Opcional**. Titulo de la Task                     |
| `description`      | `string` | **Opcional**. Descripcion de la Task               |
| `limitDate`      | `string` | **Opcional**. Fecha Limite de la Task              |
| `idUser`      | `string` | **Opcional**. Id del Usuario relacionado a la Task |
| `idTaskStatus`      | `string` | **Opcional**. Id del Estado de la Tarea            |


#### Eliminar una Tarea por Id

```http
DELETE /app/task/{id}
```

| Parámetro | Tipo     | Descripción                  |
|:----------| :------- |:-----------------------------|
| `id`      | `string` | **Requerido**. Id de la Task |


## Pila Tecnológica

**Cliente:** Swagger

**Servidor:** Java, Spring Framework, Gradle, JUnit 5


- Para acceder a Swagger, utiliza: [http://localhost:8090/swagger-ui/index.html](http://localhost:8090/swagger-ui/index.html) y
- Para acceder a la base de datos, utiliza: [http://localhost:8090/h2-console/login.jsp](http://localhost:8090/h2-console/login.jsp) y utiliza el usuario **tecnova** y el password **tecnova**.
- Para acceder a la documentacion API-Doc, utiliza: [http://localhost:8090/api-docs](http://localhost:8090/api-docs).

En el paquete de recursos se encuentra el diagrama de clases.

classDiagram

    class AppUserEntity {
        + id: String
        + userName: String
        + password: String
        + role: RoleEntity
    }

    class RoleEntity {
        + id: Integer
        + name: RoleList
    }

    class TaskEntity {
        + id: Long
        + title:String 
        + description: String 
        + limitDate: LocalDate
        + userEntity: UserEntity 
        + taskStatusEntity: TaskStatusEntity
    }

    class TaskStatusEntity {
        + id: Long
        + description: String 
        + taskList: List<TaskEntity>
    }

    class UserEntity {
        + id: Long 
        + name: String
        + email: String
        + taskList: List<TaskEntity> 
    }

![Diagrama de clases](/src/main/resources/schema.png)

## Autor

- Jorge Lopez