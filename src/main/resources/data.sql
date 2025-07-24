-- Estados iniciales
INSERT INTO estados_tarea (id, descripcion) VALUES (1, 'Pendiente');
INSERT INTO estados_tarea (id, descripcion) VALUES (2, 'En progreso');
INSERT INTO estados_tarea (id, descripcion) VALUES (3, 'Completada');

-- Usuarios
INSERT INTO usuarios (nombre, email) VALUES ('Jorge López', 'jorge@nuevo.cl');
INSERT INTO usuarios (nombre, email) VALUES ('María Pérez', 'maria@nuevo.cl');

-- Tareas
INSERT INTO tareas (titulo, descripcion, fecha_limite, usuario_id, estado_id)
VALUES ('Tarea 1', 'Analizar requerimientos', '2025-07-15', 1, 1);

INSERT INTO tareas (titulo, descripcion, fecha_limite, usuario_id, estado_id)
VALUES ('Tarea 2', 'Desarrollar módulo de login', '2025-07-20', 2, 2);

-- Insertar Roles
INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');