package com.gateway.repository;

import com.gateway.entity.Sys_dept;
import com.gateway.entity.Sys_user_info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2020/4/8.
 */
//@Repository
public interface UserRepository extends JpaRepository<Sys_user_info,String>{


     public List<Sys_user_info> findByAccountnameAndAccountpass(String account,String password);

     public Sys_user_info findUserIdByAccountname(String account);

}
