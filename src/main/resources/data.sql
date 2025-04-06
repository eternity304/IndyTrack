-- Insert students
INSERT INTO users (id, first_name, last_name, email, password, user_type) VALUES
(1111, 'Tyrion', 'Lannister', 'tyrion.lannister@mail.univ.ca', 'password1234', 'student'),
(2222, 'Cersei', 'Lannister', 'cersei.lannister@mail.univ.ca', 'password1234', 'student'),
(3333, 'Jaime', 'Lannister', 'jaime.lannister@mail.univ.ca', 'password1234', 'student'),
(4444, 'Daenerys', 'Targaryen', 'daenerys.targaryen@mail.univ.ca', 'password1234', 'student'),
(5555, 'Jon', 'Snow', 'jon.snow@mail.univ.ca', 'password1234', 'student');

-- Insert admins
INSERT INTO users (id, first_name, last_name, email, password, user_type) VALUES
(6666, 'Varys', 'Varys', 'varys.varys@mail.univ.ca', 'password1234', 'admin'),
(7777, 'Petyr', 'Baelish', 'petyr.baelish@mail.univ.ca', 'password1234', 'admin');

-- Insert core first year courses
INSERT INTO courses(code, name, course_type, credit_value, description) VALUES
('APS100H1', 'Orientation to Engineering', 'CORE', 0.25, 'Designed to help students transition into first-year engineering studies, and to develop and apply a greater understanding of the post-secondary academic learning environment, the field of engineering, application of mathematics and sciences in an engineering context, and properly frame engineering (education) as a socio-technical, people-centred endeavor. Topics include techniques for effective learning, time management, problem solving, successful teamwork, effective communications, test and exam preparation, stress management and wellness, engineering ethics and professionalism, academic integrity and the Student Code of Conduct, applications of math and science in engineering undergraduate research, extra- and co-curricular involvement, and engineering disciplines and career opportunities.'), 
('APS110H1', 'Engineering Chemistry and Materials Science', 'CORE', 0.5, 'The principle of the structure-property relationship refers to an understanding of the microstructure of a solid, that is, the nature of the bonds between atoms and the spatial arrangement of atoms, which permits the explanation of observed behaviour. Observed materials behaviour includes mechanical, electrical, magnetic, optical, and corrosive behaviour. Topics covered in this course include: structure of the atom, models of the atom, electronic configuration, the electromagnetic spectrum, band theory, atomic bonding, optical transparency of solids, magnetic properties, molecular bonding, hybridized orbitals, crystal systems, lattices and structures, crystallographic notation, imperfections in solids, reaction rates, activation energy, solid-state diffusion, materials thermodynamics, free energy, and phase equilibrium.'), 
('APS111H1', 'Engineering Strategies & Practice I', 'CORE', 0.5, 'An introduction to, and implementation of, a framework for the design process, which is used to teach in context, problem solving, professional communication, and team skills. Students are introduced to design, communication and teamwork as integral and inter-related components of engineering practice. This first course in the two Engineering Strategies and Practice course sequence introduces students to the process of engineering design, including broader considerations, written professional communication, and to strategies for successful team work. Students will write a series of team and individual engineering reports.'), 
('CIV100H1', 'Mechanics', 'CORE', 0.5, 'The principles of statics are applied to composition and resolution of forces, moments and couples. The equilibrium states of structures are examined. Throughout, the free body diagram concept is emphasized. Vector algebra is used where it is most useful, and stress blocks are introduced. Shear force diagrams, bending moment diagrams and stress-strain relationships for materials are discussed. Stress and deformation in axially loaded members and flexural members (beams) are also covered.'), 
('MAT186H1', 'Calculus I', 'CORE', 0.5, 'Topics include: limits and continuity; differentiation; applications of the derivative - related rates problems, curve sketching, optimization problems, L''Hopital''s rule; definite and indefinite integrals; the Fundamental Theorem of Calculus; applications of integration in geometry, mechanics and other engineering problems.'), 
('MAT188H1', 'Linear Algebra', 'CORE', 0.5, 'This course covers systems of linear equations and Gaussian elimination, applications; vectors in Rn, independent sets and spanning sets; linear transformations, matrices, inverses; subspaces in Rn, basis and dimension; determinants; eigenvalues and diagonalization; systems of differential equations; dot products and orthogonal sets in Rn; projections and the Gram-Schmidt process; diagonalizing symmetric matrices; least squares approximation. Includes an introduction to numeric computation in a weekly laboratory.'), 
('APS106H1', 'Fundamentals of Computer Programming', 'CORE', 0.5, 'An introduction to computer systems and software. Topics include the representation of information, algorithms, programming languages, operating systems and software engineering. Emphasis is on the design of algorithms and their implementation in software. Students will develop a competency in the Python programming language. Laboratory exercises will explore the concepts of both Structure-based and Object-Oriented programming using examples drawn from mathematics and engineering applications.'), 
('APS112H1', 'Engineering Strategies & Practice II', 'CORE', 0.5, 'An introduction to, and implementation of, a framework for the design process, which is used to teach in context, problem solving, professional communication, and team skills. Students are introduced to design, communication, and teamwork as integral and inter-related components of engineering practice. Building on the first course, this second course in the two Engineering Strategies and Practice course sequence introduces students to project management, oral professional communication, and to the design process in greater depth. Students work in teams on a term length design project. Students will write a series of team based and individual engineering reports and give a team based design project oral presentation'), 
('ECE110H1', 'Electrical Fundamentals', 'CORE', 0.5, 'An overview of the physics of electricity and magnetism: Coulomb''s law, Gauss'' law, Ampere''s law, Faraday''s law. Physics of capacitors, resistors and inductors. An introduction to circuit analysis: resistive circuits, nodal and mesh analysis, 1st order RC and RL transient response and sinusoidal steady-state analysis.'), 
('MAT187H1', 'Calculus II', 'CORE', 0.5, 'Topics include: techniques of integration, an introduction to mathematical modeling with differential equations, infinite sequences and series, Taylor series, parametric and polar curves, and application to mechanics and other engineering problems.'), 
('MIE100H1', 'Dynamics', 'CORE', 0.5, 'This course on Newtonian mechanics considers the interactions which influence 2-D, curvilinear motion. These interactions are described in terms of the concepts of force, work, momentum and energy. Initially the focus is on the kinematics and kinetics of particles. Then, the kinematics and kinetics of systems of particles and solid bodies are examined. Finally, simple harmonic motion is discussed. The occurrence of dynamic motion in natural systems, such as planetary motion, is emphasized. Applications to engineered systems are also introduced.'), 
('MIE191H1', 'Seminar Course: Introduction to Mechanical and Industrial Engineering', 'CORE', 0.15, 'This is a seminar series that will preview the core fields in Mechanical and Industrial Engineering. Each seminar will be given by a professional in one of the major areas in MIE. The format will vary and may include application examples, challenges, case studies, career opportunities, etc. The purpose of the seminar series is to provide first year students with some understanding of the various options within the Department to enable them to make educated choices for second year. This course will be offered on a credit/no credit basis. Students who receive no credit for this course must re-take it in their 2S session. Students who have not received credit for this course at the end of their 2S session will not be permitted to register in session 3F.');

