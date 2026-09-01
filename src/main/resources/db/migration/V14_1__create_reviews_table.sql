CREATE TABLE `reviews` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `show_id` int(11) NOT NULL,
  `review` text NOT NULL,
  `stars` int(11) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_reviews_users` (`user_id`),
  KEY `fk_reviews_shows` (`show_id`),
  CONSTRAINT `fk_reviews_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_reviews_shows` FOREIGN KEY (`show_id`) REFERENCES `shows` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
