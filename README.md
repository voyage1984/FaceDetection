# FaceDetection

---
### 注意：本项目仅使用x86和arm84_v8a编译，基于opencv340，也使用了opencv420的ibopencv_java4.so。
---
##### 这是一个基于opencv的人脸识别程序，以下是关于本程序的一些说明。

- ###### 初次使用
初次使用请先录入人脸，识别可使用拍照识别(识别率较低)、视频识别(识别率较高)。
仅有人脸被检测时才能拍摄。点击"拍摄人脸"后点击"保存结果"，录入成功，请于"查看数据库"中查看人物信息。
拍摄/识别时请横屏，不然无法识别人脸

- ###### 关于输入
输入人物名时禁止输入:\"/\" 、\".\"、\"\\\" 、空白符等非法字符，检测输入函数定义在"rotate.cpp"的
"CheckInput()"函数中，可前往定义非法字符串

- ###### 关于信息存储
人物信息储存在数据库(SQLite)中，人脸图像储存在手机目录的"FaceDetect/Grey/"下，以人物名字+".bmp"
后缀命名，故无法输入相同的人物名，请勿随意修改或删除人脸图像文件，否则会导致程序运行出错。安全删除请在"查看数据库"中
长按想删除的人物信息删除(点按可查看并修改人物信息)。
请勿随意更改图片保存路径，否则将导致无法读取的可能性

- ###### 关于信息比对
比对函数储存在 Compare 的 public String doCompare(Mat mat) 中，比对阈值设定为0.85，
降低该值可增加比对成功率，识别准确率降低；增加该值可降低比对成功率，识别准确度增高。同一人物同一照片识别率高，
不同照片识别率极低。拍摄、识别人脸时请保持人脸、背景不发生巨大改变，如无法识别请调低比对阈值

- ###### 关于本程序
本程序使用了opencv 4.2.0，JNILibs中也使用了"libopencv_java4.so"，默认编译使用x86和arm64_v8a架构

- ###### 关于原理
本质上是基于compareHist的图片识别比对相似度，但保存和识别的图片是人脸检测(face-detection)时的人脸部分的区域，故称人脸识别。
