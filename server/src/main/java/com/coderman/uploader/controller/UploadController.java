package com.coderman.uploader.controller;

import com.coderman.uploader.dto.FileChunkDTO;
import com.coderman.uploader.dto.FileChunkResultDTO;
import com.coderman.uploader.response.RestApiResponse;
import com.coderman.uploader.service.IUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author zhangyukang
 * @Date 2021/1/16 19:22
 * @Version 1.0
 * @Modify XJLZ
 **/
@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private IUploadService uploadService;

    /**
     * 检查分片是否存在
     *
     * @return
     */
    @GetMapping("chunk")
    public RestApiResponse<Object> checkChunkExist(FileChunkDTO chunkDTO) {
        FileChunkResultDTO fileChunkCheckDTO;
        try {
            fileChunkCheckDTO = uploadService.checkChunkExist(chunkDTO);
            return RestApiResponse.success(fileChunkCheckDTO);
        } catch (Exception e) {
            return RestApiResponse.error(e.getMessage());
        }
    }


    /**
     * 上传文件分片
     *
     * @param chunkDTO
     * @return
     */
    @PostMapping("chunk")
    public RestApiResponse<Object> uploadChunk(FileChunkDTO chunkDTO) {
        try {
            uploadService.uploadChunk(chunkDTO);
            return RestApiResponse.success(chunkDTO.getIdentifier());
        } catch (Exception e) {
            return RestApiResponse.error(e.getMessage());
        }
    }

    /**
     * 请求合并文件分片
     *
     * @param chunkDTO
     * @return
     */
    @PostMapping("merge")
    public RestApiResponse<Object> mergeChunks(@RequestBody FileChunkDTO chunkDTO) {
        try {
            boolean success = uploadService.mergeChunk(chunkDTO.getIdentifier(), chunkDTO.getFilename(), chunkDTO.getTotalChunks());
            return RestApiResponse.flag(success);
        } catch (Exception e) {
            return RestApiResponse.error(e.getMessage());
        }
    }

}
