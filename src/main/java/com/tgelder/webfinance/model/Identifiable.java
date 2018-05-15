package com.tgelder.webfinance.model;

import com.tgelder.webfinance.controller.GenericController;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Null;

@Entity
@Data
public class Identifiable {

  @Id
  @GeneratedValue
  @Null(groups = {GenericController.PostValidation.class})
  private Long id;

}
