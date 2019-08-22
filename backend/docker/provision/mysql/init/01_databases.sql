# Each database for different services has to be defined here
CREATE DATABASE IF NOT EXISTS `courses`;
CREATE DATABASE IF NOT EXISTS `tasks`;

# create root user and grant rights
CREATE USER 'courseservice'@'%' IDENTIFIED BY 'course';
GRANT ALL PRIVILEGES ON courses.* TO 'courseservice'@'%';
CREATE USER 'taskservice'@'%' IDENTIFIED BY 'task';
GRANT ALL PRIVILEGES ON courses.* TO 'taskservice'@'%';