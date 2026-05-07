INSERT INTO CINEMA(ADDRESS,CITY,NAME,POSTAL_CODE) VALUES
('Major,15', 'Tarragona', 'Oscars',43100);

INSERT INTO CINEMA(ADDRESS,CITY,NAME,POSTAL_CODE) VALUES
('Major,25', 'Tarragona', 'JCS',43100);

INSERT INTO CINEMA(ADDRESS,CITY,NAME,POSTAL_CODE) VALUES
('Major,35', 'Tarragona', 'YELMUS',43100);



INSERT INTO ROOM(NAME,CAPACITY,CINEMA_ID) VALUES ('Sala 1',100,1);
INSERT INTO ROOM(NAME,CAPACITY,CINEMA_ID) VALUES ('Sala 2',100,1);
INSERT INTO ROOM(NAME,CAPACITY,CINEMA_ID) VALUES ('Sala 3',100,1);

INSERT INTO ROOM(NAME,CAPACITY,CINEMA_ID) VALUES ('Sala 1',100,2);
INSERT INTO ROOM(NAME,CAPACITY,CINEMA_ID) VALUES ('Sala 2',100,2);
INSERT INTO ROOM(NAME,CAPACITY,CINEMA_ID) VALUES ('Sala 3',100,2);

INSERT INTO ROOM(NAME,CAPACITY,CINEMA_ID) VALUES ('Sala 1',100,3);
INSERT INTO ROOM(NAME,CAPACITY,CINEMA_ID) VALUES ('Sala 2',100,3);
INSERT INTO ROOM(NAME,CAPACITY,CINEMA_ID) VALUES ('Sala 3',100,3);

-- Movies: sense columna genre (ara es calcula automàticament com a main_genre)
INSERT INTO MOVIE(TITLE, DURATION_MINUTES, DESCRIPTION, RELEASE_DATE) VALUES
('Inception', 148, 'Un lladre que roba secrets dels somnis rep una última missió: implantar una idea.', '2010-07-16');

INSERT INTO MOVIE(TITLE, DURATION_MINUTES, DESCRIPTION, RELEASE_DATE) VALUES
('The Matrix', 136, 'Un hacker descobreix que la realitat és una simulació controlada per màquines.', '1999-03-31');

INSERT INTO MOVIE(TITLE, DURATION_MINUTES, DESCRIPTION, RELEASE_DATE) VALUES
('Interstellar', 169, 'Un grup d''astronautes viatja a través d''un forat de cuc per salvar la humanitat.', '2014-11-07');

INSERT INTO MOVIE(TITLE, DURATION_MINUTES, DESCRIPTION, RELEASE_DATE) VALUES
('Parasite', 132, 'Una família pobra s''infiltra en la vida d''una família rica amb conseqüències inesperades.', '2019-05-30');

INSERT INTO MOVIE(TITLE, DURATION_MINUTES, DESCRIPTION, RELEASE_DATE) VALUES
('The Dark Knight', 152, 'Batman s''enfronta al Joker, un criminal que vol sumir Gotham en el caos.', '2008-07-18');

INSERT INTO MOVIE(TITLE, DURATION_MINUTES, DESCRIPTION, RELEASE_DATE) VALUES
('Pulp Fiction', 154, 'Històries entrellaçades de criminals, boxejadors i gangsters a Los Angeles.', '1994-10-14');


-- Screenings per Inception (movie_id=1)
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-20T10:00:00', 8.50, 1, 1);
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-20T16:00:00', 9.00, 1, 2);
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-21T12:00:00', 8.50, 1, 4);
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-21T20:00:00', 10.00, 1, 7);
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-22T18:00:00', 9.50, 1, 5);

-- Screenings per The Matrix (movie_id=2)
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-20T11:00:00', 8.00, 2, 3);
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-20T17:30:00', 9.00, 2, 6);
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-21T10:00:00', 8.00, 2, 1);
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-21T19:00:00', 10.00, 2, 8);
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-22T15:00:00', 9.50, 2, 9);

-- Screenings per Interstellar (movie_id=3)
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-20T14:00:00', 9.00, 3, 2);
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-20T20:30:00', 10.50, 3, 5);
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-21T11:00:00', 8.50, 3, 3);
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-22T16:00:00', 9.50, 3, 7);
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-22T21:00:00', 10.50, 3, 9);

