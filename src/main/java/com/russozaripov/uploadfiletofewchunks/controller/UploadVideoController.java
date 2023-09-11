package com.russozaripov.uploadfiletofewchunks.controller;

import com.russozaripov.uploadfiletofewchunks.service.FileUploadingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/uploadTheVideo")
public class UploadVideoController {

    @Autowired
    private FileUploadingService service;
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String startMultiPartUpload(@RequestParam("fileUrl") String fileUrl){
         return service.UploadTheVideo(fileUrl);
    }
}
