INSERT INTO categories (name)
VALUES
    ('インフラ'),
    ('バックエンド'),
    ('フロントエンド')
ON CONFLICT (name) DO NOTHING;