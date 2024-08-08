CREATE TABLE clientes (
   id INT PRIMARY KEY,
   nome VARCHAR(100),
   email VARCHAR(100)
);

CREATE TABLE pedidos (
   id INT PRIMARY KEY,
   id_cliente INT,
   produto VARCHAR(100),
   FOREIGN KEY (id_cliente) REFERENCES clientes(id)
);