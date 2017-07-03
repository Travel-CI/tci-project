CREATE USER tciprojects WITH PASSWORD 'tciprojects';
CREATE USER tcicommands WITH PASSWORD 'tcicommands';
CREATE USER tcilogger WITH PASSWORD 'tcilogger';

GRANT ALL PRIVILEGES ON DATABASE travelci to tcicommands;
GRANT ALL PRIVILEGES ON DATABASE travelci to tciprojects;

DROP TABLE IF EXISTS project CASCADE;
CREATE TABLE project (id bigserial NOT NULL, branches varchar(255), created timestamp, description varchar(255), dockerfile_location varchar(255), enable boolean, last_start timestamp, name varchar(255), repository_token varchar(255), repository_url varchar(255), updated timestamp, user_name varchar(255), user_password varchar(255), PRIMARY KEY (id));
ALTER TABLE project OWNER TO tciprojects;

DROP TABLE IF EXISTS command CASCADE;
CREATE TABLE command (id bigserial NOT NULL, command varchar(255), command_order int4, enable_logs boolean, enabled boolean, name varchar(255), project_id int8, PRIMARY KEY (id));
ALTER TABLE command ADD CONSTRAINT project_id_fk FOREIGN KEY (project_id) REFERENCES project (id) ON DELETE CASCADE;
ALTER TABLE command OWNER TO tcicommands;