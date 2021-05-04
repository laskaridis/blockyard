package it.laskaridis.blockyard;

import org.junit.Test;

import java.util.Arrays;

import static it.laskaridis.blockyard.digests.Digests.HASH256;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class MerkleTreeTests {

    @Test
    public void shouldCreateEmptyMerkleTree() {
        MerkleTree tree = new MerkleTree();
        assertTrue(tree.isEmpty());
        assertEquals(0, tree.size());
        assertFalse(tree.getRoot().isPresent());
    }

    @Test
    public void shouldCreateEvenMerkleTree() {
        HashableString a = HashableString.of("A");
        HashableString b = HashableString.of("B");
        HashableString c = HashableString.of("C");
        HashableString d = HashableString.of("D");
        MerkleTree<HashableString> tree = new MerkleTree<>(asList(a, b, c, d));
        tree.validate();

        /* Should generate the following tree:
         *
         *   H(AB|CD)
         *     |
         *     +- H(A|B)
         *     |   |
         *     |   +- H(A)
         *     |   |
         *     |   +- H(B)
         *     |
         *     +- H(C|D)
         *         |
         *         +- H(C)
         *         |
         *         +- H(D)
         */

        MerkleNode<HashableString> ABCD = tree.getRoot().get();
        MerkleNode<HashableString> AB = ABCD.getLeft().get();
        MerkleNode<HashableString> CD = ABCD.getRight().get();
        MerkleNode<HashableString> A = AB.getLeft().get();
        MerkleNode<HashableString> B = AB.getRight().get();
        MerkleNode<HashableString> C = CD.getLeft().get();
        MerkleNode<HashableString> D = CD.getRight().get();

        // Node A:
        assertEquals(a.getDigest(), A.getDigest());
        assertEquals(a, A.getValue().get());
        assertTrue(A.isLeaf());

        // Node B:
        assertEquals(b.getDigest(), B.getDigest());
        assertEquals(b, B.getValue().get());
        assertTrue(B.isLeaf());

        // Node C:
        assertEquals(c.getDigest(), C.getDigest());
        assertEquals(c, C.getValue().get());
        assertTrue(C.isLeaf());

        // Node AB:
        assertEquals(HASH256.digest(A.getDigest(), B.getDigest()), AB.getDigest());
        assertFalse(AB.isLeaf());

        // Node CD:
        assertEquals(HASH256.digest(C.getDigest(), D.getDigest()), CD.getDigest());
        assertFalse(CD.isLeaf());

        // Node ABCD:
        assertEquals(HASH256.digest(AB.getDigest(), CD.getDigest()), ABCD.getDigest());
        assertFalse(ABCD.isLeaf());
        assertEquals(tree.getRoot().get(), ABCD);
    }

    @Test
    public void shouldCreateOddMerkleTree() {
        HashableString a = HashableString.of("A");
        HashableString b = HashableString.of("B");
        HashableString c = HashableString.of("C");
        MerkleTree<HashableString> tree = new MerkleTree<>(Arrays.asList(a, b, c));
        tree.validate();

        /* Should generate the following tree:
         *
         *   H(AB|CC)
         *     |
         *     +- H(A|B)
         *     |   |
         *     |   +- H(A)
         *     |   |
         *     |   +- H(B)
         *     |
         *     +- H(C|C)
         *         |
         *         +- H(C)
         */

        MerkleNode<HashableString> ABCC = tree.getRoot().get();
        MerkleNode<HashableString> AB = ABCC.getLeft().get();
        MerkleNode<HashableString> CC = ABCC.getRight().get();
        MerkleNode<HashableString> A = AB.getLeft().get();
        MerkleNode<HashableString> B = AB.getRight().get();
        MerkleNode<HashableString> C = CC.getLeft().get();

        // Node A:
        assertEquals(a.getDigest(), A.getDigest());
        assertEquals(a, A.getValue().get());
        assertTrue(A.isLeaf());

        // Node B:
        assertEquals(b.getDigest(), B.getDigest());
        assertEquals(b, B.getValue().get());
        assertTrue(B.isLeaf());

        // Node C:
        assertEquals(c.getDigest(), C.getDigest());
        assertEquals(c, C.getValue().get());
        assertTrue(C.isLeaf());

        // Node AB:
        assertEquals(HASH256.digest(A.getDigest(), B.getDigest()), AB.getDigest());
        assertFalse(AB.isLeaf());

        // Node CC:
        assertEquals(HASH256.digest(C.getDigest(), C.getDigest()), CC.getDigest());
        assertFalse(CC.isLeaf());

        // Node ABCC:
        assertEquals(HASH256.digest(AB.getDigest(), CC.getDigest()), ABCC.getDigest());
        assertFalse(ABCC.isLeaf());
        assertEquals(tree.getRoot().get(), ABCC);
    }

    @Test
    public void shouldAppendElementsToMerkleTree() {
        HashableString A = HashableString.of("A");
        HashableString B = HashableString.of("B");
        HashableString C = HashableString.of("C");
        MerkleTree<HashableString> tree = new MerkleTree<>();

        assertTrue(tree.isEmpty());
        assertFalse(tree.contains(A));
        assertFalse(tree.contains(B));
        assertFalse(tree.contains(C));
        assertEquals(0, tree.size());

        tree = tree.append(A);
        assertFalse(tree.isEmpty());
        assertFalse(tree.contains(B));
        assertFalse(tree.contains(C));
        assertTrue(tree.contains(A));
        assertEquals(1, tree.size());

        tree = tree.append(B);
        assertFalse(tree.isEmpty());
        assertFalse(tree.contains(C));
        assertTrue(tree.contains(A));
        assertTrue(tree.contains(B));
        assertEquals(2, tree.size());

        tree = tree.append(C);
        assertFalse(tree.isEmpty());
        assertTrue(tree.contains(A));
        assertTrue(tree.contains(B));
        assertTrue(tree.contains(C));
        assertEquals(3, tree.size());
    }

    @Test
    public void shouldRemoveLeafNodes() {
        HashableString A = HashableString.of("A");
        HashableString B = HashableString.of("B");
        HashableString C = HashableString.of("C");
        MerkleTree<HashableString> tree = new MerkleTree<>(Arrays.asList(A, B, C));
        assertTrue(tree.isValid());
        assertTrue(tree.contains(A));
        assertTrue(tree.contains(B));
        assertTrue(tree.contains(C));

        tree = tree.remove(A);
        assertTrue(tree.isValid());
        assertFalse(tree.contains(A));
        assertTrue(tree.contains(B));
        assertTrue(tree.contains(C));

        tree = tree.remove(B);
        assertTrue(tree.isValid());
        assertFalse(tree.contains(A));
        assertFalse(tree.contains(B));
        assertTrue(tree.contains(C));

        tree = tree.remove(C);
        assertTrue(tree.isValid());
        assertFalse(tree.contains(A));
        assertFalse(tree.contains(B));
        assertFalse(tree.contains(C));
    }

}
