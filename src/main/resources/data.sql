INSERT INTO departments (code, name, contactEmail) VALUES ('MIE', 'Mechanical and Industrial Engineering', 'reception@mie.utoronto.ca');
INSERT INTO departments (code, name, contactEmail) VALUES ('ECE', 'Electrical and Computer Engineering', 'eceinquiry@utoronto.ca');
INSERT INTO departments (code, name, contactEmail) VALUES ('MSE', 'Materials Science and Engineering', 'materials.engineering@utoronto.ca');

-- Insert students
INSERT INTO users (id, first_name, last_name, email, password, user_type) VALUES
(1111, 'Tyrion', 'Lannister', 'tyrion.lannister@mail.univ.ca', 'password', 'student'),
(2222, 'Cersei', 'Lannister', 'cersei.lannister@mail.univ.ca', 'password', 'student'),
(3333, 'Jaime', 'Lannister', 'jaime.lannister@mail.univ.ca', 'password', 'student'),
(4444, 'Daenerys', 'Targaryen', 'jaime.targaryen@mail.univ.ca', 'password', 'student'),
(5555, 'Jon', 'Snow', 'jon.snow@mail.univ.ca', 'password', 'student');

-- Insert admins
INSERT INTO users (id, first_name, last_name, email, password, user_type) VALUES
(6666, 'Varys', 'Varys', 'varys.varys@mail.univ.ca', 'password', 'admin'),
(7777, 'Petyr', 'Baelish', 'petyr.baelish@mail.univ.ca', 'password', 'admin');

-- Insert courses without preq
INSERT INTO courses (code, name, course_type, credit_value, description) VALUES
('MIE100', 'Dynamics', 'CORE', 0.5, 'This course on Newtonian mechanics considers the interactions which influence 2-D, curvilinear motion. These interactions are described in terms of the concepts of force, work, momentum and energy. Initially the focus is on the kinematics and kinetics of particles. Then, the kinematics and kinetics of systems of particles and solid bodies are examined. Finally, simple harmonic motion is discussed. The occurrence of dynamic motion in natural systems, such as planetary motion, is emphasized. Applications to engineered systems are also introduced'),
('MIE451', 'Decision Support System', 'TECHNICAL', 0.5, 'Provides students with an understanding of the role of a decision support system in an organization, its components, and the theories and techniques used to construct them. Focuses on information analysis to support organizational decision-making needs and covers topics including information retrieval, descriptive and predictive modeling using machine learning and data mining, recommendation systems, and effective visualization and communication of analytical results.'),
('APS360', 'Applied Fundamentals of Deep Learning', 'TECHNICAL', 0.5, 'A basic introduction to the history, technology, programming and applications of the fast evolving field of deep learning. Topics to be covered may include neural networks, autoencoders/decoders, recurrent neural networks, natural language processing, and generative adversarial networks. Special attention will be paid to fairness and ethics issues surrounding machine learning. An applied approach will be taken, where students get hands-on exposure to the covered techniques through the use of state-of-the-art machine learning software frameworks.'),
('MAT157', 'Analysis I', 'TECHNICAL', 0.5, 'A theoretical course in calculus; emphasizing proofs and techniques. Elementary logic, limits and continuity, least upper bounds, intermediate and extreme value theorems. Derivatives, mean value and inverse function theorems. Integrals, fundamental theorem, elementary transcendental functions. Techniques of integration. Taylor''s theorem; sequences and series; uniform convergence and power series. This course is required for the Mathematics Specialist, the Applied Mathematics Specialist, the Mathematics and Physics Specialist, and the Mathematics and Philosophy Specialist program and provides a strong theoretical mathematics background.'),
('MAT188', 'Linear Algebra', 'TECHNICAL', 0.5, 'This course covers systems of linear equations and Gaussian elimination, applications; vectors in Rn, independent sets and spanning sets; linear transformations, matrices, inverses; subspaces in Rn, basis and dimension; determinants; eigenvalues and diagonalization; systems of differential equations; dot products and orthogonal sets in Rn; projections and the Gram-Schmidt process; diagonalizing symmetric matrices; least squares approximation. Includes an introduction to numeric computation in a weekly laboratory.'),
('MAT290', 'Advanced Engineering Mathematics', 'TECHNICAL', 0.5, 'An introduction to complex variables and ordinary differential equations. Topics include: Laplace transforms, ordinary higher-order linear differential equations with constant coefficients; transform methods; complex numbers and the complex plane; complex functions; limits and continuity; derivatives and integrals; analytic functions and the Cauchy-Riemann equations; power series as analytic functions; the logarithmic and exponential functions; Cauchy''s integral theorem, Laurent series, residues, Cauchy''s integral formula, the Laplace transform as an analytic function. Examples are drawn from electrical systems.'),
('ESC194', 'Calculus I', 'TECHNICAL', 0.5, 'Topics include: theory and applications of differential and integral calculus, limits, basic theorems and elementary functions. An introduction to differential equations is also included.');

