
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using AndroidX.AppCompat.App;
using Kotlin.Coroutines;
using Kotlin.Jvm.Functions;
using KotlinX.Coroutines;

namespace ExtoleDemoApp.Droid
{
	[Activity (Label = "SecondActivity")]			
	public class SecondActivity : AppCompatActivity, IContinuation
    {
        public ICoroutineContext Context => Dispatchers.IO;

        public void ResumeWith(Java.Lang.Object result)
        {
            
        }

        protected override void OnCreate (Bundle savedInstanceState)
		{
			base.OnCreate (savedInstanceState);

            // Create your application here
		}
	}
}

