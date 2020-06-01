package com.gateway.repository;

import com.gateway.entity.Sys_dept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author kangjw
 * @Date 2020/4/14.
 */
@Repository
public interface DeptRepository extends JpaRepository<Sys_dept, Long> {


    @Query(value = "select * from  sys_dept where  dept_id in (?1)",nativeQuery = true)
    List<Sys_dept> 	queryDept(String[] ids);

}
