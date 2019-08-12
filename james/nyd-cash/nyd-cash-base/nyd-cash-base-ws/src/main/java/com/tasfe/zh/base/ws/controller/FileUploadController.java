package com.tasfe.zh.base.ws.controller;

import com.tasfe.zh.base.configs.FileUploadConfig;
import com.tasfe.zh.base.entity.File;
import com.tasfe.zh.base.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.UUID;

import static com.tasfe.zh.base.service.utils.FileUtils.createMd5;
import static com.tasfe.zh.base.service.utils.FileUtils.saveFile;

/**
 * Created by Lait on 2017/8/1.
 */
@Controller
@RequestMapping("/fileUpload")
public class FileUploadController {
    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("fileUpload/index");
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "/fileUp", method = RequestMethod.POST)
    public String fileUpload(@RequestParam("id") String id,
                             @RequestParam("name") String name,
                             @RequestParam("type") String type,
                             @RequestParam("lastModifiedDate") String lastModifiedDate,
                             @RequestParam("size") int size,
                             @RequestParam("file") MultipartFile file) {
        String fileName;

        try {
            String ext = name.substring(name.lastIndexOf("."));
            fileName = UUID.randomUUID().toString() + ext;
            saveFile(FileUploadConfig.UPLOAD_DIR, fileName, file);
        } catch (Exception ex) {
            return "{\"error\":true}";
        }
        try {
            fileService.save(new File(fileName, createMd5(file).toString(), new Date()));
        } catch (Exception e) {
            return "{\"error\":true}";
        }

        return "{jsonrpc = \"2.0\",id = id,filePath = \"/Upload/\" + fileFullName}";
    }
}
