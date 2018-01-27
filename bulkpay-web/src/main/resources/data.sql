INSERT INTO tbl_role (id, name, description) VALUES
	(1, 'ROLE_ROOT', 'Root user role')
	ON DUPLICATE key UPDATE
	name=VALUES(name),
	description=VALUES(description);

INSERT INTO tbl_user (username, password, enabled, email, role_id) VALUES
  	('admin1', '$2a$10$pR6m917TSsX4uuWwKt00Ru5zoke/hUkUAU1/7ugEGNfDnzw8hz9x6', true, 'ezrankayamba@gmail.com', 1)
  	ON DUPLICATE key UPDATE
  	username=VALUES(username),
  	password=VALUES(password),
    email=VALUES(email),
    role_id=VALUES(role_id),
  	enabled=VALUES(enabled);
