package be.bendem.rockerintellij.psi;

import be.bendem.rockerintellij.Icons;
import be.bendem.rockerintellij.RockerFileType;
import be.bendem.rockerintellij.RockerLanguage;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import org.antlr.intellij.adaptor.SymtabUtils;
import org.antlr.intellij.adaptor.psi.ScopeNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class RockerPSIFileRoot extends PsiFileBase implements ScopeNode {
    public RockerPSIFileRoot(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, RockerLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return RockerFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Rocker Language file";
    }

    @Override
    public Icon getIcon(int flags) {
        return Icons.Rocker_ICON;
    }

	/** Return null since a file scope has no enclosing scope. It is
	 *  not itself in a scope.
	 */
	@Override
	public ScopeNode getContext() {
		return null;
	}

	@Nullable
	@Override
	public PsiElement resolve(PsiNamedElement element) {
//		System.out.println(getClass().getSimpleName()+
//		                   ".resolve("+element.getName()+
//		                   " at "+Integer.toHexString(element.hashCode())+")");
		if ( element.getParent() instanceof CallSubtree ) {
			return SymtabUtils.resolve(this, RockerLanguage.INSTANCE,
			                           element, "/script/function/ID");
		}
		return SymtabUtils.resolve(this, RockerLanguage.INSTANCE,
		                           element, "/script/vardef/ID");
	}
}
