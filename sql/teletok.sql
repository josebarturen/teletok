-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema teletok
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `teletok` ;

-- -----------------------------------------------------
-- Schema teletok
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `teletok` DEFAULT CHARACTER SET utf8 ;
USE `teletok` ;

-- -----------------------------------------------------
-- Table `teletok`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `teletok`.`role` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teletok`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `teletok`.`user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `fullname` VARCHAR(45) NOT NULL,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(500) NOT NULL,
  `enable` INT(1) NOT NULL DEFAULT 1,
  `role_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC),
  INDEX `fk_user_role1_idx` (`role_id` ASC),
  CONSTRAINT `fk_user_role1`
    FOREIGN KEY (`role_id`)
    REFERENCES `teletok`.`role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teletok`.`post`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `teletok`.`post` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(45) NULL,
  `creation_date` DATETIME NOT NULL,
  `media_url` VARCHAR(45) NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_post_user1_idx` (`user_id` ASC),
  CONSTRAINT `fk_post_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `teletok`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teletok`.`post_comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `teletok`.`post_comment` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `message` VARCHAR(45) NULL,
  `user_id` INT NOT NULL,
  `post_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_post_comment_user_idx` (`user_id` ASC),
  INDEX `fk_post_comment_post1_idx` (`post_id` ASC),
  CONSTRAINT `fk_post_comment_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `teletok`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_post_comment_post1`
    FOREIGN KEY (`post_id`)
    REFERENCES `teletok`.`post` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teletok`.`post_like`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `teletok`.`post_like` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NOT NULL,
  `post_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`, `user_id`, `post_id`),
  INDEX `fk_post_like_user1_idx` (`user_id` ASC),
  INDEX `fk_post_like_post1_idx` (`post_id` ASC),
  CONSTRAINT `fk_post_like_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `teletok`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_post_like_post1`
    FOREIGN KEY (`post_id`)
    REFERENCES `teletok`.`post` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `teletok`.`role`
-- -----------------------------------------------------
START TRANSACTION;
USE `teletok`;
INSERT INTO `teletok`.`role` (`id`, `name`) VALUES (1, 'ADMIN');
INSERT INTO `teletok`.`role` (`id`, `name`) VALUES (2, 'USER');

COMMIT;

