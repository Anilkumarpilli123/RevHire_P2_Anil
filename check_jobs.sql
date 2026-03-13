SET PAGESIZE 50
SET LINESIZE 150
COLUMN title FORMAT A30
COLUMN job_id FORMAT 9999
SELECT job_id, title FROM jobs WHERE title = 'SDE 1';
SELECT COUNT(*) as app_count FROM applications WHERE job_id IN (SELECT job_id FROM jobs WHERE title = 'SDE 1');
EXIT;
