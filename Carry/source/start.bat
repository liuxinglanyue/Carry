@echo off
:start
cls
echo ===================28ר��===========================
set /p key=�������ѯ�ؼ���(�ո�ʹ��_����)��
set /p year=������ע�����ޣ�
set /p pagefrom=�������ѯ��ʼҳ����
set /p page=�������ѯ��ֹҳ����
.\jre\bin\java -Xbootclasspath/a:.\lib\httpclient-4.2.5.jar;.\lib\commons-codec-1.6.jar;.\lib\commons-logging-1.1.1.jar;.\lib\fluent-hc-4.2.5.jar;.\lib\httpclient-cache-4.2.5.jar;.\lib\httpcore-4.2.4.jar;.\lib\httpmime-4.2.5.jar;.\lib\jxl-2.6.jar; -server -Xms300m -Xmx300m -Xmn100m -XX:PermSize=80m -XX:MaxPermSize=80m -jar carry.jar %key% %year% %pagefrom% %page%
:start1
echo �Ƿ���Ҫ�����ļ���ָ��Ŀ¼����Ҫ y ����Ҫ n���� 
set /p flag=
if "%flag%"=="y" goto cmd1
if not "%flag%" == "n" goto start1
pause
goto :start
:cmd1
echo ����·����
set /p path2=
C:\Windows\System32\xcopy .\data\*.* /s %path2%
pause
goto :start