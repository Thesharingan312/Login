CREATE OR REPLACE VIEW user_monthly_budget_usage AS
SELECT
  u.id AS user_id,
  CONCAT(u.first_name, ' ', u.last_name) AS user_name,
  p.name AS profile_type,
  DATE_FORMAT(t.created_at, '%Y-%m') AS t_month,
  SUM(t.amount) AS total_expenses,
  COALESCE(SUM(b.total_amount), 0) AS total_budget,
  ROUND(
    CASE 
      WHEN COALESCE(SUM(b.total_amount), 0) = 0 THEN 0
      ELSE (SUM(t.amount) / SUM(b.total_amount)) * 100
    END
  , 2) AS budget_usage_percentage
FROM transactions t
JOIN users u ON t.user_id = u.id
JOIN profiles p ON u.profile_id = p.id
LEFT JOIN budgets b 
  ON b.user_id = u.id 
  AND b.month = MONTH(t.created_at)
  AND b.year = YEAR(t.created_at)
WHERE t.type_id = 2 -- Solo gastos
GROUP BY u.id, t_month;