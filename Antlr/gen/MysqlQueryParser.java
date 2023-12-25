// Generated from D:/Project/SS-Fighting/Antlr/src/main/antlr4/MysqlQuery.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class MysqlQueryParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, AS=11, SELECT=12, FROM=13, TABLE=14, MAX=15, SUM=16, AVG=17, 
		MIN=18, COUNT=19, ALL=20, DISTINCT=21, WHERE=22, GROUP=23, BY=24, ORDER=25, 
		HAVING=26, NOT=27, IS=28, TRUE=29, FALSE=30, UNKNOWN=31, BETWEEN=32, AND=33, 
		IN=34, NULL=35, OR=36, ASC=37, DESC=38, LIMIT=39, OFFSET=40, ID=41, TEXT_STRING=42, 
		ID_LITERAL=43, REVERSE_QUOTE_ID=44, DECIMAL_LITERAL=45, WS=46;
	public static final int
		RULE_tableName = 0, RULE_column_name = 1, RULE_function_name = 2, RULE_selectStatement = 3, 
		RULE_selectElements = 4, RULE_tableSources = 5, RULE_whereClause = 6, 
		RULE_logicExpression = 7, RULE_groupByCaluse = 8, RULE_havingCaluse = 9, 
		RULE_orderByClause = 10, RULE_limitClause = 11, RULE_orderByExpression = 12, 
		RULE_groupByItem = 13, RULE_logicalOperator = 14, RULE_comparisonOperator = 15, 
		RULE_value = 16, RULE_decimalLiteral = 17, RULE_textLiteral = 18, RULE_selectElement = 19, 
		RULE_fullColumnName = 20, RULE_functionCall = 21, RULE_aggregateWindowedFunction = 22, 
		RULE_functionArg = 23, RULE_functionArgs = 24, RULE_uid = 25;
	private static String[] makeRuleNames() {
		return new String[] {
			"tableName", "column_name", "function_name", "selectStatement", "selectElements", 
			"tableSources", "whereClause", "logicExpression", "groupByCaluse", "havingCaluse", 
			"orderByClause", "limitClause", "orderByExpression", "groupByItem", "logicalOperator", 
			"comparisonOperator", "value", "decimalLiteral", "textLiteral", "selectElement", 
			"fullColumnName", "functionCall", "aggregateWindowedFunction", "functionArg", 
			"functionArgs", "uid"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'*'", "','", "'('", "')'", "'&'", "'|'", "'='", "'>'", "'<'", 
			"'!'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, "AS", 
			"SELECT", "FROM", "TABLE", "MAX", "SUM", "AVG", "MIN", "COUNT", "ALL", 
			"DISTINCT", "WHERE", "GROUP", "BY", "ORDER", "HAVING", "NOT", "IS", "TRUE", 
			"FALSE", "UNKNOWN", "BETWEEN", "AND", "IN", "NULL", "OR", "ASC", "DESC", 
			"LIMIT", "OFFSET", "ID", "TEXT_STRING", "ID_LITERAL", "REVERSE_QUOTE_ID", 
			"DECIMAL_LITERAL", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "MysqlQuery.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MysqlQueryParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TableNameContext extends ParserRuleContext {
		public Token tmpName;
		public TerminalNode ID() { return getToken(MysqlQueryParser.ID, 0); }
		public TableNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).enterTableName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).exitTableName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MysqlQueryVisitor ) return ((MysqlQueryVisitor<? extends T>)visitor).visitTableName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableNameContext tableName() throws RecognitionException {
		TableNameContext _localctx = new TableNameContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_tableName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(52);
			((TableNameContext)_localctx).tmpName = match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Column_nameContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(MysqlQueryParser.ID, 0); }
		public Column_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_column_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).enterColumn_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).exitColumn_name(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MysqlQueryVisitor ) return ((MysqlQueryVisitor<? extends T>)visitor).visitColumn_name(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Column_nameContext column_name() throws RecognitionException {
		Column_nameContext _localctx = new Column_nameContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_column_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(54);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Function_nameContext extends ParserRuleContext {
		public Token tmpName;
		public TerminalNode ID() { return getToken(MysqlQueryParser.ID, 0); }
		public Function_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).enterFunction_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).exitFunction_name(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MysqlQueryVisitor ) return ((MysqlQueryVisitor<? extends T>)visitor).visitFunction_name(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Function_nameContext function_name() throws RecognitionException {
		Function_nameContext _localctx = new Function_nameContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_function_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(56);
			((Function_nameContext)_localctx).tmpName = match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SelectStatementContext extends ParserRuleContext {
		public TerminalNode SELECT() { return getToken(MysqlQueryParser.SELECT, 0); }
		public SelectElementsContext selectElements() {
			return getRuleContext(SelectElementsContext.class,0);
		}
		public TerminalNode FROM() { return getToken(MysqlQueryParser.FROM, 0); }
		public TableSourcesContext tableSources() {
			return getRuleContext(TableSourcesContext.class,0);
		}
		public OrderByClauseContext orderByClause() {
			return getRuleContext(OrderByClauseContext.class,0);
		}
		public LimitClauseContext limitClause() {
			return getRuleContext(LimitClauseContext.class,0);
		}
		public WhereClauseContext whereClause() {
			return getRuleContext(WhereClauseContext.class,0);
		}
		public GroupByCaluseContext groupByCaluse() {
			return getRuleContext(GroupByCaluseContext.class,0);
		}
		public HavingCaluseContext havingCaluse() {
			return getRuleContext(HavingCaluseContext.class,0);
		}
		public SelectStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).enterSelectStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).exitSelectStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MysqlQueryVisitor ) return ((MysqlQueryVisitor<? extends T>)visitor).visitSelectStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectStatementContext selectStatement() throws RecognitionException {
		SelectStatementContext _localctx = new SelectStatementContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_selectStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(58);
			match(SELECT);
			setState(59);
			selectElements();
			setState(71);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==FROM) {
				{
				setState(60);
				match(FROM);
				setState(61);
				tableSources();
				setState(63);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WHERE) {
					{
					setState(62);
					whereClause();
					}
				}

				setState(66);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==GROUP) {
					{
					setState(65);
					groupByCaluse();
					}
				}

				setState(69);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==HAVING) {
					{
					setState(68);
					havingCaluse();
					}
				}

				}
			}

			setState(74);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ORDER) {
				{
				setState(73);
				orderByClause();
				}
			}

			setState(77);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LIMIT) {
				{
				setState(76);
				limitClause();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SelectElementsContext extends ParserRuleContext {
		public Token star;
		public List<SelectElementContext> selectElement() {
			return getRuleContexts(SelectElementContext.class);
		}
		public SelectElementContext selectElement(int i) {
			return getRuleContext(SelectElementContext.class,i);
		}
		public SelectElementsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectElements; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).enterSelectElements(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).exitSelectElements(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MysqlQueryVisitor ) return ((MysqlQueryVisitor<? extends T>)visitor).visitSelectElements(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectElementsContext selectElements() throws RecognitionException {
		SelectElementsContext _localctx = new SelectElementsContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_selectElements);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(81);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
				{
				setState(79);
				((SelectElementsContext)_localctx).star = match(T__0);
				}
				break;
			case MAX:
			case SUM:
			case AVG:
			case MIN:
			case COUNT:
			case ID:
				{
				setState(80);
				selectElement();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(87);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(83);
				match(T__1);
				setState(84);
				selectElement();
				}
				}
				setState(89);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TableSourcesContext extends ParserRuleContext {
		public List<TableNameContext> tableName() {
			return getRuleContexts(TableNameContext.class);
		}
		public TableNameContext tableName(int i) {
			return getRuleContext(TableNameContext.class,i);
		}
		public TableSourcesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableSources; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).enterTableSources(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).exitTableSources(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MysqlQueryVisitor ) return ((MysqlQueryVisitor<? extends T>)visitor).visitTableSources(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableSourcesContext tableSources() throws RecognitionException {
		TableSourcesContext _localctx = new TableSourcesContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_tableSources);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(90);
			tableName();
			setState(95);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(91);
				match(T__1);
				setState(92);
				tableName();
				}
				}
				setState(97);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhereClauseContext extends ParserRuleContext {
		public TerminalNode WHERE() { return getToken(MysqlQueryParser.WHERE, 0); }
		public LogicExpressionContext logicExpression() {
			return getRuleContext(LogicExpressionContext.class,0);
		}
		public WhereClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whereClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).enterWhereClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).exitWhereClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MysqlQueryVisitor ) return ((MysqlQueryVisitor<? extends T>)visitor).visitWhereClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhereClauseContext whereClause() throws RecognitionException {
		WhereClauseContext _localctx = new WhereClauseContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_whereClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(98);
			match(WHERE);
			setState(99);
			logicExpression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LogicExpressionContext extends ParserRuleContext {
		public FullColumnNameContext fullColumnName() {
			return getRuleContext(FullColumnNameContext.class,0);
		}
		public ComparisonOperatorContext comparisonOperator() {
			return getRuleContext(ComparisonOperatorContext.class,0);
		}
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
		public TerminalNode BETWEEN() { return getToken(MysqlQueryParser.BETWEEN, 0); }
		public TerminalNode AND() { return getToken(MysqlQueryParser.AND, 0); }
		public TerminalNode IN() { return getToken(MysqlQueryParser.IN, 0); }
		public TerminalNode NOT() { return getToken(MysqlQueryParser.NOT, 0); }
		public List<LogicExpressionContext> logicExpression() {
			return getRuleContexts(LogicExpressionContext.class);
		}
		public LogicExpressionContext logicExpression(int i) {
			return getRuleContext(LogicExpressionContext.class,i);
		}
		public LogicalOperatorContext logicalOperator() {
			return getRuleContext(LogicalOperatorContext.class,0);
		}
		public LogicExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).enterLogicExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).exitLogicExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MysqlQueryVisitor ) return ((MysqlQueryVisitor<? extends T>)visitor).visitLogicExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LogicExpressionContext logicExpression() throws RecognitionException {
		return logicExpression(0);
	}

	private LogicExpressionContext logicExpression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		LogicExpressionContext _localctx = new LogicExpressionContext(_ctx, _parentState);
		LogicExpressionContext _prevctx = _localctx;
		int _startState = 14;
		enterRecursionRule(_localctx, 14, RULE_logicExpression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(132);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				{
				setState(102);
				fullColumnName();
				setState(103);
				comparisonOperator();
				setState(104);
				value();
				}
				break;
			case 2:
				{
				setState(106);
				fullColumnName();
				setState(107);
				match(BETWEEN);
				setState(108);
				value();
				setState(109);
				match(AND);
				setState(110);
				value();
				}
				break;
			case 3:
				{
				setState(112);
				fullColumnName();
				setState(114);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NOT) {
					{
					setState(113);
					match(NOT);
					}
				}

				setState(116);
				match(IN);
				setState(117);
				match(T__2);
				setState(118);
				value();
				setState(123);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__1) {
					{
					{
					setState(119);
					match(T__1);
					setState(120);
					value();
					}
					}
					setState(125);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(126);
				match(T__3);
				}
				break;
			case 4:
				{
				setState(128);
				match(T__2);
				setState(129);
				logicExpression(0);
				setState(130);
				match(T__3);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(140);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new LogicExpressionContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_logicExpression);
					setState(134);
					if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
					setState(135);
					logicalOperator();
					setState(136);
					logicExpression(6);
					}
					} 
				}
				setState(142);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class GroupByCaluseContext extends ParserRuleContext {
		public TerminalNode GROUP() { return getToken(MysqlQueryParser.GROUP, 0); }
		public TerminalNode BY() { return getToken(MysqlQueryParser.BY, 0); }
		public List<GroupByItemContext> groupByItem() {
			return getRuleContexts(GroupByItemContext.class);
		}
		public GroupByItemContext groupByItem(int i) {
			return getRuleContext(GroupByItemContext.class,i);
		}
		public GroupByCaluseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupByCaluse; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).enterGroupByCaluse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).exitGroupByCaluse(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MysqlQueryVisitor ) return ((MysqlQueryVisitor<? extends T>)visitor).visitGroupByCaluse(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupByCaluseContext groupByCaluse() throws RecognitionException {
		GroupByCaluseContext _localctx = new GroupByCaluseContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_groupByCaluse);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(143);
			match(GROUP);
			setState(144);
			match(BY);
			setState(145);
			groupByItem();
			setState(150);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(146);
				match(T__1);
				setState(147);
				groupByItem();
				}
				}
				setState(152);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class HavingCaluseContext extends ParserRuleContext {
		public TerminalNode HAVING() { return getToken(MysqlQueryParser.HAVING, 0); }
		public LogicExpressionContext logicExpression() {
			return getRuleContext(LogicExpressionContext.class,0);
		}
		public HavingCaluseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_havingCaluse; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).enterHavingCaluse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).exitHavingCaluse(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MysqlQueryVisitor ) return ((MysqlQueryVisitor<? extends T>)visitor).visitHavingCaluse(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HavingCaluseContext havingCaluse() throws RecognitionException {
		HavingCaluseContext _localctx = new HavingCaluseContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_havingCaluse);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(153);
			match(HAVING);
			setState(154);
			logicExpression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OrderByClauseContext extends ParserRuleContext {
		public TerminalNode ORDER() { return getToken(MysqlQueryParser.ORDER, 0); }
		public TerminalNode BY() { return getToken(MysqlQueryParser.BY, 0); }
		public List<OrderByExpressionContext> orderByExpression() {
			return getRuleContexts(OrderByExpressionContext.class);
		}
		public OrderByExpressionContext orderByExpression(int i) {
			return getRuleContext(OrderByExpressionContext.class,i);
		}
		public OrderByClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderByClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).enterOrderByClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).exitOrderByClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MysqlQueryVisitor ) return ((MysqlQueryVisitor<? extends T>)visitor).visitOrderByClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderByClauseContext orderByClause() throws RecognitionException {
		OrderByClauseContext _localctx = new OrderByClauseContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_orderByClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(156);
			match(ORDER);
			setState(157);
			match(BY);
			setState(158);
			orderByExpression();
			setState(163);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(159);
				match(T__1);
				setState(160);
				orderByExpression();
				}
				}
				setState(165);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LimitClauseContext extends ParserRuleContext {
		public DecimalLiteralContext offset;
		public DecimalLiteralContext limit;
		public TerminalNode LIMIT() { return getToken(MysqlQueryParser.LIMIT, 0); }
		public TerminalNode OFFSET() { return getToken(MysqlQueryParser.OFFSET, 0); }
		public List<DecimalLiteralContext> decimalLiteral() {
			return getRuleContexts(DecimalLiteralContext.class);
		}
		public DecimalLiteralContext decimalLiteral(int i) {
			return getRuleContext(DecimalLiteralContext.class,i);
		}
		public LimitClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_limitClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).enterLimitClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).exitLimitClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MysqlQueryVisitor ) return ((MysqlQueryVisitor<? extends T>)visitor).visitLimitClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LimitClauseContext limitClause() throws RecognitionException {
		LimitClauseContext _localctx = new LimitClauseContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_limitClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(166);
			match(LIMIT);
			setState(177);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				{
				setState(170);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
				case 1:
					{
					setState(167);
					((LimitClauseContext)_localctx).offset = decimalLiteral();
					setState(168);
					match(T__1);
					}
					break;
				}
				setState(172);
				((LimitClauseContext)_localctx).limit = decimalLiteral();
				}
				break;
			case 2:
				{
				setState(173);
				((LimitClauseContext)_localctx).limit = decimalLiteral();
				setState(174);
				match(OFFSET);
				setState(175);
				((LimitClauseContext)_localctx).offset = decimalLiteral();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OrderByExpressionContext extends ParserRuleContext {
		public Token order;
		public FullColumnNameContext fullColumnName() {
			return getRuleContext(FullColumnNameContext.class,0);
		}
		public TerminalNode ASC() { return getToken(MysqlQueryParser.ASC, 0); }
		public TerminalNode DESC() { return getToken(MysqlQueryParser.DESC, 0); }
		public OrderByExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderByExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).enterOrderByExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).exitOrderByExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MysqlQueryVisitor ) return ((MysqlQueryVisitor<? extends T>)visitor).visitOrderByExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderByExpressionContext orderByExpression() throws RecognitionException {
		OrderByExpressionContext _localctx = new OrderByExpressionContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_orderByExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(179);
			fullColumnName();
			setState(181);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASC || _la==DESC) {
				{
				setState(180);
				((OrderByExpressionContext)_localctx).order = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==ASC || _la==DESC) ) {
					((OrderByExpressionContext)_localctx).order = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class GroupByItemContext extends ParserRuleContext {
		public Token order;
		public FullColumnNameContext fullColumnName() {
			return getRuleContext(FullColumnNameContext.class,0);
		}
		public TerminalNode ASC() { return getToken(MysqlQueryParser.ASC, 0); }
		public TerminalNode DESC() { return getToken(MysqlQueryParser.DESC, 0); }
		public GroupByItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupByItem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).enterGroupByItem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).exitGroupByItem(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MysqlQueryVisitor ) return ((MysqlQueryVisitor<? extends T>)visitor).visitGroupByItem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupByItemContext groupByItem() throws RecognitionException {
		GroupByItemContext _localctx = new GroupByItemContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_groupByItem);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			fullColumnName();
			setState(185);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASC || _la==DESC) {
				{
				setState(184);
				((GroupByItemContext)_localctx).order = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==ASC || _la==DESC) ) {
					((GroupByItemContext)_localctx).order = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LogicalOperatorContext extends ParserRuleContext {
		public TerminalNode AND() { return getToken(MysqlQueryParser.AND, 0); }
		public TerminalNode OR() { return getToken(MysqlQueryParser.OR, 0); }
		public LogicalOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicalOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).enterLogicalOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).exitLogicalOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MysqlQueryVisitor ) return ((MysqlQueryVisitor<? extends T>)visitor).visitLogicalOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LogicalOperatorContext logicalOperator() throws RecognitionException {
		LogicalOperatorContext _localctx = new LogicalOperatorContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_logicalOperator);
		try {
			setState(193);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case AND:
				enterOuterAlt(_localctx, 1);
				{
				setState(187);
				match(AND);
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 2);
				{
				setState(188);
				match(T__4);
				setState(189);
				match(T__4);
				}
				break;
			case OR:
				enterOuterAlt(_localctx, 3);
				{
				setState(190);
				match(OR);
				}
				break;
			case T__5:
				enterOuterAlt(_localctx, 4);
				{
				setState(191);
				match(T__5);
				setState(192);
				match(T__5);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ComparisonOperatorContext extends ParserRuleContext {
		public ComparisonOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comparisonOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).enterComparisonOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).exitComparisonOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MysqlQueryVisitor ) return ((MysqlQueryVisitor<? extends T>)visitor).visitComparisonOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComparisonOperatorContext comparisonOperator() throws RecognitionException {
		ComparisonOperatorContext _localctx = new ComparisonOperatorContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_comparisonOperator);
		try {
			setState(209);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(195);
				match(T__6);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(196);
				match(T__7);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(197);
				match(T__8);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(198);
				match(T__8);
				setState(199);
				match(T__6);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(200);
				match(T__7);
				setState(201);
				match(T__6);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(202);
				match(T__8);
				setState(203);
				match(T__7);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(204);
				match(T__9);
				setState(205);
				match(T__6);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(206);
				match(T__8);
				setState(207);
				match(T__6);
				setState(208);
				match(T__7);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ValueContext extends ParserRuleContext {
		public UidContext uid() {
			return getRuleContext(UidContext.class,0);
		}
		public TextLiteralContext textLiteral() {
			return getRuleContext(TextLiteralContext.class,0);
		}
		public DecimalLiteralContext decimalLiteral() {
			return getRuleContext(DecimalLiteralContext.class,0);
		}
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).exitValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MysqlQueryVisitor ) return ((MysqlQueryVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_value);
		try {
			setState(214);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(211);
				uid();
				}
				break;
			case TEXT_STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(212);
				textLiteral();
				}
				break;
			case DECIMAL_LITERAL:
				enterOuterAlt(_localctx, 3);
				{
				setState(213);
				decimalLiteral();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DecimalLiteralContext extends ParserRuleContext {
		public TerminalNode DECIMAL_LITERAL() { return getToken(MysqlQueryParser.DECIMAL_LITERAL, 0); }
		public DecimalLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_decimalLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).enterDecimalLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).exitDecimalLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MysqlQueryVisitor ) return ((MysqlQueryVisitor<? extends T>)visitor).visitDecimalLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DecimalLiteralContext decimalLiteral() throws RecognitionException {
		DecimalLiteralContext _localctx = new DecimalLiteralContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_decimalLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(216);
			match(DECIMAL_LITERAL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TextLiteralContext extends ParserRuleContext {
		public TerminalNode TEXT_STRING() { return getToken(MysqlQueryParser.TEXT_STRING, 0); }
		public TextLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_textLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).enterTextLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).exitTextLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MysqlQueryVisitor ) return ((MysqlQueryVisitor<? extends T>)visitor).visitTextLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TextLiteralContext textLiteral() throws RecognitionException {
		TextLiteralContext _localctx = new TextLiteralContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_textLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(218);
			match(TEXT_STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SelectElementContext extends ParserRuleContext {
		public SelectElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectElement; }
	 
		public SelectElementContext() { }
		public void copyFrom(SelectElementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SelectFunctionElementContext extends SelectElementContext {
		public FunctionCallContext functionCall() {
			return getRuleContext(FunctionCallContext.class,0);
		}
		public UidContext uid() {
			return getRuleContext(UidContext.class,0);
		}
		public TerminalNode AS() { return getToken(MysqlQueryParser.AS, 0); }
		public SelectFunctionElementContext(SelectElementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).enterSelectFunctionElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).exitSelectFunctionElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MysqlQueryVisitor ) return ((MysqlQueryVisitor<? extends T>)visitor).visitSelectFunctionElement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SelectColumnElementContext extends SelectElementContext {
		public FullColumnNameContext fullColumnName() {
			return getRuleContext(FullColumnNameContext.class,0);
		}
		public UidContext uid() {
			return getRuleContext(UidContext.class,0);
		}
		public TerminalNode AS() { return getToken(MysqlQueryParser.AS, 0); }
		public SelectColumnElementContext(SelectElementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).enterSelectColumnElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).exitSelectColumnElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MysqlQueryVisitor ) return ((MysqlQueryVisitor<? extends T>)visitor).visitSelectColumnElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectElementContext selectElement() throws RecognitionException {
		SelectElementContext _localctx = new SelectElementContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_selectElement);
		int _la;
		try {
			setState(234);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				_localctx = new SelectColumnElementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(220);
				fullColumnName();
				setState(225);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==AS || _la==ID) {
					{
					setState(222);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==AS) {
						{
						setState(221);
						match(AS);
						}
					}

					setState(224);
					uid();
					}
				}

				}
				break;
			case MAX:
			case SUM:
			case AVG:
			case MIN:
			case COUNT:
				_localctx = new SelectFunctionElementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(227);
				functionCall();
				setState(232);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==AS || _la==ID) {
					{
					setState(229);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==AS) {
						{
						setState(228);
						match(AS);
						}
					}

					setState(231);
					uid();
					}
				}

				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FullColumnNameContext extends ParserRuleContext {
		public Column_nameContext column_name() {
			return getRuleContext(Column_nameContext.class,0);
		}
		public FullColumnNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fullColumnName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).enterFullColumnName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).exitFullColumnName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MysqlQueryVisitor ) return ((MysqlQueryVisitor<? extends T>)visitor).visitFullColumnName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FullColumnNameContext fullColumnName() throws RecognitionException {
		FullColumnNameContext _localctx = new FullColumnNameContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_fullColumnName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(236);
			column_name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionCallContext extends ParserRuleContext {
		public FunctionCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionCall; }
	 
		public FunctionCallContext() { }
		public void copyFrom(FunctionCallContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AggregateFunctionCallContext extends FunctionCallContext {
		public AggregateWindowedFunctionContext aggregateWindowedFunction() {
			return getRuleContext(AggregateWindowedFunctionContext.class,0);
		}
		public AggregateFunctionCallContext(FunctionCallContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).enterAggregateFunctionCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).exitAggregateFunctionCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MysqlQueryVisitor ) return ((MysqlQueryVisitor<? extends T>)visitor).visitAggregateFunctionCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionCallContext functionCall() throws RecognitionException {
		FunctionCallContext _localctx = new FunctionCallContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_functionCall);
		try {
			_localctx = new AggregateFunctionCallContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(238);
			aggregateWindowedFunction();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AggregateWindowedFunctionContext extends ParserRuleContext {
		public Token starArg;
		public Token aggregator;
		public FunctionArgContext functionArg() {
			return getRuleContext(FunctionArgContext.class,0);
		}
		public TerminalNode AVG() { return getToken(MysqlQueryParser.AVG, 0); }
		public TerminalNode MAX() { return getToken(MysqlQueryParser.MAX, 0); }
		public TerminalNode MIN() { return getToken(MysqlQueryParser.MIN, 0); }
		public TerminalNode SUM() { return getToken(MysqlQueryParser.SUM, 0); }
		public TerminalNode COUNT() { return getToken(MysqlQueryParser.COUNT, 0); }
		public FunctionArgsContext functionArgs() {
			return getRuleContext(FunctionArgsContext.class,0);
		}
		public TerminalNode DISTINCT() { return getToken(MysqlQueryParser.DISTINCT, 0); }
		public AggregateWindowedFunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_aggregateWindowedFunction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).enterAggregateWindowedFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).exitAggregateWindowedFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MysqlQueryVisitor ) return ((MysqlQueryVisitor<? extends T>)visitor).visitAggregateWindowedFunction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AggregateWindowedFunctionContext aggregateWindowedFunction() throws RecognitionException {
		AggregateWindowedFunctionContext _localctx = new AggregateWindowedFunctionContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_aggregateWindowedFunction);
		int _la;
		try {
			setState(260);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(240);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 491520L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(241);
				match(T__2);
				setState(242);
				functionArg();
				setState(243);
				match(T__3);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(245);
				match(COUNT);
				setState(246);
				match(T__2);
				setState(251);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__0:
					{
					setState(247);
					((AggregateWindowedFunctionContext)_localctx).starArg = match(T__0);
					}
					break;
				case T__3:
				case ID:
					{
					setState(249);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==ID) {
						{
						setState(248);
						functionArg();
						}
					}

					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(253);
				match(T__3);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(254);
				match(COUNT);
				setState(255);
				match(T__2);
				setState(256);
				((AggregateWindowedFunctionContext)_localctx).aggregator = match(DISTINCT);
				setState(257);
				functionArgs();
				setState(258);
				match(T__3);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionArgContext extends ParserRuleContext {
		public Column_nameContext column_name() {
			return getRuleContext(Column_nameContext.class,0);
		}
		public FunctionArgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionArg; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).enterFunctionArg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).exitFunctionArg(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MysqlQueryVisitor ) return ((MysqlQueryVisitor<? extends T>)visitor).visitFunctionArg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionArgContext functionArg() throws RecognitionException {
		FunctionArgContext _localctx = new FunctionArgContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_functionArg);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(262);
			column_name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionArgsContext extends ParserRuleContext {
		public List<Column_nameContext> column_name() {
			return getRuleContexts(Column_nameContext.class);
		}
		public Column_nameContext column_name(int i) {
			return getRuleContext(Column_nameContext.class,i);
		}
		public FunctionArgsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionArgs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).enterFunctionArgs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).exitFunctionArgs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MysqlQueryVisitor ) return ((MysqlQueryVisitor<? extends T>)visitor).visitFunctionArgs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionArgsContext functionArgs() throws RecognitionException {
		FunctionArgsContext _localctx = new FunctionArgsContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_functionArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(264);
			column_name();
			setState(269);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(265);
				match(T__1);
				setState(266);
				column_name();
				}
				}
				setState(271);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UidContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(MysqlQueryParser.ID, 0); }
		public UidContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_uid; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).enterUid(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MysqlQueryListener ) ((MysqlQueryListener)listener).exitUid(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MysqlQueryVisitor ) return ((MysqlQueryVisitor<? extends T>)visitor).visitUid(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UidContext uid() throws RecognitionException {
		UidContext _localctx = new UidContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_uid);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(272);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 7:
			return logicExpression_sempred((LogicExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean logicExpression_sempred(LogicExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 5);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001.\u0113\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001"+
		"\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0003\u0003@\b\u0003\u0001\u0003\u0003\u0003C\b\u0003\u0001"+
		"\u0003\u0003\u0003F\b\u0003\u0003\u0003H\b\u0003\u0001\u0003\u0003\u0003"+
		"K\b\u0003\u0001\u0003\u0003\u0003N\b\u0003\u0001\u0004\u0001\u0004\u0003"+
		"\u0004R\b\u0004\u0001\u0004\u0001\u0004\u0005\u0004V\b\u0004\n\u0004\f"+
		"\u0004Y\t\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0005\u0005^\b\u0005"+
		"\n\u0005\f\u0005a\t\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0003\u0007s\b\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0005\u0007z\b\u0007\n\u0007\f\u0007}\t\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0003\u0007"+
		"\u0085\b\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0005\u0007"+
		"\u008b\b\u0007\n\u0007\f\u0007\u008e\t\u0007\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0005\b\u0095\b\b\n\b\f\b\u0098\t\b\u0001\t\u0001\t\u0001\t"+
		"\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0005\n\u00a2\b\n\n\n\f\n\u00a5"+
		"\t\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0003\u000b\u00ab"+
		"\b\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0003"+
		"\u000b\u00b2\b\u000b\u0001\f\u0001\f\u0003\f\u00b6\b\f\u0001\r\u0001\r"+
		"\u0003\r\u00ba\b\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0003\u000e\u00c2\b\u000e\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0003"+
		"\u000f\u00d2\b\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0003\u0010\u00d7"+
		"\b\u0010\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0013\u0001"+
		"\u0013\u0003\u0013\u00df\b\u0013\u0001\u0013\u0003\u0013\u00e2\b\u0013"+
		"\u0001\u0013\u0001\u0013\u0003\u0013\u00e6\b\u0013\u0001\u0013\u0003\u0013"+
		"\u00e9\b\u0013\u0003\u0013\u00eb\b\u0013\u0001\u0014\u0001\u0014\u0001"+
		"\u0015\u0001\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0003\u0016\u00fa"+
		"\b\u0016\u0003\u0016\u00fc\b\u0016\u0001\u0016\u0001\u0016\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0003\u0016\u0105\b\u0016"+
		"\u0001\u0017\u0001\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0005\u0018"+
		"\u010c\b\u0018\n\u0018\f\u0018\u010f\t\u0018\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0000\u0001\u000e\u001a\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010"+
		"\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02\u0000\u0002\u0001"+
		"\u0000%&\u0001\u0000\u000f\u0012\u0123\u00004\u0001\u0000\u0000\u0000"+
		"\u00026\u0001\u0000\u0000\u0000\u00048\u0001\u0000\u0000\u0000\u0006:"+
		"\u0001\u0000\u0000\u0000\bQ\u0001\u0000\u0000\u0000\nZ\u0001\u0000\u0000"+
		"\u0000\fb\u0001\u0000\u0000\u0000\u000e\u0084\u0001\u0000\u0000\u0000"+
		"\u0010\u008f\u0001\u0000\u0000\u0000\u0012\u0099\u0001\u0000\u0000\u0000"+
		"\u0014\u009c\u0001\u0000\u0000\u0000\u0016\u00a6\u0001\u0000\u0000\u0000"+
		"\u0018\u00b3\u0001\u0000\u0000\u0000\u001a\u00b7\u0001\u0000\u0000\u0000"+
		"\u001c\u00c1\u0001\u0000\u0000\u0000\u001e\u00d1\u0001\u0000\u0000\u0000"+
		" \u00d6\u0001\u0000\u0000\u0000\"\u00d8\u0001\u0000\u0000\u0000$\u00da"+
		"\u0001\u0000\u0000\u0000&\u00ea\u0001\u0000\u0000\u0000(\u00ec\u0001\u0000"+
		"\u0000\u0000*\u00ee\u0001\u0000\u0000\u0000,\u0104\u0001\u0000\u0000\u0000"+
		".\u0106\u0001\u0000\u0000\u00000\u0108\u0001\u0000\u0000\u00002\u0110"+
		"\u0001\u0000\u0000\u000045\u0005)\u0000\u00005\u0001\u0001\u0000\u0000"+
		"\u000067\u0005)\u0000\u00007\u0003\u0001\u0000\u0000\u000089\u0005)\u0000"+
		"\u00009\u0005\u0001\u0000\u0000\u0000:;\u0005\f\u0000\u0000;G\u0003\b"+
		"\u0004\u0000<=\u0005\r\u0000\u0000=?\u0003\n\u0005\u0000>@\u0003\f\u0006"+
		"\u0000?>\u0001\u0000\u0000\u0000?@\u0001\u0000\u0000\u0000@B\u0001\u0000"+
		"\u0000\u0000AC\u0003\u0010\b\u0000BA\u0001\u0000\u0000\u0000BC\u0001\u0000"+
		"\u0000\u0000CE\u0001\u0000\u0000\u0000DF\u0003\u0012\t\u0000ED\u0001\u0000"+
		"\u0000\u0000EF\u0001\u0000\u0000\u0000FH\u0001\u0000\u0000\u0000G<\u0001"+
		"\u0000\u0000\u0000GH\u0001\u0000\u0000\u0000HJ\u0001\u0000\u0000\u0000"+
		"IK\u0003\u0014\n\u0000JI\u0001\u0000\u0000\u0000JK\u0001\u0000\u0000\u0000"+
		"KM\u0001\u0000\u0000\u0000LN\u0003\u0016\u000b\u0000ML\u0001\u0000\u0000"+
		"\u0000MN\u0001\u0000\u0000\u0000N\u0007\u0001\u0000\u0000\u0000OR\u0005"+
		"\u0001\u0000\u0000PR\u0003&\u0013\u0000QO\u0001\u0000\u0000\u0000QP\u0001"+
		"\u0000\u0000\u0000RW\u0001\u0000\u0000\u0000ST\u0005\u0002\u0000\u0000"+
		"TV\u0003&\u0013\u0000US\u0001\u0000\u0000\u0000VY\u0001\u0000\u0000\u0000"+
		"WU\u0001\u0000\u0000\u0000WX\u0001\u0000\u0000\u0000X\t\u0001\u0000\u0000"+
		"\u0000YW\u0001\u0000\u0000\u0000Z_\u0003\u0000\u0000\u0000[\\\u0005\u0002"+
		"\u0000\u0000\\^\u0003\u0000\u0000\u0000][\u0001\u0000\u0000\u0000^a\u0001"+
		"\u0000\u0000\u0000_]\u0001\u0000\u0000\u0000_`\u0001\u0000\u0000\u0000"+
		"`\u000b\u0001\u0000\u0000\u0000a_\u0001\u0000\u0000\u0000bc\u0005\u0016"+
		"\u0000\u0000cd\u0003\u000e\u0007\u0000d\r\u0001\u0000\u0000\u0000ef\u0006"+
		"\u0007\uffff\uffff\u0000fg\u0003(\u0014\u0000gh\u0003\u001e\u000f\u0000"+
		"hi\u0003 \u0010\u0000i\u0085\u0001\u0000\u0000\u0000jk\u0003(\u0014\u0000"+
		"kl\u0005 \u0000\u0000lm\u0003 \u0010\u0000mn\u0005!\u0000\u0000no\u0003"+
		" \u0010\u0000o\u0085\u0001\u0000\u0000\u0000pr\u0003(\u0014\u0000qs\u0005"+
		"\u001b\u0000\u0000rq\u0001\u0000\u0000\u0000rs\u0001\u0000\u0000\u0000"+
		"st\u0001\u0000\u0000\u0000tu\u0005\"\u0000\u0000uv\u0005\u0003\u0000\u0000"+
		"v{\u0003 \u0010\u0000wx\u0005\u0002\u0000\u0000xz\u0003 \u0010\u0000y"+
		"w\u0001\u0000\u0000\u0000z}\u0001\u0000\u0000\u0000{y\u0001\u0000\u0000"+
		"\u0000{|\u0001\u0000\u0000\u0000|~\u0001\u0000\u0000\u0000}{\u0001\u0000"+
		"\u0000\u0000~\u007f\u0005\u0004\u0000\u0000\u007f\u0085\u0001\u0000\u0000"+
		"\u0000\u0080\u0081\u0005\u0003\u0000\u0000\u0081\u0082\u0003\u000e\u0007"+
		"\u0000\u0082\u0083\u0005\u0004\u0000\u0000\u0083\u0085\u0001\u0000\u0000"+
		"\u0000\u0084e\u0001\u0000\u0000\u0000\u0084j\u0001\u0000\u0000\u0000\u0084"+
		"p\u0001\u0000\u0000\u0000\u0084\u0080\u0001\u0000\u0000\u0000\u0085\u008c"+
		"\u0001\u0000\u0000\u0000\u0086\u0087\n\u0005\u0000\u0000\u0087\u0088\u0003"+
		"\u001c\u000e\u0000\u0088\u0089\u0003\u000e\u0007\u0006\u0089\u008b\u0001"+
		"\u0000\u0000\u0000\u008a\u0086\u0001\u0000\u0000\u0000\u008b\u008e\u0001"+
		"\u0000\u0000\u0000\u008c\u008a\u0001\u0000\u0000\u0000\u008c\u008d\u0001"+
		"\u0000\u0000\u0000\u008d\u000f\u0001\u0000\u0000\u0000\u008e\u008c\u0001"+
		"\u0000\u0000\u0000\u008f\u0090\u0005\u0017\u0000\u0000\u0090\u0091\u0005"+
		"\u0018\u0000\u0000\u0091\u0096\u0003\u001a\r\u0000\u0092\u0093\u0005\u0002"+
		"\u0000\u0000\u0093\u0095\u0003\u001a\r\u0000\u0094\u0092\u0001\u0000\u0000"+
		"\u0000\u0095\u0098\u0001\u0000\u0000\u0000\u0096\u0094\u0001\u0000\u0000"+
		"\u0000\u0096\u0097\u0001\u0000\u0000\u0000\u0097\u0011\u0001\u0000\u0000"+
		"\u0000\u0098\u0096\u0001\u0000\u0000\u0000\u0099\u009a\u0005\u001a\u0000"+
		"\u0000\u009a\u009b\u0003\u000e\u0007\u0000\u009b\u0013\u0001\u0000\u0000"+
		"\u0000\u009c\u009d\u0005\u0019\u0000\u0000\u009d\u009e\u0005\u0018\u0000"+
		"\u0000\u009e\u00a3\u0003\u0018\f\u0000\u009f\u00a0\u0005\u0002\u0000\u0000"+
		"\u00a0\u00a2\u0003\u0018\f\u0000\u00a1\u009f\u0001\u0000\u0000\u0000\u00a2"+
		"\u00a5\u0001\u0000\u0000\u0000\u00a3\u00a1\u0001\u0000\u0000\u0000\u00a3"+
		"\u00a4\u0001\u0000\u0000\u0000\u00a4\u0015\u0001\u0000\u0000\u0000\u00a5"+
		"\u00a3\u0001\u0000\u0000\u0000\u00a6\u00b1\u0005\'\u0000\u0000\u00a7\u00a8"+
		"\u0003\"\u0011\u0000\u00a8\u00a9\u0005\u0002\u0000\u0000\u00a9\u00ab\u0001"+
		"\u0000\u0000\u0000\u00aa\u00a7\u0001\u0000\u0000\u0000\u00aa\u00ab\u0001"+
		"\u0000\u0000\u0000\u00ab\u00ac\u0001\u0000\u0000\u0000\u00ac\u00b2\u0003"+
		"\"\u0011\u0000\u00ad\u00ae\u0003\"\u0011\u0000\u00ae\u00af\u0005(\u0000"+
		"\u0000\u00af\u00b0\u0003\"\u0011\u0000\u00b0\u00b2\u0001\u0000\u0000\u0000"+
		"\u00b1\u00aa\u0001\u0000\u0000\u0000\u00b1\u00ad\u0001\u0000\u0000\u0000"+
		"\u00b2\u0017\u0001\u0000\u0000\u0000\u00b3\u00b5\u0003(\u0014\u0000\u00b4"+
		"\u00b6\u0007\u0000\u0000\u0000\u00b5\u00b4\u0001\u0000\u0000\u0000\u00b5"+
		"\u00b6\u0001\u0000\u0000\u0000\u00b6\u0019\u0001\u0000\u0000\u0000\u00b7"+
		"\u00b9\u0003(\u0014\u0000\u00b8\u00ba\u0007\u0000\u0000\u0000\u00b9\u00b8"+
		"\u0001\u0000\u0000\u0000\u00b9\u00ba\u0001\u0000\u0000\u0000\u00ba\u001b"+
		"\u0001\u0000\u0000\u0000\u00bb\u00c2\u0005!\u0000\u0000\u00bc\u00bd\u0005"+
		"\u0005\u0000\u0000\u00bd\u00c2\u0005\u0005\u0000\u0000\u00be\u00c2\u0005"+
		"$\u0000\u0000\u00bf\u00c0\u0005\u0006\u0000\u0000\u00c0\u00c2\u0005\u0006"+
		"\u0000\u0000\u00c1\u00bb\u0001\u0000\u0000\u0000\u00c1\u00bc\u0001\u0000"+
		"\u0000\u0000\u00c1\u00be\u0001\u0000\u0000\u0000\u00c1\u00bf\u0001\u0000"+
		"\u0000\u0000\u00c2\u001d\u0001\u0000\u0000\u0000\u00c3\u00d2\u0005\u0007"+
		"\u0000\u0000\u00c4\u00d2\u0005\b\u0000\u0000\u00c5\u00d2\u0005\t\u0000"+
		"\u0000\u00c6\u00c7\u0005\t\u0000\u0000\u00c7\u00d2\u0005\u0007\u0000\u0000"+
		"\u00c8\u00c9\u0005\b\u0000\u0000\u00c9\u00d2\u0005\u0007\u0000\u0000\u00ca"+
		"\u00cb\u0005\t\u0000\u0000\u00cb\u00d2\u0005\b\u0000\u0000\u00cc\u00cd"+
		"\u0005\n\u0000\u0000\u00cd\u00d2\u0005\u0007\u0000\u0000\u00ce\u00cf\u0005"+
		"\t\u0000\u0000\u00cf\u00d0\u0005\u0007\u0000\u0000\u00d0\u00d2\u0005\b"+
		"\u0000\u0000\u00d1\u00c3\u0001\u0000\u0000\u0000\u00d1\u00c4\u0001\u0000"+
		"\u0000\u0000\u00d1\u00c5\u0001\u0000\u0000\u0000\u00d1\u00c6\u0001\u0000"+
		"\u0000\u0000\u00d1\u00c8\u0001\u0000\u0000\u0000\u00d1\u00ca\u0001\u0000"+
		"\u0000\u0000\u00d1\u00cc\u0001\u0000\u0000\u0000\u00d1\u00ce\u0001\u0000"+
		"\u0000\u0000\u00d2\u001f\u0001\u0000\u0000\u0000\u00d3\u00d7\u00032\u0019"+
		"\u0000\u00d4\u00d7\u0003$\u0012\u0000\u00d5\u00d7\u0003\"\u0011\u0000"+
		"\u00d6\u00d3\u0001\u0000\u0000\u0000\u00d6\u00d4\u0001\u0000\u0000\u0000"+
		"\u00d6\u00d5\u0001\u0000\u0000\u0000\u00d7!\u0001\u0000\u0000\u0000\u00d8"+
		"\u00d9\u0005-\u0000\u0000\u00d9#\u0001\u0000\u0000\u0000\u00da\u00db\u0005"+
		"*\u0000\u0000\u00db%\u0001\u0000\u0000\u0000\u00dc\u00e1\u0003(\u0014"+
		"\u0000\u00dd\u00df\u0005\u000b\u0000\u0000\u00de\u00dd\u0001\u0000\u0000"+
		"\u0000\u00de\u00df\u0001\u0000\u0000\u0000\u00df\u00e0\u0001\u0000\u0000"+
		"\u0000\u00e0\u00e2\u00032\u0019\u0000\u00e1\u00de\u0001\u0000\u0000\u0000"+
		"\u00e1\u00e2\u0001\u0000\u0000\u0000\u00e2\u00eb\u0001\u0000\u0000\u0000"+
		"\u00e3\u00e8\u0003*\u0015\u0000\u00e4\u00e6\u0005\u000b\u0000\u0000\u00e5"+
		"\u00e4\u0001\u0000\u0000\u0000\u00e5\u00e6\u0001\u0000\u0000\u0000\u00e6"+
		"\u00e7\u0001\u0000\u0000\u0000\u00e7\u00e9\u00032\u0019\u0000\u00e8\u00e5"+
		"\u0001\u0000\u0000\u0000\u00e8\u00e9\u0001\u0000\u0000\u0000\u00e9\u00eb"+
		"\u0001\u0000\u0000\u0000\u00ea\u00dc\u0001\u0000\u0000\u0000\u00ea\u00e3"+
		"\u0001\u0000\u0000\u0000\u00eb\'\u0001\u0000\u0000\u0000\u00ec\u00ed\u0003"+
		"\u0002\u0001\u0000\u00ed)\u0001\u0000\u0000\u0000\u00ee\u00ef\u0003,\u0016"+
		"\u0000\u00ef+\u0001\u0000\u0000\u0000\u00f0\u00f1\u0007\u0001\u0000\u0000"+
		"\u00f1\u00f2\u0005\u0003\u0000\u0000\u00f2\u00f3\u0003.\u0017\u0000\u00f3"+
		"\u00f4\u0005\u0004\u0000\u0000\u00f4\u0105\u0001\u0000\u0000\u0000\u00f5"+
		"\u00f6\u0005\u0013\u0000\u0000\u00f6\u00fb\u0005\u0003\u0000\u0000\u00f7"+
		"\u00fc\u0005\u0001\u0000\u0000\u00f8\u00fa\u0003.\u0017\u0000\u00f9\u00f8"+
		"\u0001\u0000\u0000\u0000\u00f9\u00fa\u0001\u0000\u0000\u0000\u00fa\u00fc"+
		"\u0001\u0000\u0000\u0000\u00fb\u00f7\u0001\u0000\u0000\u0000\u00fb\u00f9"+
		"\u0001\u0000\u0000\u0000\u00fc\u00fd\u0001\u0000\u0000\u0000\u00fd\u0105"+
		"\u0005\u0004\u0000\u0000\u00fe\u00ff\u0005\u0013\u0000\u0000\u00ff\u0100"+
		"\u0005\u0003\u0000\u0000\u0100\u0101\u0005\u0015\u0000\u0000\u0101\u0102"+
		"\u00030\u0018\u0000\u0102\u0103\u0005\u0004\u0000\u0000\u0103\u0105\u0001"+
		"\u0000\u0000\u0000\u0104\u00f0\u0001\u0000\u0000\u0000\u0104\u00f5\u0001"+
		"\u0000\u0000\u0000\u0104\u00fe\u0001\u0000\u0000\u0000\u0105-\u0001\u0000"+
		"\u0000\u0000\u0106\u0107\u0003\u0002\u0001\u0000\u0107/\u0001\u0000\u0000"+
		"\u0000\u0108\u010d\u0003\u0002\u0001\u0000\u0109\u010a\u0005\u0002\u0000"+
		"\u0000\u010a\u010c\u0003\u0002\u0001\u0000\u010b\u0109\u0001\u0000\u0000"+
		"\u0000\u010c\u010f\u0001\u0000\u0000\u0000\u010d\u010b\u0001\u0000\u0000"+
		"\u0000\u010d\u010e\u0001\u0000\u0000\u0000\u010e1\u0001\u0000\u0000\u0000"+
		"\u010f\u010d\u0001\u0000\u0000\u0000\u0110\u0111\u0005)\u0000\u0000\u0111"+
		"3\u0001\u0000\u0000\u0000\u001f?BEGJMQW_r{\u0084\u008c\u0096\u00a3\u00aa"+
		"\u00b1\u00b5\u00b9\u00c1\u00d1\u00d6\u00de\u00e1\u00e5\u00e8\u00ea\u00f9"+
		"\u00fb\u0104\u010d";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}