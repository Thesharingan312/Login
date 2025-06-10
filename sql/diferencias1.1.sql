-- Usar la base de datos correcta
USE `moneyshield`;

-- Poner temporalmente FOREIGN_KEY_CHECKS en 0 para evitar problemas con la modificación de FKs
SET FOREIGN_KEY_CHECKS=0;

-- -----------------------------------------------------
-- Tabla `users`
-- -----------------------------------------------------
ALTER TABLE `users`
    MODIFY COLUMN `first_name` VARCHAR(50) NOT NULL,
    MODIFY COLUMN `last_name` VARCHAR(50) NOT NULL,
    MODIFY COLUMN `email` VARCHAR(100) NOT NULL,
    MODIFY COLUMN `profile_id` INT DEFAULT NULL, -- Permitir NULL
    MODIFY COLUMN `base_budget` DECIMAL(10,2) DEFAULT 0.00, -- Permitir NULL (DEFAULT ya lo hace nullable si no hay NOT NULL)
    MODIFY COLUMN `base_saving` DECIMAL(10,2) DEFAULT 0.00; -- Permitir NULL

-- Modificar Foreign Key para users.profile_id
-- Primero obtenemos el nombre de la FK si no es 'users_ibfk_1' (asumimos que es 'users_ibfk_1' como en ambos scripts)
ALTER TABLE `users` DROP FOREIGN KEY `users_ibfk_1`;
ALTER TABLE `users` ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`profile_id`) 
    REFERENCES `profiles` (`id`) 
    ON DELETE SET NULL 
    ON UPDATE CASCADE;

-- -----------------------------------------------------
-- Tabla `categories`
-- -----------------------------------------------------
ALTER TABLE `categories`
    MODIFY COLUMN `name` VARCHAR(50) NOT NULL;

-- Para alinear el nombre del UNIQUE KEY (opcional si la funcionalidad es la misma, pero para coincidencia exacta)
-- Es posible que necesites verificar el nombre exacto de tu constraint UK_name_category si es diferente
-- SHOW CREATE TABLE categories; para ver el nombre exacto.
-- Asumiendo que el nombre es UK_name_category como en tu script:
-- ALTER TABLE `categories` DROP INDEX `UK_name_category`; 
-- ALTER TABLE `categories` ADD UNIQUE KEY `name` (`name`);
-- Si el nombre del constraint ya es 'name' en tu DB implementada, no necesitas hacer nada para el UNIQUE KEY.
-- El dump `MoneyShieldComplete.sql` simplemente define `UNIQUE KEY name (name)`, 
-- que MySQL nombrará 'name' o algo similar si no se especifica un nombre de constraint.
-- Si tu `moneyshield-script.sql` creó `UNIQUE KEY UK_name_category (name)`, puedes dejarlo o cambiarlo.
-- Para simplicidad, si la columna es la misma, este cambio de nombre de constraint es de baja prioridad.
-- Si quieres que sea exactamente 'name':
-- Si existe un constraint con nombre diferente:
-- ALTER TABLE `categories` DROP INDEX `UK_name_category`; -- O el nombre que tenga
-- ALTER TABLE `categories` ADD CONSTRAINT `name` UNIQUE (`name`); -- O simplemente ADD UNIQUE KEY `name` (`name`);


