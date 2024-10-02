INSERT INTO client(name) VALUES ('Sandra');
INSERT INTO client(name) VALUES ('Noelia');
INSERT INTO client(name) VALUES ('Lia');
INSERT INTO client(name) VALUES ('Carlos');

INSERT INTO category(name) VALUES ('Eurogames');
INSERT INTO category(name) VALUES ('Ameritrash');
INSERT INTO category(name) VALUES ('Familiar');

INSERT INTO author(name, nationality) VALUES ('Alan R. Moon', 'US');
INSERT INTO author(name, nationality) VALUES ('Vital Lacerda', 'PT');
INSERT INTO author(name, nationality) VALUES ('Simone Luciani', 'IT');
INSERT INTO author(name, nationality) VALUES ('Perepau Llistosella', 'ES');
INSERT INTO author(name, nationality) VALUES ('Michael Kiesling', 'DE');
INSERT INTO author(name, nationality) VALUES ('Phil Walker-Harding', 'US');

INSERT INTO game(title, age, category_id, author_id) VALUES ('On Mars', '14', 1, 2);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Aventureros al tren', '8', 3, 1);
INSERT INTO game(title, age, category_id, author_id) VALUES ('1920: Wall Street', '12', 1, 4);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Barrage', '14', 1, 3);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Los viajes de Marco Polo', '12', 1, 3);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Azul', '8', 3, 5);

INSERT INTO prestamo(game_id, client_id, ini_date, end_date) VALUES (1, 1, '2024-09-01', '2024-09-15');
INSERT INTO prestamo(game_id, client_id, ini_date, end_date) VALUES (2, 2, '2024-09-05', '2024-09-20');
INSERT INTO prestamo(game_id, client_id, ini_date, end_date) VALUES (3, 3, '2024-09-10', '2024-09-25');
INSERT INTO prestamo(game_id, client_id, ini_date, end_date) VALUES (4, 1, '2024-09-02', '2024-09-16');
INSERT INTO prestamo(game_id, client_id, ini_date, end_date) VALUES (5, 2, '2024-09-07', '2024-09-21');
INSERT INTO prestamo(game_id, client_id, ini_date, end_date) VALUES (6, 3, '2024-09-12', '2024-09-26');