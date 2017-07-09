insert into command (id, command, project_id, command_order, enabled, enable_logs) values (1, 'docker build -t test .', 1, 1, 1, 1);
insert into command (id, command, project_id, command_order, enabled, enable_logs) values (2, './gradlew build', 1, 3, 1, 1);
insert into command (id, command, project_id, command_order, enabled, enable_logs) values (3, './gradlew test', 1, 2, 1, 1);
insert into command (id, command, project_id, command_order, enabled, enable_logs) values (4, 'docker build -t test .', 2, 1, 1, 1);
insert into command (id, command, project_id, command_order, enabled, enable_logs) values (5, './gradlew build', 2, 2, 1, 1);