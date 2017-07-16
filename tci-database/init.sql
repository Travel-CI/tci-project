CREATE USER tciprojects WITH PASSWORD 'tciprojects';
CREATE USER tcicommands WITH PASSWORD 'tcicommands';
CREATE USER tcilogger WITH PASSWORD 'tcilogger';

GRANT ALL PRIVILEGES ON DATABASE travelci to tcicommands;
GRANT ALL PRIVILEGES ON DATABASE travelci to tciprojects;
GRANT ALL PRIVILEGES ON DATABASE travelci to tcilogger;

DROP TABLE IF EXISTS project CASCADE;
CREATE TABLE project (id bigserial NOT NULL, branches varchar(255), created timestamp, description text, dockerfile_location varchar(255), emails varchar(255), enable boolean, last_start timestamp, name varchar(255), repository_token varchar(255), repository_url varchar(255), updated timestamp, user_name varchar(255), user_password varchar(255), PRIMARY KEY (id));
ALTER TABLE project OWNER TO tciprojects;

DROP TABLE IF EXISTS command CASCADE;
CREATE TABLE command (id bigserial NOT NULL, command varchar(255), command_order int4, enable_logs boolean, enabled boolean, project_id int8, PRIMARY KEY (id));
ALTER TABLE command ADD CONSTRAINT project_id_fk FOREIGN KEY (project_id) REFERENCES project (id) ON DELETE CASCADE;
ALTER TABLE command OWNER TO tcicommands;

DROP TABLE IF EXISTS build CASCADE;
CREATE TABLE build (id  bigserial NOT NULL, branch varchar(255), build_end timestamp, build_start timestamp, commit_hash varchar(255), commit_message varchar(255), error text, project_id int8, start_by varchar(255), status varchar(255), primary key (id));
ALTER TABLE build ADD CONSTRAINT build_project_id_fk FOREIGN KEY (project_id) REFERENCES project (id) ON DELETE CASCADE;
ALTER TABLE build OWNER TO tcilogger;

DROP TABLE IF EXISTS step CASCADE;
CREATE TABLE step (id  bigserial NOT NULL, build_id int8, command varchar(255), command_result text, status varchar(255), step_end timestamp, step_start timestamp, primary key (id));
ALTER TABLE step ADD CONSTRAINT build_id_fk FOREIGN KEY (build_id) REFERENCES build (id) ON DELETE CASCADE;
ALTER TABLE step OWNER TO tcilogger;