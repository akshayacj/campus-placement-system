-- V2__seed_branches.sql: Seed baseline academic branches
-- Provides standard academic branch codes used throughout the campus portal.

INSERT INTO branches (code, name) VALUES
('CSE', 'Computer Science and Engineering'),
('ECE', 'Electronics and Communication Engineering'),
('EEE', 'Electrical and Electronics Engineering'),
('MECH', 'Mechanical Engineering'),
('CIVIL', 'Civil Engineering'),
('IT', 'Information Technology');

