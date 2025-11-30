CREATE TABLE empleado (
    id_empleado NUMBER PRIMARY KEY,
    nombre VARCHAR2(50) NOT NULL,
    apellido VARCHAR2(50) NOT NULL,
    dni VARCHAR2(8) NOT NULL UNIQUE,
    salario NUMBER(10,2) CHECK (salario >= 0)
);
CREATE SEQUENCE seq_empleado
START WITH 1
INCREMENT BY 1
NOCACHE;
CREATE OR REPLACE TRIGGER trg_empleado_id
BEFORE INSERT ON empleado
FOR EACH ROW
BEGIN
    IF :NEW.id_empleado IS NULL THEN
        SELECT seq_empleado.NEXTVAL INTO :NEW.id_empleado FROM dual;
    END IF;
END;
/
CREATE TABLE producto (
    id_producto NUMBER PRIMARY KEY,
    nombre VARCHAR2(50) NOT NULL,
    precio NUMBER(10,2) NOT NULL CHECK (precio > 0),
    stock NUMBER NOT NULL CHECK (stock >= 0)
);
CREATE SEQUENCE seq_producto
START WITH 1
INCREMENT BY 1
NOCACHE;
CREATE OR REPLACE TRIGGER trg_producto_id
BEFORE INSERT ON producto
FOR EACH ROW
BEGIN
    IF :NEW.id_producto IS NULL THEN
        SELECT seq_producto.NEXTVAL INTO :NEW.id_producto FROM dual;
    END IF;
END;
/
CREATE TABLE cliente (
    id_cliente NUMBER PRIMARY KEY,
    nombre VARCHAR2(50) NOT NULL,
    telefono VARCHAR2(20)
);
CREATE SEQUENCE seq_cliente
START WITH 1
INCREMENT BY 1
NOCACHE;
CREATE TABLE venta (
    id_venta NUMBER PRIMARY KEY,
    id_empleado NUMBER NOT NULL,
    id_cliente NUMBER NOT NULL,
    fecha DATE DEFAULT SYSDATE,
    FOREIGN KEY (id_empleado) REFERENCES empleado(id_empleado),
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
);
CREATE SEQUENCE seq_venta
START WITH 1
INCREMENT BY 1
NOCACHE;
CREATE OR REPLACE TRIGGER trg_venta_id
BEFORE INSERT ON venta
FOR EACH ROW
BEGIN
    IF :NEW.id_venta IS NULL THEN
        SELECT seq_venta.NEXTVAL INTO :NEW.id_venta FROM dual;
    END IF;
END;
/
CREATE TABLE detalle_venta (
    id_detalle NUMBER PRIMARY KEY,
    id_venta NUMBER NOT NULL,
    id_producto NUMBER NOT NULL,
    cantidad NUMBER NOT NULL CHECK (cantidad > 0),
    subtotal NUMBER NOT NULL CHECK (subtotal > 0),
    FOREIGN KEY (id_venta) REFERENCES venta(id_venta),
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
);

CREATE SEQUENCE seq_detalle_venta
START WITH 1
INCREMENT BY 1
NOCACHE;
CREATE OR REPLACE TRIGGER trg_detalle_venta_id
BEFORE INSERT ON detalle_venta
FOR EACH ROW
BEGIN
    IF :NEW.id_detalle IS NULL THEN
        SELECT seq_detalle_venta.NEXTVAL INTO :NEW.id_detalle FROM dual;
    END IF;
END;
/
CREATE OR REPLACE VIEW vista_ventas AS
SELECT
    v.id_venta,
    v.fecha,
    e.nombre AS empleado_nombre,
    e.apellido AS empleado_apellido,
    c.nombre AS cliente_nombre
FROM venta v
JOIN empleado e ON v.id_empleado = e.id_empleado
JOIN cliente c ON v.id_cliente = c.id_cliente;
CREATE OR REPLACE VIEW vista_detalle_venta AS
SELECT
    dv.id_detalle,
    dv.id_venta,
    v.fecha,
    p.nombre AS producto,
    dv.cantidad,
    dv.subtotal
FROM detalle_venta dv
JOIN venta v ON dv.id_venta = v.id_venta
JOIN producto p ON dv.id_producto = p.id_producto;
CREATE OR REPLACE VIEW vista_stock_bajo AS
SELECT *
FROM producto
WHERE stock < 10;
CREATE OR REPLACE PROCEDURE insertar_empleado (
    p_nombre IN VARCHAR2,
    p_apellido IN VARCHAR2,
    p_dni IN VARCHAR2,
    p_salario IN NUMBER
)
AS
BEGIN
    INSERT INTO empleado (nombre, apellido, dni, salario)
    VALUES (p_nombre, p_apellido, p_dni, p_salario);
END;
/
CREATE OR REPLACE PROCEDURE actualizar_empleado (
    p_id IN NUMBER,
    p_nombre IN VARCHAR2,
    p_apellido IN VARCHAR2,
    p_dni IN VARCHAR2,
    p_salario IN NUMBER
)
AS
BEGIN
    UPDATE empleado
    SET nombre = p_nombre,
        apellido = p_apellido,
        dni = p_dni,
        salario = p_salario
    WHERE id_empleado = p_id;
END;
/
CREATE OR REPLACE PROCEDURE eliminar_empleado (
    p_id IN NUMBER
)
AS
BEGIN
    DELETE FROM empleado
    WHERE id_empleado = p_id;
END;
/
CREATE OR REPLACE PROCEDURE insertar_cliente (
    p_nombre IN VARCHAR2,
    p_telefono IN VARCHAR2
)
AS
BEGIN
    INSERT INTO cliente (nombre, telefono)
    VALUES (p_nombre, p_telefono);
END;
/
CREATE OR REPLACE PROCEDURE insertar_producto (
    p_nombre IN VARCHAR2,
    p_precio IN NUMBER,
    p_stock IN NUMBER
)
AS
BEGIN
    INSERT INTO producto (nombre, precio, stock)
    VALUES (p_nombre, p_precio, p_stock);
END;
/
CREATE OR REPLACE PROCEDURE insertar_venta (
    p_id_empleado IN NUMBER,
    p_id_cliente IN NUMBER
)
AS
BEGIN
    INSERT INTO venta (id_empleado, id_cliente)
    VALUES (p_id_empleado, p_id_cliente);
END;
/
CREATE OR REPLACE PROCEDURE insertar_detalle_venta (
    p_id_venta IN NUMBER,
    p_id_producto IN NUMBER,
    p_cantidad IN NUMBER,
    p_subtotal IN NUMBER
)
AS
BEGIN
    INSERT INTO detalle_venta (id_venta, id_producto, cantidad, subtotal)
    VALUES (p_id_venta, p_id_producto, p_cantidad, p_subtotal);

    UPDATE producto
    SET stock = stock - p_cantidad
    WHERE id_producto = p_id_producto;
END;
/













