# Each database for different services has to be defined here
CREATE DATABASE IF NOT EXISTS `courses`;

# create root user and grant rights
CREATE USER 'courseservice'@'%' IDENTIFIED BY 'course';
GRANT ALL PRIVILEGES ON courses.* TO 'courseservice'@'%';