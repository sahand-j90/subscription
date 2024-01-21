--liquibase formatted sql

--changeset sahand:99fe23d0-65d0-4348-bc8c-ce6389dab1fc  failOnError:true

INSERT INTO tlb_user (username, created_at, updated_at, optimistic_lock_version, is_enabled, password)
    VALUES ('Admin', '2024-01-20 21:02:30.000000', null, 0, true, '$2a$10$rwf6EFdZFN9T3Y.8/Vdf8OESc29Nh9DSAQcJPotUDLElp1GZ3MPcW');

INSERT INTO tbl_user_authority (username, authority) VALUES ('Admin', 'ADMIN');

-- Last line of change set