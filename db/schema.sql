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
  `SHIPMENT_ID` INT NOT NULL,
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
  `BILLING_ID` INT NOT NULL,
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
  `CARD_ID` INT NOT NULL,
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
