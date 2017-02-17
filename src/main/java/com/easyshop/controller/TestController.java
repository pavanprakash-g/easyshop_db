package com.easyshop.controller;

import com.easyshop.model.TestModel;
import com.easyshop.repository.CommonRepository;
import com.easyshop.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by admin on 22/10/16.
 */
@CrossOrigin
@RestController
@RequestMapping("/masters")
public class TestController {


    @Autowired
    private CommonRepository commonRepository;

    @Autowired
    private TestRepository testRepository;

    @RequestMapping(value = "/test", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity products() {
        return ResponseEntity.ok(commonRepository.getRowsOfTable("pav"));
    }

    @RequestMapping(value = "/testPost", method = POST, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity testPost(@Valid @RequestBody TestModel testModel) {
        testRepository.save(new TestModel(testModel.getCol1()));
        return ResponseEntity.ok().build();
    }


}
