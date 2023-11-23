grammar SQLGramma;

options {

  output=AST;

}

tokens {

    TOK_CREATE_SCHEMA,

    TOK_CREATE_STREAM;

    TOK_CREATE_WINDOW;

    TOK_SELECT;

    TOK_SCHEMA_LIST;

    TOK_NAME_TYPE;

    TOK_SELEXPR;

    TOK_SELITEM;

    TOK_SELLIST;

    TOK_DATA_MESH;
    TOK_EXPLAIN;

    TOK_FROM;

    TOK_WHERE;

    TOK_LOAD;

    TOK_TABNAME;

    TOK_TAB;

    TOK_INPUTFORMAT;

    TOK_PARTVAL;

    TOK_PARTSPEC;

    TOK_INSERT_INTO;

    TOK_DIR;

    TOK_DESTINATION;

    TOK_IFNOTEXISTS;

    TOK_TABCOLNAME;

    TOK_TABLEROWFORMAT;

    TOK_TABLESERIALIZER;

    TOK_TABLEROWFORMATFIELD;

    TOK_SERDEPROPS;

    TOK_TABLEROWFORMATCOLLITEMS;

    TOK_TABLEROWFORMATMAPKEYS;

    TOK_SERDENAME;

    TOK_UNIQUEJOIN;

    TOK_FUNCTION;

    TOK_SUBQUERY_EXPR;

    TOK_SUBQUERY_OP;

    TOK_TABLE_OR_COL;

    TOK_DEFAULT_VALUE;
    TOK_TABREF;
    TOK_QUERY;
    TOK_SUBQUERY;
    TOK_TABLEBUCKETSAMPLE;
    TOK_INSERT;
    TOK_TMP_FILE;
    TOK_SETCOLREF;
    TOK_UNIONALL;
    TOK_UNIONDISTINCT;
    TOK_SELECTDI;

    TOK_EXPLIST;

    TOK_MESHVAL;

    TOK_ITEM;

    TOK_SS;
    TOK_TEST;
    TOK_TEST_ITEM
}

statement

    : createStatement EOF
    | explainStatement EOF
    | meshStatement EOF
    ;


selectStatement

    : KW_SELECT selectList

       KW_FROM Identifier KW_WHERE columnNames=Identifier

       -> ^(TOK_SELECT selectList ^(TOK_FROM $columnNames))

    ;

explainStatement
	: KW_EXPLAIN (
	    explainOption* execStatement -> ^(TOK_EXPLAIN execStatement explainOption*)
      )
	;

execStatement
    : loadStatement;


loadStatement
    	: KW_LOAD KW_DATA (islocal=KW_LOCAL)? KW_INPATH (path=StringLiteral) (isoverwrite=KW_OVERWRITE)? KW_INTO KW_TABLE (tab=tableOrPartition) inputFileFormat? datamesh? test? partitionSpec?
    -> ^(TOK_LOAD $path $tab $islocal? $isoverwrite? inputFileFormat? datamesh? test? partitionSpec?)
    ;

meshStatement
    	: KW_SELECT selectList
    	  KW_FROM (tab=tableOrPartition) inputFileFormat? datamesh? ss?
    -> ^(TOK_SELECT selectList $tab  datamesh? ss? ^(TOK_FROM $tab))
    ;


columnConstraint[CommonTree fkColName]
        : columnName = Identifier
;

ss
      : KW_SS  -> ^(TOK_SS)
      ;


tableOrPartition
   :
   tableName partitionSpec? -> ^(TOK_TAB tableName partitionSpec?)
   ;


partitionSpec
    :
    KW_PARTITION
     LPAREN partitionVal (COMMA  partitionVal )* RPAREN -> ^(TOK_PARTSPEC partitionVal +)
    ;

partitionVal
    :
    identifier (EQUAL constant)? -> ^(TOK_PARTVAL identifier constant?)
    ;

datameshVal
    :
    identifier (EQUAL constant)? -> ^(TOK_MESHVAL identifier constant?)
    ;


constant
    :
    Number
    | StringLiteral

    ;

