@echo off
set src_dir=src/ru/ifmo/rain/usov/antlr/grammar
set package=ru.ifmo.rain.usov.antlr.grammar
java -jar antlr-4.8-complete.jar -package %package% -o %src_dir% %src_dir%/%1