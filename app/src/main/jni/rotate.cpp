#include <jni.h>
#include <opencv2/core.hpp>
#include <opencv2/objdetect.hpp>
#include <opencv2/opencv.hpp>
#include <typeinfo>

#include <string>

using  namespace cv;
using  namespace std;

std::string jstring2str(JNIEnv* env, jstring jstr)
{
    char*   rtn   =   NULL;
    jclass   clsstring   =   env->FindClass("java/lang/String");
    jstring   strencode   =   env->NewStringUTF("GB2312");
    jmethodID   mid   =   env->GetMethodID(clsstring,   "getBytes",   "(Ljava/lang/String;)[B");
    jbyteArray   barr=   (jbyteArray)env->CallObjectMethod(jstr,mid,strencode);
    jsize   alen   =   env->GetArrayLength(barr);
    jbyte*   ba   =   env->GetByteArrayElements(barr,JNI_FALSE);
    if(alen   >   0)
    {
        rtn   =   (char*)malloc(alen+1);
        memcpy(rtn,ba,alen);
        rtn[alen]=0;
    }
    env->ReleaseByteArrayElements(barr,ba,0);
    std::string stemp(rtn);
    free(rtn);
    return   stemp;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_facedetection_Record_rotateImage(JNIEnv *env, jobject thiz,
                                                  jlong addr) {
    Mat& pic = *(Mat*)addr;
    flip(pic,pic,1);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_facedetection_RecordCompare_rotateImage(JNIEnv *env, jobject thiz, jlong addr) {
    Mat& pic = *(Mat*)addr;
    flip(pic,pic,1);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_facedetection_ShotCompare_rotateImage(JNIEnv *env, jobject thiz, jlong addr) {
    Mat& pic = *(Mat*)addr;
    flip(pic,pic,1);
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_facedetection_Insert_checkInput(JNIEnv *env, jobject thiz, jstring input) {
    string check = jstring2str(env,input);
    string illeagle = "/\\.&:\n\t";
    int len = check.length();
    for(int i=0;i<len;i++){
        for(int j=0;j<illeagle.length();j++){
            if(check[i] == illeagle[j]){
                return false;
            }
        }
    }
    return true;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_facedetection_Change_CheckInput(JNIEnv *env, jobject thiz, jstring input) {
    string check = jstring2str(env,input);
    string illeagle = "/\\.&:\n\t";
    int len = check.length();
    for(int i=0;i<len;i++){
        for(int j=0;j<illeagle.length();j++){
            if(check[i] == illeagle[j]){
                return false;
            }
        }
    }
    return true;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_facedetection_Change_CheckSex(JNIEnv *env, jobject thiz, jstring input) {
    string sex = jstring2str(env,input);

    if(sex=="男"||sex=="女"){
        return true;
    }
    else{
        return false;
    }
}