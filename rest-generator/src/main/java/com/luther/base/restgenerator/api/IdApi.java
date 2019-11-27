package com.luther.base.restgenerator.api;

import com.luther.base.intf.Id;
import com.luther.base.intf.IdService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("generator")
public class IdApi {
    private final IdService idService;

    public IdApi(IdService idService) {
        this.idService = idService;
    }

    @RequestMapping("genId")
    public ResponseEntity<Long> demo() {
        return ResponseEntity.ok(idService.genId());
    }

    @GetMapping("makeId")
    public ResponseEntity<Long> makeId(@RequestParam("version") Long version, @RequestParam("type") Long type,
                                       @RequestParam("genMethod") Long genMethod, @RequestParam("machine") Long machine,
                                       @RequestParam("time") Long time, @RequestParam("seq") Long seq) {
        if (time == null || seq == null) {
            throw new IllegalArgumentException("Both time and seq are required");
        }
        long madeId = -1;
        if (version == null) {
            if (type == null) {
                if (genMethod == null) {
                    if (machine == -1) {
                        madeId = idService.makeId(time, seq);
                    } else {
                        madeId = idService.makeId(time, seq, machine);
                    }
                } else {
                    madeId = idService.makeId(genMethod, time, seq, machine);
                }
            } else {
                madeId = idService.makeId(type, genMethod, time, seq, machine);
            }
        } else {
            madeId = idService.makeId(version, type, genMethod, time, seq, machine);
        }
        return ResponseEntity.ok(madeId);
    }

    @GetMapping("expid")
    public ResponseEntity<Id> explainId(@RequestParam("id") Long id) {
        return ResponseEntity.ok(idService.expId(id));
    }

    @GetMapping("/transtime")
    public ResponseEntity<String> transTime(@RequestParam("time") Long time) {
        return ResponseEntity.ok(idService.transTime(time).toString());
    }
}
