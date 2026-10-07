-- 게시판 테이블. 앱 실행 시 JPA(ddl-auto=update)가 자동 생성하므로 수동 실행은 선택 사항이다.
CREATE TABLE IF NOT EXISTS post (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    title         VARCHAR(100) NOT NULL,
    content       TEXT         NOT NULL,
    author        VARCHAR(30)  NOT NULL,
    password_hash VARCHAR(100) NOT NULL, -- BCrypt 해시
    created_at    DATETIME(6)  NOT NULL,
    updated_at    DATETIME(6)  NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
