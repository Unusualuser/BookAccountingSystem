-- create table book
CREATE TABLE public.book
(
    book_id          BIGINT       NOT NULL,
    name             VARCHAR(30) NOT NULL,
    publication_year INTEGER      NOT NULL,
    author           VARCHAR(100) NOT NULL,
    description      VARCHAR(70),
    amount           BIGINT       NOT NULL,
    CONSTRAINT pk_book PRIMARY KEY (book_id)
);

CREATE SEQUENCE IF NOT EXISTS public.book_book_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.book_book_id_seq
    OWNER TO postgres;

-- create table user
CREATE TABLE public."user"
(
    user_id      BIGINT       NOT NULL,
    name         VARCHAR(100) NOT NULL,
    address      VARCHAR(70) NOT NULL,
    phone_number VARCHAR(12) NOT NULL,
    login        VARCHAR(30) NOT NULL,
    password     VARCHAR(255) NOT NULL,
    role         VARCHAR(30) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (user_id)
);

ALTER TABLE public."user"
    ADD CONSTRAINT uc_user_login UNIQUE (login);

CREATE SEQUENCE IF NOT EXISTS public.user_user_id_seq
INCREMENT 1
START 1
MINVALUE 1
MAXVALUE 9223372036854775807
CACHE 1;

ALTER SEQUENCE public.user_user_id_seq
    OWNER TO postgres;

-- create table book_history
CREATE TABLE public.book_history
(
    book_history_id BIGINT NOT NULL,
    book_id         BIGINT NOT NULL,
    user_id         BIGINT NOT NULL,
    rental_date     date   NOT NULL,
    return_date     date,
    CONSTRAINT pk_book_history PRIMARY KEY (book_history_id)
);

ALTER TABLE public.book_history
    ADD CONSTRAINT FK_BOOK_HISTORY_ON_BOOK FOREIGN KEY (book_id) REFERENCES public.book (book_id);

ALTER TABLE public.book_history
    ADD CONSTRAINT FK_BOOK_HISTORY_ON_USER FOREIGN KEY (user_id) REFERENCES public."user" (user_id);

CREATE SEQUENCE IF NOT EXISTS public.book_history_book_history_id_seq
INCREMENT 1
START 1
MINVALUE 1
MAXVALUE 9223372036854775807
CACHE 1;

ALTER SEQUENCE public.book_history_book_history_id_seq
    OWNER TO postgres;

-- create table request
CREATE TABLE public.request
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
    ADD CONSTRAINT FK_REQUEST_ON_USER FOREIGN KEY (user_id) REFERENCES public."user" (user_id);

CREATE SEQUENCE IF NOT EXISTS public.request_request_id_seq
INCREMENT 1
START 1
MINVALUE 1
MAXVALUE 9223372036854775807
CACHE 1;

ALTER SEQUENCE public.request_request_id_seq
    OWNER TO postgres;