

/* example employee who can't do anything  */
INSERT INTO EMPLOYEE (id, address, email, first_name, last_name, last_year_days_off, password, phone, status, this_year_days_off, user_role, years_of_service) VALUES (0, 'Grodzka 12a, 30-851 Kraków', 'mail1@mailowo.com', 'Jan', 'Kowalski', 0, 'haslo', '987654321', 'WORKING', 30, 'ADMIN', 2);
INSERT INTO EMPLOYEE (id, address, email, first_name, last_name, last_year_days_off, password, phone, status, this_year_days_off, user_role, years_of_service) VALUES (1, 'Jasnogórska 1, 30-851 Kraków', 'mail2@mailowo.com', 'Adam', 'Nowak', 0, 'haslo', '212312311', 'WORKING', 10, 'MANAGER', 2);
INSERT INTO EMPLOYEE (id, address, email, first_name, last_name, last_year_days_off, password, phone, status, this_year_days_off, user_role, years_of_service) VALUES (2, 'Kuźnia Talentów 55b, 30-851 Kraków', 'mail3@mailowo.com', 'Marek', 'Kilof', 0, 'haslo', '123112311', 'WORKING', 20, 'EMPLOYEE', 2);



/* example permissions*/
INSERT INTO PERMISSIONS (id, add_users, modify_bonus_budget) VALUES (0, TRUE, TRUE);
INSERT INTO PERMISSION_OWNERS (perm_holder, perm_setid) VALUES  (0, 0);

INSERT INTO PERMISSIONS (id, add_users, modify_bonus_budget) VALUES (1, FALSE, FALSE);
INSERT INTO PERMISSION_OWNERS (perm_holder, perm_setid) VALUES  (1, 1);

INSERT INTO PERMISSIONS (id, add_users, modify_bonus_budget) VALUES (2, FALSE, FALSE);
INSERT INTO PERMISSION_OWNERS (perm_holder, perm_setid) VALUES  (2, 2);

INSERT INTO MANAGED_USERS(perm_holder, managed_user) VALUES (1, 2);

-- /* contract requests */
INSERT INTO CONTRACT(id, date_from, date_issued, date_signed, date_to, name_at_signing, signed, annual_leave_days, duty_allowance, salary, type, working_hours, employee_id) VALUES (0, '2021-01-01', '2020-12-01','2020-12-15','2023-12-30', 'Marek Kilof', true, 25,200,2500,'EMPLOYMENT', 160, 2);
INSERT INTO document_signed_by(document,signer_id) VALUES (0, 1);


-- /* days off requests */
INSERT INTO DAYS_OFF_REQUEST (id, date_from, date_issued, date_signed, date_to, name_at_signing, signed, leave_type, employee_id) VALUES (1, '2021-03-02', '2021-02-01', NULL, '2021-03-15', 'Jan Kowalski', true, 'SICK', 0);
INSERT INTO DAYS_OFF_REQUEST (id, date_from, date_issued, date_signed, date_to, name_at_signing, signed, leave_type, employee_id) VALUES (2, '2020-03-02', '2020-02-01', NULL, '2020-03-15', 'Jan Kowalski', true, 'SICK', 0);
INSERT INTO DAYS_OFF_REQUEST (id, date_from, date_issued, date_signed, date_to, name_at_signing, signed, leave_type, employee_id) VALUES (3, '2021-08-14', '2021-02-01', NULL, '2021-08-20', 'Marek Kilof', true, 'RECREATIONAL', 2);
INSERT INTO DAYS_OFF_REQUEST (id, date_from, date_issued, date_signed, date_to, name_at_signing, signed, leave_type, employee_id) VALUES (4, '2021-03-18', '2021-02-01', NULL, '2021-03-21', 'Adam Nowak', true, 'RECREATIONAL', 1);

-- /* delegation request */
INSERT INTO DELEGATION_REQUEST (id, date_from, date_issued, date_signed, date_to, name_at_signing, signed, destination, employee_id) VALUES (5, '2021-03-02', '2021-02-01', NULL, '2021-03-15', 'Adam Nowak', true, 'Ulaanbaatar', 1);
INSERT INTO DELEGATION_REQUEST (id, date_from, date_issued, date_signed, date_to, name_at_signing, signed, destination, employee_id) VALUES (6, '2021-03-02', '2021-02-01', NULL, '2021-03-15', 'Marek Kilof', true, 'Wieliczka', 2);
INSERT INTO DELEGATION_REQUEST (id, date_from, date_issued, date_signed, date_to, name_at_signing, signed, destination, employee_id) VALUES (7, '2020-12-02', '2020-12-01', NULL, '2020-12-08', 'Marek Kilof', true, 'Kraków', 2);
INSERT INTO DELEGATION_REQUEST (id, date_from, date_issued, date_signed, date_to, name_at_signing, signed, destination, employee_id) VALUES (8, '2021-01-10', '2021-01-01', NULL, '2021-01-14', 'Marek Kilof', true, 'Warszawa', 2);
INSERT INTO DELEGATION_REQUEST (id, date_from, date_issued, date_signed, date_to, name_at_signing, signed, destination, employee_id) VALUES (9, '2021-05-02', '2021-05-01', NULL, '2021-05-10', 'Marek Kilof', true, 'Szczecin', 2);

alter sequence employee_id_seq restart with 3;
alter sequence permissions_id_seq restart with 3;
-- alter sequence days_off_request_id_seq restart with 6;
-- alter sequence delegation_request_id_seq restart with 6;