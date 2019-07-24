package be.bendem.rockerintellij.structview;

import be.bendem.rockerintellij.psi.RockerPSIFileRoot;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import org.jetbrains.annotations.NotNull;

public class RockerStructureViewModel
	extends StructureViewModelBase
	implements StructureViewModel.ElementInfoProvider
{
	public RockerStructureViewModel(RockerPSIFileRoot root) {
		super(root, new RockerStructureViewRootElement(root));
	}

	@NotNull
	public Sorter[] getSorters() {
		return new Sorter[] {Sorter.ALPHA_SORTER};
	}

	@Override
	public boolean isAlwaysLeaf(StructureViewTreeElement element) {
  		return !isAlwaysShowsPlus(element);
	}

	@Override
	public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
		Object value = element.getValue();
  		return value instanceof RockerPSIFileRoot;
	}
}
