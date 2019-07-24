package be.bendem.rockerintellij;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class RockerFileType extends LanguageFileType {
	public static final RockerFileType INSTANCE = new RockerFileType();

	protected RockerFileType() {
		super(RockerLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public String getName() {
		return "Rocker template";
	}

	@NotNull
	@Override
	public String getDescription() {
		return "Rocker template";
	}

	@NotNull
	@Override
	public String getDefaultExtension() {
		return "rocker";
	}

	@Nullable
	@Override
	public Icon getIcon() {
		return Icons.Rocker_ICON;
	}
}
