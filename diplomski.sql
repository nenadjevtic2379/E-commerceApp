SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema ecommerce
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema ecommerce
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ecommerce` DEFAULT CHARACTER SET utf8 ;
USE `ecommerce` ;

-- -----------------------------------------------------
-- Table `ecommerce`.`prodavnica`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ecommerce`.`prodavnica` (
  `ID_PRODAVNICA` INT(11) NOT NULL AUTO_INCREMENT,
  `NAZIV_PRODAVNICA` MEDIUMTEXT NULL DEFAULT NULL,
  `ID_USER` INT(11) NULL DEFAULT NULL,
  `ID_ZADUZENOG` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_PRODAVNICA`),
  UNIQUE INDEX `PRODAVNICA_PK` (`ID_PRODAVNICA` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 57
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `ecommerce`.`proizvod`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ecommerce`.`proizvod` (
  `ID_PROIZVOD` INT(11) NOT NULL AUTO_INCREMENT,
  `ID_USER` INT(11) NULL DEFAULT NULL,
  `ID_PRODAVNICA` INT(11) NULL DEFAULT NULL,
  `NAZIV_PROIZVOD` MEDIUMTEXT NULL DEFAULT NULL,
  `ROKUPOTREBE` MEDIUMTEXT NULL DEFAULT NULL,
  `STANJE` INT(11) NULL DEFAULT NULL,
  `MINIMUM` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_PROIZVOD`),
  UNIQUE INDEX `PROIZVOD_PK` (`ID_PROIZVOD` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 43
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `ecommerce`.`userrole`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ecommerce`.`userrole` (
  `ID_ROLE` INT(11) NOT NULL AUTO_INCREMENT,
  `NAZIV` MEDIUMTEXT NULL DEFAULT NULL,
  PRIMARY KEY (`ID_ROLE`),
  UNIQUE INDEX `USERROLE_PK` (`ID_ROLE` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `ecommerce`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ecommerce`.`user` (
  `ID_USER` INT(11) NOT NULL AUTO_INCREMENT,
  `ID_ROLE` INT(11) NULL DEFAULT NULL,
  `IME` MEDIUMTEXT NULL DEFAULT NULL,
  `PREZIME` MEDIUMTEXT NULL DEFAULT NULL,
  `JMBG` MEDIUMTEXT NULL DEFAULT NULL,
  `USERNAME` MEDIUMTEXT NULL DEFAULT NULL,
  `PASSWORD` MEDIUMTEXT NULL DEFAULT NULL,
  `EMAIL` MEDIUMTEXT NULL DEFAULT NULL,
  `ADDED_BY` MEDIUMTEXT NULL DEFAULT NULL,
  PRIMARY KEY (`ID_USER`),
  UNIQUE INDEX `ID_USER_UNIQUE` (`ID_USER` ASC),
  INDEX `RELATIONSHIP_1_FK` (`ID_ROLE` ASC),
  CONSTRAINT `FK_USER_RELATIONS_USERROLE`
    FOREIGN KEY (`ID_ROLE`)
    REFERENCES `ecommerce`.`userrole` (`ID_ROLE`))
ENGINE = InnoDB
AUTO_INCREMENT = 61
DEFAULT CHARACTER SET = utf8;

INSERT INTO USERROLE VALUES(1, 'KOMERCIJALISTA');
INSERT INTO USERROLE VALUES(2, 'RADNIK');
INSERT INTO USER VALUES(1,1,'A','A','A','A','A','A','A');
INSERT INTO USER VALUES(2,2,'A','A','A','B','A','A','A');
INSERT INTO PRODAVNICA VALUES(1,'A',1,2);
INSERT INTO PROIZVOD VALUES(1,1,1,'A','0000-00-00',1,1);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;