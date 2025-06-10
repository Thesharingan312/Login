-- 1. Consulta de transacciones con nombre de usuario y tipo de transacción
-- Muestra todas las transacciones con detalles del usuario y tipo
SELECT 
    t.id AS transaction_id,
    CONCAT(u.first_name, ' ', u.last_name) AS user_name,
    tt.name AS transaction_type,
    t.amount,
    t.category,
    t.description,
    t.created_at
FROM 
    transactions t
JOIN 
    users u ON t.user_id = u.id
JOIN 
    transaction_types tt ON t.type_id = tt.id
ORDER BY 
    t.created_at DESC;

-- 2. Resumen de presupuestos por usuario y categoría
-- Muestra los presupuestos totales por usuario y categoría
SELECT 
    CONCAT(u.first_name, ' ', u.last_name) AS user_name,
    bc.name AS category_name,
    b.year,
    b.month,
    SUM(b.total_amount) AS total_budget,
    COUNT(b.id) AS budget_count
FROM 
    budgets b
JOIN 
    users u ON b.user_id = u.id
JOIN 
    budget_categories bc ON b.category_id = bc.id
GROUP BY 
    u.id, bc.id, b.year, b.month
ORDER BY 
    u.first_name, bc.name, b.year DESC, b.month DESC;

-- 3. Ahorros con detalles de transacción relacionada
-- Muestra los ahorros junto con detalles de transacciones relacionadas
SELECT 
    s.id AS saving_id,
    CONCAT(u.first_name, ' ', u.last_name) AS user_name,
    s.name AS saving_name,
    st.name AS saving_type,
    s.amount AS saving_amount,
    t.amount AS transaction_amount,
    tt.name AS transaction_type,
    t.category AS transaction_category,
    t.description AS transaction_description
FROM 
    savings s
JOIN 
    users u ON s.user_id = u.id
JOIN 
    saving_types st ON s.type_id = st.id
LEFT JOIN 
    transactions t ON s.transaction_id = t.id
LEFT JOIN 
    transaction_types tt ON t.type_id = tt.id
ORDER BY 
    s.created_at DESC;

-- 4. Balance total por usuario (ingresos - gastos)
-- Calcula el balance de cada usuario basado en sus transacciones
SELECT 
    CONCAT(u.first_name, ' ', u.last_name) AS user_name,
    u.email,
    SUM(CASE WHEN tt.name = 'income' THEN t.amount ELSE 0 END) AS total_income,
    SUM(CASE WHEN tt.name = 'expense' THEN t.amount ELSE 0 END) AS total_expenses,
    SUM(CASE WHEN tt.name = 'income' THEN t.amount ELSE -t.amount END) AS balance
FROM 
    users u
LEFT JOIN 
    transactions t ON u.id = t.user_id
LEFT JOIN 
    transaction_types tt ON t.type_id = tt.id
GROUP BY 
    u.id
ORDER BY 
    balance DESC;

-- 5. Transacciones por categoría y usuario para un período específico
-- Muestra transacciones filtradas por mes y año para análisis temporal
SELECT 
    CONCAT(u.first_name, ' ', u.last_name) AS user_name,
    t.category,
    tt.name AS transaction_type,
    COUNT(*) AS transaction_count,
    SUM(t.amount) AS total_amount,
    MIN(t.amount) AS min_amount,
    MAX(t.amount) AS max_amount,
    AVG(t.amount) AS avg_amount
FROM 
    transactions t
JOIN 
    users u ON t.user_id = u.id
JOIN 
    transaction_types tt ON t.type_id = tt.id
WHERE 
    YEAR(t.created_at) = 2025 AND MONTH(t.created_at) = 1
GROUP BY 
    u.id, t.category, tt.name
ORDER BY 
    user_name, total_amount DESC;

-- 6. Comparación de presupuesto vs gastos reales por categoría
-- Compara lo presupuestado contra lo realmente gastado por categoría
SELECT 
    CONCAT(u.first_name, ' ', u.last_name) AS user_name,
    bc.name AS budget_category,
    b.year,
    b.month,
    b.total_amount AS budget_amount,
    COALESCE(SUM(t.amount), 0) AS actual_expenses,
    b.total_amount - COALESCE(SUM(t.amount), 0) AS difference,
    CASE 
        WHEN b.total_amount - COALESCE(SUM(t.amount), 0) >= 0 THEN 'Under budget'
        ELSE 'Over budget'
    END AS status
FROM 
    budgets b
JOIN 
    users u ON b.user_id = u.id
JOIN 
    budget_categories bc ON b.category_id = bc.id
LEFT JOIN 
    transactions t ON t.user_id = u.id 
    AND t.category = bc.name 
    AND YEAR(t.created_at) = b.year 
    AND (b.month = 0 OR MONTH(t.created_at) = b.month)
    AND EXISTS (SELECT 1 FROM transaction_types tt WHERE tt.id = t.type_id AND tt.name = 'expense')
