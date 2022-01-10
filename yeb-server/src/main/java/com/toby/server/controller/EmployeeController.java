package com.toby.server.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.toby.server.pojo.*;
import com.toby.server.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author tobyzhu
 * @since 2021-12-31
 */
@RestController
@RequestMapping("/employee/basic")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IPoliticsStatusService politicsStatusService;

    @Autowired
    private IJoblevelService joblevelService;

    @Autowired
    private INationService nationService;

    @Autowired
    private IPositionService positionService;

    @Autowired
    private IDepartmentService departmentService;

    @ApiOperation("Get all employees with pagination")
    @GetMapping("/")
    public RespPageBean getEmployees(@RequestParam(defaultValue = "1") Integer currentPage,
                                     @RequestParam(defaultValue = "10") Integer size,
                                     Employee employee,
                                     LocalDate[] beginDateScope) {
        return employeeService.getEmployeeByPage(currentPage, size, employee, beginDateScope);
    }

    @ApiOperation(value = "Get all politics status")
    @GetMapping("/politicsstatus")
    public List<PoliticsStatus> getAllPoliticsStatus() {
        return politicsStatusService.list();
    }

    @ApiOperation(value = "Get all job levels")
    @GetMapping("/joblevels")
    public List<Joblevel> getAllJobLevels() {
        return joblevelService.list();
    }

    @ApiOperation(value = "Get all nations")
    @GetMapping("/nations")
    public List<Nation> getAllNations() {
        return nationService.list();
    }

    @ApiOperation(value = "Get all positions")
    @GetMapping("/positions")
    public List<Position> getAllPositions() {
        return positionService.list();
    }

    @ApiOperation(value = "Get all departments")
    @GetMapping("/deps")
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @ApiOperation(value = "Get max workID")
    @GetMapping("/maxWorkID")
    public RespBean maxWorkID() {
        return employeeService.maxWorkID();
    }

    @ApiOperation(value = "Add an employee")
    @PostMapping("/")
    public RespBean addEmp(@RequestBody Employee employee) {
        return employeeService.addEmp(employee);
    }

    @ApiOperation(value = "Update employee info")
    @PutMapping("/")
    public RespBean updateEmp(@RequestBody Employee employee) {

        LocalDate beginContract = employee.getBeginContract();
        LocalDate endContract = employee.getEndContract();
        Employee empOri = employeeService.getEmployee(employee.getId()).get(0);
        if (!beginContract.isEqual(empOri.getBeginContract()) || !endContract.isEqual(empOri.getEndContract())) {
            long days = beginContract.until(endContract, ChronoUnit.DAYS);
            DecimalFormat decimalFormat = new DecimalFormat("##.00");
            employee.setContractTerm(Double.parseDouble(decimalFormat.format(days / 365.00)));
        }

        if (employeeService.updateById(employee)) {
            return RespBean.success("Updated successfully!");
        }
        return RespBean.error("Fail to update employee info!");
    }

    @ApiOperation(value = "Delete an employee")
    @DeleteMapping("/{id}")
    public RespBean deleteEmp(@PathVariable Integer id) {
        if (employeeService.removeById(id)) {
            return RespBean.success("Deleted successfully!");
        }
        return RespBean.error("Fail to delete employee info!");
    }

    @ApiOperation(value = "Export employee data")
    @GetMapping(value = "/export", produces = "application/octet-stream")
    public void exportEmp(HttpServletResponse response) {
        List<Employee> employeesList = employeeService.getEmployee(null);
        ExportParams params = new ExportParams("Employee Table", "Employee Table", ExcelType.HSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params, Employee.class, employeesList);
        ServletOutputStream stream = null;
        try {
            //流形式传输
            response.setHeader("content-type", "application/octet-stream");
            //防止中文乱码
            response.setHeader("content-disposition", "attachment;filename=" +
                    URLEncoder.encode("Employee_Table.xls", "UTF-8"));

//            response.setHeader("content-disposition", "attachment;filename=Employee_Table.xls");
            stream = response.getOutputStream();
            workbook.write(stream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 因为民族，政治面貌，部门，职称，职位的name属性不会经常变动，所以我们重写了这些entities的equals方法与hashcode，
     * 用name属性进行比较从而获得相对的id
     *
     * @param file
     * @return
     */
    @ApiOperation(value = "Import employee data")
    @PostMapping("/import")
    public RespBean importEmp(MultipartFile file) {
        ImportParams params = new ImportParams();
        //去除标题行
        params.setTitleRows(1);
        //获取所有民族，政治面貌，部门，职称，职位
        List<Nation> nationList = nationService.list();
        List<PoliticsStatus> politicsStatusList = politicsStatusService.list();
        List<Department> departmentList = departmentService.list();
        List<Joblevel> joblevelList = joblevelService.list();
        List<Position> positionList = positionService.list();

        try {
            List<Employee> empList = ExcelImportUtil.importExcel(file.getInputStream(),
                    Employee.class, params);
            empList.forEach(employee -> {
                //通过有参构造必填name获取该nation在整体nationList中的下标，从而获取相对应的nation实体，获得id
                employee.setNationId(nationList.get(nationList.indexOf(
                        new Nation(employee.getNation().getName()))).getId());
                //政治面貌id
                employee.setPoliticId(politicsStatusList.get(politicsStatusList.indexOf(
                        new PoliticsStatus(employee.getPoliticsStatus().getName()))).getId());
                //部门id
                employee.setDepartmentId(departmentList.get(departmentList.indexOf(
                        new Department(employee.getDepartment().getName()))).getId());
                //职称id
                employee.setJobLevelId(joblevelList.get(joblevelList.indexOf(
                        new Joblevel(employee.getJoblevel().getName()))).getId());
                //职位id
                employee.setPosId(positionList.get(positionList.indexOf(
                        new Position(employee.getPosition().getName()))).getId());
            });
            if (employeeService.saveBatch(empList)) {
                return RespBean.success("Imported successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("Fail to import employee data!");
    }

}
