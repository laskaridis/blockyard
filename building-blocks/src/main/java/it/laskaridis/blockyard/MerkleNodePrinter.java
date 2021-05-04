package it.laskaridis.blockyard;

import it.laskaridis.blockyard.digests.Digestable;

/**
 * Performs a breadth-first traversal of a merkle tree and prints a human readable
 * representation.
 */
public class MerkleNodePrinter<T extends Digestable> implements MerkleNodeVisitor<T> {

    private final StringBuffer buffer = new StringBuffer();

    private int depth = 0;

    @Override
    public void visit(MerkleNode<T> node) {
        int padding = depth * 2;
        String prefix = new String(new char[padding]).replace('\0', ' ');
        buffer.append(prefix).append("* ").append(node.getDigest().getValueAsString()).append("\n");
        if (node.isLeaf()) {
            buffer.append(prefix).append("  <").append(node.getValue().get().toString()).append(">\n");
        } else {
            if (node.getLeft().isPresent()) {
                depth++;
                node.getLeft().get().accept(this);
                depth--;
            }
            if (node.getRight().isPresent()) {
                depth++;
                node.getRight().get().accept(this);
                depth--;
            }
        }
    }

    public String toString() {
        return buffer.toString();
    }

    public void print() {
        System.out.print(buffer);
    }
}
