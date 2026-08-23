INSERT INTO users (username, password, role_id)
VALUES (
    'admin',
    '$2a$12$C.GQbLS7fI21IwPHrV6M.ug0bl/enwFS1hPNxMG5aw8hRV4qbY2ea',
    (SELECT id FROM roles WHERE name = 'ADMIN')
);