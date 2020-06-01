package com.gateway.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "sys_dept")
public class Sys_dept {

  @Id
  private Long dept_id;
  private Long parent_id;
  private String ancestors;
  private String dept_name;
  private Long order_num;
  private String leader;
  private String phone;
  private String email;
  private String status;
  private String del_flag;
  private String create_by;
  private java.sql.Timestamp create_time;
  private String update_by;
  private java.sql.Timestamp update_time;
  private String avatar;

}