GROUP BY 
    u.id, bc.id, b.year, b.month
ORDER BY 
    u.first_name, bc.name, b.year DESC, b.month DESC;

-- 7. Resumen de ahorros por usuario y tipo
-- Muestra un resumen de ahorros agrupados por usuario y tipo
SELECT 
    CONCAT(u.first_name, ' ', u.last_name) AS user_name,
    p.name AS profile_type,
    st.name AS saving_type,
    COUNT(*) AS saving_count,
    SUM(s.amount) AS total_saved,
    AVG(s.amount) AS average_saving
FROM 
    savings s
JOIN 
    users u ON s.user_id = u.id
JOIN 
    profiles p ON u.profile_id = p.id
JOIN 
    saving_types st ON s.type_id = st.id
GROUP BY 
    u.id, st.id
ORDER BY 
    total_saved DESC;

-- 8. Usuarios con sus presupuestos, transacciones y ahorros totales
-- Proporciona una vista completa de la actividad financiera por usuario
SELECT 
    CONCAT(u.first_name, ' ', u.last_name) AS user_name,
    u.email,
    p.name AS profile_type,
    u.base_budget,
    u.base_saving,
    COUNT(DISTINCT b.id) AS budget_count,
    SUM(b.total_amount) AS total_budgeted,
    COUNT(DISTINCT t.id) AS transaction_count,
    SUM(CASE WHEN tt.name = 'income' THEN t.amount ELSE 0 END) AS total_income,
    SUM(CASE WHEN tt.name = 'expense' THEN t.amount ELSE 0 END) AS total_expenses,
    COUNT(DISTINCT s.id) AS saving_count,
    SUM(s.amount) AS total_saved
FROM 
    users u
JOIN 
    profiles p ON u.profile_id = p.id
LEFT JOIN 
    budgets b ON u.id = b.user_id
LEFT JOIN 
    transactions t ON u.id = t.user_id
LEFT JOIN 
    transaction_types tt ON t.type_id = tt.id
LEFT JOIN 
    savings s ON u.id = s.user_id
GROUP BY 
    u.id
ORDER BY 
    total_income DESC;

-- 9. Historial detallado de actividad financiera por usuario
-- Proporciona un timeline completo de actividades financieras
SELECT 
    'Transaction' AS activity_type,
    t.id AS activity_id,
    CONCAT(u.first_name, ' ', u.last_name) AS user_name,
    tt.name AS transaction_type,
    t.category,
    t.amount,
    t.description,
    t.created_at AS activity_date
FROM 
    transactions t
JOIN 
    users u ON t.user_id = u.id
JOIN 
    transaction_types tt ON t.type_id = tt.id

UNION ALL

SELECT 
    'Budget' AS activity_type,
    b.id AS activity_id,
    CONCAT(u.first_name, ' ', u.last_name) AS user_name,
    CASE WHEN b.month = 0 THEN 'Annual' ELSE 'Monthly' END AS budget_type,
    bc.name AS category,
    b.total_amount AS amount,
    b.notes AS description,
    b.created_at AS activity_date
FROM 
    budgets b
JOIN 
    users u ON b.user_id = u.id
JOIN 
    budget_categories bc ON b.category_id = bc.id

UNION ALL

SELECT 
    'Saving' AS activity_type,
    s.id AS activity_id,
    CONCAT(u.first_name, ' ', u.last_name) AS user_name,
    st.name AS saving_type,
    s.name AS category,
    s.amount,
    s.notes AS description,
    s.created_at AS activity_date
FROM 
    savings s
JOIN 
    users u ON s.user_id = u.id
JOIN 
    saving_types st ON s.type_id = st.id
ORDER BY 
    activity_date DESC, user_name;

-- 10. Análisis de tendencias de gastos por categoría y mes
-- Muestra la evolución de gastos por categoría a lo largo del tiempo
SELECT 
    YEAR(t.created_at) AS year,
    MONTH(t.created_at) AS month,
    t.category,
    COUNT(*) AS transaction_count,
    SUM(t.amount) AS total_amount,
    AVG(t.amount) AS avg_amount,
    SUM(t.amount) / (SELECT SUM(amount) FROM transactions 
                    WHERE type_id = (SELECT id FROM transaction_types WHERE name = 'expense')
                    AND YEAR(created_at) = YEAR(t.created_at) AND MONTH(created_at) = MONTH(t.created_at)) * 100 AS percentage_of_monthly_expenses
FROM 
    transactions t
JOIN 
    transaction_types tt ON t.type_id = tt.id
WHERE 
    tt.name = 'expense'
GROUP BY 
    YEAR(t.created_at), MONTH(t.created_at), t.category
ORDER BY 
    year DESC, month DESC, total_amount DESC;
    