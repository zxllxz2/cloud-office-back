package com.toby.server.controller;


import com.toby.server.pojo.Joblevel;
import com.toby.server.pojo.Position;
import com.toby.server.pojo.RespBean;
import com.toby.server.service.IJoblevelService;
import com.toby.server.service.IPositionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
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
@RequestMapping("/system/basic/joblevel")
public class JoblevelController {

    @Autowired
    private IJoblevelService joblevelService;

    @ApiOperation(value = "Get all job-level info")
    @GetMapping("/")
    public List<Joblevel> getAllJobLevels() {
        return joblevelService.list();
    }

    @ApiOperation(value = "Add job-level info")
    @PostMapping("/")
    public RespBean addJobLevel(@RequestBody Joblevel joblevel) {
        joblevel.setCreateDate(LocalDateTime.now());
        if (joblevelService.save(joblevel)) {
            return RespBean.success("Added successfully!");
        }
        return RespBean.error("Fail to add job-level info!");
    }

    @ApiOperation(value = "Update job-level info")
    @PutMapping("/")
    public RespBean updateJobLevel(@RequestBody Joblevel joblevel) {
        if (joblevelService.updateById(joblevel)) {
            return RespBean.success("Updated successfully!");
        }
        return RespBean.error("Fail to update job-level info!");
    }

    @ApiOperation(value = "Delete job-level info")
    @DeleteMapping("/{id}")
    public RespBean deleteJobLevel(@PathVariable Integer id) {
        if (joblevelService.removeById(id)) {
            return RespBean.success("Deleted successfully!");
        }
        return RespBean.error("Fail to delete job-level info!");
    }

    @ApiOperation(value = "Batch deletion")
    @DeleteMapping("/")
    public RespBean deleteJobLevelByIds(Integer[] ids) {
        if (joblevelService.removeByIds(Arrays.asList(ids))) {
            return RespBean.success("Deleted in bulk successfully!");
        }
        return RespBean.error("Fail to delete job-level info in bulk!");
    }

}
