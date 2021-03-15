/* PRE-DEFINED USERS */
INSERT
IGNORE INTO user (dtype, email, password, first_name, last_name, role, enabled) VALUES ('Admin', 'mister@gatsby.com',
                                                                '$2a$10$NbfiJdUgVM.yNsU2FnJiqOUjJny8GwYwkljlXqCYmA1KSYRZJK1U6',
                                                                    'Gatsby', 'Mister', 'ADMIN', true); /*green123*/
INSERT
IGNORE INTO user (dtype, email, password, first_name, last_name, role, enabled) VALUES ('Teacher', 'bob@ross.com',
                                                                '$2a$10$BERcfCeDU4LI3G1/7u1ZNeCGJZouMQ7W3exUWD7r7e9FP8bL3jjdC',
                                                                    'Bob', 'Ross', 'TEACHER', true); /*happy123*/

INSERT
IGNORE INTO user (dtype, email, password, first_name, last_name, role, enabled) VALUES ('Student', 'bart@simpson.com',
                                                                '$2a$10$e3CVS9sktbc7J6.eq1EA8.kn1MzyXz5a6njbFEDVXE9ZqSAc7ziTO',
                                                                    'Bart', 'Simpson', 'STUDENT', true); /*smart123*/