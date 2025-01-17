CREATE DATABASE IF NOT EXISTS dronepizza DEFAULT CHARACTER SET utf8;
USE dronepizza;

CREATE USER 'keaEksamen'@'localhost' IDENTIFIED BY 'keaEksamen';

GRANT ALL PRIVILEGES ON dronepizza.* TO 'keaEksamen'@'localhost';

FLUSH PRIVILEGES;