package it.laskaridis.blockyard;

import org.junit.Test;

import static it.laskaridis.blockyard.digests.Digests.HASH256;
import static org.junit.Assert.*;

public class MerkleNodeTests {

    @Test
    public void shouldCreateLeafNode() {
        HashableString str = HashableString.of("test");
        MerkleNode<HashableString> node = new MerkleNode<>(str);
        assertTrue(node.isLeaf());
        assertFalse(node.getLeft().isPresent());
        assertFalse(node.getRight().isPresent());
        assertEquals(str.getDigest(), node.getDigest());
    }

    @Test
    public void shouldCreateOddParentNode() {
        HashableString str = HashableString.of("leaf");
        MerkleNode<HashableString> leaf = new MerkleNode<>(str);
        MerkleNode<HashableString> parent = new MerkleNode<>(leaf);
        assertFalse(parent.isLeaf());
        assertEquals(parent.getLeft().get(), leaf);
        assertFalse(parent.getRight().isPresent());
        assertFalse(parent.getValue().isPresent());
        assertEquals(HASH256.digest(leaf.getDigest(), leaf.getDigest()), parent.getDigest());
    }

    @Test
    public void shouldCreateEvenParentNode() {
        MerkleNode<HashableString> left = new MerkleNode<>(HashableString.of("left"));
        MerkleNode<HashableString> right = new MerkleNode<>(HashableString.of("right"));
        MerkleNode<HashableString> parent = new MerkleNode<>(left, right);
        assertFalse(parent.isLeaf());
        assertEquals(parent.getLeft().get(), left);
        assertEquals(parent.getRight().get(), right);
        assertFalse(parent.getValue().isPresent());
        assertEquals(HASH256.digest(left.getDigest(), right.getDigest()), parent.getDigest());
    }


}
