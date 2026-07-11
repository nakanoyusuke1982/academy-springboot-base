ALTER TABLE learning_data
ADD COLUMN item_name VARCHAR(50);

UPDATE learning_data
SET item_name = '未設定'
WHERE item_name IS NULL;

ALTER TABLE learning_data
ALTER COLUMN item_name SET NOT NULL;