-- Screenings per Parasite (movie_id=4)
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-20T12:00:00', 8.50, 4, 4);
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-20T18:00:00', 9.50, 4, 1);
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-21T15:00:00', 9.00, 4, 6);
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-21T21:00:00', 10.50, 4, 8);
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-22T10:00:00', 8.00, 4, 2);

-- Screenings per The Dark Knight (movie_id=5)
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-20T13:00:00', 9.00, 5, 7);
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-20T19:30:00', 10.00, 5, 3);
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-21T16:00:00', 9.50, 5, 5);
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-22T12:00:00', 8.50, 5, 9);
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-22T20:00:00', 10.50, 5, 4);

-- Screenings per Pulp Fiction (movie_id=6)
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-20T15:00:00', 8.50, 6, 8);
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-20T21:00:00', 10.00, 6, 6);
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-21T13:00:00', 9.00, 6, 2);
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-21T18:00:00', 9.50, 6, 7);
INSERT INTO Screening(screening_date_time, price, movie_id, room_id) VALUES ('2026-03-22T14:00:00', 9.00, 6, 1);

-- Gèneres predefinits
INSERT INTO GENRE(NAME) VALUES ('Acció');
INSERT INTO GENRE(NAME) VALUES ('Aventura');
INSERT INTO GENRE(NAME) VALUES ('Comèdia');
INSERT INTO GENRE(NAME) VALUES ('Drama');
INSERT INTO GENRE(NAME) VALUES ('Thriller');
INSERT INTO GENRE(NAME) VALUES ('Terror');
INSERT INTO GENRE(NAME) VALUES ('Ciència Ficció');
INSERT INTO GENRE(NAME) VALUES ('Romance');
INSERT INTO GENRE(NAME) VALUES ('Animació');
INSERT INTO GENRE(NAME) VALUES ('Documental');

-- 1=Acció, 2=Aventura, 3=Comèdia, 4=Drama, 5=Thriller, 6=Terror, 7=Ciència Ficció, 8=Romance, 9=Animació, 10=Documental

-- Inception (movie_id=1) → Ciència Ficció, Thriller
INSERT INTO MOVIE_GENRES(MOVIE_ID, GENRE_ID) VALUES (1, 7);
INSERT INTO MOVIE_GENRES(MOVIE_ID, GENRE_ID) VALUES (1, 5);

-- The Matrix (movie_id=2) → Ciència Ficció, Acció
INSERT INTO MOVIE_GENRES(MOVIE_ID, GENRE_ID) VALUES (2, 7);
INSERT INTO MOVIE_GENRES(MOVIE_ID, GENRE_ID) VALUES (2, 1);

-- Interstellar (movie_id=3) → Ciència Ficció, Aventura, Drama
INSERT INTO MOVIE_GENRES(MOVIE_ID, GENRE_ID) VALUES (3, 7);
INSERT INTO MOVIE_GENRES(MOVIE_ID, GENRE_ID) VALUES (3, 2);
INSERT INTO MOVIE_GENRES(MOVIE_ID, GENRE_ID) VALUES (3, 4);

-- Parasite (movie_id=4) → Thriller, Drama
INSERT INTO MOVIE_GENRES(MOVIE_ID, GENRE_ID) VALUES (4, 5);
INSERT INTO MOVIE_GENRES(MOVIE_ID, GENRE_ID) VALUES (4, 4);

-- The Dark Knight (movie_id=5) → Acció, Thriller, Drama
INSERT INTO MOVIE_GENRES(MOVIE_ID, GENRE_ID) VALUES (5, 1);
INSERT INTO MOVIE_GENRES(MOVIE_ID, GENRE_ID) VALUES (5, 5);
INSERT INTO MOVIE_GENRES(MOVIE_ID, GENRE_ID) VALUES (5, 4);

-- Pulp Fiction (movie_id=6) → Drama, Thriller
INSERT INTO MOVIE_GENRES(MOVIE_ID, GENRE_ID) VALUES (6, 4);
INSERT INTO MOVIE_GENRES(MOVIE_ID, GENRE_ID) VALUES (6, 5);

UPDATE MOVIE SET MAIN_GENRE = 'Ciència Ficció' WHERE ID IN (1, 2, 3);
UPDATE MOVIE SET MAIN_GENRE = 'Drama'          WHERE ID IN (4, 6);
UPDATE MOVIE SET MAIN_GENRE = 'Acció'          WHERE ID = 5;