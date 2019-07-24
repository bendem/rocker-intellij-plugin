package be.bendem.rockerintellij;

import be.bendem.rockerintellij.psi.IdentifierPSINode;
import be.bendem.rockerintellij.psi.VardefSubtree;
import com.fizzed.rocker.antlr4.RockerParser;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import org.antlr.intellij.adaptor.lexer.RuleIElementType;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import be.bendem.rockerintellij.psi.FunctionSubtree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RockerFindUsagesProvider implements FindUsagesProvider {
	/** Is "find usages" meaningful for a kind of definition subtree? */
	@Override
	public boolean canFindUsagesFor(PsiElement psiElement) {
		return psiElement instanceof IdentifierPSINode || // the case where we highlight the ID in def subtree itself
			   psiElement instanceof FunctionSubtree ||   // remaining cases are for resolve() results
			   psiElement instanceof VardefSubtree;
	}

	@Nullable
	@Override
	public WordsScanner getWordsScanner() {
		return null; // null implies use SimpleWordScanner default
	}

	@Nullable
	@Override
	public String getHelpId(PsiElement psiElement) {
		return null;
	}

	/** What kind of thing is the ID node? Can group by in "Find Usages" dialog */
	@NotNull
	@Override
	public String getType(PsiElement element) {
		// The parent of an ID node will be a RuleIElementType:
		// function, vardef, formal_arg, statement, expr, call_expr, primary
		ANTLRPsiNode parent = (ANTLRPsiNode)element.getParent();
		RuleIElementType elType = (RuleIElementType)parent.getNode().getElementType();
		switch ( elType.getRuleIndex() ) {
			case RockerParser.RULE_template :
				return "function";
			case RockerParser.RULE_value:
				return "variable";
			case RockerParser.RULE_argumentsDeclaration:
				return "parameter";
		}
		return "";
	}

	@NotNull
	@Override
	public String getDescriptiveName(PsiElement element) {
		return element.getText();
	}

	@NotNull
	@Override
	public String getNodeText(PsiElement element, boolean useFullName) {
		String text = element.getText();
		return text;
	}
}