inputFileFormat
    : KW_INPUTFORMAT inFmt=StringLiteral KW_SERDE serdeCls=StringLiteral
      -> ^(TOK_INPUTFORMAT $inFmt $serdeCls)
    ;
datamesh
    : KW_DATAMESH LPAREN url=StringLiteral RPAREN
      -> ^(TOK_DATA_MESH $url);

test
    : KW_TEST KW_OK? LPAREN testlist (COMMA testlist)* RPAREN
    -> ^(TOK_TEST testlist +)
;

testlist
    : 	a=identifier DOT b=identifier (DOT c=identifier)?
    -> ^(TOK_TEST_ITEM $a $b $c?)
    | b = identifier
    -> ^(TOK_TEST_ITEM $b)
    ;


tableName
    :
    db=identifier DOT tab=identifier (DOT meta=identifier)?
    -> ^(TOK_TABNAME $db $tab $meta?)
    |
    tab=identifier
    -> ^(TOK_TABNAME $tab)
    ;

explainOption
    : KW_EXTENDED
    | KW_FORMATTED
    ;


whereStatement
	:	;

selectList

    : selectColumn (COMMA selectColumn)*

       -> ^(TOK_SELLIST selectColumn+)

    ;

selectColumn
    : selectItem
    | selectExpression
    | item
    ;

selectItem

    : Identifier KW_AS Identifier

       -> ^(TOK_SELITEM Identifier Identifier)

    ;

item
    : Identifier (KW_AS Identifier)?
      -> ^(TOK_ITEM Identifier)
    ;


selectExpression

    : functionName=Identifier LPAREN itemName=Identifier RPAREN KW_AS asIteamName=Identifier

       -> ^(TOK_SELEXPR $functionName $itemName $ asIteamName)

    ;

createStatement

    : KW_CREATE KW_SCHEMA Identifier schemaList

       -> ^(TOK_CREATE_SCHEMA Identifier schemaList)

    | KW_CREATE streamType KW_STREAM streamName=Identifier schemaName=Identifier

       -> ^(TOK_CREATE_STREAM streamType $streamName $schemaName)

    | KW_CREATE KW_WINDOW

       windowName=Identifier LPAREN KW_SIZE Number KW_ADVANCE Number KW_ON onWhat=Identifier RPAREN

       -> ^(TOK_CREATE_WINDOW $windowName Number Number $onWhat)
    ;

schemaList

    : LPAREN columnNameType (COMMA columnNameType)* RPAREN

       -> ^(TOK_SCHEMA_LIST columnNameType+)

    ;

streamType

    : (KW_INPUT | KW_OUTPUT)

    ;

columnNameType

    : coluName=Identifier dataType

       -> ^(TOK_NAME_TYPE $coluName dataType)

    ;


intervalValue
    :
    StringLiteral | Number
    ;


dataType

    : KW_INT

    | KW_DOUBLE

    ;

// Keywords
KW_TRANSFORM:'TRANSFORM';
QUERY_HINT
    : '/*' (options { greedy=false; } : QUERY_HINT|.)* '*/' { if(getText().charAt(2) != '+') { $channel=HIDDEN; } else { setText(getText().substring(3, getText().length() - 2)); } }
    ;
KW_UNION:'UNION';
KW_OUT:'OUT';
KW_OF:'OF';
KW_BUCKET:'BUCKET';
KW_DEFAULT :'DEFAULT';
EQUAL_NS : '<=>';
NOTEQUAL : '<>' | '!=';
LESSTHANOREQUALTO : '<=';
LESSTHAN : '<';
GREATERTHANOREQUALTO : '>=';
GREATERTHAN : '>';
DIVIDE : '/';
STAR : '*';
MOD : '%';
DIV : 'DIV';

KW_RLIKE:	'RLIKE';

KW_EXPLAIN : 'EXPLAIN';

KW_ANYLIZE : 'ANLY';

KW_FROM : 'FROM';

KW_AS : 'AS';

KW_SELECT : 'SELECT';

KW_ON : 'ON';

KW_CREATE: 'CREATE';

KW_INT: 'INT';

KW_DOUBLE: 'DOUBLE';

KW_INTO: 'INTO';

