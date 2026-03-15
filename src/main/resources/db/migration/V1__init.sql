CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE IF NOT EXISTS users
(
    id            BIGSERIAL PRIMARY KEY,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    display_name  VARCHAR(100) NOT NULL,
    role          VARCHAR(20)  NOT NULL DEFAULT 'USER',
    status        VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    last_login_at TIMESTAMPTZ,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
    created_by    BIGINT,
    updated_by    BIGINT
);

CREATE INDEX IF NOT EXISTS idx_users_status ON users (status);

CREATE TABLE IF NOT EXISTS ai_models
(
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    provider_type VARCHAR(50)  NOT NULL,
    protocol_type VARCHAR(50)  NOT NULL,
    base_url      VARCHAR(255) NOT NULL,
    api_key       VARCHAR(255) NOT NULL,
    model_name    VARCHAR(100) NOT NULL,
    enabled       BOOLEAN      NOT NULL DEFAULT TRUE,
    priority      INT          NOT NULL DEFAULT 100,
    extra_config  JSONB,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
    created_by    BIGINT,
    updated_by    BIGINT
);

CREATE INDEX IF NOT EXISTS idx_ai_models_enabled_priority ON ai_models (enabled, priority);
CREATE INDEX IF NOT EXISTS idx_ai_models_provider ON ai_models (provider_type);

CREATE TABLE IF NOT EXISTS user_settings
(
    id               BIGSERIAL PRIMARY KEY,
    user_id          BIGINT      NOT NULL REFERENCES users (id),
    default_model_id BIGINT REFERENCES ai_models (id),
    temperature      NUMERIC(4, 3),
    max_tokens       INT,
    preferences      JSONB,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by       BIGINT,
    updated_by       BIGINT,
    CONSTRAINT uk_user_settings_user UNIQUE (user_id)
);

CREATE TABLE IF NOT EXISTS conversations
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT       NOT NULL REFERENCES users (id),
    model_id   BIGINT REFERENCES ai_models (id),
    title      VARCHAR(200) NOT NULL,
    pinned     BOOLEAN      NOT NULL DEFAULT FALSE,
    status     SMALLINT     NOT NULL DEFAULT 1,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT now(),
    created_by BIGINT,
    updated_by BIGINT
);

CREATE INDEX IF NOT EXISTS idx_conversations_user ON conversations (user_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_conversations_user_pinned ON conversations (user_id, pinned);

CREATE TABLE IF NOT EXISTS messages
(
    id              BIGSERIAL PRIMARY KEY,
    conversation_id BIGINT      NOT NULL REFERENCES conversations (id),
    user_id         BIGINT      NOT NULL REFERENCES users (id),
    role            VARCHAR(20) NOT NULL,
    content         TEXT        NOT NULL,
    token_count     INT,
    status          SMALLINT    NOT NULL DEFAULT 1,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),
    created_by      BIGINT,
    updated_by      BIGINT
);

CREATE INDEX IF NOT EXISTS idx_messages_conversation ON messages (conversation_id, created_at);
CREATE INDEX IF NOT EXISTS idx_messages_user ON messages (user_id);

CREATE TABLE IF NOT EXISTS files
(
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT       NOT NULL REFERENCES users (id),
    conversation_id BIGINT REFERENCES conversations (id),
    filename        VARCHAR(255) NOT NULL,
    content_type    VARCHAR(100) NOT NULL,
    size_bytes      BIGINT       NOT NULL,
    storage_path    VARCHAR(500) NOT NULL,
    status          SMALLINT     NOT NULL DEFAULT 1,
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),
    created_by      BIGINT,
    updated_by      BIGINT
);

CREATE INDEX IF NOT EXISTS idx_files_user ON files (user_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_files_conversation ON files (conversation_id);

CREATE TABLE IF NOT EXISTS refresh_tokens
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT       NOT NULL REFERENCES users (id),
    token      VARCHAR(512) NOT NULL UNIQUE,
    expires_at TIMESTAMPTZ  NOT NULL,
    revoked    BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT now(),
    created_by BIGINT,
    updated_by BIGINT
);

CREATE INDEX IF NOT EXISTS idx_refresh_tokens_user ON refresh_tokens (user_id);
