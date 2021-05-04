package it.laskaridis.blockyard;

import it.laskaridis.blockyard.annotations.Immutable;
import it.laskaridis.blockyard.annotations.ThreadSafe;
import it.laskaridis.blockyard.digests.Digestable;
import it.laskaridis.blockyard.utils.Assert;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Immutable @ThreadSafe
public final class MerkleTree <T extends Digestable> {

    private final MerkleNode<T> root;
    private final List<T> elements;

    public MerkleTree() {
        this.root = null;
        this.elements = new LinkedList<>();
    }

    public MerkleTree(List<T> elements) {
        Assert.notNull(elements, "elements can't be null for a non-empty merkle tree");
        Assert.notEmpty(elements, "elements can't be empty for a non-empty merkle tree");
        this.elements = new LinkedList<>(elements);
        this.root = calculateMerkleRoot(getLeafs());
    }

    public boolean isEmpty() {
        return this.elements.isEmpty();
    }

    public List<MerkleNode<T>> getLeafs() {
        return this.elements
                .stream()
                .map(MerkleNode::newLeafNode)
                .collect(toList());
    }

    public int size() {
        return this.elements.size();
    }

    public Optional<MerkleNode<T>> getRoot() {
        return Optional.ofNullable(this.root);
    }

    public MerkleTree<T> append(T element) {
        List<T> copy = new LinkedList<>(this.elements);
        copy.add(element);
        return new MerkleTree<>(copy);
    }

    public boolean contains(T element) {
        return this.elements.contains(element);
    }

    public void print() {
        MerkleNodePrinter pritner = new MerkleNodePrinter();
        this.root.accept(pritner);
        pritner.print();
    }

    public void validate() {
        if (getRoot().isPresent())
            getRoot().get().accept(new MerkleNodeValidator<>());
    }

    public boolean isValid() {
        try {
            validate();
        } catch (InvalidMerkleTreeException e) {
            return false;
        }
        return true;
    }

    private MerkleNode<T> calculateMerkleRoot(List<MerkleNode<T>> children) {
        // holds the parents of the specified nodes:
        List<MerkleNode<T>> parents = new LinkedList<>();

        // calculate each node's parent
        Iterator<MerkleNode<T>> i = children.iterator();
        while (i.hasNext()) {
            MerkleNode<T> left = i.next();
            MerkleNode<T> parent;
            if (i.hasNext()) {
                MerkleNode<T> right = i.next();
                parent = MerkleNode.newEvenParentNode(left, right);
            } else {
                parent = MerkleNode.newOddParentNode(left);
            }
            parents.add(parent);
        }

        // calculate the node's parent's until a single parent (root) remains
        if (parents.size() > 1)
            return calculateMerkleRoot(parents);
        else
            return parents.get(0);
    }

    public MerkleTree<T> remove(T element) {
        if (this.elements.contains(element)) {
            List<T> copy = new LinkedList<>(this.elements);
            copy.remove(element);
            if (copy.isEmpty())
                return new MerkleTree<>();
            else
                return new MerkleTree<>(copy);
        } else
            return this;
    }
}
