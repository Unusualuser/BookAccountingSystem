-- create table book
CREATE TABLE public.book
(
    book_id          BIGINT       NOT NULL,
    name             VARCHAR(30) NOT NULL,
    publication_year INTEGER      NOT NULL,
    author           VARCHAR(100) NOT NULL,
    description      VARCHAR(70),
    CONSTRAINT pk_book PRIMARY KEY (book_id)
);


-- create sequence for book
CREATE SEQUENCE book_book_id_seq
    INCREMENT 1
    START WITH 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;


-- create table book_storage
CREATE TABLE public.book_storage
(
    book_storage_id BIGINT NOT NULL,
    book_id         BIGINT NOT NULL,
    quantity        BIGINT NOT NULL,
    CONSTRAINT pk_book_storage PRIMARY KEY (book_storage_id)
);

ALTER TABLE public.book_storage
    ADD CONSTRAINT FK_BOOK_STORAGE_ON_BOOK FOREIGN KEY (book_id) REFERENCES public.book (book_id);


-- create sequence for book_storage
CREATE SEQUENCE book_storage_book_storage_id_seq
    INCREMENT 1
    START WITH 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;


-- create trigger for book_storage
CREATE TRIGGER after_insert_book_trigger
    AFTER INSERT ON public.book
    FOR EACH ROW
CALL "ru.example.testneed.AfterInsertBookTrigger";


-- create table user_
CREATE TABLE public.user_
(
    user_id      BIGINT       NOT NULL,
    name         VARCHAR(100),
    address      VARCHAR(70),
    phone_number VARCHAR(12),
    login        VARCHAR(30) NOT NULL,
    password     VARCHAR(255) NOT NULL,
    role         VARCHAR(30) NOT NULL,
    email        VARCHAR(255),
    CONSTRAINT pk_user PRIMARY KEY (user_id)
);

ALTER TABLE public.user_
    ADD CONSTRAINT uc_user_login UNIQUE (login);


-- create sequence for user_
CREATE SEQUENCE user_user_id_seq
INCREMENT 1
START WITH 1
MINVALUE 1
MAXVALUE 9223372036854775807
CACHE 1;


-- create table book_history
CREATE TABLE public.book_history
(
    book_history_id      BIGINT NOT NULL,
    book_id              BIGINT NOT NULL,
    user_id              BIGINT NOT NULL,
    rental_date          date   NOT NULL,
    return_deadline_date date   NOT NULL,
    return_date          date,
    CONSTRAINT pk_book_history PRIMARY KEY (book_history_id)
);

ALTER TABLE public.book_history
    ADD CONSTRAINT FK_BOOK_HISTORY_ON_BOOK FOREIGN KEY (book_id) REFERENCES public.book (book_id);

ALTER TABLE public.book_history
    ADD CONSTRAINT FK_BOOK_HISTORY_ON_USER FOREIGN KEY (user_id) REFERENCES public.user_ (user_id);


-- create sequence for book_history
CREATE SEQUENCE book_history_book_history_id_seq
INCREMENT 1
START WITH 1
MINVALUE 1
MAXVALUE 9223372036854775807
CACHE 1;


-- create table request
CREATE TABLE request
(
    request_id     BIGINT                      NOT NULL,
    book_id        BIGINT                      NOT NULL,
    user_id        BIGINT                      NOT NULL,
    create_dttm    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    request_status VARCHAR(20)                NOT NULL,
    CONSTRAINT pk_request PRIMARY KEY (request_id)
);

ALTER TABLE public.request
    ADD CONSTRAINT FK_REQUEST_ON_BOOK FOREIGN KEY (book_id) REFERENCES public.book (book_id);

ALTER TABLE public.request
    ADD CONSTRAINT FK_REQUEST_ON_USER FOREIGN KEY (user_id) REFERENCES public.user_ (user_id);


-- create sequence for request
CREATE SEQUENCE request_request_id_seq
INCREMENT 1
START WITH 1
MINVALUE 1
MAXVALUE 9223372036854775807
CACHE 1;