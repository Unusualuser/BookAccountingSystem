SET client_encoding TO 'utf8';

-- insert into "book"
INSERT INTO public.book(book_id, name, publication_year, author, description) VALUES
    (nextval('book_book_id_seq'), '451 градус по Фаренгейту', 1953, 'Рэй Дуглас Брэдбери', 'Антиутопия'),
    (nextval('book_book_id_seq'), 'Процесс', 1925, 'Франц Кафка', 'Интересная книга'),
    (nextval('book_book_id_seq'), 'Война и мир', 1869, 'Лев Николаевич Толстой', '4 тома'),
    (nextval('book_book_id_seq'), 'Горе от ума', 1825, 'Александр Сергеевич Грибоедов', 'Интересная книга');

-- update quantity in "book_storage"
UPDATE public.book_storage
    SET quantity = 3
    WHERE book_id = 1;

UPDATE public.book_storage
SET quantity = 4
WHERE book_id = 2;

UPDATE public.book_storage
SET quantity = 5
WHERE book_id = 3;

-- insert into "user"
INSERT INTO public."user"(user_id, name, address, phone_number, email, login, password, role) VALUES
    (nextval('user_user_id_seq'), 'Анисимов Андрей Андреевич', 'Москва, Ломоносовский проспект, 3', '+79998883344', 'anisimov.andrew.hi@gmail.com', 'admin', '$2a$10$Dp3iWGVvfENU18bOX/ytteayaXWyV5lFz663G3J3JszYTkBtygH9W', 'ROLE_ADMIN'),
    (nextval('user_user_id_seq'), 'Лежебоков Даниил Артурович', 'Москва, Ленинградский проспект, 2', '+79997772255', 'daniil@mail.ru', 'user', '$2a$10$ISUU4IuSmuAhtqVvcbBXAe/IlJrm3Fe7KuJMdJAVsRlgSjGLHqpnK', 'ROLE_USER'),
    (nextval('user_user_id_seq'), 'Рукастик Олег Борисович', 'Москва, Мичуринский проспект, 1', '+79996661166', 'oleg@mail.ru', 'moder', '$2a$10$Jet5PHluLqXTvBIbrLBgBuKPJWwKHBTaHdpLOMrFHfjNhmj4nvu1K', 'ROLE_MODERATOR');

-- insert into "book_history"
INSERT INTO public.book_history(book_history_id, book_id, user_id, rental_date, return_deadline_date, return_date) VALUES
    (nextval('book_history_book_history_id_seq'), 1, 1, '2022-07-11', '2022-09-11', '2022-08-11'),
    (nextval('book_history_book_history_id_seq'), 1, 2, '2022-06-15', '2022-07-15', '2022-08-10'),
    (nextval('book_history_book_history_id_seq'), 1, 3, '2022-05-10', '2022-07-10', NULL),
    (nextval('book_history_book_history_id_seq'), 2, 3, '2022-04-28', '2022-10-28', '2022-07-15'),
    (nextval('book_history_book_history_id_seq'), 3, 2, '2022-03-02', '2022-08-02', '2022-05-18'),
    (nextval('book_history_book_history_id_seq'), 4, 1, '2022-02-09', '2022-11-09', NULL);

-- insert into "request"
INSERT INTO public.request(request_id, book_id, user_id, create_dttm, request_status) VALUES
    (nextval('request_request_id_seq'), 4, 2, now(), 'NEW');