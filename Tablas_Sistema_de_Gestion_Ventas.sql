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

CREATE SEQUENCE seq_pedido START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE INDEX idx_pedido_cliente ON pedido(id_cliente);
CREATE INDEX idx_pedido_producto ON pedido(id_producto);
CREATE INDEX idx_producto_nombre ON producto(nombre);



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

SELECT id_producto, nombre, precio
FROM producto
WHERE precio > (SELECT AVG(precio) FROM producto);

SELECT DISTINCT c.id_cliente, c.nombre
FROM cliente c
WHERE EXISTS (
  SELECT 1
  FROM pedido p
  WHERE p.id_cliente = c.id_cliente
    AND p.total > (
      SELECT AVG(p2.total)
      FROM pedido p2
      WHERE p2.id_cliente = c.id_cliente
    )
);

SELECT p.id_producto, p.nombre, SUM(pd.cantidad) AS total_vendido
FROM pedido pd
JOIN producto p ON pd.id_producto = p.id_producto
GROUP BY p.id_producto, p.nombre
HAVING SUM(pd.cantidad) > 5
ORDER BY total_vendido DESC;

SELECT id_cliente, total_gastado
FROM (
  SELECT id_cliente, SUM(total) AS total_gastado
  FROM pedido
  GROUP BY id_cliente
  ORDER BY total_gastado DESC
)
WHERE ROWNUM = 1;

SELECT id_producto, nombre, stock
FROM producto
WHERE stock < (SELECT AVG(stock) FROM producto);

SELECT TRUNC(fecha) AS dia,
       COUNT(*) AS pedidos_dia,
       SUM(total) AS ingresos_dia
FROM pedido
GROUP BY TRUNC(fecha)
ORDER BY dia DESC;


INSERT INTO pedido (id_pedido, id_cliente, id_producto, cantidad, total)
VALUES (seq_pedido.NEXTVAL, 9001, 1001, 2, 5.00);
UPDATE producto SET stock = stock - 2 WHERE id_producto = 1001;

COMMIT;






