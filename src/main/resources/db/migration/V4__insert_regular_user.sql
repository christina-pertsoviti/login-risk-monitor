INSERT INTO users (username, password, role_id)
VALUES (
    'user',
    '$2a$12$OZKb14fufoqTEmKzciJgbOTNjJ2nVGC0VHCmY/oaLh4SpBDa5FRvi',
    (SELECT id FROM roles WHERE name = 'USER')
);