-- Insert courses with preq and its prequisites
INSERT INTO courses (code, name, course_type, credit_value, description) VALUES
('MAT257', 'Analysis II', 'TECHNICAL', 0.5, 'Topology of R^n; compactness, functions and continuity, extreme value theorem. Derivatives; inverse and implicit function theorems, maxima and minima, Lagrange multipliers. Integration; Fubini''s theorem, partitions of unity, change of variables. Differential forms. Manifolds in R^n; integration on manifolds; Stokes'' theorem for differential forms and classical versions. Some topics may vary year-to-year.'),
('MAT347', 'Groups, Rings, and Fields', 'TECHNICAL', 0.5, 'Groups, subgroups, quotient groups, Sylow theorems, Jordan-Hölder theorem, finitely generated abelian groups, solvable groups. Rings, ideals, Chinese remainder theorem; Euclidean domains and principal ideal domains: unique factorization. Noetherian rings, Hilbert basis theorem. Finitely generated modules. Field extensions, algebraic closure, straight-edge and compass constructions. Galois theory, including insolvability of the quintic.'),
('ESC195', 'Calculus II', 'TECHNICAL', 0.5, 'Topics include: techniques of integration, improper integrals, sequences, series, Taylor''s theorem, as well as an introduction to vector functions, functions of several variables, partial derivatives and the optimization of multivariable functions.'),
('AER210', 'Vector Calculus & Fluid Mechanics', 'TECHNICAL', 0.5, 'The first part covers multiple integrals and vector calculus. Topics covered include: double and triple integrals, surface area, multiple integrals in polar, cylindrical and spherical coordinates, general coordinate transformations (Jacobians), Taylor series in two variables, line and surface integrals, parametric surfaces, Green''s theorem, the divergence and Stokes''s theorems. The second part provides a general introduction to the principles of continuum fluid mechanics. The basic conservation laws are derived in both differential and integral forms using different fluid models, and the link between the two is demonstrated. Applications covered include: dimensional analysis, hydrostatics, flow visualization, incompressible and compressible frictionless flows, the speed of sound, the momentum principle, viscous flows and selected examples of real fluid flows. The students conduct two hands-on laboratory experiments involving microfluidics and flow visualization, which complement the fluid mechanics lectures and experience technical report writing.'),
('ECE367', 'Matrix Algebra & Optimization', 'TECHNICAL', 0.5, 'This course will provide students with a grounding in optimization methods and the matrix algebra upon which they are based. The first past of the course focuses on fundamental building blocks in linear algebra and their geometric interpretation: matrices, their use to represent data and as linear operators, and the matrix decompositions (such as eigen-, spectral-, and singular-vector decompositions) that reveal structural and geometric insight. The second part of the course focuses on optimization, both unconstrained and constrained, linear and non-linear, as well as convex and nonconvex; conditions for local and global optimality, as well as basic classes of optimization problems are discussed. Applications from machine learning, signal processing, and engineering are used to illustrate the techniques developed.');

INSERT INTO course_prerequisites (course_code, prerequisite_course_code) VALUES
('MAT347', 'MAT257'),
('MAT347', 'MIE100'),
('MAT257', 'MAT157'),
('ESC195', 'ESC194'),
('AER210', 'ESC195'),
('ECE367', 'AER210'),
('ECE367', 'MAT188');

-- Insert course_plans
INSERT INTO course_plans (student_id) VALUES (1111);

-- Insert semesters into course_plans
INSERT INTO semester_courses (id, semester, course_plan_id) VALUES (10, 'Fall 2024', 1), (11, 'Winter 2025', 1);

-- Insert courses into corresponding semester
INSERT INTO semester_courses_list (semester_id, course_code) VALUES 
(10, 'MIE100'),  -- Fall 2024 courses
(11, 'MIE451');  -- Winter 2025 courses

-- Insert comments
INSERT INTO comments (student_id, course_code, comment_number, body) VALUES (5555, 'MIE100', 1, 'A comment');
INSERT INTO comments (student_id, course_code, comment_number, body) VALUES (4444, 'MIE451', 1, 'A bad comment');

-- Insert minors
INSERT INTO minors (name) VALUES ('Artificial Intelligence Engineering');

-- Insert minor requirements
INSERT INTO minor_requirements (id, minor_name, required_credits) VALUES (1, 'Artificial Intelligence Engineering', 3.0);

-- Insert course_minors
INSERT INTO course_minors (course_code, minor_name) VALUES ('APS360', 'Artificial Intelligence Engineering');

-- Insert course_academic_focus
INSERT INTO course_academic_focus (course_code, academic_focus) VALUES ('APS360', 'AI_ML');

-- Insert minor_requirement_courses
INSERT INTO minor_requirement_courses (minor_requirement_id, course_code) VALUES (1, 'APS360');

-- Insert intended minors
INSERT INTO student_intended_minors (student_id, minor_name) VALUES (1111, 'Artificial Intelligence Engineering');