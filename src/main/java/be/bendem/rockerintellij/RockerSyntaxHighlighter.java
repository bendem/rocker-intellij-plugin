package be.bendem.rockerintellij;

import com.fizzed.rocker.antlr4.RockerLexer;
import com.fizzed.rocker.antlr4.RockerParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

/** A highlighter is really just a mapping from token type to
 *  some text attributes using {@link #getTokenHighlights(IElementType)}.
 *  The reason that it returns an array, TextAttributesKey[], is
 *  that you might want to mix the attributes of a few known highlighters.
 *  A {@link TextAttributesKey} is just a name for that a theme
 *  or IDE skin can set. For example, {@link com.intellij.openapi.editor.DefaultLanguageHighlighterColors#KEYWORD}
 *  is the key that maps to what identifiers look like in the editor.
 *  To change it, see dialog: Editor > Colors & Fonts > Language Defaults.
 *
 *  From <a href="http://www.jetbrains.org/intellij/sdk/docs/reference_guide/custom_language_support/syntax_highlighting_and_error_highlighting.html">doc</a>:
 *  "The mapping of the TextAttributesKey to specific attributes used
 *  in an editor is defined by the EditorColorsScheme class, and can
 *  be configured by the user if the plugin provides an appropriate
 *  configuration interface.
 *  ...
 *  The syntax highlighter returns the {@link TextAttributesKey}
 * instances for each token type which needs special highlighting.
 * For highlighting lexer errors, the standard TextAttributesKey
 * for bad characters HighlighterColors.BAD_CHARACTER can be used."
 */
public class RockerSyntaxHighlighter extends SyntaxHighlighterBase {
	private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];
	public static final TextAttributesKey PREFIX =
		createTextAttributesKey("Rocker_PREFIX", DefaultLanguageHighlighterColors.INSTANCE_FIELD);
	public static final TextAttributesKey IDENTIFIER =
		createTextAttributesKey("Rocker_IDENTIFIER", DefaultLanguageHighlighterColors.INSTANCE_FIELD);
	public static final TextAttributesKey KEYWORD =
		createTextAttributesKey("Rocker_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
	public static final TextAttributesKey EXPRESSION =
		createTextAttributesKey("Rocker_EXPRESSION", DefaultLanguageHighlighterColors.NUMBER);
	public static final TextAttributesKey BLOCK_COMMENT =
		createTextAttributesKey("Rocker_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);

	static {
		PSIElementTypeFactory.defineLanguageIElementTypes(RockerLanguage.INSTANCE,
		                                                  RockerParser.tokenNames,
		                                                  RockerParser.ruleNames);
	}

	@NotNull
	@Override
	public Lexer getHighlightingLexer() {
		RockerLexer lexer = new RockerLexer(null);
		return new ANTLRLexerAdaptor(RockerLanguage.INSTANCE, lexer);
	}

	@NotNull
	@Override
	public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
		if ( !(tokenType instanceof TokenIElementType) ) return EMPTY_KEYS;
		TokenIElementType myType = (TokenIElementType)tokenType;
		int ttype = myType.getANTLRTokenType();
		TextAttributesKey attrKey;
		switch ( ttype ) {
			// case RockerLexer.PLAIN:
			// case RockerLexer.MV_CONTENT_CLOSURE:
			// case RockerLexer.MV_VALUE_CLOSURE:
			// 	// TODO
			// 	 break;
			case RockerLexer.AT:
				attrKey = PREFIX;
				break;
			case RockerLexer.MV_EVAL:
			case RockerParser.RULE_valueExpression:
				attrKey = EXPRESSION;
				break;
			case RockerLexer.MV_VALUE:
			case RockerLexer.MV_NULL_TERNARY_LH:
			case RockerLexer.MV_NULL_TERNARY_RH:
				attrKey = IDENTIFIER;
				break;
			case RockerLexer.MV_ARGS:
			case RockerLexer.MV_IMPORT:
			case RockerLexer.MV_OPTION:
			case RockerLexer.MV_IF:
			case RockerLexer.ELSE_IF:
			case RockerLexer.MV_FOR:
			case RockerLexer.MV_WITH:
			case RockerLexer.ELSE:
			case RockerLexer.LCURLY:
			case RockerLexer.RCURLY:
				attrKey = KEYWORD;
				break;
			case RockerLexer.COMMENT:
				attrKey = BLOCK_COMMENT;
				break;
			default:
				return EMPTY_KEYS;
		}
		return new TextAttributesKey[] {attrKey};
	}
}
