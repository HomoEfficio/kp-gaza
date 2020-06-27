-- For H2
-- set referential_integrity false;
-- truncate table receipt restart identity;
-- truncate table distribution restart identity;
-- truncate table chat_user restart identity;
-- truncate table chat_room restart identity;
-- truncate table k_user restart identity;
-- set referential_integrity true;

truncate table receipt restart identity cascade;
truncate table distribution restart identity cascade;
truncate table chat_user restart identity cascade;
truncate table chat_room restart identity cascade;
truncate table k_user restart identity cascade;

insert into k_user (id, name, created_at, last_modified_at) values
(1, 'ㅋㅋㅇ', now(), now()),
(2, '콩심은데콩나고팥심은데팥난다꼭명심하세요', now(), now()),
(3, 'Guile', now(), now()),
(4, '김갑환', now(), now()),
(5, 'Chun Li', now(), now()),
(6, 'Kyoshiro', now(), now()),
(7, 'Haomaru', now(), now()),
(8, 'Charlotte', now(), now()),
(9, 'Bruce', now(), now())
;

insert into chat_room (id, name, owner_id, created_at, last_modified_at) values
('4cf5507010ae4097afcfd61a25cfd233', '방1', 1, now(), now()),
('74b45ce509394effbcd38a6277066252', '방2', 2, now(), now()),
('b7dd1bf7bf2048f898ff558f04faa35f', '방3', 2, now(), now()),
('c70f1bbc787d4706aac154cc609997ca', '방4', 2, now(), now()),
('cf12c71ee81e4c419750187f670b0847', '방5', 3, now(), now()),
('f5addd512d4547a28d15d95ece9b21b8', '방6', 5, now(), now()),
('396d7ac2aa654f568ec1ccc8b88b332c', '방7', 5, now(), now()),
('23662be4223042c9a93ba58049fbd414', '방8', 7, now(), now()),
('c2cef0fb71084f5e92e39b3011f77fbf', '방9', 9, now(), now())
;

insert into chat_user (chat_room_id, chatter_id, created_at, last_modified_at) values
('4cf5507010ae4097afcfd61a25cfd233', 1, now(), now()),
('4cf5507010ae4097afcfd61a25cfd233', 2, now(), now()),
('4cf5507010ae4097afcfd61a25cfd233', 3, now(), now()),
('4cf5507010ae4097afcfd61a25cfd233', 4, now(), now()),
('4cf5507010ae4097afcfd61a25cfd233', 5, now(), now()),
('74b45ce509394effbcd38a6277066252', 2, now(), now()),
('b7dd1bf7bf2048f898ff558f04faa35f', 2, now(), now()),
('b7dd1bf7bf2048f898ff558f04faa35f', 4, now(), now()),
('b7dd1bf7bf2048f898ff558f04faa35f', 6, now(), now()),
('c70f1bbc787d4706aac154cc609997ca', 2, now(), now()),
('cf12c71ee81e4c419750187f670b0847', 3, now(), now()),
('f5addd512d4547a28d15d95ece9b21b8', 5, now(), now()),
('396d7ac2aa654f568ec1ccc8b88b332c', 5, now(), now()),
('23662be4223042c9a93ba58049fbd414', 7, now(), now()),
('c2cef0fb71084f5e92e39b3011f77fbf', 9, now(), now())
;

insert into distribution (token, amount, targets, distributor_id, chat_room_id, created_at, last_modified_at) values
('a11', 100, 2, 1, '4cf5507010ae4097afcfd61a25cfd233', current_timestamp - (20 || ' minutes')::interval, current_timestamp - (20 || ' minutes')::interval),
('a21', 400, 4, 2, '4cf5507010ae4097afcfd61a25cfd233', current_timestamp - (11 || ' minutes')::interval, current_timestamp - (20 || ' minutes')::interval),
('c41', 300, 1, 4, 'b7dd1bf7bf2048f898ff558f04faa35f', now(), now())
;

insert into receipt (receiver_id, amount, distribution_id, status, created_at, last_modified_at) values
(null, 50, 1, 'OPEN', now(), now()),
(null, 50, 1, 'OPEN', now(), now()),
(null, 100, 2, 'OPEN', now(), now()),
(null, 100, 2, 'OPEN', now(), now()),
(null, 100, 2, 'OPEN', now(), now()),
(null, 100, 2, 'OPEN', now(), now()),
(null, 300, 3, 'OPEN', now(), now())
;
