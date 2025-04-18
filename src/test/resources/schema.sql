-- User donor table
CREATE TABLE IF NOT EXISTS user_donor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    user_status_id INT NOT NULL,
    is_delete BOOLEAN DEFAULT FALSE,
    delete_date TIMESTAMP NULL,
    is_update BOOLEAN DEFAULT FALSE,
    update_date TIMESTAMP NULL,
    reward_points INT DEFAULT 0
);

-- User recipient table
CREATE TABLE IF NOT EXISTS user_recipient (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    user_status_id INT NOT NULL,
    is_delete BOOLEAN DEFAULT FALSE,
    delete_date TIMESTAMP NULL,
    is_update BOOLEAN DEFAULT FALSE,
    update_date TIMESTAMP NULL,
    organization_id INT NOT NULL
); 