CREATE DATABASE IF NOT EXISTs contactBook;
USE contactBook;
DROP TABLE IF EXISTS contact;

CREATE TABLE contact (
ID INT NOT NULL AUTO_INCREMENT,
FirstName VARCHAR(30),
MiddleName VARCHAR(30),
LastName VARCHAR(30),
Email VARCHAR(30),
Major VARCHAR(30),

PRIMARY KEY(BookID)
);

INSERT INTO CONTACT 
  (FirstName, MiddleName, LastName, Email, Major)
VALUES 
  ('Marshall', 'C', 'Brown', 'mbrown62@elon.edu', 'Computer Science'),
  ('Haris', '', 'Cesko', 'hcesko@elon.edu', 'Computer Science'),
  ('Maddie', 'C', 'Chili', 'mchili@elon.edu', 'Computer Science'),
  ('Keith', 'A', 'Davis', 'kdavis36@elon.edu', 'Computer Science'),
  ('Z', 'H', 'Deirmendjian', 'zdeirmendjian@elon.edu', 'Computer Science');