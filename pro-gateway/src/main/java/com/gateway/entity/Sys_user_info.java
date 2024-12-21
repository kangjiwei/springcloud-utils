package com.gateway.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "sys_user_info")
public class Sys_user_info {

    @Id
    private Long user_id;
    @OneToOne
    @JoinColumn(name = "dept_id", referencedColumnName = "dept_id")
    private Sys_dept dept_id;
    private String user_name;
    private String user_type;
    private String accountname;
    private String accountpass;
    private String hmackey;
    private String email;
    private String phonenumber;
    private String sex;
    private String avatar;
    private String status;
    private String del_flag;
    private String login_ip;
    private java.sql.Timestamp login_date;
    private String create_by;
    private java.sql.Timestamp create_time;
    private String update_by;
    private java.sql.Timestamp update_time;
    private String section_name;
    private String post_name;
    private String is_admin;
    private String remark;

}
