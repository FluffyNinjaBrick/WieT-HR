

/* example employee who can't do anything  */
INSERT INTO EMPLOYEE (id, address, email, first_name, last_name, last_year_days_off, password, phone, status, this_year_days_off, user_role, years_of_service) VALUES (0, 'Grodzka 12a, 30-851 Kraków', 'mail1@mailowo.com', 'Jan', 'Kowalski', 0, 'haslo', '987654321', 'WORKING', 16, 'ADMIN', 4);
INSERT INTO EMPLOYEE (id, address, email, first_name, last_name, last_year_days_off, password, phone, status, this_year_days_off, user_role, years_of_service) VALUES (1, 'Jasnogórska 1, 30-851 Kraków', 'mail2@mailowo.com', 'Adam', 'Nowak', 0, 'haslo', '212312311', 'WORKING', 25, 'MANAGER', 4);
INSERT INTO EMPLOYEE (id, address, email, first_name, last_name, last_year_days_off, password, phone, status, this_year_days_off, user_role, years_of_service) VALUES (2, 'Kuźnia Talentów 55b, 30-851 Kraków', 'mail3@mailowo.com', 'Marek', 'Kilof', 0, 'haslo', '123112311', 'WORKING', 19, 'EMPLOYEE', 2);
INSERT INTO EMPLOYEE (id, address, email, first_name, last_name, last_year_days_off, password, phone, status, this_year_days_off, user_role, years_of_service) VALUES (3, 'Wielki Staw 18, 31-121 Kielce', 'mail4@mailowo.com', 'Anna', 'Pastel', 1, 'haslo', '65736322123', 'WORKING', 25, 'EMPLOYEE', 2);


/* example permissions*/
INSERT INTO PERMISSIONS (id, add_users, modify_bonus_budget) VALUES (0, TRUE, TRUE);
INSERT INTO PERMISSION_OWNERS (perm_holder, perm_setid) VALUES  (0, 0);

INSERT INTO PERMISSIONS (id, add_users, modify_bonus_budget) VALUES (1, FALSE, FALSE);
INSERT INTO PERMISSION_OWNERS (perm_holder, perm_setid) VALUES  (1, 1);

INSERT INTO PERMISSIONS (id, add_users, modify_bonus_budget) VALUES (2, FALSE, FALSE);
INSERT INTO PERMISSION_OWNERS (perm_holder, perm_setid) VALUES  (2, 2);

INSERT INTO PERMISSIONS (id, add_users, modify_bonus_budget) VALUES (3, FALSE, FALSE);
INSERT INTO PERMISSION_OWNERS (perm_holder, perm_setid) VALUES  (3, 3);

INSERT INTO MANAGED_USERS(perm_holder, managed_user) VALUES (0, 0);
INSERT INTO MANAGED_USERS(perm_holder, managed_user) VALUES (0, 1);
INSERT INTO MANAGED_USERS(perm_holder, managed_user) VALUES (0, 2);
INSERT INTO MANAGED_USERS(perm_holder, managed_user) VALUES (0, 3);
INSERT INTO MANAGED_USERS(perm_holder, managed_user) VALUES (1, 2);
INSERT INTO MANAGED_USERS(perm_holder, managed_user) VALUES (1, 3);

-- /* contracts */
INSERT INTO document(id, date_from, date_issued, date_signed, date_to, name_at_signing, signed,  employee_id) VALUES (0, '2021-01-01', '2020-12-01', '2020-12-15', '2023-12-30', 'Marek Kilof', true, 2);
INSERT INTO contract(id, annual_leave_days, duty_allowance, salary, type, working_hours) VALUES (0, 25,200,2500,'EMPLOYMENT', 160);
INSERT INTO document_signed_by(document,signer_id) VALUES (0, 1);

INSERT INTO document(id, date_from, date_issued, date_signed, date_to, name_at_signing, signed,  employee_id) VALUES (10, '2021-01-01', '2020-12-01', '2020-12-15', '2023-12-30', 'Anna Pastel', true, 3);
INSERT INTO contract(id, annual_leave_days, duty_allowance, salary, type, working_hours) VALUES (10, 25, 200, 2500, 'EMPLOYMENT', 160);
INSERT INTO document_signed_by(document,signer_id) VALUES (10, 1);

INSERT INTO document(id, date_from, date_issued, date_signed, date_to, name_at_signing, signed,  employee_id) VALUES (11, '2021-01-01', '2020-12-01', '2020-12-15', '2023-12-30', 'Adam Nowak', true, 1);
INSERT INTO contract(id, annual_leave_days, duty_allowance, salary, type, working_hours) VALUES (11, 25, 200, 3500, 'EMPLOYMENT', 160);
INSERT INTO document_signed_by(document,signer_id) VALUES (11, 1);

