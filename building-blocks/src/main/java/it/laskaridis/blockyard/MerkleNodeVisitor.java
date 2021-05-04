package it.laskaridis.blockyard;

import it.laskaridis.blockyard.digests.Digestable;

public interface MerkleNodeVisitor<T extends Digestable> {

    void visit(MerkleNode<T> node);
}
