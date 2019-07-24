package be.bendem.rockerintellij;

import com.intellij.core.CoreASTFactory;
import com.intellij.lang.ParserDefinition;

/** How to create parse tree nodes (Jetbrains calls them AST nodes). Later
 *  non-leaf nodes are converted to PSI nodes by the {@link ParserDefinition}.
 *  Leaf nodes are already considered PSI nodes.  This is mostly just
 *  {@link CoreASTFactory} but with comments on the methods that you might want
 *  to override.
 */
public class RockerASTFactory extends CoreASTFactory {
}
