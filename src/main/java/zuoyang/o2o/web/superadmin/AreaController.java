package zuoyang.o2o.web.superadmin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import zuoyang.o2o.entity.Area;
import zuoyang.o2o.service.AreaService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/superadmin")
@Slf4j
public class AreaController {
    private final AreaService areaService;

    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }

    @GetMapping("/listarea")
    @ResponseBody
    public Map<String, Object> listArea() {
        log.info("====start====");
        long startTime = System.currentTimeMillis();
        Map<String, Object> modelMap = new HashMap<>();
        try {
            List<Area> areaList = areaService.getAreaList();
            modelMap.put("rows", areaList);
            modelMap.put("total", areaList.size());
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
        }
        log.error("test error");
        long endTime = System.currentTimeMillis();
        log.debug("costTime:[{}ms]", endTime-startTime);
        log.info("======end======");
        return modelMap;
    }





}