-- Insert core second year courses
INSERT INTO courses(code, name, course_type, credit_value, description) VALUES 
('MAT238H1', 'Differential Equations and Discrete Math', 'CORE', 0.5, 'Ordinary differential equations. Equations of first order and first degree. Linear equations of order n. Systems of simultaneous equations. Difference equations. Forecasting. Business dynamics. Basic Set Theory. Counting, Cartesian Product, Combinations, Permutations. Basic Propositional Logic and Proofs. Throughout the course: formulating and analysing differential equation, difference equation, and discrete mathematical models for real-world problems.'), 
('MIE236H1', 'Probability', 'CORE', 0.5, 'Introduction to probability (the role of probability and data in engineering; concepts of population vs. sample). Sample space and events. Definitions of probability. Conditional probability and Bayes'' rule. Concept of random variables. Discrete, continuous, and joint distributions. Statistical independence. Expectation, variance, covariance, and correlation. Important discrete and continuous distributions that explain engineering-related phenomena. Brief introduction to the homogeneous Poisson process and related distributions. How to derive distributions. Transformation of random variables. Fundamental sampling distributions, Chi-square, t, and F distributions. Central limit theorem, laws of large numbers. One sample estimation (methods of maximum likelihood, bootstrapping, and jackknife) and hypothesis testing.'), 
('MIE242H1', 'Foundations of Cognitive Psychology', 'CORE', 0.5, 'Introduction to neuroanatomy and processes that are core to perception, memory, executive functions, language, decision making, and action. Introduction to stress and emotions, regulation of thought and behaviour, and reward processing. Case studies in Addiction, Depression, Dementia, ADHD, and Dyslexia. Role of neuroimaging and brain lesions in demonstrating the functioning of different pathways and regions of interest within the brain. Use of experiments to test hypotheses concerning brain activities and computations. Conducting a literature review and reporting experimental research, use of elementary statistics, and satisfaction of research ethics requirements.'), 
('MIE250H1', 'Fundamentals of Object Oriented Programming', 'CORE', 0.5, 'Introduction to object-oriented programming using the Java programming language with heavy emphasis on practical application; variable types; console and file input/output; arithmetic; logical expressions; control structures; arrays; modularity; functions; classes and objects; access modifiers; inheritance; polymorphism; common data structures; regular expressions; GitHub; Java Swing; unit testing; introduction to complexity analysis; introduction to parallel computing; design and implementation of programs relevant to industrial engineering needs according to strict specifications.'), 
('MIE262H1', 'Deterministic Operations Research', 'CORE', 0.5, 'Introduction to deterministic operations research. Formulations of mathematical models to improve decision making; linear and integer programming; the simplex method; the revised simplex method; branch-and-bound methods; sensitivity analysis; duality; network models; network simplex method; Dijkstra''s algorithm; Prim''s and Kruskal''s algorithms; deterministic dynamic programming; applications of deterministic OR in machine learning; common metaheuristics.'), 
('MIE223H1', 'Data Science', 'CORE', 0.5, 'Introduction to the methods of Data Science. Exploratory data analysis and visualization; tools for reproducible analysis. Principles and tools for data collection; awareness of bias in collection methods. Data cleaning. Descriptive statistics and feature analysis. Assessment of data with respect to scientific theories. Data interpretation fallacies. Geographical data representation and manipulation. Text processing, the natural language processing pipeline, and sentiment analysis. Fundamentals of social network analysis and centrality measures. Cloud-based data processing.'), 
('MIE237H1', 'Statistics', 'CORE', 0.5, 'Data gathering motivation and methods (observational vs. experimental). Modeling for inference vs. prediction. Data visualizations. Two sample estimation and hypothesis testing. Choice of sample size. Fitting distributions to data. Goodness of fit tests. Simple linear regression and correlation. Multiple linear regression. Model building and model assessment. Design and analysis of single and multi-factor experiments. Analysis of variance. Fixed and random effects models. Multiple comparisons.'), 
('MIE240H1', 'Human Factors Engineering', 'CORE', 0.5, 'Introduction to principles, methods, and tools for the analysis, design, and evaluation of human-centred systems. Consideration of impacts of human physical, physiological, perceptual, and cognitive factors on the design and use of engineered systems. Basic concepts of anthropometrics, work-related hazards, shiftwork, workload, human error and reliability, system complexity, and human factors standards. The human-centred systems design process, including task analysis, user requirements generation, prototyping, and usability evaluation. Design of work/rest schedules, procedures, displays and controls, and information and training systems; design for error prevention and human-computer interaction; design for accessibility and aging populations.'), 
('MIE245H1', 'Data Structures and Algorithms', 'CORE', 0.5, 'Introduction to algorithms (principles involved in designing, analyzing, and implementing algorithms). Basic data structures (lists, sets, maps, stacks, queues). Graphs and graph search. Decision algorithms (greedy methods and approximation algorithms). Sorting, divide-and-conquer, and recursive algorithms. Trees, heaps, and priority queues. Hashing and hash tables. Algorithmic analysis: big-O complexity. Numerical methods as examples of algorithms and big-O analysis (matrix inversion, matrix decomposition, solving linear system of equations).'), 
('MIE263H1', 'Stochastic Operations Research', 'CORE', 0.5, 'Modeling and analysis of systems subject to uncertainty using probabilistic methods. Derivation and application of Bernoulli and Poisson processes, Markov chains, Markov decision processes, Monte Carlo simulation, and queuing models. Applications to engineering, health care, finance, and management.');

