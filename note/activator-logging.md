
### Sample success log

```
[INFO] [INFO] Scanning for projects...
[INFO] [CODE] Properties present.
[INFO] [INFO] Activator [ACTIVATOR:MVELSCRIPT] project='profile-activator-extension-test-mvelscript' profile='mvelscript-verify' result='true'
```

### Sample failure log

```
[INFO] [INFO] Scanning for projects...
[INFO] [INFO] Activator [ACTIVATOR:MVELSCRIPT] project='profile-activator-extension-test-mvelscript' profile='mvelscript-verify' result='( valid=false, value=false, error=javax.script.ScriptException: [Error: unresolvable property or identifier: invalid_script]
[INFO] [Near : {... invalid_script ....}]
[INFO]                                                                       ^
[INFO] [Line: 3, Column: 5] )'
[INFO] [ERROR] [ERROR] Some problems were encountered while processing the POMs:
[INFO] [ERROR] Evaluation failure:
[INFO]     // This is MVEL.
[INFO]     // Note: can use default scope.
[INFO]     invalid_script
[INFO]     if( isdef property1 && isdef property2 ) {
[INFO]         System.out.println("[CODE] Properties present.");
[INFO]         property1 == property2;
[INFO]     } else {
[INFO]         System.out.println("[CODE] Properties missing.");
[INFO]         false;
[INFO]     }
[INFO] : project='profile-activator-extension-test-mvelscript' profile='mvelscript-verify' engine='mvel' @ line 17, column 18
[INFO]  @ 
[INFO] [ERROR] The build could not read 1 project -> [Help 1]
```
