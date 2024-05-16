using System;
using Android.App;
using Android.Content.PM;
using Android.Runtime;
using Android.OS;
//using Java.Interop;
using Android.Webkit;
using System.Collections.Generic;
using Com.Extole.Android.Sdk;
using Kotlin.Coroutines;
using KotlinX.Coroutines;
using Com.Extole.Android.Sdk.Impl;
using Xamarin.Essentials;

namespace ExtoleDemoApp.Droid
{
    [Activity(Label = "ExtoleDemoApp", Icon = "@mipmap/icon", Theme = "@style/MainTheme", MainLauncher = true, ConfigurationChanges = ConfigChanges.ScreenSize | ConfigChanges.Orientation | ConfigChanges.UiMode | ConfigChanges.ScreenLayout | ConfigChanges.SmallestScreenSize )]
    public class MainActivity : global::Xamarin.Forms.Platform.Android.FormsAppCompatActivity, IContinuation
    {
        protected override void OnCreate(Bundle savedInstanceState)
        {
            var extoleCompanion = Extole.Companion;

            var obj = extoleCompanion.Init("mobile-monitor.extole.io", "extole-mobile-test", "prod-test", this, new List<string> { "business" }, new Dictionary<string, string>(),
                                new Dictionary<string, string>(), new Dictionary<string, string>(), null, false, null, new List<IProtocolHandler>(), new List<ActionActionType>(), null, this);

            
            base.OnCreate(savedInstanceState);

            Xamarin.Essentials.Platform.Init(this, savedInstanceState);
            global::Xamarin.Forms.Forms.Init(this, savedInstanceState);

            LoadApplication(new App());
        }
        public override void OnRequestPermissionsResult(int requestCode, string[] permissions, [Android.Runtime.GeneratedEnum] Android.Content.PM.Permission[] grantResults)
        {
            Xamarin.Essentials.Platform.OnRequestPermissionsResult(requestCode, permissions, grantResults);

            base.OnRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        public ICoroutineContext Context => Dispatchers.Main;

        public void ResumeWith(Java.Lang.Object result)
        {
            Looper.Prepare();

            var extole =  result.JavaCast<ExtoleImpl>();

            MainThread.BeginInvokeOnMainThread(() =>
            {
                //extole.FetchZone("mobile_cta", new Dictionary<string, Java.Lang.Object>(), new SecondActivity());

                //extole.WebView();

                var requestData = new Dictionary<string, Java.Lang.Object>
                {
                    { "extole_name", "Puneet" },
                    { "email", "ppardasani@bofifederalbank.com" }
                };

                extole.SendEvent("deeplink", requestData, null, new SecondActivity());
            });
        }
    }
}
