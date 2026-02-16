-- Basic schema : users, portfolios, projects

CREATE TABLE users (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       email VARCHAR(255) NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       display_name VARCHAR(50) NOT NULL,
                       created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
                       updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
                       CONSTRAINT uq_users_email UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE portfolios (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            user_id BIGINT NOT NULL,
                            title VARCHAR(100) NOT NULL,
                            slug VARCHAR(120) NOT NULL,
                            status ENUM('DRAFT','PUBLISHED') NOT NULL DEFAULT 'DRAFT',
                            created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
                            updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
                            CONSTRAINT uq_portfolios_slug UNIQUE (slug),
                            CONSTRAINT fk_portfolios_user FOREIGN KEY (user_id)
                                REFERENCES users(id)
                                ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE projects (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          user_id BIGINT NOT NULL,
                          name VARCHAR(120) NOT NULL,
                          summary TEXT NULL,
                          created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
                          updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
                          CONSTRAINT fk_projects_user FOREIGN KEY (user_id)
                              REFERENCES users(id)
                              ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_portfolios_user_id ON portfolios(user_id);
CREATE INDEX idx_projects_user_id ON projects(user_id);