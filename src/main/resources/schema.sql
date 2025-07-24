DROP TABLE IF EXISTS tareas;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS estados_tarea;

DROP TABLE IF EXISTS users;

CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE estados_tarea (
    id BIGINT PRIMARY KEY,
    descripcion VARCHAR(50) NOT NULL
);

CREATE TABLE tareas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    fecha_limite DATE,
    usuario_id BIGINT NOT NULL,
    estado_id BIGINT NOT NULL,
    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_estado FOREIGN KEY (estado_id) REFERENCES estados_tarea(id)
);

-- Tabla para la entidad RoleEntity
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Tabla para la entidad AppUserEntity
CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY, -- UUIDs se suelen mapear a VARCHAR(36)
    user_name VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id INT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);