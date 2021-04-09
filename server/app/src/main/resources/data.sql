

/* example employee who can't do anything  */
INSERT INTO EMPLOYEE (id, address, email, first_name, last_name, last_year_days_off, password, phone, status, this_year_days_off, user_role, years_of_service) VALUES (0, 'Grodzka 12a, 30-851 Kraków', 'mail1@mailowo.com', 'Jan', 'Kowalski', 0, 'haslo', '987654321', 'WORKING', 10, 'EMPLOYEE', 2);
INSERT INTO EMPLOYEE (id, address, email, first_name, last_name, last_year_days_off, password, phone, status, this_year_days_off, user_role, years_of_service) VALUES (1, 'Jasnogórska 1, 30-851 Kraków', 'mail2@mailowo.com', 'Adam', 'Nowak', 0, 'haslo', '212312311', 'WORKING', 10, 'EMPLOYEE', 2);
INSERT INTO EMPLOYEE (id, address, email, first_name, last_name, last_year_days_off, password, phone, status, this_year_days_off, user_role, years_of_service) VALUES (2, 'Kuźnia Talentów 55b, 30-851 Kraków', 'mail3@mailowo.com', 'Marek', 'Kilof', 0, 'haslo', '123112311', 'WORKING', 10, 'EMPLOYEE', 2);



/* example permissions*/
INSERT INTO PERMISSIONS (id, add_users, modify_bonus_budget) VALUES (0, FALSE, FALSE);
INSERT INTO PERMISSION_OWNERS (perm_holder, perm_setid) VALUES  (0, 0);

INSERT INTO PERMISSIONS (id, add_users, modify_bonus_budget) VALUES (1, FALSE, FALSE);
INSERT INTO PERMISSION_OWNERS (perm_holder, perm_setid) VALUES  (1, 1);

INSERT INTO PERMISSIONS (id, add_users, modify_bonus_budget) VALUES (2, FALSE, FALSE);
INSERT INTO PERMISSION_OWNERS (perm_holder, perm_setid) VALUES  (2, 2);



/* days off requests */
INSERT INTO DAYS_OFF_REQUEST (id, date_from, date_issued, date_signed, date_to, name_at_signing, signed, leave_type, employee_id) VALUES (1, '2021-03-02', '2021-02-01', NULL, '2021-03-15', 'Jan Kowalski', false, 'SICK', 0);
INSERT INTO DAYS_OFF_REQUEST (id, date_from, date_issued, date_signed, date_to, name_at_signing, signed, leave_type, employee_id) VALUES (2, '2020-03-02', '2020-02-01', NULL, '2020-03-15', 'Jan Kowalski', false, 'SICK', 0);
INSERT INTO DAYS_OFF_REQUEST (id, date_from, date_issued, date_signed, date_to, name_at_signing, signed, leave_type, employee_id) VALUES (3, '2021-08-14', '2021-02-01', NULL, '2021-08-30', 'Marek Kilof', false, 'RECREATIONAL', 2);
INSERT INTO DAYS_OFF_REQUEST (id, date_from, date_issued, date_signed, date_to, name_at_signing, signed, leave_type, employee_id) VALUES (4, '2021-03-18', '2021-02-01', NULL, '2021-03-21', 'Adam Nowak', false, 'RECREATIONAL', 1);



/* delegation request */
INSERT INTO DELEGATION_REQUEST (id, date_from, date_issued, date_signed, date_to, name_at_signing, signed, destination, employee_id) VALUES (5, '2021-03-02', '2021-02-01', NULL, '2021-03-15', 'Adam Nowak', false, 'Ulaanbaatar', 1);
INSERT INTO DELEGATION_REQUEST (id, date_from, date_issued, date_signed, date_to, name_at_signing, signed, destination, employee_id) VALUES (0, '2021-03-02', '2021-02-01', NULL, '2021-03-15', 'Marek Kilof', false, 'Wieliczka', 2);

alter sequence employee_id_seq restart with 3;
alter sequence permissions_id_seq restart with 3;
-- alter sequence days_off_request_id_seq restart with 6;
-- alter sequence delegation_request_id_seq restart with 6;