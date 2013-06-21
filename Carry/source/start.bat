@echo off
:start
cls
echo ===================28专用===========================
set /p key=请输入查询关键字(空格使用_代替)：
set /p year=请输入注册年限：
set /p pagefrom=请输入查询起始页数：
set /p page=请输入查询终止页数：
.\jre\bin\java -Xbootclasspath/a:.\lib\httpclient-4.2.5.jar;.\lib\commons-codec-1.6.jar;.\lib\commons-logging-1.1.1.jar;.\lib\fluent-hc-4.2.5.jar;.\lib\httpclient-cache-4.2.5.jar;.\lib\httpcore-4.2.4.jar;.\lib\httpmime-4.2.5.jar;.\lib\jxl-2.6.jar; -server -Xms300m -Xmx300m -Xmn100m -XX:PermSize=80m -XX:MaxPermSize=80m -jar carry.jar %key% %year% %pagefrom% %page%
:start1
echo 是否需要拷贝文件到指定目录（需要 y 不需要 n）： 
set /p flag=
if "%flag%"=="y" goto cmd1
if not "%flag%" == "n" goto start1
pause
goto :start
:cmd1
echo 输入路径：
set /p path2=
C:\Windows\System32\xcopy .\data\*.* /s %path2%
pause
goto :start