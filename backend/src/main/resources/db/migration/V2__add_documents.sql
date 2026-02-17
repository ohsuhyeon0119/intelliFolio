CREATE TABLE documents (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           project_id BIGINT NOT NULL,
                           title VARCHAR(200) NOT NULL,
                           original_filename VARCHAR(255) NOT NULL,
                           content_type VARCHAR(100) NULL,
                           size_bytes BIGINT NOT NULL,
                           created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
                           updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),

                           CONSTRAINT fk_documents_project FOREIGN KEY (project_id)
                               REFERENCES projects(id)
                               ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_documents_project_id ON documents(project_id);