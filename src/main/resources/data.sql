INSERT INTO departments (code, name, contactEmail) VALUES ('MIE', 'Mechanical and Industrial Engineering', 'reception@mie.utoronto.ca');
INSERT INTO departments (code, name, contactEmail) VALUES ('ECE', 'Electrical and Computer Engineering', 'eceinquiry@utoronto.ca');
INSERT INTO departments (code, name, contactEmail) VALUES ('MSE', 'Materials Science and Engineering', 'materials.engineering@utoronto.ca');

INSERT INTO students (id, firstName, lastName, email, password, status) VALUES (1111, 'Tyrion', 'Lannister', 'tyrion.lannister@mail.univ.ca', 'password', 'verified');
INSERT INTO students (id, firstName, lastName, email, password, status) VALUES (2222, 'Cersei', 'Lannister', 'cersei.lannister@mail.univ.ca', 'password', 'verified');
INSERT INTO students (id, firstName, lastName, email, password, status) VALUES (3333, 'Jaime', 'Lannister', 'jaime.lannister@mail.univ.ca', 'password', 'verified');
INSERT INTO students (id, firstName, lastName, email, password, status) VALUES (4444, 'Daenerys', 'Targaryen', 'jaime.targaryen@mail.univ.ca', 'password', 'verified');
INSERT INTO students (id, firstName, lastName, email, password, status) VALUES (5555, 'Jon', 'Snow', 'jon.snow@mail.univ.ca', 'password', 'verified');

INSERT INTO courses (code, name, description) VALUES ('GOT123', 'A Game of Thrones', 'AAA');
INSERT INTO courses (code, name, description) VALUES ('GOT456', 'A Clash of Kings', 'BBB');
INSERT INTO courses (code, name, description) VALUES ('GOT789', 'A Storm of Swords', 'CCC');

INSERT INTO comments (studentId, courseId, time, body) VALUES (5555, 'GOT123', '1523', 'A comment');