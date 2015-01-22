/* SQL STATEMENTS TO CREATE A DATABASE (AND TABLES) FOR RASPBERRYLIFE TO USE WITH HIBERNATE */

/* Basic database */

CREATE DATABASE IF NOT EXISTS rbl_data COLLATE utf8_general_ci;


/* table to hold the logic entries */

CREATE TABLE IF NOT EXISTS logic (
logic_table_id INT(10) NOT NULL AUTO_INCREMENT,
logic_id INT(10) NOT NULL,
logic_name VARCHAR(50) NULL DEFAULT NULL,
PRIMARY KEY (logic_table_id));


/* table to hold the actuator entries */

CREATE TABLE IF NOT EXISTS actuator (
actuator_table_id INT(10) NOT NULL AUTO_INCREMENT,
actuator_id INT(10) NOT NULL,
actuator_name VARCHAR(50) NULL DEFAULT NULL,
actuator_type VARCHAR(50) NOT NULL,
PRIMARY KEY (actuator_table_id));


/* table to hold the logic initiator actuator relations */

CREATE TABLE IF NOT EXISTS logic_initiator (
actuator_table_id INT(10) NOT NULL,
logic_table_id INT(10) NOT NULL,
PRIMARY KEY (actuator_table_id, logic_table_id),
CONSTRAINT FK_INIT_ACTUATOR FOREIGN KEY (actuator_table_id) REFERENCES actuator (actuator_table_id),
CONSTRAINT FK_INIT_LOGIC FOREIGN KEY (logic_table_id) REFERENCES logic (logic_table_id));


/* table to hold the logic receiver actuator relations */

CREATE TABLE IF NOT EXISTS logic_receiver (
actuator_table_id INT(10) NOT NULL,
logic_table_id INT(10) NOT NULL,PRIMARY KEY (actuator_table_id, logic_table_id),
CONSTRAINT FK_RECEIVE_ACTUATOR FOREIGN KEY (actuator_table_id) REFERENCES actuator (actuator_table_id),
CONSTRAINT FK_RECEIVE_LOGIC FOREIGN KEY (logic_table_id) REFERENCES logic (logic_table_id));


/* table to hold the logic initiator condition relations */
CREATE TABLE IF NOT EXISTS logic_initiator_condition (
# actuator_table_id INT(10) NOT NULL,
# logic_table_id INT(10) NOT NULL,PRIMARY KEY (actuator_table_id, logic_table_id),
# CONSTRAINT FK_RECEIVE_ACTUATOR FOREIGN KEY (actuator_table_id) REFERENCES actuator (actuator_table_id),
# CONSTRAINT FK_RECEIVE_LOGIC FOREIGN KEY (logic_table_id) REFERENCES logic (logic_table_id));

/* table to hold the logic receiver trigger relations */

CREATE TABLE IF NOT EXISTS logic_receiver_trigger (
# actuator_table_id INT(10) NOT NULL,
# logic_table_id INT(10) NOT NULL,PRIMARY KEY (actuator_table_id, logic_table_id),
# CONSTRAINT FK_RECEIVE_ACTUATOR FOREIGN KEY (actuator_table_id) REFERENCES actuator (actuator_table_id),
# CONSTRAINT FK_RECEIVE_LOGIC FOREIGN KEY (logic_table_id) REFERENCES logic (logic_table_id));

/* table to hold the logic execution definitions */

CREATE TABLE IF NOT EXISTS logic_execution (
# actuator_table_id INT(10) NOT NULL,
# logic_table_id INT(10) NOT NULL,PRIMARY KEY (actuator_table_id, logic_table_id),
# CONSTRAINT FK_RECEIVE_ACTUATOR FOREIGN KEY (actuator_table_id) REFERENCES actuator (actuator_table_id),
# CONSTRAINT FK_RECEIVE_LOGIC FOREIGN KEY (logic_table_id) REFERENCES logic (logic_table_id));