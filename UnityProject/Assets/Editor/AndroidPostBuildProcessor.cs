using System.IO;
using UnityEditor.Android;
using UnityEngine;

//public class AndroidPostBuildProcessor : IPostGenerateGradleAndroidProject
//{
//    public int callbackOrder
//    {
//        get
//        {
//            return 999;
//        }
//    }

//    void IPostGenerateGradleAndroidProject.OnPostGenerateGradleAndroidProject(string path)
//    {
//        Debug.Log("Bulid path : " + path);
//        string gradlePropertiesFile = path + "/gradle.properties";
//        if (File.Exists(gradlePropertiesFile))
//        {
//            File.Delete(gradlePropertiesFile);
//        }
//        StreamWriter writer = File.CreateText(gradlePropertiesFile);
////org.gradle.jvmargs = -Xmx4096M
////org.gradle.parallel = true
////unityStreamingAssets =.unity3d
////android.useAndroidX = true
//        writer.WriteLine("org.gradle.jvmargs = -Xmx4096M");
//        writer.WriteLine("org.gradle.parallel = true");
//        writer.WriteLine("unityStreamingAssets =.unity3d");
//        writer.WriteLine("android.useAndroidX = true");
//        writer.Flush();
//        writer.Close();

//    }
//}