-- Insert core third year courses
INSERT INTO courses(code, name, course_type, credit_value, description) VALUES
('MIE353H1', 'Data Modelling', 'CORE', 0.5, 'This course provides an understanding of the principles and techniques of information modelling and data management, covering both relational theory and SQL database systems (DBMS), as well as entity-relation conceptual modelling. The course also provides an introduction to graph databases (RDF, SPARQL, and knowledge graphs), as well as UML class diagrams. The laboratory focuses on database application development using SQL DBMS, OLAP queries and data modelling.'), 
('MIE358H1', 'Engineering Economics', 'CORE', 0.5, 'This course provides students with knowledge and skills for understanding, analyzing, and solving decision making problems which involve economic concepts. These problems deal with deciding among alternatives in engineering projects with respect to costs and benefits over time. The overarching goal of the course is preparing engineers with the skills and knowledge for analyzing economic decisions quantitatively and making suitable decisions by acknowledging and incorporating the ramifications of factors like interest, depreciation, taxes, inflation, and risk in engineering projects.'), 
('MIE360H1', 'Systems Modelling and Simulation', 'CORE', 0.5, 'Principles for developing, testing and using discrete event simulation models for system performance improvement. Simulation languages, generating random variables, verifying and validating simulation models. Statistical methods for analyzing simulation model outputs, and comparing alternative system designs. Fitting input distributions, including goodness of fit tests. Role of optimization in simulation studies.'), 
('MIE370H1', 'Introduction to Machine Learning', 'CORE', 0.5, 'Intro to Machine Learning, Hypothesis Spaces, Inductive Bias. Supervised Learning: Linear and Logistic Regression. Cross Validation (CV). Support Vector Machines (SVMs) and Regression. Empirical Risk Minimization and Regularization. Unsupervised Learning: Clustering and PCA. Decision Trees, Ensembles and Random Forest. Neural Net Fundamentals. Engineering Design considerations for Deployment: Explainability, Interpretability, Bias and Fairness, Accountability, Ethics, Feedback Loops, and Technical Debt.'), 
('MIE350H1', 'Design and Analysis of Information Systems', 'CORE', 0.5, 'The course covers the software lifecycle of user-centered, computer-based information systems. Topics include software development methodologies, requirement engineering, use case analysis, process modelling, data flow diagrams, UML, design, model-driven architecture, and implementation. The course will emphasize user-centered perspectives and effective communication across the software lifecycle of information systems.'), 
('MIE359H1', 'Organization Design', 'CORE', 0.5, 'Study of work systems design in new and existing organizations. Consideration will be given to fundamental organizational theory topics such as structure, lifecycle, culture, and ethics. These concepts will be the foundation for an understanding of concepts such as bureaucracy, incentives, innovation, international business, trends in technology, and hiring. An emphasis will be placed on applying these concepts to real-world organizational examples and case studies.'), 
('MIE363H1', 'Operations and Supply Chain Management', 'CORE', 0.5, 'This course focuses on features of production/service systems and methods of modelling their operation; the material flow, information flow and control systems. Topics include demand forecasting, inventory management, supply chain management, capacity planning, and lot size planning. Emphasis will be placed on the modelling aspects of operations management, as well as the application of analytical methods in the design of production/service systems. Students will be asked to address open-ended design problems in various activities of the course.');

