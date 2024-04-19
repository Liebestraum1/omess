CREATE TABLE `member`
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    nickname   VARCHAR(30)  NOT NULL,
    email      VARCHAR(50)  NOT NULL,
    password   VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now() ON UPDATE now()
);

CREATE TABLE `project`
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(10) NOT NULL,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now() ON UPDATE now()
);

CREATE TABLE `project_member`
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now() ON UPDATE now(),
    FOREIGN KEY (member_id) REFERENCES member(id) on DELETE CASCADE,
    FOREIGN KEY (project_id) REFERENCES project(id) on DELETE CASCADE
);


CREATE TABLE `module`
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT      NOT NULL,
    title      VARCHAR(50) NOT NULL,
    category   VARCHAR(50) NOT NULL,
    CONSTRAINT FK_MODULE_PROJECT_ID FOREIGN KEY (project_id) REFERENCES project (id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now() ON UPDATE now()
);

CREATE TABLE `kanbanboard`
(
    id   BIGINT      NOT NULL PRIMARY KEY,
    path VARCHAR(20)  NULL,
    CONSTRAINT FK_KANBANBOARD_MODULE_ID FOREIGN KEY (id) REFERENCES module (id) ON DELETE CASCADE
);

CREATE TABLE `label`
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(50) NOT NULL,
    kanbanboard_id BIGINT      NOT NULL,
    path           VARCHAR(20)  NULL,
    created_at     TIMESTAMP DEFAULT now(),
    updated_at     TIMESTAMP DEFAULT now() ON UPDATE now(),
    CONSTRAINT FK_LABEL_KANBANBOARD_ID FOREIGN KEY (kanbanboard_id) REFERENCES kanbanboard (id) ON DELETE CASCADE
);

CREATE TABLE `issue`
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    title          VARCHAR(90) NOT NULL,
    content        TEXT        NULL,
    importance     TINYINT(3)         NOT NULL DEFAULT 0,
    status         TINYINT(3)         NOT NULL DEFAULT 0,
    kanbanboard_id BIGINT      NOT NULL,
    charger_id      BIGINT      NULL,
    label_id       BIGINT      NULL,
    path           VARCHAR(20)  NULL,
    created_at     TIMESTAMP            DEFAULT now(),
    updated_at     TIMESTAMP            DEFAULT now() ON UPDATE now(),
    CONSTRAINT FK_ISSUES_KANBANBOARD_ID FOREIGN KEY (kanbanboard_id) REFERENCES kanbanboard (id) ON DELETE CASCADE,
    CONSTRAINT FK_ISSUES_MEMBER_ID FOREIGN KEY (charger_id) REFERENCES member (id) ON DELETE CASCADE,
    CONSTRAINT FK_ISSUES_LABEL_ID FOREIGN KEY (label_id) REFERENCES label (id) ON DELETE CASCADE
);

CREATE TABLE `api_specification`
(
    `id` BIGINT      NOT NULL PRIMARY KEY,
    `path`           VARCHAR(20)  NULL,
    FOREIGN KEY (id) REFERENCES module (id) ON DELETE CASCADE
);

CREATE TABLE `domain`
(
    `id`                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    `api_specification_id` BIGINT      NOT NULL,
    `path`           VARCHAR(20)  NULL,
    `name`                 VARCHAR(20) NOT NULL,
    created_at             TIMESTAMP DEFAULT now(),
    updated_at             TIMESTAMP DEFAULT now() ON UPDATE now(),
    FOREIGN KEY (api_specification_id) REFERENCES api_specification (id) ON DELETE CASCADE
);


CREATE TABLE `api`
(
    `id`              BIGINT AUTO_INCREMENT PRIMARY KEY,
    `domain_id`       BIGINT        NOT NULL,
    `path`           VARCHAR(20)  NULL,
    `name`            VARCHAR(20)   NOT NULL,
    `description`     VARCHAR(50)   NULL,
    `endpoint`        VARCHAR(2000) NOT NULL,
    `method`          VARCHAR(10)   NOT NULL,
    `request_schema`  JSON          NULL,
    `response_schema` JSON          NULL,
    `status_code`     SMALLINT      NOT NULL,
    created_at        TIMESTAMP DEFAULT now(),
    updated_at        TIMESTAMP DEFAULT now() ON UPDATE now(),
    FOREIGN KEY (domain_id) REFERENCES domain (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `http_method`
(
    `id`       BIGINT AUTO_INCREMENT PRIMARY KEY,
    `method`   VARCHAR(10) NOT NULL,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now() ON UPDATE now()
);

CREATE TABLE IF NOT EXISTS `module_category`
(
    `id`       BIGINT AUTO_INCREMENT PRIMARY KEY,
    `category` VARCHAR(50) NOT NULL,
    `path`     VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now() ON UPDATE now()
);

CREATE TABLE `request_header` (
    `id`	BIGINT AUTO_INCREMENT PRIMARY KEY,
    `api_id`	bigint	NOT NULL,
    `path`	varchar(20)	NULL,
    `key`	varchar(50)	NOT NULL,
    `value`	varchar(100)	NOT NULL,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now() ON UPDATE now(),
    FOREIGN KEY (api_id) REFERENCES api(id) ON DELETE CASCADE
);


CREATE TABLE `query_param` (
	`id`	BIGINT AUTO_INCREMENT PRIMARY KEY,
	`api_id`	bigint	NOT NULL,
	`path`	varchar(20)	NULL,
	`name`	varchar(20)	NOT NULL,
	`description`	varchar(5)	NULL,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now() ON UPDATE now(),
    FOREIGN KEY (api_id) REFERENCES api(id) ON DELETE CASCADE
);

CREATE TABLE `path_variable`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY,
    `api_id`      bigint      NOT NULL,
    `path`        varchar(20) NULL,
    `name`        varchar(20) NOT NULL,
    `description` varchar(50) NULL,
    created_at    TIMESTAMP DEFAULT now(),
    updated_at    TIMESTAMP DEFAULT now() ON UPDATE now(),
    FOREIGN KEY (api_id) REFERENCES api (id) ON DELETE CASCADE
);