-- For H2
set referential_integrity false;
truncate table k_user restart identity;
set referential_integrity true;


insert into k_user (id, name) values
(1, 'ㅋㅋㅇ'),
(2, '콩심은데콩나고팥심은데팥난다꼭명심하세요')
;
