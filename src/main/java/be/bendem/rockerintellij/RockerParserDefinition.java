package be.bendem.rockerintellij;

import be.bendem.rockerintellij.psi.*;
import com.fizzed.rocker.antlr4.RockerLexer;
import com.fizzed.rocker.antlr4.RockerParser;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.RuleIElementType;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.antlr.intellij.adaptor.parser.ANTLRParserAdaptor;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RockerParserDefinition implements ParserDefinition {
	public static final IFileElementType FILE =
		new IFileElementType(RockerLanguage.INSTANCE);

	// public static final TokenIElementType ID;

	static {
		PSIElementTypeFactory.defineLanguageIElementTypes(RockerLanguage.INSTANCE,
		                                                  RockerParser.tokenNames,
		                                                  RockerParser.ruleNames);
		List<TokenIElementType> tokenIElementTypes =
			PSIElementTypeFactory.getTokenIElementTypes(RockerLanguage.INSTANCE);
		// ID = tokenIElementTypes.get(com.fizzed.rocker.antlr4.RockerLexer.ID);
	}

	public static final TokenSet COMMENTS =
		PSIElementTypeFactory.createTokenSet(
			RockerLanguage.INSTANCE,
			com.fizzed.rocker.antlr4.RockerLexer.COMMENT);

	public static final TokenSet STRING =
		PSIElementTypeFactory.createTokenSet(
			RockerLanguage.INSTANCE,
			RockerLexer.MV);

	@NotNull
	@Override
	public Lexer createLexer(Project project) {
		com.fizzed.rocker.antlr4.RockerLexer lexer = new com.fizzed.rocker.antlr4.RockerLexer(null);
		return new ANTLRLexerAdaptor(RockerLanguage.INSTANCE, lexer);
	}

	@NotNull
	public PsiParser createParser(final Project project) {
		final RockerParser parser = new RockerParser(null);
		return new ANTLRParserAdaptor(RockerLanguage.INSTANCE, parser) {
			@Override
			protected ParseTree parse(Parser parser, IElementType root) {
				if ( root instanceof IFileElementType ) {
					return ((RockerParser) parser).template();
				}

				throw new UnsupportedOperationException("Can't parse " + root.getClass().getName());
			}
		};
	}

	// /** "Tokens of those types are automatically skipped by PsiBuilder." */
	// @NotNull
	// public TokenSet getWhitespaceTokens() {
	// 	return WHITESPACE;
	// }

	@NotNull
	public TokenSet getCommentTokens() {
		return COMMENTS;
	}

	@NotNull
	public TokenSet getStringLiteralElements() {
		return STRING;
	}

	/** What is the IFileElementType of the root parse tree node? It
	 *  is called from {@link #createFile(FileViewProvider)} at least.
	 */
	@Override
	public IFileElementType getFileNodeType() {
		return FILE;
	}

	/** Create the root of your PSI tree (a PsiFile).
	 *
	 *  From IntelliJ IDEA Architectural Overview:
	 *  "A PSI (Program Structure Interface) file is the root of a structure
	 *  representing the contents of a file as a hierarchy of elements
	 *  in a particular programming language."
	 *
	 *  PsiFile is to be distinguished from a FileASTNode, which is a parse
	 *  tree node that eventually becomes a PsiFile. From PsiFile, we can get
	 *  it back via: {@link PsiFile#getNode}.
	 */
	@Override
	public PsiFile createFile(FileViewProvider viewProvider) {
		return new RockerPSIFileRoot(viewProvider);
	}

	/** Convert from *NON-LEAF* parse node (AST they call it)
	 *  to PSI node. Leaves are created in the AST factory.
	 *  Rename re-factoring can cause this to be
	 *  called on a TokenIElementType since we want to rename ID nodes.
	 *  In that case, this method is called to create the root node
	 *  but with ID type. Kind of strange, but we can simply create a
	 *  ASTWrapperPsiElement to make everything work correctly.
	 *
	 *  RuleIElementType.  Ah! It's that ID is the root
	 *  IElementType requested to parse, which means that the root
	 *  node returned from parsetree->PSI conversion.  But, it
	 *  must be a CompositeElement! The adaptor calls
	 *  rootMarker.done(root) to finish off the PSI conversion.
	 *  See {@link ANTLRParserAdaptor#parse(IElementType root,
	 *  PsiBuilder)}
	 *
	 *  If you don't care to distinguish PSI nodes by type, it is
	 *  sufficient to create a {@link ANTLRPsiNode} around
	 *  the parse tree node
	 */
	@NotNull
	public PsiElement createElement(ASTNode node) {
		IElementType elType = node.getElementType();
		if ( elType instanceof TokenIElementType ) {
			return new ANTLRPsiNode(node);
		}
		if ( !(elType instanceof RuleIElementType) ) {
			return new ANTLRPsiNode(node);
		}
		RuleIElementType ruleElType = (RuleIElementType) elType;
		switch ( ruleElType.getRuleIndex() ) {
			// FIXME
			case RockerParser.RULE_block:
			case RockerParser.RULE_templateContent:
			case RockerParser.RULE_ifBlock:
			case RockerParser.RULE_ifElseIfBlock:
			case RockerParser.RULE_ifElseBlock:
			case RockerParser.RULE_forBlock:
			case RockerParser.RULE_withBlock:
			case RockerParser.RULE_withElseBlock:
			case RockerParser.RULE_contentClosure:
			case RockerParser.RULE_contentClosureExpression:
			case RockerParser.RULE_valueClosure:
			case RockerParser.RULE_valueClosureExpression:
				return new BlockSubtree(node);
			case RockerParser.RULE_template:
			case RockerParser.RULE_plain:
			case RockerParser.RULE_plainBlock:
			case RockerParser.RULE_plainElseIfBlock:
			case RockerParser.RULE_plainElseBlock:
			case RockerParser.RULE_comment:
			case RockerParser.RULE_importDeclaration:
			case RockerParser.RULE_importStatement:
			case RockerParser.RULE_optionDeclaration:
			case RockerParser.RULE_optionStatement:
			case RockerParser.RULE_argumentsDeclaration:
			case RockerParser.RULE_argumentsStatement:
			case RockerParser.RULE_value:
			case RockerParser.RULE_valueExpression:
			case RockerParser.RULE_nullTernary:
			case RockerParser.RULE_nullTernaryExpression:
			case RockerParser.RULE_eval:
			case RockerParser.RULE_evalExpression:
			default :
				return new ANTLRPsiNode(node);
		}
	}
}