-- Insert core fourth year courses
INSERT INTO courses(code, name, course_type, credit_value, description) VALUES 
('MIE490Y1', 'Capstone Design', 'CORE', 1.0, 'An experience in engineering practice through a significant design project whereby student teams meet specific client needs through a creative, iterative, and open-ended design process. The project must include: • The application of disciplinary knowledge and skills to conduct engineering analysis and design, • The demonstration of engineering judgment in integrating economic, health, safety, environmental, social or other pertinent interdisciplinary factors, • Elements of teamwork, project management and client interaction, and • A demonstration of proof of the design concept.');

-- Insert technical elective courses
INSERT INTO courses(code, name, course_type, credit_value, description) VALUES 
('APS360H1', 'Applied Fundamentals of Deep Learning', 'TECHNICAL', 0.5, 'A basic introduction to the history, technology, programming and applications of the fast evolving field of deep learning. Topics to be covered may include neural networks, autoencoders/decoders, recurrent neural networks, natural language processing, and generative adversarial networks. Special attention will be paid to fairness and ethics issues surrounding machine learning. An applied approach will be taken, where students get hands-on exposure to the covered techniques through the use of state-of-the-art machine learning software frameworks.'),
('CSC384H1', 'Introduction to Artificial Intelligence', 'TECHNICAL', 0.5, 'Theories and algorithms that capture (or approximate) some of the core elements of computational intelligence. Topics include: search; logical representations and reasoning, classical automated planning, representing and reasoning with uncertainty, learning, decision making (planning) under uncertainty. Assignments provide practical experience, in both theory and programming, of the core topics.'), 
('MIE344H1', 'Ergonomic Design of Information Systems', 'TECHNICAL', 0.5, 'Application of information and interaction design principles in interactive systems. Focus on design and methods for understanding user needs, making sense of user research, prototyping, evaluation methods and iterative design. The course will include in depth coverage of rapid prototyping, scenario-based design, usability inspection methods, summative and formative usability evaluation, and comparison tests. Eye tracking, remote testing and experience/ journey mapping will be introduced.'), 
('MIE354H1', 'Business Process Engineering', 'TECHNICAL', 0.5, 'This course focuses on understanding and applying multiple perspectives for organizing, assessing, designing, and implementing integrated distributed information systems to support an organization''s objectives. The emphasis is on; 1) understanding how Business Process Management techniques and tools can contribute to align an organization''s business and information technology perspectives; 2) designing, developing, and deploying Business Processes as information systems. The course introduces Blockchain technologies, an emerging class of distributed information system providing the foundation for decentralized applications. Students will work in the laboratory to develop business processes that integrate blockchain smart contracts. The business processes will be specified using process modeling languages such as BPMN (an industry standard diagrammatic notation). Students will implement and test executable business processes that combine code generated from process models with additional programming.'), 
('MIE365H1', 'Advanced Operations Research', 'TECHNICAL', 0.5, 'Linear programming extensions: goal programming. Cooperative game theory (shapley values and nucleolus), interior point methods, large scale decomposition methods (Lagrangian relaxation, Dantzig-Wolfe decomposition, benders decomposition and column generation), stochastic programming. Karush-Kuhn-Tucker (KKT) conditions with application to quadratic programming and bi-level programming. Mathematical Programming formulation choices. Markov Decision Process (MDP) problems.'), 
('MIE368H1', 'Analytics in Action', 'TECHNICAL', 0.5, 'This course showcases the impact of analytics focusing on real world examples and case studies. Particular focus on decision analytics, where data and models are combined to ultimately improve decision-making. Methods include: linear and logistic regression, classification and regression trees, clustering, linear and integer optimization. Application areas include: healthcare, business, sports, manufacturing, finance, transportation, public sector.'), 
('MIE434H1', 'Industrial Ergonomics and the Workplace', 'TECHNICAL', 0.5, 'The Biology of Work: anatomical and physiological factors underlying the design of equipment and workplaces. Biomechanical factors governing physical workload and motor performance. Circadian rhythms and shift work. Measurement and specification of heat, light, and sound with respect to design of the work environment. The influence of practical and psychosocial factors on workplace ergonomic decision-making.'), 
('MIE304H1', 'Introduction to Quality Control', 'TECHNICAL', 0.5, 'Introduction to quality engineering. Quality standards and certification. TQM. Modeling processes with simulation. Making inferences about product quality from real or simulation output data. Introduction to statistical process control. Control charts for variables and attributes. Process capability analysis. Lot Acceptance Sampling.'), 
('MIE345H1', 'Case Studies in Human Factors and Ergonomics', 'TECHNICAL', 0.5, 'A detailed analysis will be made of several cases in which human factors methods have been applied to improve the efficiency with which human-machine systems operate. Examples will be chosen both from the area of basic ergonomics and from high technology. Emphasis will be placed on the practical use of material learned in earlier human factors courses.'), 
('MIE367H1', 'Cases in Operations Research', 'TECHNICAL', 0.5, 'This course focuses on the integration of the results from earlier operations research courses and an assessment of the different methods with regard to typical applications. The course is taught using the case method. Students are expected to analyze cases based on real applications on their own, in small groups and during lecture sessions, and solve them using commercial software packages.'), 
('MIE369H1', 'Introduction to Artificial Intelligence', 'TECHNICAL', 0.5, 'Introduction to Artificial Intelligence. Search. Constraint Satisfaction. Propositional and First-order Logic Knowledge Representation. Representing Uncertainty (Bayesian networks). Rationality and (Sequential) Decision Making under Uncertainty. Reinforcement Learning. Weak and Strong AI, AI as Engineering, Ethics and Safety in AI.'), 
('APS502H1', 'Financial Engineering', 'TECHNICAL', 0.5, 'This course will focus on capital budgeting, financial optimization, and project evaluation models and their solution techniques. In particular, linear, non-linear, and integer programming models and their solutions techniques will be studied. The course will give engineering students a background in modern capital budgeting and financial techniques that are relevant in practival engineering and commercial settings.'), 
('MIE435H1', '*Early-stage design methods', 'TECHNICAL', 0.5, 'This course will present design methods that focus on the conceptual and configuration stages of product design. Methods include: identification of unmet/underserved user needs through lead users; roles of function and affordance in products; fixation and cognitive biases as obstacles to creativity; concept generation methods (e.g., Theory of Inventive Problem Solving (TRIZ/TIPS), use of stimuli and analogy); configuration design methods (e.g., design for transformation, manufacture, assembly, reuse, repair, and recycling).'), 
('MIE440H1', '* Design of Effective Products', 'TECHNICAL', 0.5, 'Products should be used as intended to be effective. Thus, product design must better incorporate possible user behavior. For example, sustainability-minded products should be both technically efficient, and support people to use such products more sustainably. In addition, many products and systems nudge people to behave in ways contrary to the user''s best interests. To address the above, the course focuses on design that increases intended product use, and pro-social / pro-environmental behaviors. For projects, students will develop, prototype and test concepts that aim to increase desired behaviors.'), 
('MIE451H1', 'Decision Support Systems', 'TECHNICAL', 0.5, 'Provides students with an understanding of the role of a decision support system in an organization, its components, and the theories and techniques used to construct them. Focuses on information analysis to support organizational decision-making needs and covers topics including information retrieval, descriptive and predictive modeling using machine learning and data mining, recommendation systems, and effective visualization and communication of analytical results.'), 
('MIE498H1', 'Research Thesis', 'TECHNICAL', 0.5, 'An opportunity to conduct independent research under the supervision of a faculty member in MIE. Admission to the course requires the approval of a project proposal by the Undergraduate office. The proposal must: 1) Explain how the research project builds upon one or more aspects of engineering science introduced in the student''s academic program, 2) provide an estimate of a level of effort not less than 130 productive hours of work per term, 3) specify a deliverable in each term to be submitted by the last day of lectures, 4) be signed by the supervisor, and 5) be received by the Undergraduate Office one week prior to the last add day. Note: Approval to register for the fourth-year thesis course (MIE498H1 or MIE498Y1) must be obtained from the Associate Chair - Undergraduate and is normally restricted to fourth year students with a cumulative grade point average of at least 2.7.'), 
('MIE498Y1', 'Research Thesis', 'TECHNICAL', 1.0, 'An opportunity to conduct independent research under the supervision of a faculty member in MIE. Admission to the course requires the approval of a project proposal by the Undergraduate office. The proposal must: 1) Explain how the research project builds upon one or more aspects of engineering science introduced in the student''s academic program, 2) provide an estimate of a level of effort not less than 130 productive hours of work per term, 3) specify a deliverable in each term to be submitted by the last day of lectures, 4) be signed by the supervisor, and 5) be received by the Undergraduate Office one week prior to the last add day. Note: Approval to register for the fourth-year thesis course (MIE498H1 or MIE498Y1) must be obtained from the Associate Chair - Undergraduate and is normally restricted to fourth year students with a cumulative grade point average of at least 2.7.'), 
('MIE523H1', 'Engineering Psychology and Human Performance', 'TECHNICAL', 0.5, 'An examination of the relation between behavioural science and the design of human-machine systems, with special attention to advanced control room design. Human limitations on perception, attention, memory and decision making, and the design of displays and intelligent machines to supplement them. The human operator in process control and the supervisory control of automated and robotic systems. Laboratory exercises to introduce techniques of evaluating human performance.'), 
('MIE524H1', 'Data Mining', 'TECHNICAL', 0.5, 'Introduction to data mining and machine learning algorithms for very large datasets; Emphasis on creating scalable algorithms using MapReduce and Spark, as well as modern machine learning frameworks. Algorithms for high-dimensional data. Data mining and machine learning with large-scale graph data. Handling infinite data streams. Modern applications of scalable data mining and machine learning algorithms.'), 
('MIE562H1', 'Scheduling', 'TECHNICAL', 0.5, 'This course takes a practical approach to scheduling problems and solution techniques, motivating the different mathematical definitions of scheduling with real world scheduling systems and problems. Topics covered include: job shop scheduling, timetabling, project scheduling, and the variety of solution approaches including constraint programming, local search, heuristics, and dispatch rules. Also covered will be information engineering aspects of building scheduling systems for real world problems.'), 
('MIE566H1', 'Decision Making Under Uncertainty', 'TECHNICAL', 0.5, 'Methods of analysis for decision making in the face of uncertainty and opponents. Topics include subjective discrete and continuous probability, utility functions, decision trees, influence diagrams, bayesian networks, multi-attribute utility functions, static and dynamic games with complete and incomplete information, bayesian games. Supporting software.'), 
('BME466H1', 'Drug Delivery at Biological Barriers and Interfaces', 'TECHNICAL', 0.5, 'The human body is a highly interconnected network of different tissues, and there are all sorts of barriers to getting pharmaceutical drugs to the right place at the right time. In this course, the emphasis is on connecting physiology knowledge with drug delivery techniques and technologies to spark innovative new approaches. Through a combination of lectures, self-paced assignments, and collaborative group discussion, students will engage with their peers to understand course materials (including published literature), explore innovations in drug delivery technologies, and develop the skillset to conceptually design new drug delivery technologies. Modules will include topics around drug delivery and tight junctions, the blood brain barrier, the digestive system, mucous, the immune system and immunogenicity, and intracellular transport. Drug delivery topics such as engineering principles of controlled release, biodistribution, pharmacokinetics, toxicity of biomaterials/ drugs, and immune responses will also be covered.'), 
('BME488H1', 'Introduction to Immunoengineering', 'TECHNICAL', 0.5, 'Immunoengineering is the next frontier in the field of biomedical engineering (BME) where concepts from material science, synthetic biology, and engineering are used to modulate immune responses. We will focus on how interdisciplinary ideas can be used to tune both the design and delivery of therapies to stimulate, limit, or direct immune responses towards specific cellular targets or pathogens. The lecture contents will draw from textbooks and scientific journal articles to encompass theoretical principles and novel applications that will be learned through weekly assignments and collaborative discussions. The specific topics that will be covered include the development of adjuvants, antigens for B and T cell vaccines, tolerizing therapies, and immunotherapies such as adoptive cell transfer approaches.'), 
('MIE424H1', 'Optimization in Machine Learning', 'TECHNICAL', 0.5, '1. To enable deeper understanding and more flexible use of standard machine learning methods, through development of machine learning from an Optimization perspective. 2. To enable students to apply these machine learning methods to problems in finance and marketing, such as stock return forecasting, credit risk scoring, portfolio management, fraud detection and customer segmentation.'), 
('MIE457H1', 'Knowledge Modelling and Management', 'TECHNICAL', 0.5, 'This course explores both the modelling of knowledge and its management within and among organizations. Knowledge modelling will focus on knowledge types and their semantic representation. It will review emerging representations for knowledge on the World Wide Web (e.g., schemas, RDF). Knowledge management will explore the acquisition, indexing, distribution and evolution of knowledge within and among organizations. Emerging Knowledge Management System software will be used in the laboratory.'), 
('MIE469H1', 'Reliability and Maintainability Engineering', 'TECHNICAL', 0.5, 'An introduction to the life cycle costing concept for equipment acquisition, operation, and replacement decision-making. Designing for reliability and determination of optimal maintenance and replacement policies for both capital equipment and components. Introduction to quality engineering, statistical process control and process capability analysis. Topics include: identification of an item''s failure distribution and reliability function, reliability of series, parallel, and redundant systems design configurations, age and block replacement policies for components, the economic life model for capital equipment, provisioning of spare parts.'), 
('MIE519H1', '* Advanced Manufacturing Technologies', 'TECHNICAL', 0.5, 'This course is designed to provide an integrated multidisciplinary approach to Advanced Manufacturing Engineering, and provide a strong foundation including fundamentals and applications of advanced manufacturing (AM). Topics include: additive manufacturing, 3D printing, micro- and nano-manufacturing, continuous & precision manufacturing, green and biological manufacturing. New applications of AM in sectors such as automotive, aerospace, biomedical, and electronics.'), 
('MIE535H1', 'Electrification Via Electricity Markets', 'TECHNICAL', 0.5, 'Challenges of meeting net-zero, fundamentals of markets, structures and participants, spot markets, economic dispatch, day-ahead markets, optimal unit commitment, forward markets, settlement process, storage and demand management, renewable and distributed energy resources, trading over transmission networks, nodal pricing, reliability resources, generation and transmission capacity investment models, capacity markets.'), 
('MIE542H1', 'Human Factors Integration', 'TECHNICAL', 0.5, 'The integration of human factors into engineering projects. Human factors integration (HFI) process and systems organizational/process constraints, HFI tools, and HFI best practices. Examples of HFI are drawn from energy, healthcare, military, and software systems. Application of HFI theory and methods to a capstone design project, including HFI problem specification, requirements generation, concept development, communication of design issues, and consideration of risk, through an iterative and open-ended design process.'), 
('MIE561H1', 'Case Studies in Healthcare', 'TECHNICAL', 0.5, 'MIE 561 is a "cap-stone" course. Its purpose is to give students an opportunity to integrate the Industrial Engineering tools learned in previous courses by applying them to real world problems. While the specific focus of the case studies used to illustrate the application of Industrial Engineering will be the Canadian health care system, the approach to problem solving adopted in this course will be applicable to any setting. This course will provide a framework for identifying and resolving problems in a complex, unstructured decision-making environment. It will give students the opportunity to apply a problem identification framework through real world case studies. The case studies will involve people from the health care industry bringing current practical problems to the class. Students work in small groups preparing a feasibility study discussing potential approaches. Although the course is directed at Industrial Engineering fourth year and graduate students, it does not assume specific previous knowledge, and the course is open to students in other disciplines.'), 
('MIE567H1', 'Multi-agent Reinforcement Learning', 'TECHNICAL', 0.5, 'This course is to provide fundamental concepts and mathematical frameworks for sequential decision making of a team of decision makers in the presence of uncertainty. Topics include Markov decision processes, reinforcement learning, theory of games and stochastic games, multi-agent reinforcement learning and decentralized Markov decision processes. The course places an emphasize on conceptual understanding of core concepts and expects students to be able to implement the concepts to demonstrate their understanding.');

