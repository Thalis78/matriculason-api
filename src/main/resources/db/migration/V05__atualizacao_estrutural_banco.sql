ALTER TABLE usuarios
    MODIFY COLUMN senha VARCHAR(12) NOT NULL;


SET @idx_name = (SELECT INDEX_NAME
                 FROM INFORMATION_SCHEMA.STATISTICS
                 WHERE TABLE_NAME = 'administradores'
                 AND INDEX_NAME = 'UK16vb32dwfq32vxgxd6k6awqrq');

SET @sql = IF(@idx_name IS NOT NULL, 'ALTER TABLE administradores DROP INDEX UK16vb32dwfq32vxgxd6k6awqrq', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @idx_name2 = (SELECT INDEX_NAME
                 FROM INFORMATION_SCHEMA.STATISTICS
                 WHERE TABLE_NAME = 'administradores'
                 AND INDEX_NAME = 'UKmims61qt1j972b3ggvn2dgl5c');

SET @sql2 = IF(@idx_name2 IS NOT NULL, 'ALTER TABLE administradores DROP INDEX UKmims61qt1j972b3ggvn2dgl5c', 'SELECT 1');
PREPARE stmt2 FROM @sql2;
EXECUTE stmt2;
DEALLOCATE PREPARE stmt2;


