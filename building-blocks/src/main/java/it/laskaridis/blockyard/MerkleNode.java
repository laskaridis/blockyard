package it.laskaridis.blockyard;

import it.laskaridis.blockyard.annotations.Immutable;
import it.laskaridis.blockyard.annotations.ThreadSafe;
import it.laskaridis.blockyard.digests.Digest;
import it.laskaridis.blockyard.digests.Digestable;
import it.laskaridis.blockyard.utils.Assert;

import java.util.Objects;
import java.util.Optional;

import static it.laskaridis.blockyard.digests.Digests.HASH256;

@Immutable @ThreadSafe
public final class MerkleNode<T extends Digestable> {

    private final MerkleNode<T> left;
    private final MerkleNode<T> right;
    private final T value;
    private final Digest digest;

    public static <T extends Digestable> MerkleNode<T> newLeafNode(T value) {
        return new MerkleNode<T>(value);
    }

    public static <T extends Digestable> MerkleNode<T> newOddParentNode(MerkleNode<T> left) {
        return new MerkleNode<T>(left);
    }

    public static <T extends Digestable> MerkleNode<T> newEvenParentNode(MerkleNode<T> left, MerkleNode<T> right) {
        return new MerkleNode<T>(left, right);
    }

    public MerkleNode(T value) {
        Assert.notNull(value, "value can't be null for leaf nodes");
        this.value = value;
t s
        this.left = null;
        this.right = null;
        this.digest = value.getDigest();
    }

    public MerkleNode(MerkleNode<T> left) {
        Assert.notNull(left, "left child node can't be null for odd parent nodes");
        this.left = left;
        this.right = null;
        this.value = null;
        this.digest = HASH256.digest(left.getDigest(), left.getDigest());
    }

    public MerkleNode(MerkleNode<T> left, MerkleNode<T> right) {
        Assert.notNull(left, "left child node can't be null for even parent nodes");
        Assert.notNull(right, "right child node can't be null for even parent nodes");
        this.left = left;
        this.right = right;
        this.value = null;
        this.digest = HASH256.digest(left.getDigest(), right.getDigest());
    }

    public void accept(MerkleNodeVisitor<T> visitor) {
        visitor.visit(this);
    }

    public boolean isLeaf() {
        return !(getLeft().isPresent() || getRight().isPresent());
    }

    public Digest getDigest() {
        return this.digest;
    }

    public Optional<T> getValue() {
        return Optional.ofNullable(value);
    }

    public Optional<MerkleNode<T>> getLeft() {
        return Optional.ofNullable(left);
    }

    public Optional<MerkleNode<T>> getRight() {
        return Optional.ofNullable(right);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MerkleNode<?> that = (MerkleNode<?>) o;
        return digest.equals(that.digest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(digest);
    }

    public boolean isOddParent() {
        return !getRight().isPresent();
    }
}
