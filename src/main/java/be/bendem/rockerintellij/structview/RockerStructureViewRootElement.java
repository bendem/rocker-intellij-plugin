package be.bendem.rockerintellij.structview;

import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class RockerStructureViewRootElement extends RockerStructureViewElement {
	public RockerStructureViewRootElement(PsiFile element) {
		super(element);
	}

	@NotNull
	@Override
	public ItemPresentation getPresentation() {
		return new RockerRootPresentation((PsiFile)element);
	}
}
