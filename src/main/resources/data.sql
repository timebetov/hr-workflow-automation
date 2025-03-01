DELETE FROM users;
DELETE FROM jobs;
DELETE FROM candidates;

INSERT INTO users (id, first_name, last_name, email, password, role, created_by) VALUES
(RANDOM_UUID(), 'Admin', 'User', 'main.admin@example.com',
'$2a$12$ie7v8hx0CpTYqPs9DRBwOeak07ypn4JRzf2ikwMKMa6lvtogUw7ye', 'ADMIN', 'SYSTEM'),
(RANDOM_UUID(), 'Alice', 'Smith', 'alice.smith@example.com',
'$2a$12$HlJaiMZXxoxuGl26In/oiOHV0HjtOYFUo5CzK657XxORHeoHwz9Ti', 'HR', 'SYSTEM'),
(RANDOM_UUID(), 'David', 'Brown', 'david.brown@example.com',
'$2a$12$jUzybdwtvWSZnEAr7dlvEO5M17XqGdMZvCaLG/x307w6rdhTRRx0a', 'HR', 'SYSTEM');

INSERT INTO users (id, first_name, last_name, email, password, role, created_by) VALUES
(RANDOM_UUID(), 'Bob', 'Johnson', 'bob.johnson@example.com',
'$2a$12$oJXsLbawhSWre0ps1tT21.1kkO6J8E3FNSdQWZIt7mnzA5BruBfzy', 'CANDIDATE', 'SYSTEM'),
(RANDOM_UUID(), 'Mark', 'Hopson', 'mark.hopson@example.com',
'$2a$12$sITqDVxP1sGkyfx3pvsybutPZywdH6avix2v0tNatc5wWdaKMJBEy', 'CANDIDATE', 'SYSTEM'),
(RANDOM_UUID(), 'Stravert', 'Sawoli', 'stravert.sawoli@example.com',
'$2a$12$3/Y1ssvL0Vm9r4PeImajdOY/Fc8eAZETsV1W2tjyDZxq8qTGBKLtC', 'CANDIDATE', 'SYSTEM'),
(RANDOM_UUID(), 'Jacob', 'Males', 'jakob.males@example.com',
'$2a$12$wNCiMOnOLTA6z4UGGVmNwuQtqGvSBhngv/SdT0zF5lDPhRffQnQPa', 'CANDIDATE', 'SYSTEM'),
(RANDOM_UUID(), 'Skail', 'Gut', 'skail.gut@example.com',
'$2a$12$tGSpD5KQ/NEo2nH1gyc4dOdu.398ZCkB72xxCy1IdygzAfrbG5gl6', 'CANDIDATE', 'SYSTEM'),
(RANDOM_UUID(), 'Tim', 'Parts', 'tim.parts@example.com',
'$2a$12$BkfXqQ/yOJZeTyAIizWfZ.KNVs2bnu9L7GBOdWXPsPVxyi3g75tVS', 'CANDIDATE', 'SYSTEM'),
(RANDOM_UUID(), 'Tom', 'Wred', 'tom.wred@example.com',
'$2a$12$qBXQU58X7IN6wUoSeAYxBOM5shHcpmemR.JDnV49nSsVa/ghGO0za', 'CANDIDATE', 'SYSTEM'),
(RANDOM_UUID(), 'Alex', 'Trowx', 'alex.trowx@example.com',
'$2a$12$.gWYCOGM2/bGCMTrp/xuYOPCmebT8OKmNzx3nbRIp7u8IF/kntw1W', 'CANDIDATE', 'SYSTEM'),
(RANDOM_UUID(), 'Jackson', 'Blade', 'jackson.blade@example.com',
'$2a$12$qDXjKXYWqOcoKYu3OWCIvedDq5D9S1cKoSyX9CIjAFJvU24SnNaHu', 'CANDIDATE', 'SYSTEM'),
(RANDOM_UUID(), 'Charlie', 'Miller', 'charlie.miller@example.com',
'$2a$12$vKC7LDAnFMVNqWD12BoSIekUfj8CIzb.ZXu/wkYAc9Xpc9UezB6Oq', 'CANDIDATE', 'SYSTEM');


INSERT INTO jobs (id, title, description, status, created_by) VALUES
(RANDOM_UUID(), 'Java Backend Developer', 'Develop and maintain backend services using Java and Spring.', 'ACTIVE', 'Alice Smith'),
(RANDOM_UUID(), 'React Frontend Developer', 'Develop and maintain frontend services using JavaScript and React.', 'ACTIVE', 'Alice Smith'),
(RANDOM_UUID(), 'QA Kotlin Developer', 'Develop and maintain tests using Java, Kotlin.', 'ACTIVE', 'Alice Smith'),
(RANDOM_UUID(), 'QA Python Developer', 'Develop and maintain tests using Python.', 'CLOSED', 'Alice Smith'),
(RANDOM_UUID(), 'Angular Frontend Developer', 'Develop and maintain frontend services using JavaScript, TypeScript, Angular.', 'CLOSED', 'Alice Smith'),
(RANDOM_UUID(), 'Golang Backend Developer', 'Develop and maintain microservices using Golang, Gin, Fiber.', 'ACTIVE', 'Alice Smith');