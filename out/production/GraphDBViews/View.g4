grammar View;



/* Could change to have stuff like... parser rules for each of the things we know. that would be easier but
for the sake of having a valid parser I believe it's better to start off more general. */

/*
TODO: (n:Person { attributes here } )

*/
root : COMMAND NAME viewuse query |
        'CG' query ;

query :  KEYWORD expr conditions returnstmt |
         KEYWORD path conditions returnstmt
        ;

viewuse  : 'WITH VIEWS' usedviews | ;
usedviews : NAME*;

viewatom : '(' NAME ':' NAME ')' |
             viewatom '-[' relation ']-' viewatom |
             viewatom '-[' relation ']-' variable |
             variable '-[' relation ']-' viewatom |
             variable '-[' relation ']-' variable
             ;
returnstmt : RETURN retval;
retval : 'NODES(' NAME ')' |
         attribute
         ;

expr : viewatom | variable ;
variable : nodeName | '('nodeName')' | '('nodeName':'type')';
type    : NAME ;
nodeName : NAME ;
relation : relationValue?(':'type)? ;
relationValue : NAME ;
path    : NAME '=' expr ;
conditions  : 'WHERE' boolexpr | ;
boolexpr    :
              attribute COMPARISON attribute |
              attribute COMPARISON val |
              attribute '=' attribute |
              attribute '=' val |
              boolexpr 'AND' boolexpr |
              boolexpr 'OR' boolexpr |
              VALUE OPERATOR attribute | //not supported
              NAME 'IN' NAME | //viewUse
              '(' boolexpr ')' ;
attribute   : NAME('.'NAME)? ;
val         : VALUE | NAME | CONSTANTS;
test : attribute COMPARISON attribute;
/*

Lexer rules

*/


KEYWORD   : 'MATCH' | 'MERGE' ;
RETURN  : 'RETURN' ;
COMMAND : 'CREATE VIEW AS' | 'USE VIEW' ;
COMPARISON : '>' | '<' | '>=' |'<=' ;
OPERATOR : 'CASE'|'CONTAINS'|'ELSE'|'END'|
           'ENDS WITH'|'IN'|'IS NOT NULL'|'IS NULL'|'NOT'|'STARTS WITH'|'THEN'|'WHEN'|'XOR';
CONSTANTS : 'true' | 'false' | 'null';

VALUE   : [0]|[1-9][0-9]* | [0]'.'[0-9]*[1-9]
            | '"'[a-zA-Z_ ]+[0-9]*'"';
NAME    : [a-zA-Z_]+[0-9]* ;
WHITESPACE : ' '-> skip ;
ANY     : . ;


