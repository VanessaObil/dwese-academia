
-- Crear tabla 'profesores'
CREATE TABLE profesores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE
);

-- Crear tabla 'cursos'
CREATE TABLE cursos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_curso VARCHAR(150) NOT NULL,
    descripcion TEXT,
    profesor_id BIGINT NOT NULL,
    CONSTRAINT fk_profesor FOREIGN KEY (profesor_id) REFERENCES profesores (id) ON DELETE CASCADE
);

-- Crear la tabla 'users'
CREATE TABLE IF NOT EXISTS users (
   id BIGINT PRIMARY KEY AUTO_INCREMENT,
   username VARCHAR(50) UNIQUE NOT NULL,
   password VARCHAR(100) NOT NULL,
   enabled BOOLEAN NOT NULL,
   first_name VARCHAR(50) NOT NULL,
   last_name VARCHAR(50) NOT NULL,
   image VARCHAR(255),
   created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   last_password_change_date TIMESTAMP
);

-- Crear la tabla 'roles'
CREATE TABLE IF NOT EXISTS roles (
   id BIGINT PRIMARY KEY AUTO_INCREMENT,
   name VARCHAR(50) UNIQUE NOT NULL
);

-- Crear la tabla 'user_roles'
CREATE TABLE IF NOT EXISTS user_roles (
   user_id BIGINT NOT NULL,
   role_id BIGINT NOT NULL,
   PRIMARY KEY (user_id, role_id),
   FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
   FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);