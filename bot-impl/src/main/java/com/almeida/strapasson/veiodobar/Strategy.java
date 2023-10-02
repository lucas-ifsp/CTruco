package com.almeida.strapasson.veiodobar;

public interface Strategy {
    boolean hasMajorityCouple();
    boolean hasMinorCouple();

    boolean hasGoodHand();
    boolean hasMediumHand();
    boolean hasBadHand();
}
