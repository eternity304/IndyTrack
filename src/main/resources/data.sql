INSERT INTO departments (code, name, contactEmail) VALUES ('MIE', 'Mechanical and Industrial Engineering', 'reception@mie.utoronto.ca');
INSERT INTO departments (code, name, contactEmail) VALUES ('ECE', 'Electrical and Computer Engineering', 'eceinquiry@utoronto.ca');
INSERT INTO departments (code, name, contactEmail) VALUES ('MSE', 'Materials Science and Engineering', 'materials.engineering@utoronto.ca');

-- Insert students by first inserting into users table and then inserting into students table the id as the JOIN strategy is used for inheritance
-- If you wish to insert a student relation, you must insert that relation in both the students and the users table this needs to be done because
-- the JOIN strategy is used for inheritance and the id is the primary key for the users table

INSERT INTO users (id, firstName, lastName, email, password, status)
VALUES
(1111, 'Tyrion', 'Lannister', 'tyrion.lannister@mail.univ.ca', 'password', 'verified'),
(2222, 'Cersei', 'Lannister', 'cersei.lannister@mail.univ.ca', 'password', 'verified'),
(3333, 'Jaime', 'Lannister', 'jaime.lannister@mail.univ.ca', 'password', 'verified'),
(4444, 'Daenerys', 'Targaryen', 'jaime.targaryen@mail.univ.ca', 'password', 'verified'),
(5555, 'Jon', 'Snow', 'jon.snow@mail.univ.ca', 'password', 'verified');

INSERT INTO students (id) VALUES (1111), (2222), (3333), (4444), (5555);

-- Insert admin by first inserting into users table and then inserting into admins table the id as the JOIN strategy is used for inheritance
-- If you wish to insert an admin relation, you must insert that relation in both the admins and the users table this needs to be done because
-- the JOIN strategy is used for inheritance and the id is the primary key for the users table
INSERT INTO users (id, firstName, lastName, email, password, status)
VALUES
(6666, 'Varys', 'Varys', 'varys@mail.univ.ca', 'password', 'verified');

INSERT INTO admins (id) VALUES (6666);

-- Insert courses without preq
INSERT INTO courses (code, name, description)
VALUES
('GOT123', 'A Game of Thrones', 'AAA'),
('GOT456', 'A Clash of Kings', 'BBB'),
('GOT789', 'A Storm of Swords', 'CCC'),
('MAT257', 'Analysis II', 'Topology of R^n; compactness, functions and continuity, extreme value theorem. Derivatives; 
inverse and implicit function theorems, maxima and minima, Lagrange multipliers. Integration; Fubini''s theorem, partitions of unity, 
change of variables. Differential forms. Manifolds in R^n; integration on manifolds; Stokes'' 
theorem for differential forms and classical versions. Some topics may vary year-to-year.');

-- Insert courses with preq and its prequisites
INSERT INTO courses (code, name, description)
VALUES
('MAT347', 'Groups, Rings, and Fields', 'Groups, subgroups, quotient groups, Sylow theorems, 
Jordan-Hölder theorem, finitely generated abelian groups, solvable groups. Rings, ideals, Chinese
 remainder theorem; Euclidean domains and principal ideal domains: unique factorization. Noetherian rings, 
 Hilbert basis theorem. Finitely generated modules. Field extensions, algebraic closure, straight-edge and 
 compass constructions. Galois theory, including insolvability of the quintic.');

INSERT INTO coursePrerequisites(courseId, prerequisitesId)
VALUES
('MAT347', 'MAT257'),
('MAT347', 'GOT123');


-- Insert comments
INSERT INTO comments (studentId, courseId, time, body) VALUES (5555, 'GOT123', '1523', 'A comment');
INSERT INTO comments (studentId, courseId, time, body) VALUES (4444, 'GOT456', '1525', 'A bad comment');

-- Insert coursePlan
INSERT INTO coursePlans (studentId) 
VALUES (1111);

-- Insert semesters into coursePlan
INSERT INTO semesters (id, semester, coursePlanId) 
VALUES (10, 'Fall 2024', 1), (11, 'Winter 2025', 1);

-- Insert courses into corresponding semester
INSERT INTO semesterCoursesList (semesterId, courseId) 
VALUES 
    (10, 'GOT123'),  -- Fall 2024 courses
    (11, 'GOT456');  -- Winter 2025 courses