-- Insert complementary studies courses
INSERT INTO courses(code, name, course_type, credit_value, description) VALUES 
('TEP234H1', 'Entrepreneurship and Small Business', 'CS', 0.5, 'The age of enterprise has arrived. Strategic use of technology in all sorts of businesses makes the difference between success and failure for these firms. Wealth creation is a real option for many and the business atmosphere is ready for you! Increasingly, people are seeing the advantages of doing their own thing, in their own way, in their own time. Entrepreneurs can control their own lives, structure their own progress and be accountable for their own success - they can fail, but they cannot be fired! After all, engineers are the most capable people to be in the forefront of this drive to the business life of the 21st century. This course is the first of a series of two dealing with entrepreneurship and management of a small company. It is intended the student would take the follow-up course TEP432 as they progress toward their engineering degree. Therefore, it is advisable that the descriptions of both courses be studied, prior enrolling in this one. This is a limited enrolment course. If the number of students electing to take the course exceeds the class size limit, selection of the final group will be made on the basis of the "Entrepreneur''s Test". A certificate will be awarded upon the successful completion of both courses, attesting to the student having passed this Entrepreneurial Course Series at the University of Toronto. The course is based on real life issues, not theoretical developments or untried options. Topics covered include: Who is an entrepreneur; Canadian business environment; Acquisitions; Different business types (retail, wholesale, manufacturing, and services); Franchising; Human resources, Leadership, Business Law; and many others. Several invited visitors provide the student with the opportunity to meet real entrepreneurs. There will be several assignments and a session project. Please note, the 5 hours per week would be used for whatever is needed at the time. Tutorials will not normally happen as the calendar indicates them.'), 
('APS305H1', 'Energy Policy', 'CS', 0.5, 'Introduction to public policy including the role and interaction of technology and regulation, policy reinforcing/feedback cycles; procedures for legislation and policy setting at the municipal, provincial and federal levels; dimensions of energy policy; energy planning and forecasting including demand management and conservation incentives; policy institution, analysis, implementation, evaluation and evolution; Critical analyses of case studies of energy and associated environmental policies with respect to conservation and demand management for various utilities and sectors; policy derivatives for varied economic and social settings, developing countries and associated impacts.'), 
('JRE300H1', 'Fundamentals of Accounting and Finance', 'CS', 0.5, 'Introduces a brief overview of essential concepts in accounting and corporate finance. The first part of the course covers the fundamentals of accounting. We start by exploring the basic language of accounting and the fundamental concepts of financial reporting. Students learn to read and analyze basic financial statements including the statements of financial position, comprehensive income, changes in equity, and cash flows. We then introduce key management accounting concepts and explore various methods of costing for decision-making. The second part of the course covers the fundamentals of corporate finance. In the second half, students will learn how to make financial projections and how to value complex investment opportunities. Following this, students learn various techniques for controlling risk and how to determine the appropriate cost of capital. Finally, the course considers issues in cash flow management and overviews project valuation as it relates to corporate mergers.'), 
('JRE410H1', 'Markets and Competitive Strategy', 'CS', 0.5, 'Introduces the basic concepts, frameworks and methodologies useful to managers in crafting and executing entrepreneurial business strategies in technology-based and selected CPG companies. In the first part of the course, students gain an understanding of the external, internal, and dynamic environments of a business and the elements of a superior competitive position. In the second part, we focus on designing and delivering customer value, which involves strategic decisions about segmentation, targeting and positioning, and tactical decisions related to product introductions, marketing communications, distribution channels and pricing. In the third part of the course, we build on these fundamentals and examine considerations related to commercialization, modes to exploit technology/product, intellectual property, and approaches to business start-up.');

