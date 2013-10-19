"c:\Program Files\Java\jdk1.6.0_12\bin\keytool.exe" -genkey -keyalg rsa -keysize 1024 -alias toonviewer -keystore "C:\Documents and Settings\shalafi\Desktop\raistlinthewiz\3darmory_viewers\output\toonviewer.keystore"
-validity 360

"c:\Program Files\Java\jdk1.6.0_12\bin\keytool.exe" -certreq -alias toonviewer -keystore "C:\Documents and Settings\shalafi\Desktop\raistlinthewiz\3darmory_viewers\output\toonviewer.keystore" -file "C:\Documents and Settings\shalafi\Desktop\raistlinthewiz\3darmory_viewers\output\toonviewer.csr"

"c:\Program Files\Java\jdk1.6.0_12\bin\jarsigner.exe" -keystore "C:\Documents and Settings\shalafi\Desktop\raistlinthewiz\3darmory_viewers\output\toonviewer.keystore" "C:\Documents and Settings\shalafi\Desktop\raistlinthewiz\3darmory_viewers\output\toonviewer.jar" toonviewer

