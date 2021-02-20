# Each database for different services has to be defined here
CREATE DATABASE IF NOT EXISTS `courses`;
CREATE DATABASE IF NOT EXISTS `files`;
CREATE DATABASE IF NOT EXISTS `tasks`;
CREATE DATABASE IF NOT EXISTS `feedback`;
CREATE DATABASE IF NOT EXISTS `users`;

# create root user and grant rights
CREATE USER 'courseservice'@'%' IDENTIFIED BY 'course';
GRANT ALL PRIVILEGES ON courses.* TO 'courseservice'@'%';

CREATE USER 'fileservice'@'%' IDENTIFIED BY 'file';
GRANT ALL PRIVILEGES ON files.* TO 'fileservice'@'%';

CREATE USER 'taskservice'@'%' IDENTIFIED BY 'task';
GRANT ALL PRIVILEGES ON tasks.* TO 'taskservice'@'%';

CREATE USER 'feedbackservice'@'%' IDENTIFIED BY 'feedback';
GRANT ALL PRIVILEGES ON feedback.* TO 'feedbackservice'@'%';

CREATE USER 'userservice'@'%' IDENTIFIED BY 'user123';
GRANT ALL PRIVILEGES ON users.* TO 'userservice'@'%';
