DROP DATABASE IF EXISTS students;

CREATE DATABASE students DEFAULT CHARACTER SET 'utf8';

USE students;

create table groups
(
  group_id int unsigned not null auto_increment,
  group_name varchar(255) not null,
  curator varchar(255) not null,
  speciality varchar(255) not null,
  primary key (group_id)
) engine=InnoDB;

create table students
(
  student_id int unsigned not null auto_increment,
  first_name varchar(255) not null,
  surname varchar(255) not null,
  date_of_birth date not null,
  sex char(1),
  group_id int not null,
  education_year int not null,
  primary key (student_id)
) engine=InnoDB;

set names 'utf8';

insert into groups (group_name, curator, speciality) values ('First', 'Prof. Bromental Ph.D.', 'Making dogs from humans');
insert into groups (group_name, curator, speciality) values ('Second', 'Prof. Preobrazhenskyi Ph.D.', 'Making humans from dogs');

insert into students (first_name, surname, sex, date_of_birth, group_id, education_year)
values ('Eduard', 'Fadieiev', 'M', '1992-07-02', 1, 2020);

insert into students (first_name, surname, sex, date_of_birth, group_id, education_year)
values ('Ivan', 'Stepanov', 'M', '1990-03-20', 1, 2020);

insert into students (first_name, surname, sex, date_of_birth, group_id, education_year)
values ('Natalia', 'Chichikova', 'F', '1990-06-10', 1, 2020);

insert into students (first_name, surname, sex, date_of_birth, group_id, education_year)
values ('Viktor', 'Belov', 'M', '1990-01-10', 1, 2020);

insert into students (first_name, surname, sex, date_of_birth, group_id, education_year)
values ('Peter', 'Sushkin', 'M', '1991-03-12', 2, 2020);

insert into students (first_name, surname, sex, date_of_birth, group_id, education_year)
values ('Veronika', 'Kovaleva', 'F', '1991-07-19', 2, 2020);

insert into students (first_name, surname, sex, date_of_birth, group_id, education_year)
values ('Irina', 'Istomina', 'F', '1991-04-29', 2, 2020);

insert into students (first_name, surname, sex, date_of_birth, group_id, education_year)
values ('Valentina', 'Pushakereva', 'F', '1990-03-17', 2, 2020);
