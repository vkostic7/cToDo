-- Kreiranje tabele users
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255),
    password VARCHAR(255),
    invite_code VARCHAR(255),
    UNIQUE (username),
    UNIQUE (invite_code)
);

-- Kreiranje tabele tasks
CREATE TABLE tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    task_name VARCHAR(255),
    description TEXT,
    task_difficulty ENUM('EASY', 'MEDIUM', 'HARD') NOT NULL,
    task_status ENUM('OPEN', 'IN_PROGRESS', 'COMPLETED'),
    creator_id INT NOT NULL,
    FOREIGN KEY (creator_id) REFERENCES users(id)
);

-- Kreiranje tabele user_tasks
CREATE TABLE user_tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    task_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (task_id) REFERENCES tasks(id),
    UNIQUE KEY user_task_unique (user_id, task_id)
);

-- Kreiranje tabele user_connections
CREATE TABLE user_connections (
    id INT AUTO_INCREMENT PRIMARY KEY,
    inviter_id INT NOT NULL,
    invited_id INT NOT NULL,
    FOREIGN KEY (inviter_id) REFERENCES users(id),
    FOREIGN KEY (invited_id) REFERENCES users(id),
    UNIQUE KEY connection_unique (inviter_id, invited_id)
);

-- Kreiranje tabele shared_task_lists
CREATE TABLE shared_task_lists (
    id INT AUTO_INCREMENT PRIMARY KEY,
    connection_id INT UNIQUE,
    FOREIGN KEY (connection_id) REFERENCES user_connections(id)
);

-- Dodavanje foreign key za shared_task_list u tabeli tasks
ALTER TABLE tasks
ADD COLUMN shared_task_list_id INT NULL,
ADD FOREIGN KEY (shared_task_list_id) REFERENCES shared_task_lists(id);


ALTER TABLE tasks
MODIFY COLUMN task_difficulty ENUM('WEAK', 'NORMAL', 'HARD') NOT NULL;
