using System.Collections;
using System.Collections.Generic;
using Newtonsoft.Json;
using UnityEngine;

public class NewBehaviourScript : MonoBehaviour
{

    public string clientWebId = "";
    // Start is called before the first frame update
    void Start()
    {
        print("Start");
        var UnityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        var activity = UnityPlayer.GetStatic<AndroidJavaObject>("currentActivity");

        var GoogleLoginActivity = new AndroidJavaClass("com.thirdparty.GoogleLoginActivity");

        GoogleLoginActivity.CallStatic("create", activity, clientWebId);
        GoogleLoginActivity.CallStatic("signIn");
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    class ReturnData
    {
        public string DisplayName;
        public string FamilyName;
        public string GivenName;
        public string Email;
        public string Id;
        public string IdToken;
        public string ServerAuthCode;
        public string PhotoUrl;
        public string Error;
        public bool Cancel;
    }

    void OnGoogleLoginResult(string json)
    {
        print("OnGoogleLoginResult:"+json);
        var GoogleLoginActivity = new AndroidJavaClass("com.thirdparty.GoogleLoginActivity");
        GoogleLoginActivity.CallStatic("signOut");
        var rdata = JsonConvert.DeserializeObject<ReturnData>(json);
        print("IdToken:" + rdata.IdToken);
    }
}
