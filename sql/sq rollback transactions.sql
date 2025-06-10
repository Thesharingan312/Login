START TRANSACTION;

-- 1. Verificar que Juan (id: 1) tiene saldo suficiente
-- (En un sistema real lo harías desde backend, aquí asumimos que tiene suficiente)

-- 2. Registrar el gasto de Juan (type_id = 2 -> expense)
INSERT INTO transactions (user_id, type_id, amount, description, category_id)
VALUES (1, 2, 200.00, 'Transferencia a Ana', 1);

-- 3. Registrar el ingreso de Ana (type_id = 1 -> income)
INSERT INTO transactions (user_id, type_id, amount, description, category_id)
VALUES (2, 1, 200.00, 'Transferencia recibida de Juan', 1);

-- 4. Ajustar el ahorro de Juan (descontar)
UPDATE users
SET base_saving = base_saving - 200.00
WHERE id = 1 AND base_saving >= 200.00;

-- 5. Aumentar el ahorro de Ana
UPDATE users
SET base_saving = base_saving + 200.00
WHERE id = 2;

-- Confirmar si todo fue exitoso
COMMIT;

-- Si algo hubiera fallado, se usaría esto:
-- ROLLBACK;