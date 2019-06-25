package com.lin.cms.demo.service.impl;

import com.lin.cms.core.exception.*;
import com.lin.cms.demo.service.base.AbstractService;
import com.lin.cms.demo.extensions.file.LocalUploader;
import com.lin.cms.demo.mapper.FileMapper;
import com.lin.cms.demo.model.FilePO;
import com.lin.cms.demo.service.FileService;
import com.lin.cms.demo.view.UploadFileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * Created by lin on 2019/06/14.
 */
@Service
public class FileServiceImpl extends AbstractService<FilePO> implements FileService {
    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private LocalUploader uploader;

    @Override
    public List<UploadFileVO> upload(MultiValueMap<String, MultipartFile> fileMap) throws NotFound, Parameter, FileTooMany, FileExtension, FileTooLarge {
        List<UploadFileVO> res = uploader.upload(fileMap);
        return res;
    }

}
