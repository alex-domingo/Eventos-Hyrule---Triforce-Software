CREATE DATABASE IF NOT EXISTS eventos_hyrule
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;
USE eventos_hyrule;

-- Tabla: evento
CREATE TABLE evento (
  codigo        VARCHAR(12)  PRIMARY KEY,
  fecha_evento  DATE         NOT NULL,
  tipo          ENUM('charla','congreso','taller','debate') NOT NULL,
  titulo        VARCHAR(200) NOT NULL,
  lugar         VARCHAR(150) NOT NULL,
  cupo          INT          NOT NULL CHECK (cupo >= 0),
  tarifa        DECIMAL(10,2) NOT NULL CHECK (tarifa >= 0),
  CONSTRAINT chk_evento_codigo CHECK (REGEXP_LIKE(codigo, '^EVT-[0-9]{8}$'))
) ENGINE=InnoDB;

-- Tabla: participante
CREATE TABLE participante (
  correo         VARCHAR(254) PRIMARY KEY,
  nombre_completo VARCHAR(60)  NOT NULL,
  tipo           ENUM('estudiante','profesional','invitado') NOT NULL,
  institucion    VARCHAR(150) NOT NULL
) ENGINE=InnoDB;

-- Tabla: inscripcion
CREATE TABLE inscripcion (
  correo        VARCHAR(254) NOT NULL,
  codigo_evento VARCHAR(12)  NOT NULL,
  tipo          ENUM('asistente','conferencista','tallerista','otro') NOT NULL,
  estado        ENUM('pendiente','validada','anulada') NOT NULL DEFAULT 'pendiente',
  creado_en     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (correo, codigo_evento),
  INDEX idx_inscripcion_evento (codigo_evento),
  CONSTRAINT fk_ins_participante FOREIGN KEY (correo) REFERENCES participante(correo),
  CONSTRAINT fk_ins_evento       FOREIGN KEY (codigo_evento) REFERENCES evento(codigo)
) ENGINE=InnoDB;

-- Tabla: pago
CREATE TABLE pago (
  id            BIGINT PRIMARY KEY AUTO_INCREMENT,
  correo        VARCHAR(254) NOT NULL,
  codigo_evento VARCHAR(12)  NOT NULL,
  metodo        ENUM('efectivo','transferencia','tarjeta') NOT NULL,
  monto         DECIMAL(10,2) NOT NULL CHECK (monto > 0),
  creado_en     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_pago_inscripcion (correo, codigo_evento),
  CONSTRAINT fk_pago_participante FOREIGN KEY (correo) REFERENCES participante(correo),
  CONSTRAINT fk_pago_evento       FOREIGN KEY (codigo_evento) REFERENCES evento(codigo)
) ENGINE=InnoDB;

-- Tabla: actividad
CREATE TABLE actividad (
  codigo            VARCHAR(12)  PRIMARY KEY,
  codigo_evento     VARCHAR(12)  NOT NULL,
  tipo              ENUM('charla','taller','debate','otra') NOT NULL,
  titulo            VARCHAR(200) NOT NULL,
  correo_instructor VARCHAR(254) NOT NULL,
  hora_inicio       TIME NOT NULL,
  hora_fin          TIME NOT NULL,
  cupo              INT  NOT NULL CHECK (cupo >= 0),
  CONSTRAINT chk_actividad_codigo CHECK (REGEXP_LIKE(codigo, '^ACT-[0-9]{8}$')),
  CONSTRAINT chk_horario CHECK (hora_inicio < hora_fin),
  INDEX idx_actividad_evento (codigo_evento),
  CONSTRAINT fk_act_evento     FOREIGN KEY (codigo_evento) REFERENCES evento(codigo),
  CONSTRAINT fk_act_instructor FOREIGN KEY (correo_instructor) REFERENCES participante(correo)
) ENGINE=InnoDB;

-- Tabla: asistencia
CREATE TABLE asistencia (
  correo          VARCHAR(254) NOT NULL,
  codigo_actividad VARCHAR(12) NOT NULL,
  asistio_en      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (correo, codigo_actividad),
  INDEX idx_asistencia_actividad (codigo_actividad),
  CONSTRAINT fk_asist_participante FOREIGN KEY (correo) REFERENCES participante(correo),
  CONSTRAINT fk_asist_actividad    FOREIGN KEY (codigo_actividad) REFERENCES actividad(codigo)
) ENGINE=InnoDB;

-- Tabla: certificado
CREATE TABLE certificado (
  id            BIGINT PRIMARY KEY AUTO_INCREMENT,
  correo        VARCHAR(254) NOT NULL,
  codigo_evento VARCHAR(12)  NOT NULL,
  ruta          VARCHAR(500) NOT NULL,
  emitido_en    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uq_certificado (correo, codigo_evento),
  CONSTRAINT fk_cert_participante FOREIGN KEY (correo) REFERENCES participante(correo),
  CONSTRAINT fk_cert_evento       FOREIGN KEY (codigo_evento) REFERENCES evento(codigo)
) ENGINE=InnoDB;