grammar Python;
tokens { INDENT, DEDENT }

@lexer::header {
    import ru.ifmo.rain.usov.antlr.DenterHelper;
}

@lexer::members {
    private final DenterHelper denter = DenterHelper.builder()
        .nl(NEWLINE).indent(PythonParser.INDENT)
        .dedent(PythonParser.DEDENT)
        .pullToken(PythonLexer.super::nextToken);

    @Override
    public Token nextToken() {
        return denter.nextToken();
    }
}

root : statement+ ;
statement : complex_statement | single_line ;
complex_statement : for_cycle | while_cycle | if_condition | function | clazz ;
single_line : (assignment | update | ret | 'break' | 'continue' | 'pass' | import_lib | func_call) NEWLINE ;

function: func_def ':' func_body ;
func_def : 'def' NAME func_params ;
func_params : '(' arg? (',' arg)* ')';
arg: typed_arg | key_arg | simple_arg ;
typed_arg: NAME ':' NAME ;
key_arg : NAME '=' (support_types | func_call) ;
simple_arg : NAME ;
func_body : block ;

clazz : 'class' NAME ancestors? ':' clazz_body ;
ancestors: '(' NAME? (',' NAME)*  ')';
clazz_body : block ;

assignment : lvalue '=' rvalue ;
update : lvalue UPDATE rvalue ;
ret : 'return' rvalue? ;
import_lib : ('from' complex_name)? 'import' complex_name ('as' NAME)? ;

func_call : (built_in | complex_name) call_params ;
built_in : 'print' | 'len' ;
call_params : '(' param? (',' param)* ')';
param : key_param | simple_param ;
key_param : NAME '=' rvalue ;
simple_param : rvalue ;

lvalue : complex_name ;
rvalue : comp_or ;

for_cycle : 'for' (arithmetic_cycle | range_based) ':' cycle ;
arithmetic_cycle : NAME 'in' 'range' '(' int_value (',' int_value)? (',' int_value)? ')' ;
range_based : NAME 'in' complex_name ;
int_value : integral | complex_name ;
while_cycle : 'while' predicate ':' cycle ;
cycle : block ;

if_condition : 'if' predicate ':' branch (elif_condition | else_condition?) ;
elif_condition : 'elif' predicate ':' branch (elif_condition | else_condition?) ;
else_condition : 'else' ':' branch ;
branch : block ;

predicate : comp_or ;
comp_or : comp_xor comp_or_prime ;
comp_or_prime : ('or' comp_xor comp_or_prime)* ;
comp_xor : comp_and comp_xor_prime ;
comp_xor_prime : ('xor' comp_and comp_xor_prime)* ;
comp_and : comp_not comp_and_prime ;
comp_and_prime : ('and' comp_not comp_and_prime)* ;
comp_not : ('not' comp_not) | arithmetic_comparison ;

arithmetic_comparison : arithmetic (COMPARE rvalue)? ;
arithmetic : term (('+' | '-') term)* ;
term : factor (('//' | '*' | '/' | '%') factor)* ;
factor : (('+' | '-') factor) | power ;
power : atom ('**' factor)? ;
atom : support_types | func_call | complex_name | ('(' rvalue ')') ;

complex_name : (SELF NAME) | (NAME ('.' NAME)?) ;
block : INDENT statement+ DEDENT ;

support_types : bool | none | floating | integral | string;
bool : 'True' | 'False' ;
none : 'None' ;
floating : '-'? INT '.' INT;
integral : '-'? INT ;
string : '"' (.)*? '"' ;

UPDATE : ('+=' | '-=' | '*=' | '/=') ;
COMPARE : ('==' | '!=' | '<' | '<=' | '>' | '>=') ;

SELF : 'self.' ;
NAME : (LETTER | '_') (LETTER | '_' | INT)* ;
LETTER : ('a'..'z'|'A'..'Z') ;
INT : ('0'..'9')+ ;

NEWLINE : '\r'? '\n' ' '* ;
WS : ' '+ -> skip ;