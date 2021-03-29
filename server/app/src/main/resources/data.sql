

/* example employee who can't do anything*/
INSERT INTO EMPLOYEE (id, address, email, first_name, last_name, last_year_days_off, password, phone, status, this_year_days_off, user_role, years_of_service) VALUES (1, 'Grodzka 12a, 30-851 Kraków', 'mail@mailowo.com', 'Jan', 'Kowalski', 0, 'haslo', '987654321', 'WORKING', 10, 'EMPLOYEE', 2);


-- INSERT INTO PERMISSIONS (
--     user_id,
--     add_users,
--     modify_bonus_budget
--     ) VALUES (
--     1,
--     FALSE,
--     FALSE
-- );
--
-- INSERT INTO PERMISSIONS_OWNERS (
--     perm_holder,
--     perm_setid
-- ) VALUES  (
--     1,
--     1
-- );
--


-- /* This creates the initial admin account. It is sacred, and shall not be removed */
--
-- INSERT INTO ROLES (name) VALUES ('USER');
-- INSERT INTO ROLES (name) VALUES ('ADMIN');
--
-- INSERT INTO USERS (email, first_name, last_name, password, username)
-- VALUES ('test@test.com', 'Test', 'Teston', '123', 'test');
--
-- INSERT INTO USER_ROLES (user_id, role_id) VALUES (1, 'ADMIN');
-- INSERT INTO USER_ROLES (user_id, role_id) VALUES (1, 'USER');
--
--
-- /* This initializes example data for testing. Feel free to modify however you want */
--
-- INSERT INTO MOVIES (author, description, title)
-- VALUES ('Cameron', 'Blue people in space', 'Avatar');
--
-- INSERT INTO SCREENING_ROOMS (floor, number) VALUES (1, 'A1');
-- INSERT INTO SCREENINGS (date, ticket_cost, movie_id, screening_room_id)
-- VALUES (CURRENT_TIMESTAMP, 10.0, 1, 1);
--
-- INSERT INTO SEATS (row_number, seat_number, screening_room_id) VALUES (1, 1, 1);
-- INSERT INTO RESERVATIONS (screening_id, seat_id, user_id) VALUES (1, 1, 1);
