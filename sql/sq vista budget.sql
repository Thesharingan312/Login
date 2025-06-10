SELECT * FROM budget_vs_actual;

-- Consultar datos específicos
SELECT * FROM budget_vs_actual WHERE category = 'Food';

-- Consultar por usuario
SELECT * FROM budget_vs_actual WHERE first_name = 'Emmanuel' AND last_name = 'Pallares';

-- Ordenar por diferencia (sobregasto vs ahorro)
SELECT * FROM budget_vs_actual ORDER BY difference ASC;