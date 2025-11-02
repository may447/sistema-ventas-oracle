CREATE TABLE cliente (
id_cliente NUMBER PRIMARY KEY,
nombre VARCHAR2(150) NOT NULL,
telefono VARCHAR2(20)
);

CREATE TABLE producto (
id_producto NUMBER PRIMARY KEY,
nombre VARCHAR2(150) NOT NULL,
precio NUMBER(10,2) NOT NULL
);


CREATE TABLE pedido (
id_pedido NUMBER PRIMARY KEY,
fecha DATE DEFAULT SYSDATE,
id_cliente NUMBER REFERENCES cliente(id_cliente),
id_producto NUMBER REFERENCES producto(id_producto),
cantidad NUMBER NOT NULL,
total NUMBER(12,2)
);

COMMIT;



INSERT INTO cliente (nombre, telefono) VALUES ('Luis García', '987112233');
INSERT INTO cliente (nombre, telefono) VALUES ('Ana Torres', '912345678');
INSERT INTO cliente (nombre, telefono) VALUES ('Juan Pérez', '987654321');

INSERT INTO producto (nombre, precio) VALUES ('Coca-Cola 500ml', 3.50);
INSERT INTO producto (nombre, precio) VALUES ('Hamburguesa', 12.00);
INSERT INTO producto (nombre, precio) VALUES ('Papas Fritas', 3.50);


INSERT INTO pedido (id_cliente, id_producto, cantidad, total) VALUES (3, 3, 2, 10.50);
INSERT INTO pedido (id_cliente, id_producto, cantidad, total) VALUES (4, 4, 2, 24.00);
INSERT INTO pedido (id_cliente, id_producto, cantidad, total) VALUES (5, 5, 1, 3.50);


