CREATE TABLE user_entity (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,

  login_id VARCHAR(50) NOT NULL COMMENT '로그인 ID',
  password VARCHAR(64) NOT NULL COMMENT '비밀번호',

  creator BIGINT NULL COMMENT '생성자',
  created_at TIMESTAMP NOT NULL DEFAULT now() COMMENT '레코드 생성 시각',

  UNIQUE (login_id)
) COMMENT '파일' ENGINE = InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE file_entity (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,

  size BIGINT NOT NULL COMMENT '파일 사이즈',
  original_name VARCHAR(256) NOT NULL COMMENT '파일 원본 이름',
  path VARCHAR(1024) NOT NULL COMMENT '파일 저장 경로',

  created_at TIMESTAMP NOT NULL DEFAULT now() COMMENT '레코드 생성 시각'
) COMMENT '파일' ENGINE = InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;