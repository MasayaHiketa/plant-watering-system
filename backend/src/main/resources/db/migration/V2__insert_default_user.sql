INSERT INTO users (username, password)
SELECT 'masaya', '{noop}password'
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'masaya'
);
