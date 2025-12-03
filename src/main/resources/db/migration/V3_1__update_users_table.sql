ALTER TABLE `users` ADD UNIQUE(`login`);
ALTER TABLE `users` ADD UNIQUE(`email`);
ALTER TABLE `users` ADD `deleted_at` DATETIME NULL AFTER `created_at`;