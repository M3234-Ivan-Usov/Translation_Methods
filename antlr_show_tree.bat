@echo off
set package=ru.ifmo.rain.usov.antlr.grammar
set bin_dir=out/production/MT
set src_dir=src/ru/ifmo/rain/usov/antlr/grammar
java -cp %bin_dir%;antlr-4.8-complete.jar org.antlr.v4.gui.TestRig %package%.%1 %3 -gui %src_dir%/%2