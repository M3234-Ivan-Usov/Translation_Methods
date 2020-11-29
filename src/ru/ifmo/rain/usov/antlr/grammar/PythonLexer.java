// Generated from src/ru/ifmo/rain/usov/antlr/grammar/Python.g4 by ANTLR 4.8
package ru.ifmo.rain.usov.antlr.grammar;

    import ru.ifmo.rain.usov.antlr.DenterHelper;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PythonLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, T__37=38, 
		T__38=39, T__39=40, UPDATE=41, COMPARE=42, SELF=43, NAME=44, LETTER=45, 
		INT=46, NEWLINE=47, WS=48;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
			"T__17", "T__18", "T__19", "T__20", "T__21", "T__22", "T__23", "T__24", 
			"T__25", "T__26", "T__27", "T__28", "T__29", "T__30", "T__31", "T__32", 
			"T__33", "T__34", "T__35", "T__36", "T__37", "T__38", "T__39", "UPDATE", 
			"COMPARE", "SELF", "NAME", "LETTER", "INT", "NEWLINE", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'break'", "'continue'", "'pass'", "':'", "'def'", "'('", "','", 
			"')'", "'='", "'class'", "'return'", "'from'", "'import'", "'as'", "'print'", 
			"'len'", "'for'", "'in'", "'range'", "'while'", "'if'", "'elif'", "'else'", 
			"'or'", "'xor'", "'and'", "'not'", "'+'", "'-'", "'//'", "'*'", "'/'", 
			"'%'", "'**'", "'.'", "'True'", "'False'", "'None'", "'\"'", "'\\'", 
			null, null, "'self.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, "UPDATE", "COMPARE", "SELF", "NAME", "LETTER", 
			"INT", "NEWLINE", "WS"
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


	    private final DenterHelper denter = DenterHelper.builder()
	        .nl(NEWLINE).indent(PythonParser.INDENT)
	        .dedent(PythonParser.DEDENT)
	        .pullToken(PythonLexer.super::nextToken);

	    @Override
	    public Token nextToken() {
	        return denter.nextToken();
	    }


	public PythonLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Python.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\62\u0140\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\6\3"+
		"\6\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3"+
		"\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21"+
		"\3\21\3\21\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24"+
		"\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\27\3\27\3\27\3\27"+
		"\3\27\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\33"+
		"\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\35\3\35\3\36\3\36\3\37\3\37\3\37"+
		"\3 \3 \3!\3!\3\"\3\"\3#\3#\3#\3$\3$\3%\3%\3%\3%\3%\3&\3&\3&\3&\3&\3&\3"+
		"\'\3\'\3\'\3\'\3\'\3(\3(\3)\3)\3*\3*\3*\3*\3*\3*\3*\3*\5*\u0109\n*\3+"+
		"\3+\3+\3+\3+\3+\3+\3+\3+\3+\5+\u0115\n+\3,\3,\3,\3,\3,\3,\3-\3-\5-\u011f"+
		"\n-\3-\3-\3-\7-\u0124\n-\f-\16-\u0127\13-\3.\3.\3/\6/\u012c\n/\r/\16/"+
		"\u012d\3\60\5\60\u0131\n\60\3\60\3\60\7\60\u0135\n\60\f\60\16\60\u0138"+
		"\13\60\3\61\6\61\u013b\n\61\r\61\16\61\u013c\3\61\3\61\2\2\62\3\3\5\4"+
		"\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22"+
		"#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C"+
		"#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62\3\2\3\4\2C\\c|\2\u014f\2\3\3\2"+
		"\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17"+
		"\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2"+
		"\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3"+
		"\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3"+
		"\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2"+
		"=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3"+
		"\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2"+
		"\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\3"+
		"c\3\2\2\2\5i\3\2\2\2\7r\3\2\2\2\tw\3\2\2\2\13y\3\2\2\2\r}\3\2\2\2\17\177"+
		"\3\2\2\2\21\u0081\3\2\2\2\23\u0083\3\2\2\2\25\u0085\3\2\2\2\27\u008b\3"+
		"\2\2\2\31\u0092\3\2\2\2\33\u0097\3\2\2\2\35\u009e\3\2\2\2\37\u00a1\3\2"+
		"\2\2!\u00a7\3\2\2\2#\u00ab\3\2\2\2%\u00af\3\2\2\2\'\u00b2\3\2\2\2)\u00b8"+
		"\3\2\2\2+\u00be\3\2\2\2-\u00c1\3\2\2\2/\u00c6\3\2\2\2\61\u00cb\3\2\2\2"+
		"\63\u00ce\3\2\2\2\65\u00d2\3\2\2\2\67\u00d6\3\2\2\29\u00da\3\2\2\2;\u00dc"+
		"\3\2\2\2=\u00de\3\2\2\2?\u00e1\3\2\2\2A\u00e3\3\2\2\2C\u00e5\3\2\2\2E"+
		"\u00e7\3\2\2\2G\u00ea\3\2\2\2I\u00ec\3\2\2\2K\u00f1\3\2\2\2M\u00f7\3\2"+
		"\2\2O\u00fc\3\2\2\2Q\u00fe\3\2\2\2S\u0108\3\2\2\2U\u0114\3\2\2\2W\u0116"+
		"\3\2\2\2Y\u011e\3\2\2\2[\u0128\3\2\2\2]\u012b\3\2\2\2_\u0130\3\2\2\2a"+
		"\u013a\3\2\2\2cd\7d\2\2de\7t\2\2ef\7g\2\2fg\7c\2\2gh\7m\2\2h\4\3\2\2\2"+
		"ij\7e\2\2jk\7q\2\2kl\7p\2\2lm\7v\2\2mn\7k\2\2no\7p\2\2op\7w\2\2pq\7g\2"+
		"\2q\6\3\2\2\2rs\7r\2\2st\7c\2\2tu\7u\2\2uv\7u\2\2v\b\3\2\2\2wx\7<\2\2"+
		"x\n\3\2\2\2yz\7f\2\2z{\7g\2\2{|\7h\2\2|\f\3\2\2\2}~\7*\2\2~\16\3\2\2\2"+
		"\177\u0080\7.\2\2\u0080\20\3\2\2\2\u0081\u0082\7+\2\2\u0082\22\3\2\2\2"+
		"\u0083\u0084\7?\2\2\u0084\24\3\2\2\2\u0085\u0086\7e\2\2\u0086\u0087\7"+
		"n\2\2\u0087\u0088\7c\2\2\u0088\u0089\7u\2\2\u0089\u008a\7u\2\2\u008a\26"+
		"\3\2\2\2\u008b\u008c\7t\2\2\u008c\u008d\7g\2\2\u008d\u008e\7v\2\2\u008e"+
		"\u008f\7w\2\2\u008f\u0090\7t\2\2\u0090\u0091\7p\2\2\u0091\30\3\2\2\2\u0092"+
		"\u0093\7h\2\2\u0093\u0094\7t\2\2\u0094\u0095\7q\2\2\u0095\u0096\7o\2\2"+
		"\u0096\32\3\2\2\2\u0097\u0098\7k\2\2\u0098\u0099\7o\2\2\u0099\u009a\7"+
		"r\2\2\u009a\u009b\7q\2\2\u009b\u009c\7t\2\2\u009c\u009d\7v\2\2\u009d\34"+
		"\3\2\2\2\u009e\u009f\7c\2\2\u009f\u00a0\7u\2\2\u00a0\36\3\2\2\2\u00a1"+
		"\u00a2\7r\2\2\u00a2\u00a3\7t\2\2\u00a3\u00a4\7k\2\2\u00a4\u00a5\7p\2\2"+
		"\u00a5\u00a6\7v\2\2\u00a6 \3\2\2\2\u00a7\u00a8\7n\2\2\u00a8\u00a9\7g\2"+
		"\2\u00a9\u00aa\7p\2\2\u00aa\"\3\2\2\2\u00ab\u00ac\7h\2\2\u00ac\u00ad\7"+
		"q\2\2\u00ad\u00ae\7t\2\2\u00ae$\3\2\2\2\u00af\u00b0\7k\2\2\u00b0\u00b1"+
		"\7p\2\2\u00b1&\3\2\2\2\u00b2\u00b3\7t\2\2\u00b3\u00b4\7c\2\2\u00b4\u00b5"+
		"\7p\2\2\u00b5\u00b6\7i\2\2\u00b6\u00b7\7g\2\2\u00b7(\3\2\2\2\u00b8\u00b9"+
		"\7y\2\2\u00b9\u00ba\7j\2\2\u00ba\u00bb\7k\2\2\u00bb\u00bc\7n\2\2\u00bc"+
		"\u00bd\7g\2\2\u00bd*\3\2\2\2\u00be\u00bf\7k\2\2\u00bf\u00c0\7h\2\2\u00c0"+
		",\3\2\2\2\u00c1\u00c2\7g\2\2\u00c2\u00c3\7n\2\2\u00c3\u00c4\7k\2\2\u00c4"+
		"\u00c5\7h\2\2\u00c5.\3\2\2\2\u00c6\u00c7\7g\2\2\u00c7\u00c8\7n\2\2\u00c8"+
		"\u00c9\7u\2\2\u00c9\u00ca\7g\2\2\u00ca\60\3\2\2\2\u00cb\u00cc\7q\2\2\u00cc"+
		"\u00cd\7t\2\2\u00cd\62\3\2\2\2\u00ce\u00cf\7z\2\2\u00cf\u00d0\7q\2\2\u00d0"+
		"\u00d1\7t\2\2\u00d1\64\3\2\2\2\u00d2\u00d3\7c\2\2\u00d3\u00d4\7p\2\2\u00d4"+
		"\u00d5\7f\2\2\u00d5\66\3\2\2\2\u00d6\u00d7\7p\2\2\u00d7\u00d8\7q\2\2\u00d8"+
		"\u00d9\7v\2\2\u00d98\3\2\2\2\u00da\u00db\7-\2\2\u00db:\3\2\2\2\u00dc\u00dd"+
		"\7/\2\2\u00dd<\3\2\2\2\u00de\u00df\7\61\2\2\u00df\u00e0\7\61\2\2\u00e0"+
		">\3\2\2\2\u00e1\u00e2\7,\2\2\u00e2@\3\2\2\2\u00e3\u00e4\7\61\2\2\u00e4"+
		"B\3\2\2\2\u00e5\u00e6\7\'\2\2\u00e6D\3\2\2\2\u00e7\u00e8\7,\2\2\u00e8"+
		"\u00e9\7,\2\2\u00e9F\3\2\2\2\u00ea\u00eb\7\60\2\2\u00ebH\3\2\2\2\u00ec"+
		"\u00ed\7V\2\2\u00ed\u00ee\7t\2\2\u00ee\u00ef\7w\2\2\u00ef\u00f0\7g\2\2"+
		"\u00f0J\3\2\2\2\u00f1\u00f2\7H\2\2\u00f2\u00f3\7c\2\2\u00f3\u00f4\7n\2"+
		"\2\u00f4\u00f5\7u\2\2\u00f5\u00f6\7g\2\2\u00f6L\3\2\2\2\u00f7\u00f8\7"+
		"P\2\2\u00f8\u00f9\7q\2\2\u00f9\u00fa\7p\2\2\u00fa\u00fb\7g\2\2\u00fbN"+
		"\3\2\2\2\u00fc\u00fd\7$\2\2\u00fdP\3\2\2\2\u00fe\u00ff\7^\2\2\u00ffR\3"+
		"\2\2\2\u0100\u0101\7-\2\2\u0101\u0109\7?\2\2\u0102\u0103\7/\2\2\u0103"+
		"\u0109\7?\2\2\u0104\u0105\7,\2\2\u0105\u0109\7?\2\2\u0106\u0107\7\61\2"+
		"\2\u0107\u0109\7?\2\2\u0108\u0100\3\2\2\2\u0108\u0102\3\2\2\2\u0108\u0104"+
		"\3\2\2\2\u0108\u0106\3\2\2\2\u0109T\3\2\2\2\u010a\u010b\7?\2\2\u010b\u0115"+
		"\7?\2\2\u010c\u010d\7#\2\2\u010d\u0115\7?\2\2\u010e\u0115\7>\2\2\u010f"+
		"\u0110\7>\2\2\u0110\u0115\7?\2\2\u0111\u0115\7@\2\2\u0112\u0113\7@\2\2"+
		"\u0113\u0115\7?\2\2\u0114\u010a\3\2\2\2\u0114\u010c\3\2\2\2\u0114\u010e"+
		"\3\2\2\2\u0114\u010f\3\2\2\2\u0114\u0111\3\2\2\2\u0114\u0112\3\2\2\2\u0115"+
		"V\3\2\2\2\u0116\u0117\7u\2\2\u0117\u0118\7g\2\2\u0118\u0119\7n\2\2\u0119"+
		"\u011a\7h\2\2\u011a\u011b\7\60\2\2\u011bX\3\2\2\2\u011c\u011f\5[.\2\u011d"+
		"\u011f\7a\2\2\u011e\u011c\3\2\2\2\u011e\u011d\3\2\2\2\u011f\u0125\3\2"+
		"\2\2\u0120\u0124\5[.\2\u0121\u0124\7a\2\2\u0122\u0124\5]/\2\u0123\u0120"+
		"\3\2\2\2\u0123\u0121\3\2\2\2\u0123\u0122\3\2\2\2\u0124\u0127\3\2\2\2\u0125"+
		"\u0123\3\2\2\2\u0125\u0126\3\2\2\2\u0126Z\3\2\2\2\u0127\u0125\3\2\2\2"+
		"\u0128\u0129\t\2\2\2\u0129\\\3\2\2\2\u012a\u012c\4\62;\2\u012b\u012a\3"+
		"\2\2\2\u012c\u012d\3\2\2\2\u012d\u012b\3\2\2\2\u012d\u012e\3\2\2\2\u012e"+
		"^\3\2\2\2\u012f\u0131\7\17\2\2\u0130\u012f\3\2\2\2\u0130\u0131\3\2\2\2"+
		"\u0131\u0132\3\2\2\2\u0132\u0136\7\f\2\2\u0133\u0135\7\"\2\2\u0134\u0133"+
		"\3\2\2\2\u0135\u0138\3\2\2\2\u0136\u0134\3\2\2\2\u0136\u0137\3\2\2\2\u0137"+
		"`\3\2\2\2\u0138\u0136\3\2\2\2\u0139\u013b\7\"\2\2\u013a\u0139\3\2\2\2"+
		"\u013b\u013c\3\2\2\2\u013c\u013a\3\2\2\2\u013c\u013d\3\2\2\2\u013d\u013e"+
		"\3\2\2\2\u013e\u013f\b\61\2\2\u013fb\3\2\2\2\f\2\u0108\u0114\u011e\u0123"+
		"\u0125\u012d\u0130\u0136\u013c\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}