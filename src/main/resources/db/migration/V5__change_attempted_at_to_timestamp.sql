ALTER TABLE login_attempts
    MODIFY COLUMN attempted_at TIMESTAMP(6) NOT NULL;