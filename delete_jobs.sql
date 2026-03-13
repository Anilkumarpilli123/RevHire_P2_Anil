-- Delete favorite_jobs referencing 'SDE 1' jobs
DELETE FROM favorite_jobs WHERE job_id IN (SELECT job_id FROM jobs WHERE title = 'SDE 1');
-- Delete applications referencing 'SDE 1' jobs
DELETE FROM applications WHERE job_id IN (SELECT job_id FROM jobs WHERE title = 'SDE 1');
-- Delete the 'SDE 1' jobs
DELETE FROM jobs WHERE title = 'SDE 1';
COMMIT;
EXIT;
