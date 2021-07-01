<template>
  <div>
    <uploader
      :autoStart="false"
      :options="options"
      :file-status-text="statusText"
      class="uploader-example"
      @file-complete="fileComplete"
      @complete="complete"
      @file-success="fileSuccess"
      @files-added="filesAdded"
    >
      <uploader-unsupport></uploader-unsupport>
      <uploader-drop>
        <p>Drop files here to upload or</p>
        <uploader-btn>select files</uploader-btn>
        <uploader-btn :attrs="attrs">select images</uploader-btn>
        <uploader-btn :directory="true">select folder</uploader-btn>
      </uploader-drop>
      <!-- <uploader-list></uploader-list> -->
      <uploader-files> </uploader-files>
    </uploader>
    <br />
    <button @click="allStart()" :disabled="disabled">全部开始</button>
    <button @click="allStop()">全部暂停</button>
    <button @click="allRemove()">全部移除</button>
  </div>
</template>

<script>
import axios from "axios";
import SparkMD5 from "spark-md5";
export default {
  data() {
    return {
      skip: false,
      options: {
        target: "//localhost:8989/upload/chunk",
        // 开启服务端分片校验功能
        testChunks: true,
        parseTimeRemaining: function (timeRemaining, parsedTimeRemaining) {
          return parsedTimeRemaining
            .replace(/\syears?/, "年")
            .replace(/\days?/, "天")
            .replace(/\shours?/, "小时")
            .replace(/\sminutes?/, "分钟")
            .replace(/\sseconds?/, "秒");
        },
        // 服务器分片校验函数
        checkChunkUploadedByResponse: (chunk, message) => {
          const result = JSON.parse(message);
          if (result.data.skipUpload) {
            this.skip = true;
            return true;
          }
          return (result.data.uploaded || []).indexOf(chunk.offset + 1) >= 0;
        },
      },
      attrs: {
        accept: "image/*",
      },
      statusText: {
        success: "上传成功",
        error: "上传出错了",
        uploading: "上传中...",
        paused: "暂停中...",
        waiting: "等待中...",
        cmd5: "计算文件MD5中...",
      },
      fileList: [],
      disabled: true,
    };
  },
  watch: {
    fileList(o, n) {
      this.disabled = false;
    },
  },
  methods: {
    fileSuccess(rootFile, file, response, chunk) {
      // console.log(rootFile);
      // console.log(file);
      // console.log(message);
      // console.log(chunk);
      const result = JSON.parse(response);
      console.log(result.success, this.skip);

      if (result.success && !this.skip) {
        axios
          .post("http://127.0.0.1:8989/upload/merge", {
            identifier: file.uniqueIdentifier,
            filename: file.name,
            totalChunks: chunk.offset,
          })
          .then((res) => {
            if (res.data.success) {
              console.log("上传成功");
            } else {
              console.log(res);
            }
          })
          .catch(function (error) {
            console.log(error);
          });
      } else {
        console.log("上传成功，不需要合并");
      }
      if (this.skip) {
        this.skip = false;
      }
    },
    fileComplete(rootFile) {
      // 一个根文件（文件夹）成功上传完成。
      // console.log("fileComplete", rootFile);
      // console.log("一个根文件（文件夹）成功上传完成。");
    },
    complete() {
      // 上传完毕。
      // console.log("complete");
    },
    filesAdded(file, fileList, event) {
      // console.log(file);
      file.forEach((e) => {
        this.fileList.push(e);
        this.computeMD5(e);
      });
    },
    computeMD5(file) {
      let fileReader = new FileReader();
      let time = new Date().getTime();
      let blobSlice =
        File.prototype.slice ||
        File.prototype.mozSlice ||
        File.prototype.webkitSlice;
      let currentChunk = 0;
      const chunkSize = 1024 * 1024;
      let chunks = Math.ceil(file.size / chunkSize);
      let spark = new SparkMD5.ArrayBuffer();
      // 文件状态设为"计算MD5"
      file.cmd5 = true; //文件状态为“计算md5...”
      file.pause();
      loadNext();
      fileReader.onload = (e) => {
        spark.append(e.target.result);
        if (currentChunk < chunks) {
          currentChunk++;
          loadNext();
          // 实时展示MD5的计算进度
          console.log(
            `第${currentChunk}分片解析完成, 开始第${
              currentChunk + 1
            } / ${chunks}分片解析`
          );
        } else {
          let md5 = spark.end();
          console.log(
            `MD5计算完毕：${file.name} \nMD5：${md5} \n分片：${chunks} 大小:${
              file.size
            } 用时：${new Date().getTime() - time} ms`
          );
          spark.destroy(); //释放缓存
          file.uniqueIdentifier = md5; //将文件md5赋值给文件唯一标识
          file.cmd5 = false; //取消计算md5状态
          file.resume(); //开始上传
        }
      };
      fileReader.onerror = function () {
        this.error(`文件${file.name}读取出错，请检查该文件`);
        file.cancel();
      };
      function loadNext() {
        let start = currentChunk * chunkSize;
        let end =
          start + chunkSize >= file.size ? file.size : start + chunkSize;
        fileReader.readAsArrayBuffer(blobSlice.call(file.file, start, end));
      }
    },
    allStart() {
      console.log(this.fileList);
      this.fileList.map((e) => {
        if (e.paused) {
          e.resume();
        }
      });
    },
    allStop() {
      console.log(this.fileList);
      this.fileList.map((e) => {
        if (!e.paused) {
          e.pause();
        }
      });
    },
    allRemove() {
      this.fileList.map((e) => {
        e.cancel();
      });
      this.fileList = [];
    },
  },
};
</script>

<style>
.uploader-example {
  width: 880px;
  padding: 15px;
  margin: 40px auto 0;
  font-size: 12px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.4);
}
.uploader-example .uploader-btn {
  margin-right: 4px;
}
.uploader-example .uploader-list {
  max-height: 440px;
  overflow: auto;
  overflow-x: hidden;
  overflow-y: auto;
}
</style>
