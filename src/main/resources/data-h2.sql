insert into USER(ID, USER_ID, PASSWORD, NAME, EMAIL) VALUES(1, 'rebwon', '1234', 'rebwon', 'msolo021015@naver.com');
insert into USER(ID, USER_ID, PASSWORD, NAME, EMAIL) VALUES(2, 'kitty', '1234', 'kitty', 'kitty@naver.com');
insert into USER(ID, USER_ID, PASSWORD, NAME, EMAIL) VALUES(3, 'jeremy', '1234', 'jeremy', 'jeremy@naver.com');

insert into QUESTION(ID, TITLE, CONTENTS, WRITER_ID, CREATE_DATE) VALUES(1, '앞으로의 추세는 스프링 WebFlux를 활용한 비동기 프로그래밍!', '비동기 프로그래밍', 1, now());
insert into QUESTION(ID, TITLE, CONTENTS, WRITER_ID, CREATE_DATE) VALUES(2, '요즘 아이들이 제일 좋아하는 캐릭터는 헬로키티!', '헬로 키티 짱짱', 2, now());
insert into QUESTION(ID, TITLE, CONTENTS, WRITER_ID, CREATE_DATE) VALUES(3, '우아한 형제들 백엔드 프로그래머로 입사해보자!!', 'SpringBoot와 JPA 그리고 QueryDSL을 마스터해보자.', 3, now());