-- -----------------------------------------------------
-- Tabla `transactions`
-- -----------------------------------------------------
ALTER TABLE `transactions`
    DROP COLUMN `transaction_date`,
    ADD COLUMN `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP AFTER `description`; -- O en la posición que prefieras

-- Modificar Foreign Keys para `transactions`
-- FK para user_id
ALTER TABLE `transactions` DROP FOREIGN KEY `transactions_ibfk_1`;
ALTER TABLE `transactions` ADD CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`user_id`) 
    REFERENCES `users` (`id`) 
    ON DELETE CASCADE 
    ON UPDATE CASCADE;

-- FK para type_id
ALTER TABLE `transactions` DROP FOREIGN KEY `transactions_ibfk_2`;
ALTER TABLE `transactions` ADD CONSTRAINT `transactions_ibfk_2` FOREIGN KEY (`type_id`) 
    REFERENCES `transaction_types` (`id`) 
    ON UPDATE CASCADE; -- ON DELETE RESTRICT (comportamiento por defecto si no se especifica)

-- FK para category_id
ALTER TABLE `transactions` DROP FOREIGN KEY `transactions_ibfk_3`;
ALTER TABLE `transactions` ADD CONSTRAINT `transactions_ibfk_3` FOREIGN KEY (`category_id`) 
    REFERENCES `categories` (`id`) 
    ON DELETE SET NULL 
    ON UPDATE CASCADE;

-- -----------------------------------------------------
-- Tabla `savings`
-- -----------------------------------------------------
ALTER TABLE `savings`
    MODIFY COLUMN `amount` DECIMAL(15,2) NOT NULL;

-- Modificar Foreign Keys para `savings`
-- FK para user_id
ALTER TABLE `savings` DROP FOREIGN KEY `savings_ibfk_1`;
ALTER TABLE `savings` ADD CONSTRAINT `savings_ibfk_1` FOREIGN KEY (`user_id`) 
    REFERENCES `users` (`id`) 
    ON DELETE CASCADE 
    ON UPDATE CASCADE;

-- FK para type_id
ALTER TABLE `savings` DROP FOREIGN KEY `savings_ibfk_2`;
ALTER TABLE `savings` ADD CONSTRAINT `savings_ibfk_2` FOREIGN KEY (`type_id`) 
    REFERENCES `saving_types` (`id`) 
    ON UPDATE CASCADE; -- ON DELETE RESTRICT (comportamiento por defecto si no se especifica)

-- -----------------------------------------------------
-- Tabla `budgets`
-- -----------------------------------------------------
-- No hay cambios de columnas, solo de FKs y potencialmente nombres de índices.

-- Modificar Foreign Keys para `budgets`
-- FK para user_id
ALTER TABLE `budgets` DROP FOREIGN KEY `budgets_ibfk_1`;
ALTER TABLE `budgets` ADD CONSTRAINT `budgets_ibfk_1` FOREIGN KEY (`user_id`) 
    REFERENCES `users` (`id`) 
    ON DELETE CASCADE 
    ON UPDATE CASCADE;

-- FK para category_id
ALTER TABLE `budgets` DROP FOREIGN KEY `budgets_ibfk_2`;
ALTER TABLE `budgets` ADD CONSTRAINT `budgets_ibfk_2` FOREIGN KEY (`category_id`) 
    REFERENCES `categories` (`id`) 
    ON UPDATE CASCADE; -- ON DELETE RESTRICT (comportamiento por defecto si no se especifica)

-- -----------------------------------------------------
-- Vistas
-- -----------------------------------------------------

-- La vista `user_monthly_budget_usage` de `MoneyShieldComplete.sql`
-- Si la quieres y tienes una llamada `user_monthly_budget_summary` en `moneyshield-script.sql` que es su equivalente
-- y quieres reemplazarla (considerando el cambio de `transaction_date` a `created_at` en la tabla `transactions`):

DROP VIEW IF EXISTS `user_monthly_budget_summary`; -- Elimina la versión antigua si existe con este nombre
DROP VIEW IF EXISTS `user_monthly_budget_usage`; -- Elimina por si acaso

/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `user_monthly_budget_usage` AS 
select 
    `u`.`id` AS `user_id`,
    concat(`u`.`first_name`,' ',`u`.`last_name`) AS `user_name`,
    `p`.`name` AS `profile_type`,
    date_format(`t`.`created_at`,'%Y-%m') AS `t_month`, -- Usar created_at
    sum(`t`.`amount`) AS `total_expenses`,
    coalesce(sum(`b`.`total_amount`),0) AS `total_budget`,
    round((case 
        when (coalesce(sum(`b`.`total_amount`),0) = 0) then 0 
        else ((sum(`t`.`amount`) / sum(`b`.`total_amount`)) * 100) 
    end),2) AS `budget_usage_percentage` 
from 
    (((`transactions` `t` join `users` `u` on((`t`.`user_id` = `u`.`id`))) 
    join `profiles` `p` on((`u`.`profile_id` = `p`.`id`))) 
    left join `budgets` `b` on(((`b`.`user_id` = `u`.`id`) and (`b`.`month` = month(`t`.`created_at`)) and (`b`.`year` = year(`t`.`created_at`)) and (`b`.`category_id` = `t`.`category_id`)))) -- Añadido para que el budget coincida con la categoría del gasto
where 
    (`t`.`type_id` = 2) -- Asumiendo que 2 es Gasto
group by 
    `u`.`id`, `u`.`first_name`, `u`.`last_name`, `p`.`name`, `t_month`;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;


-- Las vistas `budget_vs_actual` y `savings_summary` existen en `moneyshield-script.sql`
-- pero no en `MoneyShieldComplete.sql`. Si quieres mantenerlas, no hagas nada.
-- Si `MoneyShieldComplete.sql` fuera la única fuente de verdad y estas no estuvieran,
-- deberías borrarlas. Pero tu petición es "agregar las diferencias de Complete a la implementada".

-- El procedimiento `CategorizeNullTransactions` existe en `moneyshield-script.sql`
-- pero no en `MoneyShieldComplete.sql`. Mismo caso que las vistas extra.

-- Reactivar la comprobación de claves foráneas
SET FOREIGN_KEY_CHECKS=1;