INSERT INTO document(id, date_from, date_issued, date_signed, date_to, name_at_signing, signed,  employee_id) VALUES (12, '2021-01-01', '2020-12-01', '2020-12-15', '2023-12-30', 'Jan Kowalski', true, 1);
INSERT INTO contract(id, annual_leave_days, duty_allowance, salary, type, working_hours) VALUES (12, 30, 200, 4500, 'EMPLOYMENT', 160);
INSERT INTO document_signed_by(document,signer_id) VALUES (12, 1);


-- /* days off requests */
INSERT INTO document (id, date_from, date_issued, date_signed, date_to, name_at_signing, signed, employee_id) VALUES (1, '2021-03-02', '2021-02-01', '2021-02-15', '2021-03-15', 'Jan Kowalski', true, 0);
INSERT INTO DAYS_OFF_REQUEST (id, leave_type) VALUES (1, 'SICK');
INSERT INTO document_signed_by(document,signer_id) VALUES (1, 0);

INSERT INTO document (id, date_from, date_issued, date_signed, date_to, name_at_signing, signed, employee_id) VALUES (2, '2020-03-02', '2020-02-01', '2020-02-10', '2020-03-15', 'Jan Kowalski', true, 0);
INSERT INTO DAYS_OFF_REQUEST (id, leave_type) VALUES (2, 'SICK');
INSERT INTO document_signed_by(document,signer_id) VALUES (2, 0);

INSERT INTO document (id, date_from, date_issued, date_signed, date_to, name_at_signing, signed, employee_id) VALUES (3, '2021-08-14', '2021-07-04', '2021-07-07', '2021-08-20', 'Marek Kilof', true, 2);
INSERT INTO DAYS_OFF_REQUEST (id, leave_type) VALUES (3, 'RECREATIONAL');
INSERT INTO document_signed_by(document,signer_id) VALUES (3, 1);

INSERT INTO document  (id, date_from, date_issued, date_signed, date_to, name_at_signing, signed, employee_id) VALUES (4, '2021-06-18', '2021-05-01', NULL, '2021-06-21', 'Adam Nowak', false, 1);
INSERT INTO DAYS_OFF_REQUEST (id, leave_type) VALUES (4, 'RECREATIONAL');

-- -- /* delegation request */
INSERT INTO document (id, date_from, date_issued, date_signed, date_to, name_at_signing, signed, employee_id) VALUES (5, '2021-03-02', '2021-02-01', '2021-02-20', '2021-03-15', 'Adam Nowak', true, 1);
INSERT INTO DELEGATION_REQUEST (id, destination) VALUES (5, 'Ulaanbaatar');
INSERT INTO document_signed_by(document,signer_id) VALUES (5, 0);

INSERT INTO document (id, date_from, date_issued, date_signed, date_to, name_at_signing, signed, employee_id) VALUES (6, '2021-03-02', '2021-02-01', '2021-02-18', '2021-03-15', 'Marek Kilof', true, 2);
INSERT INTO DELEGATION_REQUEST (id, destination) VALUES (6, 'Wieliczka');
INSERT INTO document_signed_by(document,signer_id) VALUES (6, 1);

INSERT INTO document (id, date_from, date_issued, date_signed, date_to, name_at_signing, signed, employee_id) VALUES (7, '2020-12-02', '2020-12-01', '2020-12-12', '2020-12-08', 'Marek Kilof', true, 2);
INSERT INTO DELEGATION_REQUEST (id, destination) VALUES (7, 'Kraków');
INSERT INTO document_signed_by(document,signer_id) VALUES (7, 1);

INSERT INTO document (id, date_from, date_issued, date_signed, date_to, name_at_signing, signed, employee_id) VALUES (8, '2021-01-10', '2021-01-01', '2021-01-09', '2021-01-14', 'Anna Pastel', true, 3);
INSERT INTO DELEGATION_REQUEST (id, destination) VALUES (8, 'Warszawa');
INSERT INTO document_signed_by(document,signer_id) VALUES (8, 1);

INSERT INTO document (id, date_from, date_issued, date_signed, date_to, name_at_signing, signed, employee_id) VALUES (9, '2021-07-10', '2021-05-01', NULL, '2021-07-16', 'Anna Pastel', false, 3);
INSERT INTO DELEGATION_REQUEST (id, destination) VALUES (9, 'Szczecin');

alter sequence employee_id_seq restart with 4;
alter sequence permissions_id_seq restart with 4;
alter sequence doc_gen restart with 62;
