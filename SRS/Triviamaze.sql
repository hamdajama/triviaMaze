create database TriviaMaze;
create table TrueFalse (
	id INT,
    qestion text NOT NULL,
    correct_answer BOOLEAN NOT NULL,
    PRIMARY KEY (id) );
create table ShortAnswer (
id INT ,
qestion TEXT NOT NULL,
correct_answer varchar(255) NOT NULL,
PRIMARY KEY (id) ) ;
create table MultipleQuestion (
id INT ,
qestion TEXT NOT NULL,
correct_answer char(1) NOT NULL, 
PRIMARY KEY (id) );
create table MultipleChoice (
qestion_id int  NOT NULL, 
choice char(1) NOT NULL,
choice_text TEXT NOT NULL,
FOREIGN KEY (qestion_id) REFERENCES MultipleQuestion(id) );









 




