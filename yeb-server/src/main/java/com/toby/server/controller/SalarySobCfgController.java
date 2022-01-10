package com.toby.server.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.toby.server.pojo.Employee;
import com.toby.server.pojo.RespBean;
import com.toby.server.pojo.RespPageBean;
import com.toby.server.pojo.Salary;
import com.toby.server.service.IEmployeeService;
import com.toby.server.service.ISalaryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工账套
 */
@RestController
@RequestMapping("/salary/sobcfg")
public class SalarySobCfgController {

    @Autowired
    private ISalaryService salaryService;

    @Autowired
    private IEmployeeService employeeService;

    @ApiOperation(value = "Get all payrolls")
    @GetMapping("/salaries")
    public List<Salary> getAllSalaries() {
        return salaryService.list();
    }

    @ApiOperation(value = "Get all employees' payrolls")
    @GetMapping("/")
    public RespPageBean getEmployeeWithSalary(@RequestParam(defaultValue = "1") Integer currentPage,
                                              @RequestParam(defaultValue = "10") Integer size) {
        return employeeService.getEmployeeWithSalary(currentPage, size);
    }

    @ApiOperation(value = "Update an employee's payroll")
    @PutMapping("/")
    public RespBean updateEmployeeSalary(Integer eid, Integer sid) {
        if (employeeService.update(new UpdateWrapper<Employee>().set("salaryId", sid)
                .eq("id", eid))) {
            return RespBean.success("Updated successfully!");
        }
        return RespBean.error("Fail to update salary info!");
    }

}
