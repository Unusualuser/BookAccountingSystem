INSERT INTO public.book(book_id, name, publication_year, author, description) VALUES
    (nextval('book_book_id_seq'), 'bookForTest', 2022, 'author', 'description');

DELETE FROM public.book_storage WHERE book_id = currval('book_book_id_seq');
