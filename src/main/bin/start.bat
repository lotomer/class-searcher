@echo off
set bin_dir=%~dp0%
cd %bin_dir%
title Do not close this window...
java -cp class-searcher.jar org.tomstools.utils.classsearcher.shell.UIClassSearcher
