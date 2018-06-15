@echo on
@ECHO ##########################################################################
@ECHO.
@ECHO  Gradle startup script for Windows
@ECHO.
@ECHO ##########################################################################

@ECHO Choose action:
@ECHO [1] setup for eclipse
@ECHO [2] setup for IDEA
@ECHO [3] clean&setup for eclipse
@ECHO [4] clean&setup for IDEA
@ECHO [5] build
@ECHO [0] exit
@ECHO. 
@ECHO. 
:tryagain
@echo off
set /p variable=""
IF "%variable%"=="1" (goto eclipse)
IF "%variable%"=="2" (goto intelij)
IF "%variable%"=="3" (goto ceclipse)
IF "%variable%"=="4" (goto cintelij)
IF "%variable%"=="5" (goto build)
IF "%variable%"=="0" exit

pause
@echo on
@ECHO Incorrect option, try again.
goto tryagain


:eclipse
@echo on
@ECHO ##########################################################################
gradlew setupDecompWorkspace eclipse --refresh-dependencies
exit

:intelij
@echo on
@ECHO ##########################################################################
gradlew setupDecompWorkspace idea --refresh-dependencies
exit


:ceclipse
@echo on
@ECHO ##########################################################################
gradlew clean setupDecompWorkspace eclipse --refresh-dependencies
exit

:cintelij
@echo on
@ECHO ##########################################################################
gradlew clean setupDecompWorkspace idea --refresh-dependencies
exit


:build
@echo on
@ECHO ##########################################################################
gradlew build
exit