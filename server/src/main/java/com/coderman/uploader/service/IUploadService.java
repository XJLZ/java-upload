package com.coderman.uploader.service;

import com.coderman.uploader.dto.FileChunkDTO;
import com.coderman.uploader.dto.FileChunkResultDTO;
import com.coderman.uploader.response.error.BusinessException;

import java.io.IOException;

/**
 * @Author zhangyukang
 * @Date 2021/1/17 10:21
 * @Version 1.0
 * @Modify XJLZ
 **/
public interface IUploadService {

    /**
     * 检查文件是否存在，如果存在则跳过该文件的上传，如果不存在，返回需要上传的分片集合
     * @param chunkDTO
     * @return
     */
    FileChunkResultDTO checkChunkExist(FileChunkDTO chunkDTO) throws BusinessException;


    /**
     * 上传文件分片
     * @param chunkDTO
     */
    void uploadChunk(FileChunkDTO chunkDTO) throws BusinessException, IOException;


    /**
     * 合并文件分片
     * @param identifier
     * @param fileName
     * @param totalChunks
     * @return
     * @throws BusinessException
     * @throws IOException
     */
    boolean mergeChunk(String identifier,String fileName,Integer totalChunks)throws BusinessException, IOException;
}
