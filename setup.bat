@echo on
@ECHO ##########################################################################
@ECHO.
@ECHO Gradle startup script for Windows
@ECHO.
@ECHO ##########################################################################

:again
@ECHO Choose action:
@ECHO [1] Eclipse - setup for eclipse
@ECHO [2] Idea (Intelij) - setup for idea
@ECHO [3] Clean - clean everything
@ECHO [4] Build - build project
@ECHO [0] exit - Exit
@ECHO.
@ECHO.
:tryagain
@echo off
set /p variable=""
IF "%variable%"=="1" (goto eclipse)
IF "%variable%"=="eclipse" (goto eclipse)
IF "%variable%"=="Eclipse" (goto eclipse)

IF "%variable%"=="2" (goto intelij)
IF "%variable%"=="Intelij" (goto intelij)
IF "%variable%"=="intelij" (goto intelij)
IF "%variable%"=="idea" (goto intelij)
IF "%variable%"=="Idea" (goto intelij)

IF "%variable%"=="3" (goto clean)
IF "%variable%"=="clean" (goto clean)
IF "%variable%"=="Сlean" (goto clean)

IF "%variable%"=="4" (goto build)
IF "%variable%"=="build" (goto build)
IF "%variable%"=="Build" (goto build)

IF "%variable%"=="0" (goto exit)
IF "%variable%"=="exit" (goto exit)
IF "%variable%"=="Exit" (goto exit)

pause
@echo on
@ECHO Incorrect option, try again.
goto tryagain


:eclipse
@echo on
@ECHO ##########################################################################
gradlew setupDecompWorkspace --refresh-dependencies eclipse

@ECHO ##########################################################################
@ECHO.
@ECHO  Mod is ready to be opened in Eclipse
@ECHO  NOTE: you must manually add the dependencies from the lib folder, aside from buildcraft, to your project structure
@ECHO.
@ECHO ##########################################################################
@pause


:intelij
@echo on
@ECHO ##########################################################################
gradlew setupDecompWorkspace --refresh-dependencies idea

@ECHO ##########################################################################
@ECHO.
@ECHO  Mod is ready to be opened in Intelij/Idea
@ECHO  NOTE: you must manually add the dependencies from the lib folder, aside from buildcraft, to your project structure
@ECHO.
@ECHO ##########################################################################
@pause


:clean
@echo on
@ECHO ##########################################################################
gradlew clean

@ECHO ##########################################################################
@ECHO.
@ECHO  Project cleaned successfully
@ECHO.
@ECHO ##########################################################################
goto again

:build
@echo on
@ECHO ##########################################################################
gradlew build

@ECHO ##########################################################################
@ECHO.
@ECHO  Mod build finished. Get it in /build/libs/
@ECHO.
@ECHO ##########################################################################
@pause

:exit
exit