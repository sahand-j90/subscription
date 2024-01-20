--liquibase formatted sql

--changeset sahand:99fe23d0-65d0-4348-bc8c-ce6389dab1fc  failOnError:true

INSERT INTO tlb_user (username, created_at, updated_at, optimistic_lock_version, is_enabled, password)
    VALUES ('Admin', '2024-01-20 21:02:30.000000', null, 0, true, '$2a$10$D5dF8p2kyF8xdLA.nUXpYuZyjH8a03b/bV7nBhDAWiwFH9tGdcln6');

INSERT INTO tbl_user_authority (username, authority) VALUES ('Admin', 'Admin');

-- Last line of change set