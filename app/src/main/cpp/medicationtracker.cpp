#include "DbManager.h"
#include "DatabaseController.h"

#include <jni.h>
#include <android/log.h>
#include <string>

extern "C"
JNIEXPORT jboolean JNICALL
Java_projects_medicationtracker_Helpers_NativeDbHelper_dbExporter(JNIEnv *env, jobject thiz,
                                                                          jstring database_name,
                                                                          jstring export_directory,
                                                                          jobjectArray ignoredTables) {
    std::string db = env->GetStringUTFChars(database_name, new jboolean(true));
    std::string exportDir = env->GetStringUTFChars(export_directory, new jboolean(true));
    std::vector<std::string> ignoredTbls;
    int len = env->GetArrayLength(ignoredTables);

    for (int i = 0; i < len; i++) {
        auto str = (jstring) (env->GetObjectArrayElement(ignoredTables, i));
        string rawString = env->GetStringUTFChars(str, JNI_FALSE);

        ignoredTbls.push_back(rawString);
    }

    DatabaseController controller(db);

    try {
        controller.exportJSON(exportDir, ignoredTbls);
    } catch (exception& e) {
        __android_log_write(ANDROID_LOG_ERROR, nullptr, e.what());

        return false;
    }

    return true;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_projects_medicationtracker_Helpers_NativeDbHelper_dbImporter(JNIEnv *env, jobject thiz,
                                                                  jstring db_path,
                                                                  jstring import_path,
                                                                  jobjectArray ignored_tables) {
    std::string db = env->GetStringUTFChars(db_path, new jboolean(true));
    std::string importPath = env->GetStringUTFChars(import_path, new jboolean(true));
    std::vector<std::string> ignoredTbls;
    int len = env->GetArrayLength(ignored_tables);

    for (int i = 0; i < len; i++) {
        auto str = (jstring) (env->GetObjectArrayElement(ignored_tables, i));
        string rawString = env->GetStringUTFChars(str, JNI_FALSE);

        ignoredTbls.push_back(rawString);
    }

    DatabaseController controller(db);

    try {
        controller.importJSON(importPath, ignoredTbls);
    } catch (exception &e) {
        __android_log_write(ANDROID_LOG_ERROR, nullptr, e.what());

        return false;
    }

    return true;
}

extern "C"
JNIEXPORT void JNICALL
Java_projects_medicationtracker_Helpers_NativeDbHelper_dbCreate(JNIEnv *env, jobject thiz,
                                                                jstring db_path) {
    // TODO: implement dbCreate()
}
extern "C"
JNIEXPORT void JNICALL
Java_projects_medicationtracker_Helpers_NativeDbHelper_dbUpdate(JNIEnv *env, jobject thiz,
                                                                jstring db_path) {
    // TODO: implement dbUpdate()
}