-- Insert humanities and social sciences elective courses
INSERT INTO courses(code, name, course_type, credit_value, description) VALUES 
('JRE420H1', 'People Management and Organizational Behaviour', 'HSS', 0.5, 'Spans three inter-related topics within organizational behavior and human resources: individual behaviour, group behaviour, and leadership. It provides students with both the theory and practice of how to work, lead, and thrive in organizations. Topics include theories of personality, learning, power, decision making, ethics, culture, leadership, teamwork, and motivation. These topics are taught in three ways: Case studies, role play & simulation exercises followed by class discussion, Surveys of Personality & Skills, Lectures, discussions, and readings based on the current research on the topic');

-- Insert first and second year course prerequisites
INSERT INTO course_prerequisites (course_code, prerequisite_code) VALUES 
('MAT187H1', 'MAT186H1'), 
('MIE250H1', 'APS106H1'), 
('MIE262H1', 'MAT186H1'), 
('MIE262H1', 'MAT187H1'), 
('MIE223H1','APS106H1'), 
('MIE223H1', 'MIE236H1'), 
('MIE237H1', 'MIE236H1'), 
('MIE240H1', 'MIE242H1'), 
('MIE245H1', 'MIE262H1'),  
('MIE263H1', 'MIE236H1');

-- Insert third year core course prerequisites
INSERT INTO course_prerequisites (course_code, prerequisite_code) VALUES 
('MIE353H1', 'MIE250H1'), 
('MIE358H1', 'MIE236H1'), 
('MIE360H1', 'MIE236H1'), 
('MIE370H1', 'MIE236H1'), 
('MIE370H1', 'MIE237H1'), 
('MIE350H1', 'MIE353H1'), 
('MIE359H1', 'APS111H1'), 
('MIE359H1', 'APS112H1'), 
('MIE359H1', 'MIE358H1'), 
('MIE363H1', 'MIE236H1'), 
('MIE363H1', 'MIE262H1');