KW_SCHEMA: 'SCHEMA';

KW_INPUT: 'INPUT';

KW_OUTPUT: 'OUTPUT';

KW_STREAM: 'STREAM';

KW_WINDOW: 'WINDOW';

KW_SIZE: 'SIZE';

KW_ADVANCE: 'ADVANCE';

KW_DATAMESH : 'DATAMESH';

KW_DATABASES : 'DATABASES';

KW_TABLE : 'TABLE';

KW_WHERE : 'WHERE';

KW_EXTENDED : 'EXTENDED';

KW_FORMATTED : 'FORMATTED';

KW_LOAD	: 'LOAD';

KW_DATA	: 'DATA';

KW_LOCAL : 'LOCAL';

KW_INPATH : 'INPATH';

KW_OVERWRITE : 'OVERWRITE';

KW_INPUTFORMAT : 'INPUTFORMAT';

KW_SERDE : 'SERDE';

KW_PARTITION : 'PARTITION';

KW_INSERT : 'INSERT';

KW_DIRECTORY : 'DIRECTORY';

KW_IF : 'IF';

KW_NOT	: 'NOT';

KW_EXISTS : 'EXISTS';

KW_ROW : 'ROW';
KW_FORMAT : 'FORMAT';
KW_DELIMITED : 'DELIMITED';

KW_FIELDS:'FIELDS';
KW_TERMINATED :'TERMINATED';
KW_BY : 'BY';
KW_ESCAPED:'ESCAPED';

KW_COLLECTION : 'COLLECTION';

KW_ITEMS : 'ITEMS';

KW_MAP : 'MAP';
KW_KEYS : 'KEYS';

KW_UNIQUEJOIN : 'UNIQUEJOIN';

KW_PRESERVE : 'PRESERVE';

KW_TABLESAMPLE :'TABLESAMPLE';

KW_OR	: 'OR';

KW_AND	: 'AND';

KW_IS	:	'IS';
KW_SS 	:	'SS';

AMPERSAND : '&';
TILDE : '~';
BITWISEOR : '|';
CONCATENATE : '||';
BITWISEXOR : '^';
QUESTION : '?';
DOLLAR : '$';

KW_TRUE : 'TRUE';
KW_FALSE : 'FALSE';
KW_UNKNOWN : 'UNKNOWN';
KW_ALL : 'ALL';
KW_SOME : 'SOME';
KW_NONE: 'NONE';

KW_LIKE : 'LIKE';
KW_ANY : 'ANY';

KW_REGEXP : 'REGEXP';

KW_NULL : 'NULL';
KW_DISTINCT : 'DISTINCT';

KW_BETWEEN :'BETWEEN';

KW_IN:'IN';

KW_TEST	:	'TEST';
KW_OK 	:	'OK';





// Operators

// NOTE: if you add a new function/operator, add it to sysFuncNames so that describe function _FUNC_ will work.



DOT : '.'; // generated as a part of Number rule

COMMA : ',' ;

SEMICOLON : ';' ;

LPAREN : '(' ;

RPAREN : ')' ;

LSQUARE : '[' ;

RSQUARE : ']' ;

MINUS : '-';

PLUS : '+';



// LITERALS

fragment

Letter

    : 'a'..'z' | 'A'..'Z'

    ;

fragment

Digit

    : '0'..'9'

    ;

fragment

Exponent

    : 'e' ( PLUS|MINUS )? (Digit)+

    ;

fragment
EQUAL : '=' | '==';

Number

    : (Digit)+ ( DOT (Digit)* (Exponent)? | Exponent)?

    ;

Identifier

    : (Letter | Digit) (Letter | Digit | '_')*

    ;

WS  :  (' '|'\r'|'\t'|'\n') {$channel=HIDDEN;}

    ;

identifier
    :
    Identifier
    | nonReserved -> Identifier[$nonReserved.start]
    ;

nonReserved
	:	KW_DATABASES;

StringLiteral
    :
    ( '\"' ( ~('\"'|'\\') | ('\\' .) )* '\"' | '\'' ( ~('\''|'\\') | ('\\' .) )* '\'' )+
    ;
