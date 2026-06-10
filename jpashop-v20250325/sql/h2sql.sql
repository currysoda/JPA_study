
-- 테이블 목록 출력
show tables;

-- 멤버 테이블의 모든 항목 출력
select *
from member
limit 100;

SHOW DATABASES;

SHOW TABLES;

SHOW CREATE TABLE 테이블명;

DESCRIBE 테이블명;

EXPLAIN SELECT * FROM 테이블명;

-- 멤버 전부 삭제
delete
from member;