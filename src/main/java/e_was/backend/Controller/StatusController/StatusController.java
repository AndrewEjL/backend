package e_was.backend.Controller.StatusController;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import e_was.backend.Model.StatusModel.MyStatus;
import e_was.backend.Service.StatusService.MyStatusService;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    @Autowired
    private MyStatusService myStatusService;

    @GetMapping("/{tableName}/all")
    public List<? extends MyStatus> getAll(@PathVariable String tableName) {
        return myStatusService.getAll(tableName);
    }

    @GetMapping("/{tableName}/{id}")
    public MyStatus getByID(@PathVariable String tableName, @PathVariable int id) {
        return myStatusService.getByID(id, tableName);
    }

    @PostMapping("/{tableName}/add")
    public MyStatus addStatus(@RequestBody MyStatus status, @PathVariable String tableName) {
        return myStatusService.save(status, tableName);
    }

    @PutMapping("/{tableName}/update/{id}")
    public MyStatus updateStatus(@RequestBody MyStatus status, @PathVariable String tableName, @PathVariable int id) {
        return myStatusService.update(status, id, tableName);
    }

    @DeleteMapping("/{tableName}/delete/{id}")
    public void deleteStatus(@PathVariable String tableName, @PathVariable int id) {
        myStatusService.delete(id, tableName);
    }

    // itemtype
    // @PostMapping("/item-type/add")
    // public ItemTypes addItemType(@RequestBody ItemTypes itemType) {
    //     return myStatusService.saveItemType(itemType);
    // } 
}
