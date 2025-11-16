CREATE TABLE cliente (
    id_cliente NUMBER(8) PRIMARY KEY,
    nombre     VARCHAR2(100) NOT NULL,
    telefono   VARCHAR2(20) UNIQUE CHECK (REGEXP_LIKE(telefono, '^[0-9]+$'))
);
CREATE TABLE producto (
    id_producto NUMBER(8) PRIMARY KEY,
    nombre      VARCHAR2(100) NOT NULL,
    precio      NUMBER(10,2) CHECK (precio > 0),
    stock       NUMBER(5) DEFAULT 50 CHECK (stock >= 0)
);
CREATE TABLE pedido (
    id_pedido   NUMBER(10) PRIMARY KEY,
    fecha       DATE DEFAULT SYSDATE,
    id_cliente  NUMBER(8) NOT NULL,
    id_producto NUMBER(8) NOT NULL,
    cantidad    NUMBER(5) CHECK (cantidad > 0),
    total       NUMBER(10,2) CHECK (total >= 0),
    CONSTRAINT fk_cliente FOREIGN KEY (id_cliente)
        REFERENCES cliente (id_cliente)
        ON DELETE CASCADE,
    CONSTRAINT fk_producto FOREIGN KEY (id_producto)
        REFERENCES producto (id_producto)
        ON DELETE CASCADE
);


INSERT INTO cliente (id_cliente, nombre, telefono)
VALUES (9001, 'Carlos Pérez', '987654321');
INSERT INTO producto (id_producto, nombre, precio, stock)
VALUES (1001, 'Mouse Inalámbrico', 25.00, 30);
INSERT INTO pedido (id_pedido, id_cliente, id_producto, cantidad, total)
VALUES (seq_pedido.NEXTVAL, 9001, 1001, 2, 50.00);

UPDATE cliente
SET telefono = '999111222'
WHERE id_cliente = 9001;
UPDATE producto
SET precio = 29.90
WHERE id_producto = 1001;
UPDATE pedido
SET cantidad = 3,
    total = 75.00
WHERE id_pedido = 1;
UPDATE producto
SET stock = stock - 2
WHERE id_producto = 1001;

DELETE FROM cliente
WHERE id_cliente = 9001;
DELETE FROM producto
WHERE id_producto = 1001;
DELETE FROM pedido
WHERE id_pedido = 10;





CREATE SEQUENCE seq_pedido START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE INDEX idx_pedido_cliente ON pedido(id_cliente);
CREATE INDEX idx_pedido_producto ON pedido(id_producto);
CREATE INDEX idx_producto_nombre ON producto(nombre);

BEGIN
    INSERT INTO pedido (id_pedido, id_cliente, id_producto, cantidad, total)
    VALUES (seq_pedido.NEXTVAL, 9001, 1001, 2, 5.00);

    UPDATE producto 
    SET stock = stock - 2 
    WHERE id_producto = 1001;

    COMMIT;
END;


INSERT INTO cliente (id_cliente, nombre, telefono) VALUES (9001, 'Juan Perez', '999111222');
INSERT INTO cliente (id_cliente, nombre, telefono) VALUES (9002, 'María Gómez', '988333444');

INSERT INTO producto (id_producto, nombre, precio, stock) VALUES (1001, 'Coca-Cola 500ml', 2.50, 120);
INSERT INTO producto (id_producto, nombre, precio, stock) VALUES (1002, 'Café Americano', 1.80, 60);
INSERT INTO producto (id_producto, nombre, precio, stock) VALUES (2001, 'Sándwich', 4.50, 25);

SELECT table_name FROM user_tables WHERE table_name IN ('CLIENTE','PRODUCTO','PEDIDO');
SELECT COUNT(*) FROM cliente;
SELECT COUNT(*) FROM producto;
SELECT COUNT(*) FROM pedido;
SELECT * FROM cliente;
SELECT * FROM producto;
SELECT seq_pedido.NEXTVAL FROM dual;
SELECT * FROM pedido ORDER BY fecha DESC;
SELECT stock FROM producto WHERE id_producto = 1001;

SELECT ped.id_pedido,ped.fecha,
       c.id_cliente,
       c.nombre AS cliente,
       p.id_producto,
       p.nombre AS producto,
       ped.cantidad,
       ped.total
FROM pedido ped
JOIN cliente c ON ped.id_cliente = c.id_cliente
JOIN producto p ON ped.id_producto = p.id_producto
ORDER BY ped.fecha DESC;


SELECT p.id_producto,
       p.nombre,
       SUM(pd.cantidad) AS total_unidades_vendidas,
       SUM(pd.total) AS total_ingresos
FROM pedido pd
JOIN producto p ON pd.id_producto = p.id_producto
GROUP BY p.id_producto, p.nombre
ORDER BY total_unidades_vendidas DESC;

SELECT id_cliente,
       SUM(total) AS total_gastado,
       COUNT(*) AS num_pedidos
FROM pedido
GROUP BY id_cliente
ORDER BY total_gastado DESC;

SELECT
    MIN(precio) AS precio_minimo,
    MAX(precio) AS precio_maximo,
    AVG(precio) AS precio_promedio
FROM producto;
SELECT id_producto, AVG(cantidad) AS promedio_cantidad
FROM pedido
GROUP BY id_producto;


INSERT INTO pedido (id_pedido, id_cliente, id_producto, cantidad, total)
VALUES (seq_pedido.NEXTVAL, 9001, 1001, 2, 5.00);
UPDATE producto SET stock = stock - 2 WHERE id_producto = 1001;

COMMIT;










