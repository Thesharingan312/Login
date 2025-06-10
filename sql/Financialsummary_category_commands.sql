-- Para ver el resumen financiero de todos los usuarios
SELECT * FROM user_financial_summary;

-- Para ver el resumen de un usuario específico
SELECT * FROM user_financial_summary WHERE user_id = 1;

-- Para ver los gastos por categoría
SELECT * FROM user_expenses_by_category;

-- Para ver los gastos por categoría de un usuario específico
SELECT * FROM user_expenses_by_category WHERE user_id = 1;