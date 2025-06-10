-- Primera vista: Resumen financiero general por usuario
CREATE OR REPLACE VIEW user_financial_summary AS
SELECT 
    u.id AS user_id,
    CONCAT(u.first_name, ' ', u.last_name) AS full_name,
    u.email,
    p.name AS profile_type,
    u.base_budget,
    u.base_saving,
    -- Ingresos totales
    COALESCE((
        SELECT SUM(t_in.amount) 
        FROM transactions t_in
        JOIN transaction_types tt_in ON t_in.type_id = tt_in.id
        WHERE t_in.user_id = u.id AND tt_in.name = 'Ingreso'
    ), 0) AS total_income,
    
    -- Gastos totales
    COALESCE((
        SELECT SUM(t_ex.amount) 
        FROM transactions t_ex
        JOIN transaction_types tt_ex ON t_ex.type_id = tt_ex.id
        WHERE t_ex.user_id = u.id AND tt_ex.name = 'Gasto'
    ), 0) AS total_expenses,
    
    -- Balance actual = ingresos - gastos
    COALESCE((
        SELECT SUM(t_in.amount) 
        FROM transactions t_in
        JOIN transaction_types tt_in ON t_in.type_id = tt_in.id
        WHERE t_in.user_id = u.id AND tt_in.name = 'Ingreso'
    ), 0) - 
    COALESCE((
        SELECT SUM(t_ex.amount) 
        FROM transactions t_ex
        JOIN transaction_types tt_ex ON t_ex.type_id = tt_ex.id
        WHERE t_ex.user_id = u.id AND tt_ex.name = 'Gasto'
    ), 0) AS current_balance,
    
    -- Presupuesto total para el mes/año actual
    COALESCE((
        SELECT SUM(b.total_amount)
        FROM budgets b
        WHERE b.user_id = u.id 
          AND b.month = MONTH(CURRENT_DATE()) 
          AND b.year = YEAR(CURRENT_DATE())
    ), u.base_budget) AS total_budget,
    
    -- Ahorros totales
    COALESCE((
        SELECT SUM(s.amount)
        FROM savings s
        WHERE s.user_id = u.id
    ), u.base_saving) AS total_savings,
    
    -- Estado del ahorro
    CASE 
        WHEN (
            COALESCE((
                SELECT SUM(t_in.amount) 
                FROM transactions t_in
                JOIN transaction_types tt_in ON t_in.type_id = tt_in.id
                WHERE t_in.user_id = u.id AND tt_in.name = 'Ingreso'
            ), 0) - 
            COALESCE((
                SELECT SUM(t_ex.amount) 
                FROM transactions t_ex
                JOIN transaction_types tt_ex ON t_ex.type_id = tt_ex.id
                WHERE t_ex.user_id = u.id AND tt_ex.name = 'Gasto'
            ), 0)
        ) >= COALESCE((
            SELECT SUM(s.amount)
            FROM savings s
            WHERE s.user_id = u.id
        ), u.base_saving) THEN 'Positivo'
        ELSE 'Negativo'
    END AS saving_status,
    
    -- Estado del presupuesto
    CASE 
        WHEN COALESCE((
            SELECT SUM(t_ex.amount) 
            FROM transactions t_ex
            JOIN transaction_types tt_ex ON t_ex.type_id = tt_ex.id
            WHERE t_ex.user_id = u.id AND tt_ex.name = 'Gasto'
        ), 0) <= COALESCE((
            SELECT SUM(b.total_amount)
            FROM budgets b
            WHERE b.user_id = u.id 
              AND b.month = MONTH(CURRENT_DATE()) 
              AND b.year = YEAR(CURRENT_DATE())
        ), u.base_budget) THEN 'En presupuesto'
        ELSE 'Excedido'
    END AS budget_status,
    
    MONTH(CURRENT_DATE()) AS current_month,
    YEAR(CURRENT_DATE()) AS current_year
FROM users u
JOIN profiles p ON u.profile_id = p.id;

-- Segunda vista: Gastos por categoría por usuario
CREATE OR REPLACE VIEW user_expenses_by_category AS
SELECT 
    u.id AS user_id,
    CONCAT(u.first_name, ' ', u.last_name) AS full_name,
    c.name AS category_name,
    SUM(t.amount) AS category_amount,
    COALESCE((
        SELECT b.total_amount
        FROM budgets b
        WHERE b.user_id = u.id 
          AND b.category_id = c.id
          AND b.month = MONTH(CURRENT_DATE()) 
          AND b.year = YEAR(CURRENT_DATE())
    ), 0) AS category_budget,
    CASE 
        WHEN SUM(t.amount) <= COALESCE((
            SELECT b.total_amount
            FROM budgets b
            WHERE b.user_id = u.id 
              AND b.category_id = c.id
              AND b.month = MONTH(CURRENT_DATE()) 
              AND b.year = YEAR(CURRENT_DATE())
        ), 0) OR (
            SELECT b.total_amount
            FROM budgets b
            WHERE b.user_id = u.id 
              AND b.category_id = c.id
              AND b.month = MONTH(CURRENT_DATE()) 
              AND b.year = YEAR(CURRENT_DATE())
        ) IS NULL THEN 'En presupuesto'
        ELSE 'Excedido'
    END AS category_status
FROM users u
JOIN transactions t ON u.id = t.user_id
JOIN categories c ON t.category_id = c.id
JOIN transaction_types tt ON t.type_id = tt.id
WHERE tt.name = 'Gasto'
GROUP BY u.id, u.first_name, u.last_name, c.name;