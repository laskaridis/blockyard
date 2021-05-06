package it.laskaridis.blockyard;

/**
 * Thrown when a merkle node is found to be invalid during the validation
 * of a merkle tree.
 *
 * @see MerkleNodeValidator
 */
public class InvalidMerkleTreeException extends RuntimeException {

    public InvalidMerkleTreeException(String s) {
        super(s);
    }
}
