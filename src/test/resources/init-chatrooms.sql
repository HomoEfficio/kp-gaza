set referential_integrity false;
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
