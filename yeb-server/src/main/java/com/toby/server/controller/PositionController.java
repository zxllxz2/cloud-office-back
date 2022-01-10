package com.toby.server.controller;

import com.toby.server.pojo.Position;
import com.toby.server.pojo.RespBean;
import com.toby.server.service.IPositionService;
import io.swagger.annotations.Api;
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
@RequestMapping("/system/basic/pos")
public class PositionController {

    @Autowired
    private IPositionService positionService;

    @ApiOperation(value = "Get all position info")
    @GetMapping("/")
    public List<Position> getAllPositions() {
        return positionService.list();
    }

    @ApiOperation(value = "Add position info")
    @PostMapping("/")
    public RespBean addPosition(@RequestBody Position position) {
        position.setCreateDate(LocalDateTime.now());
        if (positionService.save(position)) {
            return RespBean.success("Added successfully!");
        }
        return RespBean.error("Fail to add position info!");
    }

    @ApiOperation(value = "Update position info")
    @PutMapping("/")
    public RespBean updatePosition(@RequestBody Position position) {
        if (positionService.updateById(position)) {
            return RespBean.success("Updated successfully!");
        }
        return RespBean.error("Fail to update position info!");
    }

    @ApiOperation(value = "Delete position info")
    @DeleteMapping("/{id}")
    public RespBean deletePosition(@PathVariable Integer id) {
        if (positionService.removeById(id)) {
            return RespBean.success("Deleted successfully!");
        }
        return RespBean.error("Fail to delete position info!");
    }

    @ApiOperation(value = "Batch deletion")
    @DeleteMapping("/")
    public RespBean deletePositionByIds(Integer[] ids) {
        if (positionService.removeByIds(Arrays.asList(ids))) {
            return RespBean.success("Deleted in bulk successfully!");
        }
        return RespBean.error("Fail to delete position info in bulk!");
    }

}
