set referential_integrity false;
truncate table receipt restart identity;
truncate table distribution restart identity;
truncate table chat_user restart identity;
truncate table chat_room restart identity;
truncate table k_user restart identity;
set referential_integrity true;

insert into k_user (id, name) values
(1, 'ㅋㅋㅇ'),
(2, '콩심은데콩나고팥심은데팥난다꼭명심하세요'),
(3, 'Guile'),
(4, '김갑환'),
(5, 'Chun Li'),
(6, 'Kyoshiro'),
(7, 'Haomaru'),
(8, 'Charlotte'),
(9, 'Bruce')
;

insert into chat_room (id, name, owner_id) values
('4cf5507010ae4097afcfd61a25cfd233', '방1', 1),
('74b45ce509394effbcd38a6277066252', '방2', 2),
('b7dd1bf7bf2048f898ff558f04faa35f', '방3', 2),
('c70f1bbc787d4706aac154cc609997ca', '방4', 2),
('cf12c71ee81e4c419750187f670b0847', '방5', 3),
('f5addd512d4547a28d15d95ece9b21b8', '방6', 5),
('396d7ac2aa654f568ec1ccc8b88b332c', '방7', 5),
('23662be4223042c9a93ba58049fbd414', '방8', 7),
('c2cef0fb71084f5e92e39b3011f77fbf', '방9', 9)
;

insert into chat_user (chat_room_id, chatter_id) values
('4cf5507010ae4097afcfd61a25cfd233', 1),
('4cf5507010ae4097afcfd61a25cfd233', 2),
('4cf5507010ae4097afcfd61a25cfd233', 3),
('4cf5507010ae4097afcfd61a25cfd233', 4),
('4cf5507010ae4097afcfd61a25cfd233', 5),
('74b45ce509394effbcd38a6277066252', 2),
('b7dd1bf7bf2048f898ff558f04faa35f', 2),
('b7dd1bf7bf2048f898ff558f04faa35f', 4),
('b7dd1bf7bf2048f898ff558f04faa35f', 6),
('c70f1bbc787d4706aac154cc609997ca', 2),
('cf12c71ee81e4c419750187f670b0847', 3),
('f5addd512d4547a28d15d95ece9b21b8', 5),
('396d7ac2aa654f568ec1ccc8b88b332c', 5),
('23662be4223042c9a93ba58049fbd414', 7),
('c2cef0fb71084f5e92e39b3011f77fbf', 9)
;

insert into distribution (token, amount, targets, distributor_id, chat_room_id, created_at) values
('a11', 100, 2, 1, '4cf5507010ae4097afcfd61a25cfd233', timestampadd(day, -7, now())),
('a21', 400, 4, 2, '4cf5507010ae4097afcfd61a25cfd233', now()),
('c41', 300, 1, 4, 'b7dd1bf7bf2048f898ff558f04faa35f', now())
;

insert into receipt (id, receiver_id, amount, distribution_id, status) values
(1, null, 50, 1, 'OPEN'),
(2, null, 50, 1, 'OPEN'),
(3, null, 100, 2, 'OPEN'),
(4, null, 100, 2, 'OPEN'),
(5, null, 100, 2, 'OPEN'),
(6, null, 100, 2, 'OPEN'),
(7, null, 300, 3, 'OPEN')
;

update receipt
set
receiver_id = 2,
status = 'CLOSED'
where id = 1;

update receipt
set
receiver_id = 3,
status = 'CLOSED'
where id = 2;

update receipt
set
receiver_id = 5,
status = 'CLOSED'
where id = 3;

update receipt
set
receiver_id = 3,
status = 'CLOSED'
where id = 4;
