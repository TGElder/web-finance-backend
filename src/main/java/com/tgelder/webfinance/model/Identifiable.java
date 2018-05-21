package com.tgelder.webfinance.model;

import com.tgelder.webfinance.controller.GenericGetPostController;
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
  @Null(groups = {GenericGetPostController.PostValidation.class})
  private Long id;

}