-- Insert technical elective course prerequisites
INSERT INTO course_prerequisites (course_code, prerequisite_code) VALUES 
('APS360H1', 'APS106H1'), 
('APS360H1', 'MAT187H1'), 
('APS360H1', 'MAT188H1'), 
('CSC384H1', 'MIE245H1'), 
('CSC384H1', 'MIE236H1'), 
('MIE344H1', 'MIE240H1'), 
('MIE354H1', 'MIE245H1'), 
('MIE354H1', 'MIE250H1'), 
('MIE365H1', 'MIE262H1'), 
('MIE365H1', 'MIE263H1'), 
('MIE368H1', 'MIE237H1'), 
('MIE368H1', 'MIE262H1'), 
('MIE368H1', 'MIE263H1'), 
('MIE434H1', 'MIE236H1'), 
('MIE345H1', 'MIE240H1'), 
('MIE367H1', 'MIE263H1'), 
('MIE369H1', 'MIE250H1'), 
('MIE369H1', 'MIE236H1'), 
('APS502H1', 'MAT186H1'), 
('APS502H1', 'MAT187H1'), 
('APS502H1', 'MAT188H1'), 
('APS502H1', 'MIE236H1'), 
('APS502H1', 'MIE237H1'), 
('MIE451H1', 'MIE353H1'), 
('MIE451H1', 'MIE350H1'), 
('MIE524H1', 'MIE350H1'), 
('MIE524H1', 'MIE236H1'), 
('MIE524H1', 'MIE245H1'), 
('MIE562H1', 'MIE262H1'), 
('MIE566H1', 'MIE236H1'), 
('MIE424H1', 'MIE365H1'), 
('MIE457H1', 'MIE353H1'), 
('MIE457H1', 'MIE350H1'), 
('MIE469H1', 'MIE236H1'), 
('MIE469H1', 'MIE358H1'), 
('MIE535H1', 'MIE358H1'), 
('MIE542H1', 'MIE240H1');

