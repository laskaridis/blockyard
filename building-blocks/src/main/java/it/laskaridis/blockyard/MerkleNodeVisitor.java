package it.laskaridis.blockyard;

import it.laskaridis.blockyard.digests.Digestable;

/**
 * Interface that must be implemented by all visitor classes used
 * to traverse a {@link MerkleTree}'s {@link MerkleNode}s/
 *
 * @see MerkleNodePrinter
 * @see MerkleNodeValidator
 */
public interface MerkleNodeVisitor<T extends Digestable> {

    /**
     * Invoked when the visitor visits a {@link MerkleNode} during a {@link MerkleTree}
     * traversal.
     *
     * @param node the visited node
     */
    void visit(MerkleNode<T> node);
}
