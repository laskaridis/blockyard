package it.laskaridis.blockyard;

import it.laskaridis.blockyard.digests.Digest;
import it.laskaridis.blockyard.digests.Digestable;
import it.laskaridis.blockyard.digests.Digests;

public class MerkleNodeValidator<T extends Digestable> implements MerkleNodeVisitor<T> {

    @Override
    public void visit(MerkleNode<T> node) {
        if (node.isLeaf()) {
            validateLeafNode(node);
        } else {
            if (node.isOddParent())
                validateOddParentNode(node);
            else
                validateEvenParentNode(node);
        }
    }

    private void validateLeafNode(MerkleNode<T> node) {
        Digest expected = node.getValue().get().getDigest();
        if (!expected.equals(node.getDigest()))
            throwErrorForNode(node);
    }

    private void validateEvenParentNode(MerkleNode<T> node) {
        Digest expected = Digests.HASH256.digest(
                node.getLeft().get().getDigest(),
                node.getRight().get().getDigest()
        );
        if (!expected.equals(node.getDigest()))
            throwErrorForNode(node);
        else {
            node.getLeft().get().accept(this);
            node.getRight().get().accept(this);
        }
    }

    private void validateOddParentNode(MerkleNode<T> node) {
        Digest expected = Digests.HASH256.digest(
                node.getLeft().get().getDigest(),
                node.getLeft().get().getDigest()
        );
        if (!expected.equals(node.getDigest()))
            throwErrorForNode(node);
        else {
            node.getLeft().get().accept(this);
        }
    }

    private void throwErrorForNode(MerkleNode<T> node) {
        throw new InvalidMerkleTreeException("node does not validate: <" + node.getDigest().getValueAsString() + ">");
    }
}