-- Insert course_plans
INSERT INTO course_plans (student_id) VALUES (1111);

-- Insert semesters into course_plans
INSERT INTO semester_courses (id, semester, course_plan_id) VALUES 
(10, 'Fall2023', 1), 
(11, 'Winter2024', 1), 
(12, 'Fall2024', 1), 
(13, 'Winter2025', 1);

-- Insert courses into corresponding semester
INSERT INTO semester_courses_list (semester_id, course_code) VALUES 
(10, 'MIE236H1'), 
(10, 'MIE368H1'), 
(11, 'MIE237H1'), 
(11, 'MIE369H1'),
(11, 'APS360H1'),  
(12, 'MIE354H1'), 
(12, 'MIE344H1'), 
(13, 'JRE410H1'), 
(13, 'MIE451H1');

-- Insert reviews
INSERT INTO reviews (course_code, student_id, rating, comment, upload_time) VALUES 
('MIE236H1', 5555, 5, 'A good comment', '2025-02-19T14:30:15.123'), 
('MIE237H1', 5555, 4, 'A good comment', '2025-02-19T08:45:30.456789'), 
('MIE236H1', 4444, 3, 'A bad comment', '2025-02-19T23:59:59'), 
('MIE237H1', 4444, 2, 'A bad comment', '2023-08-25T12:00:00');

-- Insert course_academic_focus
INSERT INTO course_academic_focus (course_code, academic_focus) VALUES 
('MIE354H1', 'OR'), 
('MIE365H1', 'OR'), 
('MIE367H1', 'OR'), 
('MIE368H1', 'OR'), 
('APS360H1', 'AI_ML'), 
('MIE368H1', 'AI_ML'), 
('MIE369H1', 'AI_ML'), 
('MIE354H1', 'IE'), 
('MIE344H1', 'IE'), 
('MIE368H1', 'IE'), 
('MIE344H1', 'HF'), 
('MIE345H1', 'HF');

-- Insert minors
INSERT INTO minors (name) VALUES 
('Advanced Manufacturing'), 
('Artificial Intelligence Engineering'), 
('Bioengineering'), 
('Biomedical Engineering'), 
('Engineering Business'), 
('Environmental Engineering'), 
('Global Leadership'), 
('Music Performance'), 
('Nanoengineering'), 
('Robotics & Mechatronics'), 
('Sustainable Energy');

-- Insert minor_requirements
INSERT INTO minor_requirements (minor_name, required_credits) VALUES 
('Artificial Intelligence Engineering', 0.5), 
('Artificial Intelligence Engineering', 0.5), 
('Artificial Intelligence Engineering', 0.5), 
('Artificial Intelligence Engineering', 0.5), 
('Artificial Intelligence Engineering', 1.0);

-- Insert minor_requirement_courses
INSERT INTO minor_requirement_courses (minor_requirement_id, course_code) VALUES 
(1, 'APS360H1'), 
(2, 'MIE245H1'), 
(3, 'MIE369H1'), 
(3, 'CSC384H1'), 
(4, 'MIE424H1'), 
(5, 'MIE368H1'), 
(5, 'MIE451H1'), 
(5, 'MIE457H1'), 
(5, 'MIE524H1'), 
(5, 'MIE562H1'), 
(5, 'MIE566H1'), 
(5, 'MIE567H1');

-- Insert student_intended_minors
INSERT INTO student_intended_minors (student_id, minor_name) VALUES 
(1111, 'Artificial Intelligence Engineering'), 
(1111, 'Engineering Business');