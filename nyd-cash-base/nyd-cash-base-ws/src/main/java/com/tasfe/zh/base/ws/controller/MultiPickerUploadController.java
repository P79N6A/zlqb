package com.tasfe.zh.base.ws.controller;

import com.tasfe.zh.base.model.FileSuffix;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Lait on 2017/8/1.
 * 多个文件选择器上传文件，一个选择器对应一个文件
 */
@Controller
@RequestMapping("/multiPickerUpload")
public class MultiPickerUploadController {

    @GetMapping(value = "/index")
    public String Index() {
        return "multiPicker/index";
    }

    @PostMapping("/")
    public ResponseEntity<Void> fileUpload(@RequestParam("type") String type,
                                           @RequestParam("name") String name,
                                           @RequestParam("file") MultipartFile file) throws Exception {
        FileSuffix suffix = FileSuffix.get(type);
        switch (suffix) {
            case doc:
                //save file
                break;
            case docx:
                //save file
                break;
            case jpg:
                //save file
                break;
            case png:
                //save file
                break;
            default:
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
