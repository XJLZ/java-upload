package com.coderman.uploader.dto;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件分片
 *
 * @Author zhangyukang
 * @Date 2021/1/16 19:35
 * @Version 1.0
 **/
public class FileChunkDTO {
    /**
     * 文件md5
     */
    private String identifier;
    /**
     * 分块文件
     */
    MultipartFile file;
    /**
     * 当前分块序号
     */
    private Integer chunkNumber;
    /**
     * 分块大小
     */
    private Long chunkSize;
    /**
     * 当前分块大小
     */
    private Long currentChunkSize;
    /**
     * 文件总大小
     */
    private Long totalSize;
    /**
     * 分块总数
     */
    private Integer totalChunks;
    /**
     * 文件名
     */
    private String filename;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }


    public Integer getChunkNumber() {
        return chunkNumber;
    }

    public void setChunkNumber(Integer chunkNumber) {
        this.chunkNumber = chunkNumber;
    }

    public Long getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(Long chunkSize) {
        this.chunkSize = chunkSize;
    }

    public Long getCurrentChunkSize() {
        return currentChunkSize;
    }

    public void setCurrentChunkSize(Long currentChunkSize) {
        this.currentChunkSize = currentChunkSize;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public Integer getTotalChunks() {
        return totalChunks;
    }

    public void setTotalChunks(Integer totalChunks) {
        this.totalChunks = totalChunks;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return "FileChunkDTO{" +
                "identifier='" + identifier + '\'' +
                ", file=" + file +
                ", chunkNumber=" + chunkNumber +
                ", chunkSize=" + chunkSize +
                ", currentChunkSize=" + currentChunkSize +
                ", totalSize=" + totalSize +
                ", totalChunks=" + totalChunks +
                ", filename='" + filename + '\'' +
                '}';
    }
}
