

/* example employee who can't do anything*/
INSERT INTO EMPLOYEE (id, address, email, first_name, last_name, last_year_days_off, password, phone, status, this_year_days_off, user_role, years_of_service) VALUES (1, 'Grodzka 12a, 30-851 Kraków', 'mail@mailowo.com', 'Jan', 'Kowalski', 0, 'haslo', '987654321', 'WORKING', 10, 'EMPLOYEE', 2);

/* example permissions*/
INSERT INTO PERMISSIONS (user_id, add_users, modify_bonus_budget) VALUES (1, FALSE, FALSE);
INSERT INTO PERMISSION_OWNERS (perm_holder, perm_setid) VALUES  (1, 1);


