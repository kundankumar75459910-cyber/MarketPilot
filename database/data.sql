-- SAMPLE DATA: all companies and numbers are FICTIONAL, for education only.
-- Users are NOT seeded here: register through the app so passwords get BCrypt-hashed.
USE portfoliopro;

INSERT INTO stocks (symbol, company_name, sector, current_price, previous_price, market_cap, pe_ratio, eps, revenue) VALUES
('TCHX', 'TechNova Systems',      'Technology',  1520.50, 1498.00, 950000000000, 28.40, 53.54, 210000000000),
('INFY2', 'InfoBridge Labs',      'Technology',   845.25,  852.10, 410000000000, 24.10, 35.07, 120000000000),
('BNKA', 'Bharat Sample Bank',    'Finance',      640.75,  636.20, 520000000000, 15.60, 41.07, 180000000000),
('FINX', 'FinEdge Capital',       'Finance',      312.40,  315.90, 150000000000, 18.20, 17.16,  60000000000),
('PHRM', 'MediCure Pharma',       'Healthcare',   980.00,  971.30, 300000000000, 32.50, 30.15,  90000000000),
('ENRG', 'GreenGrid Energy',      'Energy',       270.60,  268.00, 220000000000, 12.90, 20.98, 140000000000),
('AUTO', 'MotoDrive Auto',        'Automobile',  1175.90, 1190.40, 340000000000, 21.70, 54.18, 250000000000),
('FMCG', 'DailyNeeds Consumer',   'Consumer',    2410.00, 2402.50, 580000000000, 45.30, 53.20, 160000000000),
('STEL', 'IronForge Steel',       'Metals',       155.35,  153.90, 190000000000, 9.80,  15.85, 170000000000),
('TELC', 'ConnectWave Telecom',   'Telecom',      420.80,  419.00, 260000000000, 30.10, 13.98, 130000000000);

-- Generate 60 days of SIMULATED closing prices per stock (deterministic wave that
-- converges to each stock's current_price on the last day). Requires MySQL 8+.
INSERT INTO price_history (stock_id, price_date, close_price)
WITH RECURSIVE days AS (
    SELECT 0 AS n
    UNION ALL
    SELECT n + 1 FROM days WHERE n < 59
)
SELECT s.id,
       DATE_SUB(CURDATE(), INTERVAL (59 - d.n) DAY),
       ROUND(s.current_price * (1 + 0.06 * SIN(d.n / 6 + s.id) * (59 - d.n) / 59), 2)
FROM stocks s CROSS JOIN days d;
