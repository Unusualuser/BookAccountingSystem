INSERT INTO public.request(request_id, book_id, user_id, create_dttm, request_status) VALUES
    (nextval('request_request_id_seq'), 4, 2, now(), 'NEW'),
    (nextval('request_request_id_seq'), 4, 3, now(), 'NEW');