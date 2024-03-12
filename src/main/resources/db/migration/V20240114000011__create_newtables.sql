CREATE TABLE `bookstore` (
    `store_id` INT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `address` VARCHAR(255) NOT NULL,
    `contact_person` VARCHAR(100),
    `open_days` VARCHAR(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `inventory` (
    `inventory_id` varchar(36) NOT NULL,
    `store_id` INT,
    `book_id` varchar(36) NOT NULL,
    `quantity` INT NOT NULL,
    PRIMARY KEY (`inventory_id`),
    FOREIGN KEY (`store_id`) REFERENCES `bookstore` (`store_id`),
    FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;