insert into command (id, name, command, project_id, command_order, enabled, enable_logs) values (1, 'Build Docker Image', 'docker build -t test .', 1, 1, 1, 1);
insert into command (id, name, command, project_id, command_order, enabled, enable_logs) values (2, 'Gradle build', './gradlew build', 1, 3, 1, 1);
insert into command (id, name, command, project_id, command_order, enabled, enable_logs) values (3, 'Gradle test', './gradlew test', 1, 2, 1, 1);
insert into command (id, name, command, project_id, command_order, enabled, enable_logs) values (4, 'Build Docker Image', 'docker build -t test .', 2, 1, 1, 1);
insert into command (id, name, command, project_id, command_order, enabled, enable_logs) values (5, 'Gradle build', './gradlew build', 2, 2, 1, 1);