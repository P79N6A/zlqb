package com.tasfe.zh.base.ws.controller;

import com.tasfe.zh.base.entity.File;
import com.tasfe.zh.base.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Lait on 2017/8/1.
 */
@Controller
@RequestMapping("/imgUpload")
public class ImageUploadController {
    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String Upload() {
        return "imgUpload/upload";
    }

    @ResponseBody
    @RequestMapping(value = "/imgUpload", method = RequestMethod.POST)
    public String fileUpload(@RequestParam("id") String id,
                             @RequestParam("name") String name,
                             @RequestParam("type") String type,
                             @RequestParam("lastModifiedDate") String lastModifiedDate,
                             @RequestParam("size") int size,
                             @RequestParam("file") MultipartFile file) {
        String fileName = "";
        MultipartFile saveFile = null;

        /*try {
            saveFile = (MultipartFile) deepClone(file);
            java.io.File tempFile = new java.io.File(UUID.randomUUID().toString());
            file.transferTo(tempFile);
            if (!isImage(tempFile)) {
                return "{\"error\":true}";
            }
            String realpath = getRealPath();
            String ext = name.substring(name.lastIndexOf("."));
            fileName = UUID.randomUUID().toString() + ext;
            saveFile(realpath, fileName, saveFile);

            fileService.save(new File(fileName, createMd5(file).toString(), new Date()));

        } catch (Exception ex) {
            return "{\"error\":true}";
        }*/

        return "{jsonrpc = \"2.0\",id = id,filePath = \"/Upload/\" + fileFullName}";
    }
}

