package be.bendem.rockerintellij;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class RockerColorSettingsPage implements ColorSettingsPage {
	private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
		new AttributesDescriptor("Identifier", RockerSyntaxHighlighter.IDENTIFIER),
		new AttributesDescriptor("Prefix", RockerSyntaxHighlighter.PREFIX),
		new AttributesDescriptor("Keyword", RockerSyntaxHighlighter.KEYWORD),
		new AttributesDescriptor("Block comment", RockerSyntaxHighlighter.BLOCK_COMMENT),
	};

	@Nullable
	@Override
	public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
		return null;
	}

	@Nullable
	@Override
	public Icon getIcon() {
		return Icons.Rocker_ICON;
	}

	@NotNull
	@Override
	public SyntaxHighlighter getHighlighter() {
		return new RockerSyntaxHighlighter();
	}

	@NotNull
	@Override
	public String getDemoText() {
		return
			"@import java.util.Map;\n" +
			"\n" +
			"@args(String name, Map<String, String> values)\n" +
			"\n" +
			"Hello, @name.\n" +
			"\n" +
			"@for((k, v) : values) {\n" +
			"    We welcome @k at @v \n" +
			"}";
	}

	@NotNull
	@Override
	public AttributesDescriptor[] getAttributeDescriptors() {
		return DESCRIPTORS;
	}

	@NotNull
	@Override
	public ColorDescriptor[] getColorDescriptors() {
		return ColorDescriptor.EMPTY_ARRAY;
	}

	@NotNull
	@Override
	public String getDisplayName() {
		return "Rocker";
	}
}
