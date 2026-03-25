drop database if exists db_account;
create database db_account;
use db_account;

CREATE TABLE Accounts
(
    AccountId VARCHAR(10) PRIMARY KEY,
    FullName  NVARCHAR(50),
    Balance   DECIMAL(18, 2)
);

INSERT INTO Accounts
VALUES ('ACC01', 'Nguyen Van A', 5000),
       ('ACC02', 'Tran Thi B', 2000);

DELIMITER //
CREATE PROCEDURE sp_UpdateBalance(
    Id VARCHAR(10),
    Amount DECIMAL(18, 2)
)
BEGIN
    UPDATE Accounts SET Balance = Balance + Amount WHERE AccountId = Id;
END //
delimiter ;

select *
from accounts;