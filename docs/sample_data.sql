-- =============================================================
-- RevHire Sample Data Script (Oracle XE)
-- Run this as revhire_user against XEPDB1
-- =============================================================

-- STEP 1: Disable FK constraints temporarily
-- (Oracle doesn't support TRUNCATE with FK refs unless disabled)
BEGIN
  FOR c IN (SELECT constraint_name, table_name FROM user_constraints WHERE constraint_type = 'R') LOOP
    EXECUTE IMMEDIATE 'ALTER TABLE ' || c.table_name || ' DISABLE CONSTRAINT ' || c.constraint_name;
  END LOOP;
END;
/

-- STEP 2: Truncate all application tables
TRUNCATE TABLE APPLICATIONS;
TRUNCATE TABLE NOTIFICATIONS;
TRUNCATE TABLE FAVORITE_JOBS;
TRUNCATE TABLE RESUMES;
TRUNCATE TABLE JOBS;
TRUNCATE TABLE JOB_SEEKERS;
TRUNCATE TABLE EMPLOYER_PROFILES;
TRUNCATE TABLE COMPANIES;
TRUNCATE TABLE USERS;

-- Reset sequences (optional, keeps IDs clean)
DROP SEQUENCE USER_SEQ;
CREATE SEQUENCE USER_SEQ START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

DROP SEQUENCE JOB_SEEKER_SEQ;
CREATE SEQUENCE JOB_SEEKER_SEQ START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

DROP SEQUENCE EMPLOYER_PROFILE_SEQ;
CREATE SEQUENCE EMPLOYER_PROFILE_SEQ START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

DROP SEQUENCE COMPANY_SEQ;
CREATE SEQUENCE COMPANY_SEQ START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

DROP SEQUENCE JOB_SEQ;
CREATE SEQUENCE JOB_SEQ START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

DROP SEQUENCE APPLICATION_SEQ;
CREATE SEQUENCE APPLICATION_SEQ START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

DROP SEQUENCE NOTIFICATION_SEQ;
CREATE SEQUENCE NOTIFICATION_SEQ START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- STEP 3: Re-enable FK constraints
BEGIN
  FOR c IN (SELECT constraint_name, table_name FROM user_constraints WHERE constraint_type = 'R') LOOP
    EXECUTE IMMEDIATE 'ALTER TABLE ' || c.table_name || ' ENABLE CONSTRAINT ' || c.constraint_name;
  END LOOP;
END;
/

-- ================================================
-- STEP 4: Insert Users
-- Passwords are BCrypt hash for "Password@123"
-- ================================================
INSERT INTO USERS (user_id, email, password, role)
VALUES (USER_SEQ.NEXTVAL, 'priya.sharma@infosys.com',   '$2a$10$7QJ8W0kTm3u7o8fJtJqxDO8MHD5GtYO9JCjXVvJXZV3n6vb5bGqmK', 'EMPLOYER');

INSERT INTO USERS (user_id, email, password, role)
VALUES (USER_SEQ.NEXTVAL, 'rahul.nair@wipro.com',       '$2a$10$7QJ8W0kTm3u7o8fJtJqxDO8MHD5GtYO9JCjXVvJXZV3n6vb5bGqmK', 'EMPLOYER');

INSERT INTO USERS (user_id, email, password, role)
VALUES (USER_SEQ.NEXTVAL, 'deepika.rao@tcs.com',        '$2a$10$7QJ8W0kTm3u7o8fJtJqxDO8MHD5GtYO9JCjXVvJXZV3n6vb5bGqmK', 'EMPLOYER');

INSERT INTO USERS (user_id, email, password, role)
VALUES (USER_SEQ.NEXTVAL, 'anil.kumar@revature.com',    '$2a$10$7QJ8W0kTm3u7o8fJtJqxDO8MHD5GtYO9JCjXVvJXZV3n6vb5bGqmK', 'SEEKER');

INSERT INTO USERS (user_id, email, password, role)
VALUES (USER_SEQ.NEXTVAL, 'meera.patel@gmail.com',      '$2a$10$7QJ8W0kTm3u7o8fJtJqxDO8MHD5GtYO9JCjXVvJXZV3n6vb5bGqmK', 'SEEKER');

INSERT INTO USERS (user_id, email, password, role)
VALUES (USER_SEQ.NEXTVAL, 'rohan.das@gmail.com',        '$2a$10$7QJ8W0kTm3u7o8fJtJqxDO8MHD5GtYO9JCjXVvJXZV3n6vb5bGqmK', 'SEEKER');

INSERT INTO USERS (user_id, email, password, role)
VALUES (USER_SEQ.NEXTVAL, 'kavitha.rajan@gmail.com',    '$2a$10$7QJ8W0kTm3u7o8fJtJqxDO8MHD5GtYO9JCjXVvJXZV3n6vb5bGqmK', 'SEEKER');

INSERT INTO USERS (user_id, email, password, role)
VALUES (USER_SEQ.NEXTVAL, 'sameer.joshi@gmail.com',     '$2a$10$7QJ8W0kTm3u7o8fJtJqxDO8MHD5GtYO9JCjXVvJXZV3n6vb5bGqmK', 'SEEKER');

COMMIT;

-- ================================================
-- STEP 5: Insert Companies
-- ================================================
INSERT INTO COMPANIES (company_id, name, industry, description, website, location)
VALUES (COMPANY_SEQ.NEXTVAL, 'Infosys Ltd.', 'Information Technology',
  'A global leader in digital services and consulting, enabling clients in 46 countries to navigate digital transformation.',
  'https://www.infosys.com', 'Bengaluru, Karnataka');

INSERT INTO COMPANIES (company_id, name, industry, description, website, location)
VALUES (COMPANY_SEQ.NEXTVAL, 'Wipro Technologies', 'Information Technology',
  'A leading global IT company focused on cloud, data, AI and cybersecurity to accelerate the digital journeys of clients.',
  'https://www.wipro.com', 'Hyderabad, Telangana');

INSERT INTO COMPANIES (company_id, name, industry, description, website, location)
VALUES (COMPANY_SEQ.NEXTVAL, 'Tata Consultancy Services', 'IT Services & Consulting',
  'TCS is a global IT services company offering technology and innovation to businesses across the world.',
  'https://www.tcs.com', 'Mumbai, Maharashtra');

COMMIT;

-- ================================================
-- STEP 6: Insert Employer Profiles
-- Link users 1-3 to companies 1-3
-- ================================================
INSERT INTO EMPLOYER_PROFILES (employer_profile_id, user_id, company_id, name, phone, location, job_role)
VALUES (EMPLOYER_PROFILE_SEQ.NEXTVAL,
  (SELECT user_id FROM USERS WHERE email = 'priya.sharma@infosys.com'),
  (SELECT company_id FROM COMPANIES WHERE name = 'Infosys Ltd.'),
  'Priya Sharma', '+91-9876501110', 'Bengaluru, Karnataka', 'Senior HR Manager');

INSERT INTO EMPLOYER_PROFILES (employer_profile_id, user_id, company_id, name, phone, location, job_role)
VALUES (EMPLOYER_PROFILE_SEQ.NEXTVAL,
  (SELECT user_id FROM USERS WHERE email = 'rahul.nair@wipro.com'),
  (SELECT company_id FROM COMPANIES WHERE name = 'Wipro Technologies'),
  'Rahul Nair', '+91-9876502220', 'Hyderabad, Telangana', 'Talent Acquisition Lead');

INSERT INTO EMPLOYER_PROFILES (employer_profile_id, user_id, company_id, name, phone, location, job_role)
VALUES (EMPLOYER_PROFILE_SEQ.NEXTVAL,
  (SELECT user_id FROM USERS WHERE email = 'deepika.rao@tcs.com'),
  (SELECT company_id FROM COMPANIES WHERE name = 'Tata Consultancy Services'),
  'Deepika Rao', '+91-9876503330', 'Mumbai, Maharashtra', 'Recruitment Manager');

COMMIT;

-- ================================================
-- STEP 7: Insert Job Seeker Profiles
-- ================================================
INSERT INTO JOB_SEEKERS (job_seeker_id, user_id, name, phone, location, employment_status, company_name, job_role, experience, profile_completion)
VALUES (JOB_SEEKER_SEQ.NEXTVAL,
  (SELECT user_id FROM USERS WHERE email = 'anil.kumar@revature.com'),
  'Anil Kumar', '+91-9876504440', 'Hyderabad, Telangana', 'EMPLOYED', 'Revature Pvt Ltd', 'Software Engineer', '2 Years', 100);

INSERT INTO JOB_SEEKERS (job_seeker_id, user_id, name, phone, location, employment_status, company_name, job_role, experience, profile_completion)
VALUES (JOB_SEEKER_SEQ.NEXTVAL,
  (SELECT user_id FROM USERS WHERE email = 'meera.patel@gmail.com'),
  'Meera Patel', '+91-9876505550', 'Pune, Maharashtra', 'UNEMPLOYED', 'Cognizant Technology', 'Data Analyst', '3 Years', 100);

INSERT INTO JOB_SEEKERS (job_seeker_id, user_id, name, phone, location, employment_status, company_name, job_role, experience, profile_completion)
VALUES (JOB_SEEKER_SEQ.NEXTVAL,
  (SELECT user_id FROM USERS WHERE email = 'rohan.das@gmail.com'),
  'Rohan Das', '+91-9876506660', 'Kolkata, West Bengal', 'UNEMPLOYED', NULL, 'DevOps Engineer', 'Fresher', 100);

INSERT INTO JOB_SEEKERS (job_seeker_id, user_id, name, phone, location, employment_status, college_name, branch, experience, profile_completion)
VALUES (JOB_SEEKER_SEQ.NEXTVAL,
  (SELECT user_id FROM USERS WHERE email = 'kavitha.rajan@gmail.com'),
  'Kavitha Rajan', '+91-9876507770', 'Chennai, Tamil Nadu', 'STUDENT', 'IIT Madras', 'Computer Science Engineering', 'Internship', 100);

INSERT INTO JOB_SEEKERS (job_seeker_id, user_id, name, phone, location, employment_status, company_name, job_role, experience, profile_completion)
VALUES (JOB_SEEKER_SEQ.NEXTVAL,
  (SELECT user_id FROM USERS WHERE email = 'sameer.joshi@gmail.com'),
  'Sameer Joshi', '+91-9876508880', 'Mumbai, Maharashtra', 'EMPLOYED', 'Accenture India', 'Product Manager', '5 Years', 100);

COMMIT;

-- ================================================
-- STEP 8: Insert Resumes
-- ================================================
INSERT INTO RESUMES (resume_id, job_seeker_id, objective, education, experience, skills, projects, certifications)
VALUES (1,
  (SELECT job_seeker_id FROM JOB_SEEKERS WHERE name = 'Anil Kumar'),
  'Motivated Software Engineer with 2+ years of experience in Java and Spring Boot, seeking opportunities to solve complex backend challenges at scale.',
  'B.Tech in Computer Science, Osmania University, 2022 — CGPA: 8.4/10',
  'Software Engineer, Revature Pvt Ltd (2022–Present): Developed RESTful APIs using Spring Boot. Migrated monolithic HR app to microservices. Reduced API latency by 35%.',
  'Java, Spring Boot, Hibernate, Oracle SQL, REST APIs, Maven, Git, Docker',
  'RevHire – Job portal application using Spring MVC, Thymeleaf, JPA. Deployed on local Oracle XE DB.',
  'Oracle Certified Java SE Programmer, AWS Cloud Practitioner (2023)');

INSERT INTO RESUMES (resume_id, job_seeker_id, objective, education, experience, skills, projects, certifications)
VALUES (2,
  (SELECT job_seeker_id FROM JOB_SEEKERS WHERE name = 'Meera Patel'),
  'Detail-oriented Data Analyst with 3 years of experience transforming data into actionable business insights using Python and SQL.',
  'M.Sc. Data Science, Symbiosis Institute of Technology, 2021 — First Class Distinction',
  'Data Analyst, Cognizant Technology Solutions (2021–2024): Built dashboards using Power BI. Automated reporting pipelines saving 10 hours/week. Analyzed sales data for 5 enterprise clients.',
  'Python (Pandas, NumPy), SQL, Power BI, Tableau, Excel, Machine Learning basics',
  'Customer Churn Prediction Model using Random Forest with 92% accuracy.',
  'Google Data Analytics Professional Certificate, Microsoft Power BI Data Analyst (PL-300)');

INSERT INTO RESUMES (resume_id, job_seeker_id, objective, education, experience, skills, projects, certifications)
VALUES (3,
  (SELECT job_seeker_id FROM JOB_SEEKERS WHERE name = 'Kavitha Rajan'),
  'Final year CSE student passionate about cloud computing and DevOps, seeking internship to apply academic knowledge in real-world environments.',
  'B.Tech Computer Science Engineering, IIT Madras (2021–2025) — CGPA 9.1/10',
  'Intern, Zoho Corporation (Summer 2024): Contributed to CI/CD pipeline automation using Jenkins and Docker. Wrote unit tests for internal ticketing tool.',
  'Python, Java, AWS (EC2, S3, Lambda), Docker, Kubernetes, Jenkins, Linux, Git',
  'Cloud-based Student Portal deployed on AWS EC2 with S3 file storage and RDS database.',
  'AWS Certified Cloud Practitioner, Docker Essentials (IBM SkillsBuild)');

COMMIT;

-- ================================================
-- STEP 9: Insert Jobs
-- ================================================
INSERT INTO JOBS (job_id, company_id, title, location, job_type, salary_range, experience_required, description, requirements, status, posted_date)
VALUES (JOB_SEQ.NEXTVAL,
  (SELECT company_id FROM COMPANIES WHERE name = 'Infosys Ltd.'),
  'Java Full Stack Developer',
  'Bengaluru, Karnataka',
  'Full-Time',
  '6 LPA - 12 LPA',
  2,
  'Design, build and maintain efficient, reusable, and reliable Java and Angular code. Work in an Agile team delivering enterprise-scale applications.',
  'Minimum 2 years Java/Spring Boot experience. Knowledge of Angular or React. Strong SQL skills. Good communication.',
  'OPEN',
  SYSDATE - 3);

INSERT INTO JOBS (job_id, company_id, title, location, job_type, salary_range, experience_required, description, requirements, status, posted_date)
VALUES (JOB_SEQ.NEXTVAL,
  (SELECT company_id FROM COMPANIES WHERE name = 'Infosys Ltd.'),
  'Data Engineer',
  'Pune, Maharashtra',
  'Full-Time',
  '8 LPA - 15 LPA',
  3,
  'Build and maintain large-scale data pipelines. Work with big data technologies like Spark and Hadoop to process petabytes of data daily.',
  '3+ years in data engineering. Experience with PySpark, Kafka, Airflow. Strong Python and SQL skills.',
  'OPEN',
  SYSDATE - 7);

INSERT INTO JOBS (job_id, company_id, title, location, job_type, salary_range, experience_required, description, requirements, status, posted_date)
VALUES (JOB_SEQ.NEXTVAL,
  (SELECT company_id FROM COMPANIES WHERE name = 'Wipro Technologies'),
  'Cloud Solutions Architect',
  'Hyderabad, Telangana',
  'Full-Time',
  '18 LPA - 30 LPA',
  5,
  'Design cloud infrastructure solutions for enterprise clients on AWS and Azure. Lead technical workshops and develop architecture blueprints.',
  '5+ years cloud experience (AWS/Azure). Solutions Architect certification preferred. Leadership and client-facing skills required.',
  'OPEN',
  SYSDATE - 1);

INSERT INTO JOBS (job_id, company_id, title, location, job_type, salary_range, experience_required, description, requirements, status, posted_date)
VALUES (JOB_SEQ.NEXTVAL,
  (SELECT company_id FROM COMPANIES WHERE name = 'Wipro Technologies'),
  'Cybersecurity Analyst',
  'Remote',
  'Full-Time',
  '7 LPA - 13 LPA',
  1,
  'Monitor, detect, and respond to security incidents. Perform vulnerability assessments and penetration testing. Maintain SIEM dashboards.',
  '1+ year in cybersecurity. CEH or CompTIA Security+ preferred. Experience with SIEM tools like Splunk.',
  'OPEN',
  SYSDATE - 5);

INSERT INTO JOBS (job_id, company_id, title, location, job_type, salary_range, experience_required, description, requirements, status, posted_date)
VALUES (JOB_SEQ.NEXTVAL,
  (SELECT company_id FROM COMPANIES WHERE name = 'Tata Consultancy Services'),
  'DevOps Engineer',
  'Chennai, Tamil Nadu',
  'Full-Time',
  '9 LPA - 16 LPA',
  2,
  'Automate infrastructure provisioning using Terraform. Build CI/CD pipelines with Jenkins and GitHub Actions. Manage Kubernetes clusters on GCP.',
  '2+ years DevOps experience. Strong knowledge of Docker, Kubernetes, Jenkins. Scripting in Bash or Python.',
  'OPEN',
  SYSDATE - 2);

INSERT INTO JOBS (job_id, company_id, title, location, job_type, salary_range, experience_required, description, requirements, status, posted_date)
VALUES (JOB_SEQ.NEXTVAL,
  (SELECT company_id FROM COMPANIES WHERE name = 'Tata Consultancy Services'),
  'UI/UX Designer Intern',
  'Remote',
  'Internship',
  '15,000 - 25,000 per month',
  0,
  'Design intuitive user interfaces for web and mobile applications. Create wireframes, prototypes and conduct usability testing.',
  'Pursuing B.Tech/BCA/B.Des. Proficiency in Figma or Adobe XD. Strong portfolio preferred.',
  'OPEN',
  SYSDATE - 10);

COMMIT;

-- ================================================
-- DONE!
-- All tables cleared and professional data inserted.
-- ================================================
