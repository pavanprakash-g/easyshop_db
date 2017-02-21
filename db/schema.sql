--drop table if exists sampletable
--create table sampletable(
.............)

-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema easyshop
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema easyshop
-- -----------------------------------------------------
CREATE DATABASE IF NOT EXISTS `easyshop` DEFAULT CHARACTER SET utf8 ;
USE `easyshop` ;

-- -----------------------------------------------------
-- Table `easyshop`.`questions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `easyshop`.`questions`;

CREATE TABLE IF NOT EXISTS `easyshop`.`questions` (
  `SECURITY_QUES_ID` INT NOT NULL,
  `SECURITY_QUES_DESCRIPTION` VARCHAR(45) NOT NULL,
  `SECURITY_QUES_STATUS` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`SECURITY_QUES_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `easyshop`.`customer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `easyshop`.`customer`;

CREATE TABLE IF NOT EXISTS `easyshop`.`customer` (
  `CUST_ID` INT NOT NULL AUTO_INCREMENT,
  `CUST_FIRST_NAME` VARCHAR(45) NOT NULL,
  `CUST_LAST_NAME` VARCHAR(45) NOT NULL,
  `CUST_EMAILID` VARCHAR(45) NOT NULL,
  `CUST_PHONE_NUMBER` VARCHAR(20) NOT NULL,
  `CUST_PASSWORD` VARCHAR(255) NOT NULL,
  `ADDRESS1` VARCHAR(45) NULL,
  `ADDRESS2` VARCHAR(45) NULL,
  `CITY` VARCHAR(45) NOT NULL,
  `STATE` VARCHAR(45) NOT NULL,
  `ZIPCODE` INT NOT NULL,
  `SECURITY_QUES_ID` INT NOT NULL,
  `SECURITY_QUES_ANS` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`CUST_ID`),
  INDEX `SECURITY_QUES_ID_idx` (`SECURITY_QUES_ID` ASC),
  CONSTRAINT `SECURITY_QUES_ID`
    FOREIGN KEY (`SECURITY_QUES_ID`)
    REFERENCES `easyshop`.`questions` (`SECURITY_QUES_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '		';


-- -----------------------------------------------------
-- Table `easyshop`.`shipmentaddress`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `easyshop`.`shipmentaddress`;

CREATE TABLE IF NOT EXISTS `easyshop`.`shipmentaddress` (
  `SHIPMENT_ID` INT NOT NULL AUTO_INCREMENT,
  `CUST_ID1` INT NOT NULL,
  `SHIPMENT_ADDRESS1` VARCHAR(45) NULL,
  `SHIPMENT_ADDRESS2` VARCHAR(45) NOT NULL,
  `SHIPMENT_CITY` VARCHAR(45) NOT NULL,
  `SHIPMENT_STATE` VARCHAR(45) NOT NULL,
  `SHIPMENT_PHONE_NUMBER` INT NULL,
  PRIMARY KEY (`SHIPMENT_ID`, `SHIPMENT_ADDRESS2`),
  UNIQUE INDEX `SHIPPING_ID_UNIQUE` (`SHIPMENT_ID` ASC),
  INDEX `CUST_ID1_idx` (`CUST_ID1` ASC),
  CONSTRAINT `CUST_ID1`
    FOREIGN KEY (`CUST_ID1`)
    REFERENCES `easyshop`.`customer` (`CUST_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `easyshop`.`billingaddress`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `easyshop`.`billingaddress`;

CREATE TABLE IF NOT EXISTS `easyshop`.`billingaddress` (
  `BILLING_ID` INT NOT NULL AUTO_INCREMENT,
  `CUST_ID2` INT NOT NULL,
  `BILLING_ADDRESS1` VARCHAR(45) NULL,
  `BILLING_ADDRESS2` VARCHAR(45) NULL,
  `BILLING_CITY` VARCHAR(45) NOT NULL,
  `BILLING_STATE` VARCHAR(45) NOT NULL,
  `BILLING_ZIPCODE` INT NOT NULL,
  `BILLING_PHONE_NUMBER` INT NOT NULL,
  PRIMARY KEY (`BILLING_ID`),
  UNIQUE INDEX `BILLING_ID_UNIQUE` (`BILLING_ID` ASC),
  INDEX `CUST_ID2_idx` (`CUST_ID2` ASC),
  CONSTRAINT `CUST_ID2`
    FOREIGN KEY (`CUST_ID2`)
    REFERENCES `easyshop`.`customer` (`CUST_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `easyshop`.`card`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `easyshop`.`card`;

CREATE TABLE IF NOT EXISTS `easyshop`.`card` (
  `CARD_ID` INT NOT NULL AUTO_INCREMENT,
  `CUST_ID3` INT NOT NULL,
  `CARD_NUMBER` INT NOT NULL,
  `CARD_CVV` INT NOT NULL,
  PRIMARY KEY (`CARD_ID`),
  UNIQUE INDEX `CARD_ID_UNIQUE` (`CARD_ID` ASC),
  INDEX `CUST_ID3_idx` (`CUST_ID3` ASC),
  CONSTRAINT `CUST_ID3`
    FOREIGN KEY (`CUST_ID3`)
    REFERENCES `easyshop`.`customer` (`CUST_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

ALTER TABLE `easyshop`.`customer` ADD ACTIVE_STATUS BIT;

ALTER TABLE `easyshop`.`billingaddress` CHANGE BILLING_PHONE_NUMBER BILLING_PHONE_NUMBER VARCHAR(15) ;

ALTER TABLE `easyshop`.`shipmentaddress` CHANGE CUST_ID1 CUST_ID int;

ALTER TABLE `easyshop`.`billingaddress` CHANGE CUST_ID2 CUST_ID int;

ALTER TABLE `easyshop`.`shipmentaddress` ADD SHIPMENT_ZIPCODE int;

alter table `easyshop`.`shipmentaddress` drop primary key;

alter table `easyshop`.`billingaddress` drop primary key;

alter table `easyshop`.`shipmentaddress` change SHIPMENT_ID SHIPMENT_ID INT PRIMARY KEY AUTO_INCREMENT;

alter table `easyshop`.`billingaddress` change BILLING_ID BILLING_ID INT PRIMARY KEY AUTO_INCREMENT;

alter table `easyshop`.`customer` ADD AUTH_TOKEN varchar(45);

ALTER TABLE `easyshop`.`card` CHANGE CUST_ID3 CUST_ID int;

ALTER TABLE `easyshop`.`card` ADD CARD_EXP_MON int;

ALTER TABLE `easyshop`.`card` ADD CARD_EXP_YR int;

ALTER TABLE `easyshop`.`card` CHANGE CARD_NUMBER CARD_NUMBER varchar(25);

create table address(
 ADDRESS_ID INT PRIMARY KEY AUTO_INCREMENT,
 CUST_ID INT,
 ADDRESS1 VARCHAR(45),
 ADDRESS2 VARCHAR(45),
 CITY VARCHAR(45),
 STATE VARCHAR(45),
 ZIPCODE INT,
 PHONE_NUMBER VARCHAR(45),
 COUNTRY VARCHAR(30),
 CONSTRAINT ADDRESS_1
 FOREIGN KEY (CUST_ID) REFERENCES customer(CUST_ID));


CREATE TABLE IF NOT EXISTS `easyshop`.`item` (
  `ITEM_ID` BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `ITEM_NAME` VARCHAR(45) NOT NULL,
  `ITEM_DESCRIPTION` VARCHAR(100) NOT NULL,
  `ITEM_PRICE` FLOAT NOT NULL,
  `ITEM_QUANTITY` INT(11) NOT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;