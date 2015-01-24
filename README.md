# Android Crash Catcher sample project

A sample project to demonstrate how one can catch an unhandled exception in Android and display a custom error dialog, like this:

<img src="http://i.imgur.com/lgZ1ReW.png?1"></img>

Unlike other solutions around the web this one doesn't restart the application 
via `System.exit` once the exception is caught, but rather uses tricky `try-catch` block to allow 
the app survive the exceptions thrown in the UI and background threads.


More details can be found in my SO post:  
http://stackoverflow.com/a/28126501/648313



## License
```
This software is licensed under the Apache 2 license, quoted below.

Copyright 2011, 2015 Volodymyr Galandzij (aka Idolon)

Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy of
the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.
```
