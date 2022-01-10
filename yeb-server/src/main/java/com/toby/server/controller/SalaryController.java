package com.toby.server.controller;


import com.toby.server.pojo.RespBean;
import com.toby.server.pojo.Salary;
import com.toby.server.service.ISalaryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tobyzhu
 * @since 2021-12-31
 */
@RestController
@RequestMapping("/salary/sob")
public class SalaryController {

    @Autowired
    private ISalaryService salaryService;

    @ApiOperation(value = "Get all payrolls")
    @GetMapping("/")
    public List<Salary> getAllSalaries() {
        return salaryService.list();
    }

    @ApiOperation(value = "Add a payroll")
    @PostMapping("/")
    public RespBean addSalary(@RequestBody Salary salary) {
        salary.setCreateDate(LocalDateTime.now());
        if (salaryService.save(salary)) {
            return RespBean.success("Added successfully!");
        }
        return RespBean.error("Fail to add salary info!");
    }

    @ApiOperation(value = "Update a payroll")
    @PutMapping("/")
    public RespBean updateSalary(@RequestBody Salary salary) {
        if (salaryService.updateById(salary)) {
            return RespBean.success("Updated successfully!");
        }
        return RespBean.error("Fail to update salary info!");
    }

    @ApiOperation(value = "Delete a payroll")
    @DeleteMapping("/{id}")
    public RespBean deleteSalary(@PathVariable Integer id) {
        if (salaryService.removeById(id)) {
            return RespBean.success("Deleted successfully!");
        }
        return RespBean.error("Fail to delete salary info!");
    }

}
