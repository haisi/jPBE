/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
 */
package li.selman.jpbe.classifier;

import com.google.common.base.Objects;
import java.util.List;
import java.util.stream.Collectors;

/**
 * List of conjunct predicates.
 *
 * @author Hasan Selman Kara
 */
class Conjunct {

    private final List<Predicate> predicates;

    Conjunct(List<Predicate> predicates) {
        if (predicates.isEmpty()) throw new IllegalArgumentException("Predicates can't be empty");

        this.predicates = predicates;
    }

    /**
     * Checks whether the conjuct matches.
     * @return {@code true} if all predicates match {@code s}, else {@code false}
     */
    boolean matches(String s) {
        return predicates.stream()
            .map(predicate -> predicate.matches(s))
            .noneMatch(aBoolean -> Objects.equal(aBoolean, Boolean.FALSE));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Conjunct conjunct = (Conjunct) o;

        return predicates.equals(conjunct.predicates);
    }

    @Override
    public int hashCode() {
        return predicates.hashCode();
    }

    @Override
    public String toString() {
        String dnfString = predicates.stream().map(Object::toString).collect(Collectors.joining(" ∨ "));
        return "({" + dnfString + "})";
    }
}
