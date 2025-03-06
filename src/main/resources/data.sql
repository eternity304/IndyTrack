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
('MAT157', 'Analysis I', 'A theoretical course in calculus; emphasizing proofs and techniques. Elementary logic, limits and continuity, least upper bounds, intermediate and extreme value theorems. Derivatives, mean value and inverse function theorems. Integrals, fundamental theorem, elementary transcendental functions. Techniques of integration. Taylor''s theorem; sequences and series; uniform convergence and power series. This course is required for the Mathematics Specialist, the Applied Mathematics Specialist, the Mathematics and Physics Specialist, and the Mathematics and Philosophy Specialist program and provides a strong theoretical mathematics background.'),
('MAT188', 'Linear Algebra', 'This course covers systems of linear equations and Gaussian elimination, applications; vectors in Rn, independent sets and spanning sets; linear transformations, matrices, inverses; subspaces in Rn, basis and dimension; determinants; eigenvalues and diagonalization; systems of differential equations; dot products and orthogonal sets in Rn; projections and the Gram-Schmidt process; diagonalizing symmetric matrices; least squares approximation. Includes an introduction to numeric computation in a weekly laboratory.'),
('MAT290', 'Advanced Engineering Mathematics', 'An introduction to complex variables and ordinary differential equations. Topics include: Laplace transforms, ordinary higher-order linear differential equations with constant coefficients; transform methods; complex numbers and the complex plane; complex functions; limits and continuity; derivatives and integrals; analytic functions and the Cauchy-Riemann equations; power series as analytic functions; the logarithmic and exponential functions; Cauchy''s integral theorem, Laurent series, residues, Cauchy''s integral formula, the Laplace transform as an analytic function. Examples are drawn from electrical systems.'),
('ESC194', 'Calculus I', 'Topics include: theory and applications of differential and integral calculus, limits, basic theorems and elementary functions. An introduction to differential equations is also included.');

-- Insert courses with preq and its prequisites
INSERT INTO courses (code, name, description)
VALUES
('MAT257', 'Analysis II', 'Topology of R^n; compactness, functions and continuity, extreme value theorem. Derivatives; inverse and implicit function theorems, maxima and minima, Lagrange multipliers. Integration; Fubini''s theorem, partitions of unity, change of variables. Differential forms. Manifolds in R^n; integration on manifolds; Stokes'' theorem for differential forms and classical versions. Some topics may vary year-to-year.'),
('MAT347', 'Groups, Rings, and Fields', 'Groups, subgroups, quotient groups, Sylow theorems, Jordan-Hölder theorem, finitely generated abelian groups, solvable groups. Rings, ideals, Chinese remainder theorem; Euclidean domains and principal ideal domains: unique factorization. Noetherian rings, Hilbert basis theorem. Finitely generated modules. Field extensions, algebraic closure, straight-edge and compass constructions. Galois theory, including insolvability of the quintic.'),
('ESC195', 'Calculus II', 'Topics include: techniques of integration, improper integrals, sequences, series, Taylor''s theorem, as well as an introduction to vector functions, functions of several variables, partial derivatives and the optimization of multivariable functions.'),  
('AER210', 'Vector Calculus & Fluid Mechanics', 'The first part covers multiple integrals and vector calculus. Topics covered include: double and triple integrals, surface area, multiple integrals in polar, cylindrical and spherical coordinates, general coordinate transformations (Jacobians), Taylor series in two variables, line and surface integrals, parametric surfaces, Green''s theorem, the divergence and Stokes''s theorems. The second part provides a general introduction to the principles of continuum fluid mechanics. The basic conservation laws are derived in both differential and integral forms using different fluid models, and the link between the two is demonstrated. Applications covered include: dimensional analysis, hydrostatics, flow visualization, incompressible and compressible frictionless flows, the speed of sound, the momentum principle, viscous flows and selected examples of real fluid flows. The students conduct two hands-on laboratory experiments involving microfluidics and flow visualization, which complement the fluid mechanics lectures and experience technical report writing.'),
('ECE367', 'Matrix Algebra & Optimization', 'This course will provide students with a grounding in optimization methods and the matrix algebra upon which they are based. The first past of the course focuses on fundamental building blocks in linear algebra and their geometric interpretation: matrices, their use to represent data and as linear operators, and the matrix decompositions (such as eigen-, spectral-, and singular-vector decompositions) that reveal structural and geometric insight. The second part of the course focuses on optimization, both unconstrained and constrained, linear and non-linear, as well as convex and nonconvex; conditions for local and global optimality, as well as basic classes of optimization problems are discussed. Applications from machine learning, signal processing, and engineering are used to illustrate the techniques developed.');

INSERT INTO coursePrerequisites(courseId, prerequisitesId)
VALUES
('MAT347', 'MAT257'),
('MAT347', 'GOT123'),
('MAT257', 'MAT157'),
('ESC195', 'ESC194'),
('AER210', 'ESC195'),
('ECE367', 'AER210'),
('ECE367', 'MAT188');  

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